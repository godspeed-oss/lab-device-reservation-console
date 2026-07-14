package com.lab.reservation.service;

import com.lab.reservation.dao.ReservationDao;
import com.lab.reservation.entity.Device;
import com.lab.reservation.entity.Reservation;

import java.time.LocalTime;
import java.util.ArrayList;

public class ReservationService {
    private ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ArrayList<Reservation> findAllReservations() throws Exception {
        return reservationDao.findAll();
    }

    public int countAllReservations() throws Exception {
        return reservationDao.countAll();
    }

    public int addReservation(Device device, int deviceId, String userName, String reservationDate, String startTime, String endTime) throws Exception {
        if (device == null) {
            System.out.println("设备不存在，无法预约");
            return -1;
        }

        if (!"可预约".equals(device.getStatus())) {
            System.out.println("该设备当前状态为：" + device.getStatus() + "，无法预约");
            return -1;
        }

        if (!isValidTimeRange(startTime, endTime)) {
            System.out.println("时间段不合法，开始时间必须早于结束时间");
            return -1;
        }

        if (hasTimeConflict(0, deviceId, reservationDate, startTime, endTime)) {
            System.out.println("预约失败：该设备在这个时间段已有预约");
            return -1;
        }

        Reservation reservation = new Reservation(0, deviceId, userName, reservationDate, startTime, endTime);
        return reservationDao.add(reservation);
    }

    public boolean updateReservation(int reservationId, Device device, int deviceId, String userName, String reservationDate, String startTime, String endTime) throws Exception {
        Reservation oldReservation = reservationDao.findById(reservationId);
        if (oldReservation == null) {
            System.out.println("预约记录不存在，修改失败");
            return false;
        }

        if (device == null) {
            System.out.println("设备不存在，无法修改预约");
            return false;
        }

        if (!"可预约".equals(device.getStatus())) {
            System.out.println("该设备当前状态为：" + device.getStatus() + "，无法预约");
            return false;
        }

        if (!isValidTimeRange(startTime, endTime)) {
            System.out.println("时间段不合法，开始时间必须早于结束时间");
            return false;
        }

        if (hasTimeConflict(reservationId, deviceId, reservationDate, startTime, endTime)) {
            System.out.println("修改失败：该设备在这个时间段已有预约");
            return false;
        }

        Reservation newReservation = new Reservation(reservationId, deviceId, userName, reservationDate, startTime, endTime);
        return reservationDao.update(newReservation);
    }

    public boolean deleteReservation(int reservationId) throws Exception {
        return reservationDao.deleteById(reservationId);
    }

    public ArrayList<Reservation> findReservationsByDeviceId(int deviceId) throws Exception {
        return reservationDao.findByDeviceId(deviceId);
    }

    public ArrayList<Reservation> findReservationsByDate(String reservationDate) throws Exception {
        return reservationDao.findByDate(reservationDate);
    }

    private boolean isValidTimeRange(String startTime, String endTime) {
        try {
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);
            return start.isBefore(end);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean hasTimeConflict(int currentReservationId, int deviceId, String reservationDate, String startTime, String endTime) throws Exception {
        ArrayList<Reservation> reservations = reservationDao.findByDeviceId(deviceId);

        LocalTime newStart = LocalTime.parse(startTime);
        LocalTime newEnd = LocalTime.parse(endTime);

        for (Reservation reservation : reservations) {
            if (reservation.getId() == currentReservationId) {
                continue;
            }

            if (!reservationDate.equals(reservation.getReservationDate())) {
                continue;
            }

            LocalTime existingStart = LocalTime.parse(reservation.getStartTime());
            LocalTime existingEnd = LocalTime.parse(reservation.getEndTime());

            boolean conflict = newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);
            if (conflict) {
                return true;
            }
        }

        return false;
    }
}