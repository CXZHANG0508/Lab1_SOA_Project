# 微服务 API 文档

## 1. 内部微服务 API (供网关调用)

### 1.1 Personnel (人员) 管理服务

**基础 URL:** `http://localhost:8083`  
**负责人:** 组员 C

#### 获取所有人员列表
```http
GET /personnel
```
**功能描述:** 查：获取所有人员列表

**响应 (200 OK):**
```json
[
  {
    "id": "p001",
    "name": "张三",
    "department": "计算机学院"
  }
]
```

#### 创建新人员
```http
POST /personnel
```
**功能描述:** 增：创建新的人员

**请求体:**
```json
{
  "name": "李四",
  "department": "软件学院"
}
```

**响应 (201 Created):**
```json
{
  "id": "p002",
  "name": "李四",
  "department": "软件学院"
}
```

#### 获取特定人员
```http
GET /personnel/{id}
```
**功能描述:** 查：获取特定ID的人员

**响应 (200 OK):**
```json
{
  "id": "p001",
  "name": "张三",
  "department": "计算机学院"
}
```

#### 更新人员信息
```http
PUT /personnel/{id}
```
**功能描述:** 改：更新特定ID的人员

**请求体:**
```json
{
  "department": "人工智能系"
}
```

**响应 (200 OK):**
```json
{
  "id": "p001",
  "name": "张三",
  "department": "人工智能系"
}
```

#### 删除人员
```http
DELETE /personnel/{id}
```
**功能描述:** 删：删除特定ID的人员

**响应 (204 No Content):**  
(无返回体)

### 1.2 Material (物资) 管理服务

**基础 URL:** `http://localhost:8082`  
**负责人:** 组员 B

#### 获取所有物资列表
```http
GET /materials
```
**功能描述:** 查：获取所有物资列表

**响应 (200 OK):**
```json
[
  {
    "id": "m001",
    "name": "树莓派",
    "stock": 10
  }
]
```

#### 新增物资
```http
POST /materials
```
**功能描述:** 增：新增一个物资

**请求体:**
```json
{
  "name": "烙铁",
  "stock": 5
}
```

**响应 (201 Created):**
```json
{
  "id": "m002",
  "name": "烙铁",
  "stock": 5
}
```

#### 获取特定物资
```http
GET /materials/{id}
```
**功能描述:** 查：获取特定ID的物资

**响应 (200 OK):**
```json
{
  "id": "m001",
  "name": "树莓派",
  "stock": 10
}
```

#### 更新物资库存
```http
PUT /materials/{id}
```
**功能描述:** 改：更新物资 (核心：减库存)

**请求体 (用于编排):**
```json
{
  "action": "decrease_stock",
  "quantity": 1
}
```

**响应 (200 OK):**
```json
{
  "id": "m001",
  "name": "树莓派",
  "stock": 9
}
```

#### 删除物资
```http
DELETE /materials/{id}
```
**功能描述:** 删：删除特定ID的物资

**响应 (204 No Content):**  
(无)

### 1.3 Borrow (借用) 管理服务

**基础 URL:** `http://localhost:8081`  
**负责人:** 组员 A

#### 获取所有借用记录
```http
GET /borrows
```
**功能描述:** 查：获取所有借用记录

**响应 (200 OK):**
```json
[
  {
    "borrowId": "b001",
    "personnelId": "p001",
    "materialId": "m001",
    "borrowDate": "2025-11-10T..."
  }
]
```

#### 创建借用记录
```http
POST /borrows
```
**功能描述:** 增：创建新的借用记录

**请求体 (用于编排):**
```json
{
  "personnelId": "p001",
  "materialId": "m002",
  "borrowDate": "2025-11-11T..."
}
```

**响应 (201 Created):**
```json
{
  "borrowId": "b002",
  "personnelId": "p001",
  "materialId": "m002",
  "borrowDate": "2025-11-11T..."
}
```

#### 获取特定借用记录
```http
GET /borrows/{id}
```
**功能描述:** 查：获取特定ID的记录

**响应 (200 OK):**
```json
{
  "borrowId": "b001",
  "personnelId": "p001",
  "materialId": "m001",
  "borrowDate": "2025-11-10T..."
}
```

#### 删除借用记录
```http
DELETE /borrows/{id}
```
**功能描述:** 删：删除记录 (归还)

**响应 (204 No Content):**  
(无)

## 2. 外部网关 API (供客户端调用)

> [!IMPORTANT]
> 这是你 (Java 网关) 需要对外提供的 API。你写的 *GatewayController.java 就是在实现这部分。

**基础 URL:** `http://localhost:8080`  
**负责人:** 你的名字

### 2.1 人员接口 (转发到 8083)

#### 获取所有人员列表
```http
GET /api/personnel
```
**功能描述:** 查：获取所有人员列表

**响应 (200 OK):**
```json
[
  { "id": "p001", "name": "张三", "department": "计算机学院" }
]
```

#### 创建新人员
```http
POST /api/personnel
```
**功能描述:** 增：创建新的人员

**请求体:**
```json
{ "name": "李四", "department": "软件学院" }
```

**响应 (200 OK):**
```json
{ "id": "p002", "name": "李四", "department": "软件学院" }
```

#### 获取特定人员
```http
GET /api/personnel/{id}
```
**功能描述:** 查：获取特定ID的人员

**响应 (200 OK):**
```json
{ "id": "p001", "name": "张三", "department": "计算机学院" }
```

#### 更新人员信息
```http
PUT /api/personnel/{id}
```
**功能描述:** 改：更新特定ID的人员

**请求体:**
```json
{ "department": "New Dept" }
```

**响应 (200 OK):**
```json
{ "message": "Personnel updated successfully" }
```

#### 删除人员
```http
DELETE /api/personnel/{id}
```
**功能描述:** 删：删除特定ID的人员

**响应 (200 OK):**
```json
{ "message": "Personnel deleted successfully" }
```

### 2.2 物资接口 (转发到 8082)

#### 获取所有物资列表
```http
GET /api/materials
```
**功能描述:** 查：获取所有物资列表

**响应 (200 OK):**
```json
[
  { "id": "m001", "name": "树莓派", "stock": 10 }
]
```

#### 新增物资
```http
POST /api/materials
```
**功能描述:** 增：新增一个物资

**请求体:**
```json
{ "name": "烙铁", "stock": 5 }
```

**响应 (200 OK):**
```json
{ "id": "m002", "name": "烙铁", "stock": 5 }
```

#### 获取特定物资
```http
GET /api/materials/{id}
```
**功能描述:** 查：获取特定ID的物资

**响应 (200 OK):**
```json
{ "id": "m001", "name": "树莓派", "stock": 10 }
```

#### 更新物资信息
```http
PUT /api/materials/{id}
```
**功能描述:** 改：更新特定ID的物资

**请求体:**
```json
{ "stock": 25 }
```

**响应 (200 OK):**
```json
{ "message": "Material updated successfully" }
```

#### 删除物资
```http
DELETE /api/materials/{id}
```
**功能描述:** 删：删除特定ID的物资

**响应 (200 OK):**
```json
{ "message": "Material deleted successfully" }
```

### 2.3 借用接口 (转发到 8081)

#### 获取所有借用记录
```http
GET /api/borrows
```
**功能描述:** 查：获取所有借用记录

**响应 (200 OK):**
```json
[
  { "borrowId": "b001", "personnelId": "p001", "materialId": "m001", "borrowDate": "2025-11-10T..." }
]
```

#### 获取特定借用记录
```http
GET /api/borrows/{id}
```
**功能描述:** 查：获取特定ID的记录

**响应 (200 OK):**
```json
{
  "borrowId": "b001",
  "personnelId": "p001",
  "materialId": "m001",
  "borrowDate": "2025-11-10T..."
}
```

#### 删除借用记录
```http
DELETE /api/borrows/{id}
```
**功能描述:** 删：删除记录 (归还)

**响应 (200 OK):**
```json
{ "message": "Borrow record deleted successfully (returned)" }
```

## 3. 核心服务编排 API (网关)

这是系统的核心业务逻辑，由 Java 网关协调多个内部服务完成。

#### 借用物资 (核心业务)
```http
POST /api/borrow-material
```
**功能描述:** 协调"借用服务"和"物资服务"来完成一次借用操作

**请求体 (Request Body):**
```json
{
  "personnelId": "p001",
  "materialId": "m002"
}
```

**内部编排流程:**
1. 调用 Python (8081): POST `/borrows` (创建新记录)
2. 调用 Node.js (8082): PUT `/materials/m002` (减少库存)

**成功响应 (200 OK):**
(返回由 Python 服务创建的新借用记录)
```json
{
  "borrowId": "b002",
  "personnelId": "p001",
  "materialId": "m002",
  "borrowDate": "2025-11-11T..."
}
```