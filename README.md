# 实验室设备预约系统（Java 控制台版）

本项目是一个基于 Java 和 MySQL 的实验室设备预约管理系统控制台版本。项目用于练习 Java 面向对象编程、集合操作、JDBC 数据库连接、DAO 分层设计、业务逻辑拆分以及 Git/GitHub 版本管理。

## 项目背景

实验室中常见设备数量有限，不同学生或教师需要在不同时间段预约使用设备。如果缺少管理系统，容易出现设备状态不清晰、预约记录混乱、同一设备同一时间段重复预约等问题。

本项目围绕“实验室设备预约”这一场景，实现了设备管理和预约管理的基础功能。

## 已实现功能

### 设备管理

- 查看设备列表
- 从 MySQL 数据库读取设备信息
- 修改设备状态
- 将设备状态更新写回 MySQL

### 预约管理

- 查看全部预约记录
- 从 MySQL 数据库读取预约记录
- 新增预约记录
- 将新增预约写入 MySQL
- 删除预约记录
- 从 MySQL 删除预约记录
- 按设备编号查询预约记录
- 按日期查询预约记录

### 业务规则

- 设备不存在时不能预约
- 设备状态不是“可预约”时不能预约
- 预约时间段格式不合法时不能预约
- 同一设备在同一天的重叠时间段不能重复预约

## 技术栈

- Java
- MySQL
- JDBC
- MySQL Connector/J
- Git
- GitHub
- Windows PowerShell

## 项目结构

```text
lab-device-reservation-console
├── Main.java
├── Device.java
├── Reservation.java
├── DeviceDao.java
├── ReservationDao.java
├── ReservationService.java
├── DbUtil.java
├── .gitignore
└── README.md

文件	                                作用
Main.java	                        程序入口，负责菜单显示和用户输入
Device.java	                设备实体类
Reservation.java	        预约实体类
DeviceDao.java	        负责 device 表的数据库操作
ReservationDao.java	负责 reservation 表的数据库操作
ReservationService.java	负责预约相关业务逻辑
DbUtil.java	                负责创建数据库连接

数据库设计
数据库名：
lab_reservation_db
device 表
CREATE TABLE device (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL
);
字段	        含义
id	        设备编号
name	设备名称
type	        设备类型
status	设备状态

reservation 表
CREATE TABLE reservation (
    id INT PRIMARY KEY AUTO_INCREMENT,
    device_id INT NOT NULL,
    user_name VARCHAR(50) NOT NULL,
    reservation_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    FOREIGN KEY (device_id) REFERENCES device(id)
);
字段	                        含义
id	                        预约编号
device_id	                设备编号
user_name	        预约人
reservation_date	预约日期
start_time	                开始时间
end_time	                结束时间

初始化数据
CREATE DATABASE IF NOT EXISTS lab_reservation_db;

USE lab_reservation_db;

CREATE TABLE IF NOT EXISTS device (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS reservation (
    id INT PRIMARY KEY AUTO_INCREMENT,
    device_id INT NOT NULL,
    user_name VARCHAR(50) NOT NULL,
    reservation_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    FOREIGN KEY (device_id) REFERENCES device(id)
);

INSERT INTO device (id, name, type, status) VALUES
(1, '显微镜', '光学设备', '可预约'),
(2, '3D打印机', '制造设备', '维修中'),
(3, '高速离心机', '实验设备', '可预约'); 

核心逻辑说明
时间段冲突判断
系统将时间转换成分钟数，再判断两个时间段是否重叠。
判断条件：
return newStart < existingEnd && existingStart < newEnd;
例如已有预约：
09:00-11:00
以下时间段会被判断为冲突：
10:00-12:00
08:00-10:00
09:30-10:30
以下时间段不冲突：
11:00-12:00
14:00-16:00