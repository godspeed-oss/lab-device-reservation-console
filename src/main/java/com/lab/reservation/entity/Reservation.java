package com.lab.reservation.entity;

public class Reservation {
    private int id;
    private int deviceId;
    private String userName;
    private String date;
    private String startTime;
    private String endTime;

    public Reservation(int id, int deviceId, String userName, String date, String startTime, String endTime) {
        this.id = id;
        this.deviceId = deviceId;
        this.userName = userName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public String getUserName() {
        return userName;
    }

    public String getDate() {
        return date;
    }

    public String getReservationDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getTimeSlot() {
        return startTime + "-" + endTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setReservationDate(String date) {
        this.date = date;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void printInfo() {
        System.out.println("预约编号：" + id);
        System.out.println("设备编号：" + deviceId);
        System.out.println("预约人：" + userName);
        System.out.println("预约日期：" + date);
        System.out.println("预约时间：" + startTime + "-" + endTime);
        System.out.println("--------------------");
    }
}