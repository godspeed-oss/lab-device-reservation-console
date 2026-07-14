package com.lab.reservation.entity;

public class Reservation {
    private int id;
    private int deviceId;
    private String userName;
    private String reservationDate;
    private String startTime;
    private String endTime;

    public Reservation(int id, int deviceId, String userName, String reservationDate, String startTime, String endTime) {
        this.id = id;
        this.deviceId = deviceId;
        this.userName = userName;
        this.reservationDate = reservationDate;
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

    public String getReservationDate() {
        return reservationDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
