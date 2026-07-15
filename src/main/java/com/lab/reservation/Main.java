package com.lab.reservation;

import com.lab.reservation.dao.DeviceDao;
import com.lab.reservation.dao.ReservationDao;
import com.lab.reservation.entity.Device;
import com.lab.reservation.entity.Reservation;
import com.lab.reservation.exception.BusinessException;
import com.lab.reservation.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final Scanner scanner = new Scanner(System.in);

    private static final DeviceDao deviceDao = new DeviceDao();
    private static final ReservationDao reservationDao = new ReservationDao();
    private static final ReservationService reservationService = new ReservationService(reservationDao, deviceDao);

    public static void main(String[] args) {
        logger.info("实验室设备预约系统启动");

        while (true) {
            printMenu();

            int choice = readInt("请输入功能编号：");

            try {
                switch (choice) {
                    case 1 -> showDeviceList();
                    case 2 -> showReservationList();
                    case 3 -> addReservation();
                    case 4 -> showReservationsByDeviceId();
                    case 5 -> deleteReservation();
                    case 6 -> updateDeviceStatus();
                    case 7 -> showReservationsByDate();
                    case 8 -> addDevice();
                    case 9 -> updateDevice();
                    case 10 -> deleteDevice();
                    case 11 -> searchDevicesByName();
                    case 12 -> searchDevicesByStatus();
                    case 13 -> updateReservation();
                    case 14 -> showStatistics();
                    case 0 -> {
                        logger.info("实验室设备预约系统退出");
                        System.out.println("系统已退出");
                        return;
                    }
                    default -> System.out.println("功能编号不存在，请重新输入");
                }
            } catch (BusinessException e) {
                logger.warn("业务操作失败：{}", e.getMessage());
                System.out.println("操作失败：" + e.getMessage());
            } catch (Exception e) {
                logger.error("系统异常", e);
                System.out.println("系统异常，请稍后重试");
                System.out.println("错误信息：" + e.getMessage());
            }

            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("实验室设备预约系统");
        System.out.println("1. 查看设备列表");
        System.out.println("2. 查看预约记录");
        System.out.println("3. 新增预约");
        System.out.println("4. 查询指定设备预约记录");
        System.out.println("5. 删除预约记录");
        System.out.println("6. 修改设备状态");
        System.out.println("7. 按日期查询预约记录");
        System.out.println("8. 新增设备");
        System.out.println("9. 修改设备信息");
        System.out.println("10. 删除设备");
        System.out.println("11. 按设备名称搜索设备");
        System.out.println("12. 按设备状态筛选设备");
        System.out.println("13. 修改预约记录");
        System.out.println("14. 查看统计信息");
        System.out.println("0. 退出系统");
    }

    private static void showDeviceList() throws Exception {
        ArrayList<Device> devices = new ArrayList<>(deviceDao.findAll());

        System.out.println("实验室设备列表：");
        printDeviceList(devices);
    }

    private static void showReservationList() throws Exception {
        ArrayList<Reservation> reservations = reservationService.findAllReservations();

        System.out.println("预约记录列表：");
        printReservationList(reservations);
    }

    private static void addReservation() throws Exception {
        int deviceId = readInt("请输入设备编号：");
        Device device = deviceDao.findById(deviceId);

        String userName = readRequiredText("请输入预约人姓名：");
        String reservationDate = readRequiredText("请输入预约日期，例如 2026-07-15：");
        String startTime = readRequiredText("请输入开始时间，例如 09:00：");
        String endTime = readRequiredText("请输入结束时间，例如 11:00：");

        int newId = reservationService.addReservation(device, deviceId, userName, reservationDate, startTime, endTime);
        logger.info("新增预约成功，预约编号：{}，设备编号：{}，预约人：{}", newId, deviceId, userName);
        System.out.println("预约新增成功，预约编号：" + newId);
    }

    private static void showReservationsByDeviceId() throws Exception {
        int deviceId = readInt("请输入设备编号：");
        ArrayList<Reservation> reservations = reservationService.findReservationsByDeviceId(deviceId);

        System.out.println("指定设备预约记录：");
        printReservationList(reservations);
    }

    private static void deleteReservation() throws Exception {
        int id = readInt("请输入要删除的预约编号：");

        if (!confirm("确认删除该预约记录吗？")) {
            System.out.println("已取消删除");
            return;
        }

        boolean success = reservationService.deleteReservation(id);
        if (success) {
            logger.info("删除预约记录成功，预约编号：{}", id);
            System.out.println("预约记录删除成功");
        } else {
            System.out.println("预约记录删除失败");
        }
    }

    private static void updateDeviceStatus() throws Exception {
        int deviceId = readInt("请输入设备编号：");
        Device device = deviceDao.findById(deviceId);

        if (device == null) {
            throw new BusinessException("设备不存在，无法修改状态");
        }

        System.out.println("当前设备信息：");
        device.printInfo();

        String status = readDeviceStatus();

        device.setStatus(status);

        boolean success = deviceDao.update(device);
        if (success) {
            logger.info("修改设备状态成功，设备编号：{}，新状态：{}", deviceId, status);
            System.out.println("设备状态修改成功");
        } else {
            System.out.println("设备状态修改失败");
        }
    }

    private static void showReservationsByDate() throws Exception {
        String reservationDate = readRequiredText("请输入预约日期，例如 2026-07-15：");
        ArrayList<Reservation> reservations = reservationService.findReservationsByDate(reservationDate);

        System.out.println("指定日期预约记录：");
        printReservationList(reservations);
    }

    private static void addDevice() throws Exception {
        String name = readRequiredText("请输入设备名称：");
        String type = readRequiredText("请输入设备类型：");
        String status = readDeviceStatus();

        Device device = new Device(0, name, type, status);
        int newId = deviceDao.add(device);

        logger.info("新增设备成功，设备编号：{}，设备名称：{}", newId, name);
        System.out.println("设备新增成功，设备编号：" + newId);
    }

    private static void updateDevice() throws Exception {
        int deviceId = readInt("请输入要修改的设备编号：");
        Device device = deviceDao.findById(deviceId);

        if (device == null) {
            throw new BusinessException("设备不存在，无法修改");
        }

        System.out.println("当前设备信息：");
        device.printInfo();

        String name = readRequiredText("请输入新的设备名称：");
        String type = readRequiredText("请输入新的设备类型：");
        String status = readDeviceStatus();

        device.setName(name);
        device.setType(type);
        device.setStatus(status);

        boolean success = deviceDao.update(device);
        if (success) {
            logger.info("修改设备信息成功，设备编号：{}", deviceId);
            System.out.println("设备信息修改成功");
        } else {
            System.out.println("设备信息修改失败");
        }
    }

    private static void deleteDevice() throws Exception {
        int deviceId = readInt("请输入要删除的设备编号：");
        Device device = deviceDao.findById(deviceId);

        if (device == null) {
            throw new BusinessException("设备不存在，无法删除");
        }

        if (reservationDao.existsByDeviceId(deviceId)) {
            throw new BusinessException("该设备已有预约记录，不能直接删除");
        }

        if (!confirm("确认删除该设备吗？")) {
            System.out.println("已取消删除");
            return;
        }

        boolean success = deviceDao.deleteById(deviceId);
        if (success) {
            logger.info("删除设备成功，设备编号：{}", deviceId);
            System.out.println("设备删除成功");
        } else {
            System.out.println("设备删除失败");
        }
    }

    private static void searchDevicesByName() throws Exception {
        String keyword = readRequiredText("请输入设备名称关键字：");
        ArrayList<Device> devices = new ArrayList<>(deviceDao.findByName(keyword));

        System.out.println("搜索结果：");
        printDeviceList(devices);
    }

    private static void searchDevicesByStatus() throws Exception {
        String status = readDeviceStatus();
        ArrayList<Device> devices = new ArrayList<>(deviceDao.findByStatus(status));

        System.out.println("筛选结果：");
        printDeviceList(devices);
    }

    private static void updateReservation() throws Exception {
        int id = readInt("请输入要修改的预约编号：");

        int deviceId = readInt("请输入新的设备编号：");
        Device device = deviceDao.findById(deviceId);

        String userName = readRequiredText("请输入新的预约人姓名：");
        String reservationDate = readRequiredText("请输入新的预约日期，例如 2026-07-15：");
        String startTime = readRequiredText("请输入新的开始时间，例如 09:00：");
        String endTime = readRequiredText("请输入新的结束时间，例如 11:00：");

        if (!confirm("确认修改该预约记录吗？")) {
            System.out.println("已取消修改");
            return;
        }

        boolean success = reservationService.updateReservation(id, device, deviceId, userName, reservationDate, startTime, endTime);
        if (success) {
            logger.info("修改预约记录成功，预约编号：{}", id);
            System.out.println("预约记录修改成功");
        } else {
            System.out.println("预约记录修改失败");
        }
    }

    private static void showStatistics() throws Exception {
        int deviceCount = deviceDao.findAll().size();
        int availableCount = deviceDao.findByStatus("可预约").size();
        int repairingCount = deviceDao.findByStatus("维修中").size();
        int disabledCount = deviceDao.findByStatus("停用").size();
        int reservationCount = reservationService.countAllReservations();

        System.out.println("统计信息：");
        System.out.println("设备总数：" + deviceCount);
        System.out.println("可预约设备数量：" + availableCount);
        System.out.println("维修中设备数量：" + repairingCount);
        System.out.println("停用设备数量：" + disabledCount);
        System.out.println("预约记录总数：" + reservationCount);
    }

    private static void printDeviceList(ArrayList<Device> devices) {
        if (devices.isEmpty()) {
            System.out.println("暂无设备数据");
            return;
        }

        for (Device device : devices) {
            device.printInfo();
        }
    }

    private static void printReservationList(ArrayList<Reservation> reservations) {
        if (reservations.isEmpty()) {
            System.out.println("暂无预约记录");
            return;
        }

        for (Reservation reservation : reservations) {
            reservation.printInfo();
        }
    }

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("请输入正确的数字");
            }
        }
    }

    private static String readRequiredText(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println("输入内容不能为空");
        }
    }

    private static String readDeviceStatus() {
        while (true) {
            System.out.println("请选择设备状态：");
            System.out.println("1. 可预约");
            System.out.println("2. 维修中");
            System.out.println("3. 停用");

            int choice = readInt("请输入状态编号：");

            switch (choice) {
                case 1:
                    return "可预约";
                case 2:
                    return "维修中";
                case 3:
                    return "停用";
                default:
                    System.out.println("状态编号不存在，请重新输入");
                    break;
            }
        }
    }

    private static boolean confirm(String message) {
        System.out.print(message + "（Y/N）：");
        String input = scanner.nextLine().trim();

        return "Y".equalsIgnoreCase(input);
    }
}