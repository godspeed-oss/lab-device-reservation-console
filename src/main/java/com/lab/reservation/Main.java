package com.lab.reservation;

import com.lab.reservation.dao.DeviceDao;
import com.lab.reservation.dao.ReservationDao;
import com.lab.reservation.entity.Device;
import com.lab.reservation.entity.Reservation;
import com.lab.reservation.service.DeviceService;
import com.lab.reservation.service.ReservationService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("请输入数字");
            }
        }
    }

    private static String readRequiredText(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println("输入内容不能为空");
        }
    }

    private static boolean confirm(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt + "（y/n）：");
            String input = scanner.nextLine().trim();

            if ("y".equalsIgnoreCase(input)) {
                return true;
            }

            if ("n".equalsIgnoreCase(input)) {
                return false;
            }

            System.out.println("请输入 y 或 n");
        }
    }

    private static String readDate(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            try {
                LocalDate.parse(input);
                return input;
            } catch (Exception e) {
                System.out.println("日期格式不正确，请输入类似 2026-07-09 的日期");
            }
        }
    }

    private static String readTime(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();

            try {
                LocalTime.parse(input);
                return input;
            } catch (Exception e) {
                System.out.println("时间格式不正确，请输入类似 09:00 的时间");
            }
        }
    }

    private static String readDeviceStatus(Scanner scanner) {
        while (true) {
            System.out.print("请选择设备状态：1. 可预约  2. 维修中  3. 停用：");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    return "可预约";
                case "2":
                    return "维修中";
                case "3":
                    return "停用";
                default:
                    System.out.println("状态编号不存在，请重新输入");
            }
        }
    }

    public static void main(String[] args) {
        DeviceDao deviceDao = new DeviceDao();
        ReservationDao reservationDao = new ReservationDao();
        DeviceService deviceService = new DeviceService(deviceDao, reservationDao);
        ReservationService reservationService = new ReservationService(reservationDao);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("实验室设备预约系统");
            System.out.println("1. 查看设备列表");
            System.out.println("2. 查看预约记录");
            System.out.println("3. 新增预约");
            System.out.println("4. 查询指定设备预约记录");
            System.out.println("5. 删除预约记录");
            System.out.println("6. 修改设备状态");
            System.out.println("7. 按日期查询预约记录");
            System.out.println("8. 按设备名称搜索设备");
            System.out.println("9. 新增设备");
            System.out.println("10. 删除设备");
            System.out.println("11. 修改设备信息");
            System.out.println("12. 按设备状态筛选设备");
            System.out.println("13. 修改预约记录");
            System.out.println("14. 查看系统统计信息");
            System.out.println("0. 退出系统");

            int choice = readInt(scanner, "请输入功能编号：");

            if (choice == 0) {
                System.out.println("系统已退出");
                break;
            }

            try {
                switch (choice) {
                    case 1:
                        showDevices(deviceService.findAllDevices());
                        break;
                    case 2:
                        showReservations(reservationService.findAllReservations());
                        break;
                    case 3:
                        addReservation(scanner, deviceService, reservationService);
                        break;
                    case 4:
                        findReservationsByDevice(scanner, reservationService);
                        break;
                    case 5:
                        deleteReservation(scanner, reservationService);
                        break;
                    case 6:
                        updateDeviceStatus(scanner, deviceService);
                        break;
                    case 7:
                        findReservationsByDate(scanner, reservationService);
                        break;
                    case 8:
                        searchDevices(scanner, deviceService);
                        break;
                    case 9:
                        addDevice(scanner, deviceService);
                        break;
                    case 10:
                        deleteDevice(scanner, deviceService);
                        break;
                    case 11:
                        updateDevice(scanner, deviceService);
                        break;
                    case 12:
                        findDevicesByStatus(scanner, deviceService);
                        break;
                    case 13:
                        updateReservation(scanner, deviceService, reservationService);
                        break;
                    case 14:
                        showStatistics(deviceService, reservationService);
                        break;
                    default:
                        System.out.println("功能编号不存在，请重新输入");
                }
            } catch (Exception e) {
                System.out.println("操作失败，请检查数据库连接或输入内容");
                System.out.println("错误信息：" + e.getMessage());
            }

            System.out.println();
        }

        scanner.close();
    }

    private static void showDevices(ArrayList<Device> devices) {
        System.out.println("实验室设备列表：");
        System.out.println("--------------------");

        if (devices.isEmpty()) {
            System.out.println("暂无设备记录");
            return;
        }

        for (Device device : devices) {
            printDevice(device);
        }
    }

    private static void showReservations(ArrayList<Reservation> reservations) {
        System.out.println("预约记录列表：");
        System.out.println("--------------------");

        if (reservations.isEmpty()) {
            System.out.println("暂无预约记录");
            return;
        }

        for (Reservation reservation : reservations) {
            printReservation(reservation);
        }
    }

    private static void addReservation(Scanner scanner, DeviceService deviceService, ReservationService reservationService) throws Exception {
        int deviceId = readInt(scanner, "请输入设备编号：");

        Device device = deviceService.findDeviceById(deviceId);

        String userName = readRequiredText(scanner, "请输入预约人姓名：");

        String reservationDate = readDate(scanner, "请输入预约日期，例如 2026-07-09：");

        String startTime = readTime(scanner, "请输入开始时间，例如 09:00：");

        String endTime = readTime(scanner, "请输入结束时间，例如 11:00：");

        int reservationId = reservationService.addReservation(device, deviceId, userName, reservationDate, startTime, endTime);

        if (reservationId > 0) {
            System.out.println("预约新增成功，预约编号：" + reservationId);
        } else {
            System.out.println("预约新增失败");
        }
    }

    private static void updateReservation(Scanner scanner, DeviceService deviceService, ReservationService reservationService) throws Exception {
        int reservationId = readInt(scanner, "请输入要修改的预约编号：");

        int deviceId = readInt(scanner, "请输入新的设备编号：");
        Device device = deviceService.findDeviceById(deviceId);

        String userName = readRequiredText(scanner, "请输入新的预约人姓名：");

        String reservationDate = readDate(scanner, "请输入新的预约日期，例如 2026-07-09：");

        String startTime = readTime(scanner, "请输入新的开始时间，例如 09:00：");

        String endTime = readTime(scanner, "请输入新的结束时间，例如 11:00：");

        if (!confirm(scanner, "确认修改该预约记录吗")) {
            System.out.println("已取消修改");
            return;
        }

        boolean success = reservationService.updateReservation(
                reservationId,
                device,
                deviceId,
                userName,
                reservationDate,
                startTime,
                endTime
        );

        if (success) {
            System.out.println("预约记录修改成功");
        } else {
            System.out.println("预约记录修改失败");
        }
    }

    private static void findReservationsByDevice(Scanner scanner, ReservationService reservationService) throws Exception {
        int deviceId = readInt(scanner, "请输入设备编号：");

        ArrayList<Reservation> reservations = reservationService.findReservationsByDeviceId(deviceId);
        showReservations(reservations);
    }

    private static void deleteReservation(Scanner scanner, ReservationService reservationService) throws Exception {
        int reservationId = readInt(scanner, "请输入要删除的预约编号：");

        if (!confirm(scanner, "确认删除该预约记录吗")) {
            System.out.println("已取消删除");
            return;
        }

        boolean success = reservationService.deleteReservation(reservationId);

        if (success) {
            System.out.println("预约删除成功");
        } else {
            System.out.println("预约不存在，删除失败");
        }
    }

    private static void updateDeviceStatus(Scanner scanner, DeviceService deviceService) throws Exception {
        int deviceId = readInt(scanner, "请输入设备编号：");

        String status = readDeviceStatus(scanner);

        if (!confirm(scanner, "确认修改该设备状态吗")) {
            System.out.println("已取消修改");
            return;
        }

        boolean success = deviceService.updateDeviceStatus(deviceId, status);

        if (success) {
            System.out.println("设备状态修改成功");
        } else {
            System.out.println("设备状态修改失败");
        }
    }

    private static void findReservationsByDate(Scanner scanner, ReservationService reservationService) throws Exception {
        String reservationDate = readDate(scanner, "请输入预约日期，例如 2026-07-09：");

        ArrayList<Reservation> reservations = reservationService.findReservationsByDate(reservationDate);
        showReservations(reservations);
    }

    private static void searchDevices(Scanner scanner, DeviceService deviceService) throws Exception {
        String keyword = readRequiredText(scanner, "请输入设备名称关键词：");

        ArrayList<Device> devices = deviceService.searchDevicesByName(keyword);
        showDevices(devices);
    }

    private static void addDevice(Scanner scanner, DeviceService deviceService) throws Exception {
        String name = readRequiredText(scanner, "请输入设备名称：");
        String type = readRequiredText(scanner, "请输入设备类型：");
        String status = readDeviceStatus(scanner);

        int deviceId = deviceService.addDevice(name, type, status);

        if (deviceId > 0) {
            System.out.println("设备新增成功，设备编号：" + deviceId);
        } else {
            System.out.println("设备新增失败");
        }
    }

    private static void deleteDevice(Scanner scanner, DeviceService deviceService) throws Exception {
        int deviceId = readInt(scanner, "请输入要删除的设备编号：");

        if (!confirm(scanner, "确认删除该设备吗")) {
            System.out.println("已取消删除");
            return;
        }

        boolean success = deviceService.deleteDevice(deviceId);

        if (success) {
            System.out.println("设备删除成功");
        } else {
            System.out.println("设备删除失败");
        }
    }

    private static void updateDevice(Scanner scanner, DeviceService deviceService) throws Exception {
        int deviceId = readInt(scanner, "请输入要修改的设备编号：");

        String name = readRequiredText(scanner, "请输入新的设备名称：");
        String type = readRequiredText(scanner, "请输入新的设备类型：");
        String status = readDeviceStatus(scanner);

        if (!confirm(scanner, "确认修改该设备信息吗")) {
            System.out.println("已取消修改");
            return;
        }

        boolean success = deviceService.updateDevice(deviceId, name, type, status);

        if (success) {
            System.out.println("设备信息修改成功");
        } else {
            System.out.println("设备信息修改失败");
        }
    }

    private static void findDevicesByStatus(Scanner scanner, DeviceService deviceService) throws Exception {
        String status = readDeviceStatus(scanner);

        ArrayList<Device> devices = deviceService.findDevicesByStatus(status);
        showDevices(devices);
    }

    private static void showStatistics(DeviceService deviceService, ReservationService reservationService) throws Exception {
        int totalDevices = deviceService.countAllDevices();
        int availableDevices = deviceService.countDevicesByStatus("可预约");
        int repairingDevices = deviceService.countDevicesByStatus("维修中");
        int disabledDevices = deviceService.countDevicesByStatus("停用");
        int totalReservations = reservationService.countAllReservations();

        System.out.println("系统统计信息：");
        System.out.println("--------------------");
        System.out.println("设备总数：" + totalDevices);
        System.out.println("可预约设备数量：" + availableDevices);
        System.out.println("维修中设备数量：" + repairingDevices);
        System.out.println("停用设备数量：" + disabledDevices);
        System.out.println("预约记录总数：" + totalReservations);
        System.out.println("--------------------");
    }

    private static void printDevice(Device device) {
        System.out.println("设备编号：" + device.getId());
        System.out.println("设备名称：" + device.getName());
        System.out.println("设备类型：" + device.getType());
        System.out.println("设备状态：" + device.getStatus());
        System.out.println("--------------------");
    }

    private static void printReservation(Reservation reservation) {
        System.out.println("预约编号：" + reservation.getId());
        System.out.println("设备编号：" + reservation.getDeviceId());
        System.out.println("预约人：" + reservation.getUserName());
        System.out.println("预约日期：" + reservation.getReservationDate());
        System.out.println("预约时间：" + reservation.getStartTime() + "-" + reservation.getEndTime());
        System.out.println("--------------------");
    }
}