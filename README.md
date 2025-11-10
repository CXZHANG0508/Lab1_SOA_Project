# Lab1_SOA_Project
## 实验1：REST API的开发、部署与调用

[cite_start]本项目是《SOA原理与技术》的实验1 [cite: 1][cite_start]，旨在构建一个由不同语言开发的、相互调用的微服务体系，统一通过 Java 网关暴露 REST API [cite: 4, 1]。

### 🚀 1. 项目架构

[cite_start]本系统采用“微服务 + API网关”的类SOA架构 [cite: 4]，由5个独立的服务构成：

| 服务/模块 | 语言/框架 | 端口 | 核心功能 | 负责人     |
| :--- | :--- | :--- | :--- |:--------|
| **java-gateway** | Java / Spring Boot | `8080` | 统一API网关、服务编排 | （已基本实现） |
| **python-borrows** | Python / Flask | `8081` | 借用记录管理 (Borrows) |（未实现）|
| **node-materials** | Node.js / Express | `8082` | 物资管理 (Materials) | （未实现）|
| **personnel-service** | (第4语言) | `8083` | 人员管理 (Personnel) | （未实现）|
| **(待定)** | (N/A) | `N/A` | 文档与集成测试 | （未实现）|

---

### 📖 2. API 接口文档

本文档定义了“内部微服务 API”和“外部网关 API”。所有客户端**只应**调用 `http://localhost:8080` 上的“外部网关 API”。

#### 2.1 内部微服务 API (供网关调用)

**A. Personnel (人员) 管理服务 (运行在 `http://localhost:8083`)**

| HTTP 方法 | 内部 URL | 描述 (CRUD) |
| :--- | :--- | :--- |
| `GET` | `/personnel` | **查**：获取所有人员列表 |
| `POST` | `/personnel` | **增**：创建新的人员 |
| `GET` | `/personnel/{id}` | **查**：获取特定 ID 的人员信息 |
| `PUT` | `/personnel/{id}` | **改**：更新特定 ID 的人员信息 |
| `DELETE` | `/personnel/{id}` | **删**：删除特定 ID 的人员 |

**B. Material (物资) 管理服务 (运行在 `http://localhost:8082`)**

| HTTP 方法 | 内部 URL | 描述 (CRUD) |
| :--- | :--- | :--- |
| `GET` | `/materials` | **查**：获取所有物资列表 |
| `POST` | `/materials` | **增**：新增一个物资 |
| `GET` | `/materials/{id}` | **查**：获取特定 ID 的物资信息 |
| `PUT` | `/materials/{id}` | **改**：更新特定 ID 的物资信息 (如库存) |
| `DELETE` | `/materials/{id}` | **删**：删除特定 ID 的物资 |

**C. Borrow (借用) 管理服务 (运行在 `http://localhost:8081`)**

| HTTP 方法 | 内部 URL | 描述 (CRUD) |
| :--- | :--- | :--- |
| `GET` | `/borrows` | **查**：获取所有借用记录 |
| `POST` | `/borrows` | **增**：创建一条新的借用记录 |
| `GET` | `/borrows/{id}` | **查**：获取特定 ID 的借用记录 |
| `DELETE` | `/borrows/{id}` | **删**：删除 (归还) 特定 ID 的借用记录 |

---

#### 2.2 外部网关 API (由 Java 网关 @ 8080 提供)

**A. 人员接口 (转发到 8083)**

| HTTP 方法 | 外部 URL | 描述 |
| :--- | :--- | :--- |
| `GET` | `/api/personnel` | 查：获取所有人员 |
| `POST` | `/api/personnel` | 增：创建新的人员 |
| `GET` | `/api/personnel/{id}` | 查：获取特定人员 |
| `PUT` | `/api/personnel/{id}` | 改：更新特定人员 |
| `DELETE`| `/api/personnel/{id}`| 删：删除特定人员 |

**B. 物资接口 (转发到 8082)**

| HTTP 方法 | 外部 URL | 描述 |
| :--- | :--- | :--- |
| `GET` | `/api/materials` | 查：获取所有物资 |
| `POST` | `/api/materials` | 增：新增一个物资 |
| `GET` | `/api/materials/{id}` | 查：获取特定物资 |
| `PUT` | `/api/materials/{id}` | 改：更新特定物资 |
| `DELETE`| `/api/materials/{id}`| 删：删除特定物资 |

**C. 借用接口 (转发到 8081)**

| HTTP 方法 | 外部 URL | 描述 |
| :--- | :--- | :--- |
| `GET` | `/api/borrows` | 查：获取所有借用记录 |
| `GET` | `/api/borrows/{id}` | 查：获取特定借用记录 |
| `DELETE`| `/api/borrows/{id}`| 删：归还物资 |

---

#### 2.3 核心服务编排 API (由 Java 网关 @ 8080 提供)

这是本系统的核心业务逻辑，由 Java 网关协调多个内部服务完成。

**业务：借用物资**

| HTTP 方法 | 外部 URL |
| :--- | :--- |
| `POST` | `/api/borrow-material` |

**请求体 (Request Body) 示例:**
```json
{
  "personnelId": "p001",
  "materialId": "m002"
}