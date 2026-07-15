package com.lab.reservation.entity;

public class Device {
    private int id;
    private String name;
    private String type;
    private String status;

    public Device(int id, String name, String type, String status) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getDeviceId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDeviceName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDeviceType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getDeviceStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDeviceId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDeviceName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDeviceType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDeviceStatus(String status) {
        this.status = status;
    }

    public void printInfo() {
        System.out.println("设备编号：" + id);
        System.out.println("设备名称：" + name);
        System.out.println("设备类型：" + type);
        System.out.println("设备状态：" + status);
        System.out.println("--------------------");
    }
}