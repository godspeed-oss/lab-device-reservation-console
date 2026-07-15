package com.lab.reservation.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeviceTest {
    @Test
    void shouldCreateDevice() {
        Device device = new Device(1, "显微镜", "光学设备", "可预约");

        assertEquals(1, device.getId());
        assertEquals("显微镜", device.getName());
        assertEquals("光学设备", device.getType());
        assertEquals("可预约", device.getStatus());
    }

    @Test
    void shouldUpdateDeviceStatus() {
        Device device = new Device(1, "显微镜", "光学设备", "可预约");

        device.setStatus("维修中");

        assertEquals("维修中", device.getStatus());
    }

    @Test
    void shouldKeepCompatibleGetterNames() {
        Device device = new Device(1, "显微镜", "光学设备", "可预约");

        assertEquals(device.getId(), device.getDeviceId());
        assertEquals(device.getName(), device.getDeviceName());
        assertEquals(device.getType(), device.getDeviceType());
        assertEquals(device.getStatus(), device.getDeviceStatus());
    }
}