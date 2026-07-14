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
            System.out.print("请输入新的设备状态：1. 可预约  2. 维修中  3. 停用：");
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
        DeviceService deviceService = new DeviceService(deviceDao);
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
            System.out.println("0. 退出系统");

            int choice = readInt(scanner, "请输入功能编号：");

            if (choice == 0) {
                System.out.println("系统已退出");
                break;
            }

            try {
                switch (choice) {
                    case 1:
                        showDevices(deviceService);
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

    private static void showDevices(DeviceService deviceService) throws Exception {
        ArrayList<Device> devices = deviceService.findAllDevices();

        System.out.println("实验室设备列表：");
        System.out.println("--------------------");

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

        boolean success = reservationService.addReservation(device, deviceId, userName, reservationDate, startTime, endTime);

        if (success) {
            System.out.println("预约新增成功");
        } else {
            System.out.println("预约新增失败");
        }
    }

    private static void findReservationsByDevice(Scanner scanner, ReservationService reservationService) throws Exception {
        int deviceId = readInt(scanner, "请输入设备编号：");

        ArrayList<Reservation> reservations = reservationService.findReservationsByDeviceId(deviceId);
        showReservations(reservations);
    }

    private static void deleteReservation(Scanner scanner, ReservationService reservationService) throws Exception {
        int reservationId = readInt(scanner, "请输入要删除的预约编号：");

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