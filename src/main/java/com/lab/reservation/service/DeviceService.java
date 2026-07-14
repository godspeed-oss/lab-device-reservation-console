package com.lab.reservation.service;

import com.lab.reservation.dao.DeviceDao;
import com.lab.reservation.dao.ReservationDao;
import com.lab.reservation.entity.Device;

import java.util.ArrayList;

public class DeviceService {
    private DeviceDao deviceDao;
    private ReservationDao reservationDao;

    public DeviceService(DeviceDao deviceDao, ReservationDao reservationDao) {
        this.deviceDao = deviceDao;
        this.reservationDao = reservationDao;
    }

    public ArrayList<Device> findAllDevices() throws Exception {
        return deviceDao.findAll();
    }

    public Device findDeviceById(int deviceId) throws Exception {
        return deviceDao.findById(deviceId);
    }

    public ArrayList<Device> searchDevicesByName(String keyword) throws Exception {
        return deviceDao.findByNameKeyword(keyword);
    }

    public int addDevice(String name, String type, String status) throws Exception {
        if (!isValidStatus(status)) {
            System.out.println("设备状态不合法，只能输入：可预约 / 维修中 / 停用");
            return -1;
        }

        Device device = new Device(0, name, type, status);
        return deviceDao.add(device);
    }

    public boolean updateDeviceStatus(int deviceId, String status) throws Exception {
        if (!isValidStatus(status)) {
            System.out.println("设备状态不合法，只能输入：可预约 / 维修中 / 停用");
            return false;
        }

        return deviceDao.updateStatus(deviceId, status);
    }

    public boolean deleteDevice(int deviceId) throws Exception {
        Device device = deviceDao.findById(deviceId);
        if (device == null) {
            System.out.println("设备不存在，删除失败");
            return false;
        }

        if (reservationDao.existsByDeviceId(deviceId)) {
            System.out.println("该设备已有预约记录，不能删除");
            return false;
        }

        return deviceDao.deleteById(deviceId);
    }

    private boolean isValidStatus(String status) {
        return "可预约".equals(status) || "维修中".equals(status) || "停用".equals(status);
    }
}