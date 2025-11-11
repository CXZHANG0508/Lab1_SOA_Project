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
```
## 3. 数据库设置与使用 （sql文件已上传main分支）

> [!IMPORTANT]
> **架构核心原则:** 本项目**严格**遵循“一个服务一个数据库” (Database-per-Service) 的微服务架构原则。
> 
> * **严禁** 多个模块服务共享同一个数据库 
> * **严禁** 一个服务跨界连接另一个服务的数据库 (例如：Python 服务去 `SELECT` 人员表)。
> * 服务之间的所有通信 **必须** 通过 `API文档.md` 中定义的 **API (HTTP)** 来完成。

---

### 3.1 数据库分工 (Database Responsibilities)

根据“一个服务一个数据库”的原则，本项目共有 **3 个独立的数据库**，请由负责三个模块的成员分别管理。

#### 1. Personnel (人员) 服务 (@ 8083)

* **数据库:** `personnel_db` (使用 `MySQL`)
* **表:** `personnel` (详情见 `personnel.sql`)
* **任务:**
    1.  你需要在你本地的 MySQL 中**创建 `personnel_db` 数据库**。
    2.  执行 `personnel.sql` 脚本，创建 `personnel` 表。
    3.  你的服务 (区别于js和python的第四种语言) 是**唯一**连接 `personnel_db` 的程序。
    4.  你负责实现包括 `GET /personnel` 等在内的所有与人员相关的 API。

#### 2. Material (物资) 服务 (@ 8082)

* **数据库:** `materials_db` (使用 `MySQL`)
* **表:** `materials` (详情见 `materials.sql`)
* **任务:**
    1.  你需要在你本地的 MySQL 中**创建 `materials_db` 数据库**。
    2.  执行 `materials.sql` 脚本，创建 `materials` 表。
    3.  你的 Node.js 服务是**唯一**连接 `materials_db` 的程序。
    4.  你负责实现包括 `GET /materials` 等在内所有与物资相关的 API。

#### 3. Borrow (借用) 服务 (@ 8081)

* **数据库:** `borrows_db` (使用 `MySQL`)
* **表:** `borrows` (详情见 `borrows.sql`)
* **任务:**
    1.  你需要在你本地的 MySQL 中**创建 `borrows_db` 数据库**。
    2.  执行 `borrows.sql` 脚本，创建 `borrows` 表。
    3.  你的 Python 服务是**唯一**连接 `borrows_db` 的程序。
    4.  你负责实现包括 `GET /borrows` 等在内的所有与借用记录相关的 API。

#### 4.  Java 网关 (@ 8080)（目前基本实现）

* **数据库:** **无 (NONE)**
* **任务:**
    1.   `java-gateway` 服务是一个**“纯网关”**，**不连接任何数据库**。
    2.  确保 `pom.xml` 中**没有** `spring-data-jpa` 和 `mysql-driver` 依赖。
    3.  确保 `application.properties` 中**没有** `spring.datasource.url` 等配置。
    4.  工作是 100% 通过 `RestTemplate` (HTTP API) 来调用 8081, 8082, 和 8083。