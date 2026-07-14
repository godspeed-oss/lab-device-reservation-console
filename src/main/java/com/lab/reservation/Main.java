package com.lab.reservation;

import com.lab.reservation.dao.DeviceDao;
import com.lab.reservation.dao.ReservationDao;
import com.lab.reservation.entity.Device;
import com.lab.reservation.entity.Reservation;
import com.lab.reservation.service.ReservationService;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        DeviceDao deviceDao = new DeviceDao();
        ReservationDao reservationDao = new ReservationDao();
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
            System.out.print("请输入功能编号：");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 0) {
                System.out.println("系统已退出");
                break;
            }

            switch (choice) {
                case 1:
                    showDevices(deviceDao);
                    break;
                case 2:
                    showReservations(reservationDao.findAll());
                    break;
                case 3:
                    addReservation(scanner, deviceDao, reservationService);
                    break;
                case 4:
                    findReservationsByDevice(scanner, reservationDao);
                    break;
                case 5:
    deleteReservation(scanner, reservationService);
    break;
                case 6:
                    updateDeviceStatus(scanner, deviceDao);
                    break;
                case 7:
                    findReservationsByDate(scanner, reservationDao);
                    break;
                default:
                    System.out.println("功能编号不存在，请重新输入");
            }

            System.out.println();
        }

        scanner.close();
    }

    private static void showDevices(DeviceDao deviceDao) throws Exception {
        ArrayList<Device> devices = deviceDao.findAll();

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

    private static void addReservation(Scanner scanner, DeviceDao deviceDao, ReservationService reservationService) throws Exception {
        System.out.print("请输入设备编号：");
        int deviceId = scanner.nextInt();
        scanner.nextLine();

        Device device = deviceDao.findById(deviceId);

        System.out.print("请输入预约人姓名：");
        String userName = scanner.nextLine();

        System.out.print("请输入预约日期，例如 2026-07-09：");
        String reservationDate = scanner.nextLine();

        System.out.print("请输入开始时间，例如 09:00：");
        String startTime = scanner.nextLine();

        System.out.print("请输入结束时间，例如 11:00：");
        String endTime = scanner.nextLine();

        boolean success = reservationService.addReservation(device, deviceId, userName, reservationDate, startTime, endTime);

        if (success) {
            System.out.println("预约新增成功");
        } else {
            System.out.println("预约新增失败");
        }
    }

    private static void findReservationsByDevice(Scanner scanner, ReservationDao reservationDao) throws Exception {
        System.out.print("请输入设备编号：");
        int deviceId = scanner.nextInt();
        scanner.nextLine();

        ArrayList<Reservation> reservations = reservationDao.findByDeviceId(deviceId);
        showReservations(reservations);
    }

   private static void deleteReservation(Scanner scanner, ReservationService reservationService) throws Exception {
    System.out.print("请输入要删除的预约编号：");
    int reservationId = scanner.nextInt();
    scanner.nextLine();

    boolean success = reservationService.deleteReservation(reservationId);

    if (success) {
        System.out.println("预约删除成功");
    } else {
        System.out.println("预约不存在，删除失败");
    }
}

    private static void updateDeviceStatus(Scanner scanner, DeviceDao deviceDao) throws Exception {
        System.out.print("请输入设备编号：");
        int deviceId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("请输入新的设备状态，例如 可预约 / 维修中：");
        String status = scanner.nextLine();

        boolean success = deviceDao.updateStatus(deviceId, status);

        if (success) {
            System.out.println("设备状态修改成功");
        } else {
            System.out.println("设备不存在，修改失败");
        }
    }

    private static void findReservationsByDate(Scanner scanner, ReservationDao reservationDao) throws Exception {
        System.out.print("请输入预约日期，例如 2026-07-09：");
        String reservationDate = scanner.nextLine();

        ArrayList<Reservation> reservations = reservationDao.findByDate(reservationDate);
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