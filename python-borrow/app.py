import requests
from flask import Flask, jsonify, request
from flask_sqlalchemy import SQLAlchemy
from datetime import datetime

app = Flask(__name__)

# 数据库配置（自行修改）
app.config['SQLALCHEMY_DATABASE_URI'] = "mysql+pymysql://root:123456@localhost:3306/soa_exp1"
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

db = SQLAlchemy(app)

PERSONNEL_SERVICE_URL = "http://localhost:8083/personnel"

# 数据库模型（映射 borrow 表）
class Borrow(db.Model):
    __tablename__ = 'borrows'

    borrow_id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    personnel_id = db.Column(db.String(50), nullable=False)
    material_id = db.Column(db.String(50), nullable=False)
    quantity = db.Column(db.Integer, default=1)
    borrow_date = db.Column(db.DateTime, default=datetime.now)
    return_date = db.Column(db.DateTime, nullable=True)
    status = db.Column(db.String(20), default="BORROWED")

    def to_dict(self):
        return {
            "borrow_id": self.borrow_id,
            "personnel_id": self.personnel_id,
            "material_id": self.material_id,
            "quantity": self.quantity,
            "borrow_date": self.borrow_date.strftime("%Y-%m-%d %H:%M:%S"),
            "return_date": self.return_date.strftime("%Y-%m-%d %H:%M:%S") if self.return_date else None,
            "status": self.status
        }


# 1. 获取全部借用记录
@app.route("/borrows", methods=["GET"])
def get_all_borrows():
    records = Borrow.query.all()
    return jsonify([r.to_dict() for r in records])


# 2. 获取单条借用记录
@app.route("/borrows/<int:borrow_id>", methods=["GET"])
def get_borrow(borrow_id):
    record = Borrow.query.get(borrow_id)
    if not record:
        return jsonify({"error": "Record not found"}), 404
    return jsonify(record.to_dict())


# 3. 创建借用记录（POST）
@app.route("/borrows", methods=["POST"])
def create_borrow():
    data = request.json

    personnel_id = data.get("personnelId")

    if "personnelId" not in data or "materialId" not in data:
        return jsonify({"error": "personnelId and materialId required"}), 400

    try:
        resp = requests.get(f"{PERSONNEL_SERVICE_URL}/{personnel_id}")
        if resp.status_code != 200:
            return jsonify({"error": "Personnel not found"}), 400

    except requests.RequestException:
        return jsonify({"error": "Personnel service unavailable"}), 503

    new_record = Borrow(
        personnel_id=data["personnelId"],
        material_id=data["materialId"],
        quantity=data.get("quantity", 1),
        status="BORROWED"
    )

    db.session.add(new_record)
    db.session.commit()

    return jsonify(new_record.to_dict()), 201


# 4. 更新借用记录（PATCH / PUT）
@app.route("/borrows/<int:borrow_id>", methods=["PATCH", "PUT"])
def update_borrow(borrow_id):
    record = Borrow.query.get(borrow_id)
    if not record:
        return jsonify({"error": "Record not found"}), 404

    data = request.json

    if "personnel_id" in data:
        record.personnel_id = data["personnel_id"]
    if "material_id" in data:
        record.material_id = data["material_id"]
    if "quantity" in data:
        record.quantity = data["quantity"]

    # 更新状态
    if "status" in data:
        record.status = data["status"]

        # 如果变成 returned，自动写 return_date
        if data["status"] == "RETURNED":
            record.return_date = datetime.now()

    db.session.commit()
    return jsonify(record.to_dict())


@app.route("/borrows/<int:borrow_id>", methods=["DELETE"])
def return_borrow(borrow_id):
    record = Borrow.query.get(borrow_id)
    if not record:
        return jsonify({"error": "Record not found"}), 404

    if record.status == "RETURNED":
        return jsonify({"message": "Already returned"}), 400

    record.status = "RETURNED"
    record.return_date = datetime.now()

    db.session.commit()

    return jsonify({
        "message": "Borrow record returned successfully",
        "record": {
            "borrow_id": record.borrow_id,
            "status": record.status,
            "return_date": record.return_date
        }
    })


# 启动服务
if __name__ == "__main__":
    app.run(port=8081, debug=True)
