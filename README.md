# Lab Device Reservation Console

实验室设备预约系统控制台版本。项目以 Java + MySQL 为核心，实现实验室设备信息管理、预约管理、业务校验、日志记录和基础单元测试。

这个项目的目标不是单纯堆叠菜单功能，而是通过一个完整的小型业务系统，练习 Java 后端项目中常见的工程能力：面向对象建模、数据库持久化、分层设计、异常处理、日志、测试、Maven 构建和 Git 版本管理。

## 技术栈

- Java 17
- Maven
- MySQL
- JDBC
- JUnit 5
- SLF4J
- Logback
- Git / GitHub

## 功能模块

### 设备管理

- 查看设备列表
- 新增设备
- 修改设备信息
- 修改设备状态
- 删除设备
- 按设备名称搜索
- 按设备状态筛选

### 预约管理

- 查看全部预约记录
- 新增预约
- 修改预约记录
- 删除预约记录
- 查询指定设备的预约记录
- 按日期查询预约记录

### 业务规则

- 不存在的设备不能预约
- 非“可预约”状态的设备不能预约
- 预约开始时间必须早于结束时间
- 同一设备同一天的预约时间不能冲突
- 不存在的预约记录不能修改或删除
- 已存在预约记录的设备不能直接删除

### 工程化能力

- Maven 标准项目结构
- MySQL 数据库存储
- DAO / Service / Entity 分层
- `BusinessException` 处理业务异常
- SLF4J + Logback 日志记录
- JUnit 5 基础单元测试
- Maven Shade Plugin 支持打包为可执行 jar
- Git tag 标记阶段版本

## 项目结构

```text
lab-device-reservation-console
├── pom.xml
├── README.md
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── lab
│   │   │           └── reservation
│   │   │               ├── Main.java
│   │   │               ├── dao
│   │   │               │   ├── DeviceDao.java
│   │   │               │   └── ReservationDao.java
│   │   │               ├── entity
│   │   │               │   ├── Device.java
│   │   │               │   └── Reservation.java
│   │   │               ├── exception
│   │   │               │   └── BusinessException.java
│   │   │               ├── service
│   │   │               │   ├── DeviceService.java
│   │   │               │   └── ReservationService.java
│   │   │               └── util
│   │   │                   └── DbUtil.java
│   │   └── resources
│   │       ├── db.example.properties
│   │       ├── db.properties
│   │       ├── logback.xml
│   │       └── schema.sql
│   └── test
│       └── java
│           └── com
│               └── lab
│                   └── reservation
│                       ├── entity
│                       │   ├── DeviceTest.java
│                       │   └── ReservationTest.java
│                       └── exception
│                           └── BusinessExceptionTest.java
└── target
```

## 分层说明

### Entity

实体类用于表示业务对象。

- `Device`：实验室设备
- `Reservation`：设备预约记录

### DAO

DAO 层负责和数据库交互，只处理 SQL 和数据转换。

- `DeviceDao`：设备表相关操作
- `ReservationDao`：预约表相关操作

### Service

Service 层负责业务规则判断。

- 判断设备是否存在
- 判断设备状态是否可预约
- 判断预约时间是否合法
- 判断预约时间是否冲突
- 判断记录是否允许修改或删除

### Main

`Main` 是控制台入口，负责菜单展示、用户输入、结果输出和异常提示。

## 数据库设计

数据库名称：

```sql
lab_reservation_db
```

### device 表

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | INT | 设备编号，主键，自增 |
| name | VARCHAR(50) | 设备名称 |
| type | VARCHAR(50) | 设备类型 |
| status | VARCHAR(20) | 设备状态 |

### reservation 表

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | INT | 预约编号，主键，自增 |
| device_id | INT | 设备编号，外键 |
| user_name | VARCHAR(50) | 预约人 |
| reservation_date | DATE | 预约日期 |
| start_time | TIME | 开始时间 |
| end_time | TIME | 结束时间 |

## 本地运行

### 1. 准备环境

需要提前安装：

- JDK 17 或更高版本
- Maven
- MySQL
- Git

### 2. 初始化数据库

在 MySQL 中执行：

```sql
source src/main/resources/schema.sql;
```

也可以手动打开 `src/main/resources/schema.sql`，复制其中 SQL 到 MySQL 控制台或数据库工具中执行。

### 3. 配置数据库连接

复制示例配置：

```powershell
copy src\main\resources\db.example.properties src\main\resources\db.properties
```

然后修改 `db.properties`：

```properties
db.url=jdbc:mysql://localhost:3306/lab_reservation_db?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8
db.user=root
db.password=your_password
```

`db.properties` 是本地配置文件，不应该提交到 GitHub。

### 4. 编译项目

```powershell
mvn clean compile
```

### 5. 运行项目

开发阶段运行：

```powershell
mvn exec:java
```

打包后运行：

```powershell
mvn clean package
java -jar target\lab-device-reservation-console-1.0-SNAPSHOT.jar
```

### 6. 运行测试

```powershell
mvn test
```

## 日志

项目使用 SLF4J + Logback 记录日志。

运行后会生成：

```text
logs/app.log
```

日志主要记录：

- 系统启动
- 系统退出
- 新增预约
- 删除预约
- 修改设备
- 业务操作失败
- 系统异常

## 版本记录

### v0.1-console

基础控制台版本。

- Java 控制台菜单
- 设备列表
- 预约记录
- 新增预约
- Git / GitHub 基础版本管理

### v0.2-engineering

工程化增强版本。

- Maven 标准结构
- MySQL 持久化
- DAO / Service 分层
- 业务异常
- 日志记录
- 基础单元测试
- 可执行 jar 打包

## 当前不足

- 仍然是控制台交互，用户体验有限
- DAO 层直接使用 JDBC，SQL 代码较多
- 测试主要覆盖实体类和异常类，Service 和 DAO 测试还不完整
- 还没有用户登录和权限控制
- 还没有 Web API 和前端页面
- 数据校验规则还可以进一步统一

## 后续规划

下一阶段计划升级为 Spring Boot 版本：

- 使用 Spring Boot 搭建 Web 后端
- 使用 Controller 提供 REST API
- 保留 Service 层业务规则
- 使用 MyBatis 或 Spring Data JDBC 优化数据库访问
- 引入统一响应格式
- 引入全局异常处理
- 后续增加前端页面，实现完整的实验室设备预约系统