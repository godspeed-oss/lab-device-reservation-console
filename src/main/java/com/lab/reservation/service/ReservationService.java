package com.lab.reservation.service;

import com.lab.reservation.dao.DeviceDao;
import com.lab.reservation.dao.ReservationDao;
import com.lab.reservation.entity.Device;
import com.lab.reservation.entity.Reservation;
import com.lab.reservation.exception.BusinessException;

import java.util.ArrayList;

public class ReservationService {
    private final ReservationDao reservationDao;
    private final DeviceDao deviceDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
        this.deviceDao = new DeviceDao();
    }

    public ReservationService(ReservationDao reservationDao, DeviceDao deviceDao) {
        this.reservationDao = reservationDao;
        this.deviceDao = deviceDao;
    }

    public ArrayList<Reservation> findAllReservations() throws Exception {
        return new ArrayList<>(reservationDao.findAll());
    }

    public ArrayList<Reservation> findReservationsByDeviceId(int deviceId) throws Exception {
        Device device = deviceDao.findById(deviceId);
        if (device == null) {
            throw new BusinessException("设备不存在，无法查询预约记录");
        }

        return new ArrayList<>(reservationDao.findByDeviceId(deviceId));
    }

    public ArrayList<Reservation> findReservationsByDate(String reservationDate) throws Exception {
        validateText(reservationDate, "预约日期不能为空");
        return new ArrayList<>(reservationDao.findByDate(reservationDate));
    }

    public int countAllReservations() throws Exception {
        return reservationDao.findAll().size();
    }

    public int addReservation(Device device, int deviceId, String userName, String reservationDate, String startTime, String endTime) throws Exception {
        validateReservationInput(device, deviceId, userName, reservationDate, startTime, endTime, 0);

        Reservation reservation = new Reservation(0, deviceId, userName, reservationDate, startTime, endTime);
        return reservationDao.add(reservation);
    }

    public boolean updateReservation(int id, Device device, int deviceId, String userName, String reservationDate, String startTime, String endTime) throws Exception {
        Reservation oldReservation = reservationDao.findById(id);
        if (oldReservation == null) {
            throw new BusinessException("预约记录不存在，无法修改");
        }

        validateReservationInput(device, deviceId, userName, reservationDate, startTime, endTime, id);

        Reservation reservation = new Reservation(id, deviceId, userName, reservationDate, startTime, endTime);
        return reservationDao.update(reservation);
    }

    public boolean deleteReservation(int id) throws Exception {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new BusinessException("预约记录不存在，无法删除");
        }

        return reservationDao.deleteById(id);
    }

    private void validateReservationInput(Device device, int deviceId, String userName, String reservationDate, String startTime, String endTime, int excludeReservationId) throws Exception {
        if (device == null) {
            device = deviceDao.findById(deviceId);
        }

        if (device == null) {
            throw new BusinessException("设备不存在，无法预约");
        }

        if (!"可预约".equals(device.getStatus())) {
            throw new BusinessException("该设备当前状态为：" + device.getStatus() + "，不能预约");
        }

        validateText(userName, "预约人姓名不能为空");
        validateText(reservationDate, "预约日期不能为空");
        validateText(startTime, "开始时间不能为空");
        validateText(endTime, "结束时间不能为空");

        if (startTime.compareTo(endTime) >= 0) {
            throw new BusinessException("开始时间必须早于结束时间");
        }

        boolean conflict = reservationDao.hasConflict(
                deviceId,
                reservationDate,
                startTime,
                endTime,
                excludeReservationId
        );

        if (conflict) {
            throw new BusinessException("该设备在这个时间段已经被预约");
        }
    }

    private void validateText(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessException(message);
        }
    }
}