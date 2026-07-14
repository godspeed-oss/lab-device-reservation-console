package com.lab.reservation.service;

import com.lab.reservation.dao.DeviceDao;
import com.lab.reservation.entity.Device;

import java.util.ArrayList;

public class DeviceService {
    private DeviceDao deviceDao;

    public DeviceService(DeviceDao deviceDao) {
        this.deviceDao = deviceDao;
    }

    public ArrayList<Device> findAllDevices() throws Exception {
        return deviceDao.findAll();
    }

    public Device findDeviceById(int deviceId) throws Exception {
        return deviceDao.findById(deviceId);
    }

    public boolean updateDeviceStatus(int deviceId, String status) throws Exception {
        return deviceDao.updateStatus(deviceId, status);
    }
}