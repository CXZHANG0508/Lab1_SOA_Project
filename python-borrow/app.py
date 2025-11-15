from flask import Flask, jsonify, request
from flask_sqlalchemy import SQLAlchemy
from datetime import datetime

app = Flask(__name__)

# 数据库配置（自行修改）
app.config['SQLALCHEMY_DATABASE_URI'] = "mysql+pymysql://root:123456@localhost:3306/soa_exp1"
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

db = SQLAlchemy(app)


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
    if "personnel_id" not in data or "material_id" not in data:
        return jsonify({"error": "personnel_id and material_id required"}), 400

    new_record = Borrow(
        personnel_id=data["personnel_id"],
        material_id=data["material_id"],
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


# 5. 删除记录
@app.route("/borrows/<int:borrow_id>", methods=["DELETE"])
def delete_borrow(borrow_id):
    record = Borrow.query.get(borrow_id)
    if not record:
        return jsonify({"error": "Record not found"}), 404

    db.session.delete(record)
    db.session.commit()
    return jsonify({"message": "Record deleted"})


# 6. 查询某人的借用记录（供 Java 网关调用）
@app.route("/persons/<personnel_id>/borrows", methods=["GET"])
def get_borrows_by_person(personnel_id):
    records = Borrow.query.filter_by(personnel_id=personnel_id).all()
    return jsonify([r.to_dict() for r in records])


# 7. 查询某物资借出数量（供 Node 或 Gateway 调用）
@app.route("/materials/<material_id>/borrows/count", methods=["GET"])
def get_borrow_count(material_id):
    count = Borrow.query.filter_by(material_id=material_id, status="BORROWED").count()
    return jsonify({"material_id": material_id, "borrowed_count": count})


# 启动服务
if __name__ == "__main__":
    app.run(port=8081, debug=True)
