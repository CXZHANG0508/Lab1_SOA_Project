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

---
## 4. 快速上手指南 (Developer Quick Start)

本指南旨在帮助快速启动、开发和测试你所负责的微服务。

### A.  (Python Borrows Service @ 8081)

任务是实现 `python-borrows` 模块。

1.  **进入目录:**
    ```bash
    cd python-borrows
    ```
2.  **创建并激活虚拟环境:**
    ```bash
    # (Windows)
    python -m venv .venv
    .\.venv\Scripts\activate
    
    # (macOS/Linux)
    python3 -m venv .venv
    source .venv/bin/activate
    ```
3.  **安装依赖:** (确保 `requirements.txt` 文件已存在)
    ```bash
    pip install -r requirements.txt
    pip install flask-sqlalchemy pymysql 
    ```
4.  **开发:**
  * **核心文件:** 在 `app.py` (或 `main.py`) 中编写你的 Flask 代码。
  * **数据库:** 在此文件中，配置 `SQLAlchemy` 连接到你本地的 `borrows_db` (MySQL)。
  * **任务:** 严格按照 `API文档.md` 文档，实现所有 `GET /borrows`, `POST /borrows` 等 4 个 API 路由和数据库逻辑。
5.  **运行你的服务:**
    ```bash
    # 确保在 8081 端口运行
    flask run --port=8081
    ```
6.  **独立测试:**
  * 使用 Postman 或 curl **独立测试**你的服务。
  * **示例:** `GET http://localhost:8081/borrows` ，确保它能正确返回 JSON 数据或空列表 `[]`。

---

### B.(Node.js Materials Service @ 8082)

任务是实现 `node-materials` 模块。

1.  **进入目录:**
    ```bash
    cd node-materials
    ```
2.  **安装依赖:**
    ```bash
    npm install
    # (你可能还需要安装 mysql 驱动)
    npm install mysql2 
    ```
3.  **开发:**
  * **核心文件:** 在 `app.js` (或 `routes/materials.js`) 中编写你的 Express 代码。
  * **数据库:** 在此文件中，配置 `mysql2` 库连接到你本地的 `materials_db`。
  * **任务:** 严格按照 `API文档.md` 文档，实现所有 `GET /materials`, `POST /materials` 等 5 个 API 路由和数据库逻辑。
5.  **运行你的服务:**
    ```bash
    # 确保你的 package.json "start" 脚本指定了 8082 端口
    npm start
    ```
6.  **独立测试:**
  * 使用 Postman **独立测试**你的服务。
  * **示例:** `GET http://localhost:8082/materials` ，确保它能正确返回 JSON 数据。

---

好的，使用 Go 语言（Golang）是一个非常棒的选择，它就是为构建这种高性能微服务而生的。

我已经帮你**修改了 `README.md` 中组员 C 的那一部分**，把步骤换成了使用 Go 语言和 Gin 框架（Go 社区最流行的 Web 框架）的具体指南。

你只需要**复制下面的内容**，替换掉你 `README.md` 里 `## 5. 🧑‍💻 开发者快速上手指南` 中的 `### C. 组员 C` 那一段即可。

-----

### C. (Go Personnel Service @ 8083)

你的任务是实现 `personnel-service` 模块 (假如使用 Go 语言)。

1.  **创建目录并初始化:**
    ```bash
    # 1. 在 IDEA 项目根目录创建新文件夹
    mkdir personnel-service
    cd personnel-service
    # 2. 初始化 Go 模块 (将 "personnel-service" 替换为你的 GitHub 路径)
    # 例如: go mod init github.com/CXZHANG0508/Lab1_SOA_Project/personnel-service
    go mod init personnel-service 
    ```
2.  **安装/配置 (获取 Go 依赖):**
  * **Gin 框架 (推荐，用于 Web API):**
    ```bash
    go get -u github.com/gin-gonic/gin
    ```
  * **MySQL 驱动:**
    ```bash
    go get -u github.com/go-sql-driver/mysql
    ```
3.  **开发:**
  * **核心文件:** `main.go`。
  * **数据库:** 在 `main.go` 中，使用 `sql.Open("mysql", ...)` 来配置和连接到你本地的 `personnel_db`。
  * **任务:** 严格按照 `API文档.md` 文档，使用 Gin 框架 (`router.GET("/personnel", ...)`, `router.POST("/personnel", ...)` 等) 实现所有 5 个 API 路由和数据库逻辑。
4.  **运行服务:**
    ```bash
    # Go 会自动编译并运行
    # 确保你的 Go 代码中 http.ListenAndServe(":8083", router) 是 8083 端口
    go run main.go
    ```
5.  **独立测试:**
  * 使用 Postman **独立测试**你的服务。
  * **示例:** `GET http://localhost:8083/personnel`。

-----
### D.  (Java Gateway & 集成测试 @ 8080)

任务是**集成**和**联调**。

1.  **运行网关:**
  * 在 IDEA 中打开 `java-gateway`，点击 "Run" 启动 (运行在 8080)。
2.  **联调 (Integration Test):**
  * 必须**同时运行所有 4 个服务** (8080, 8081, 8082, 8083)。
  * **测试简单转发:**
    * 打开 Postman，调用**网关**的 API：`GET http://localhost:8080/api/personnel`
    * 检查 Java 网关 (8080) 是否成功调用了 8083 服务并返回了数据。
  * **测试核心编排:**
    * 打开 Postman，调用**网关**的核心 API：
    * `POST http://localhost:8080/api/borrow-material`
    * **请求体 (Body):**
      ```json
      {
        "personnelId": "p001", 
        "materialId": "m001",
        "quantity": 1
      }
      ```
    * **检查结果:** 检查 `borrows_db` 和 `materials_db` 数据库，看数据是否被正确 `INSERT` 和 `UPDATE`。