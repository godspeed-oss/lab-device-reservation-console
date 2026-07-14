CREATE DATABASE IF NOT EXISTS lab_reservation_db;

USE lab_reservation_db;

DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS device;

CREATE TABLE device (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE reservation (
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