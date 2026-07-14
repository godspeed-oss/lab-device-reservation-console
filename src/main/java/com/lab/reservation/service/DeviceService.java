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
        if (!isValidStatus(status)) {
            System.out.println("设备状态不合法，只能输入：可预约 / 维修中 / 停用");
            return false;
        }

        return deviceDao.updateStatus(deviceId, status);
    }

    private boolean isValidStatus(String status) {
        return "可预约".equals(status) || "维修中".equals(status) || "停用".equals(status);
    }
}