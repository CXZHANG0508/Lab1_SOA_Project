import requests
import json

BASE_URL = "http://localhost:8081"   # 你的 Flask 服务运行端口


def print_response(resp):
    print("状态码:", resp.status_code)
    try:
        print("返回:", json.dumps(resp.json(), ensure_ascii=False, indent=2))
    except:
        print("返回内容无法解析为 JSON")
    print("-" * 50)


# 1. 测试创建借用记录（POST /borrows）
def test_create_borrow():
    print("1. 测试创建借用记录:")

    data = {
        "personnel_id": "1",
        "material_id": "2",
        "quantity": 2
    }

    resp = requests.post(f"{BASE_URL}/borrows", json=data)
    print_response(resp)


# 2. 测试获取全部借用记录（GET /borrows）
def test_get_all_borrows():
    print("2. 获取全部借用记录:")
    resp = requests.get(f"{BASE_URL}/borrows")
    print_response(resp)


# 3. 测试获取单条借用记录（GET /borrows/{id}）
def test_get_borrow_by_id():
    print("3. 获取 borrow_id = 1 的借用记录:")
    resp = requests.get(f"{BASE_URL}/borrows/1")
    print_response(resp)


# 4. 测试更新借用记录（PATCH /borrows/{id}）
def test_update_borrow():
    print("4. 更新借用记录（将其 status 改为 RETURNED）:")

    data = {
        "status": "RETURNED"
    }

    resp = requests.patch(f"{BASE_URL}/borrows/1", json=data)
    print_response(resp)


# 5. 测试删除借用记录（DELETE /borrows/{id}）
def test_delete_borrow():
    print("5. 删除 borrow_id = 2 的借用记录:")
    resp = requests.delete(f"{BASE_URL}/borrows/2")
    print_response(resp)


# 6. 按人员 ID 查询借用记录（GET /persons/{id}/borrows）
def test_get_by_person():
    print("6. 获取人员 ID = 1 的所有借用记录:")
    resp = requests.get(f"{BASE_URL}/persons/1/borrows")
    print_response(resp)


# 7. 按物资统计借用数量（GET /materials/{id}/borrows/count）
def test_count_by_material():
    print("7. 获取物资 ID = 2 当前借出数量:")
    resp = requests.get(f"{BASE_URL}/materials/2/borrows/count")
    print_response(resp)


# 主执行逻辑（依次执行所有测试案例）
if __name__ == "__main__":
    print("\n====== 开始测试 Flask 借用记录服务 ======\n")

    test_create_borrow()
    test_get_all_borrows()
    test_get_borrow_by_id()
    test_update_borrow()
    test_delete_borrow()
    test_get_by_person()
    test_count_by_material()

    print("\n====== 全部测试执行完毕 ======\n")
