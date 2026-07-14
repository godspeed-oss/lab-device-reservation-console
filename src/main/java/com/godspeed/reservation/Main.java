package com.godspeed.reservation;

import com.godspeed.reservation.dao.DeviceDao;
import com.godspeed.reservation.dao.ReservationDao;
import com.godspeed.reservation.entity.Device;
import com.godspeed.reservation.entity.Reservation;
import com.godspeed.reservation.service.ReservationService;

import java.util.ArrayList;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("请在运行时传入 MySQL 密码");
System.out.println("示例：mvn exec:java \"-Dexec.args=你的MySQL密码\"");
            return;
        }

        String mysqlPassword = args[0];

        DeviceDao deviceDao = new DeviceDao(mysqlPassword);
        ReservationDao reservationDao = new ReservationDao(mysqlPassword);
        ReservationService reservationService = new ReservationService(reservationDao);

        ArrayList<Device> devices = deviceDao.findAll();
        ArrayList<Reservation> reservations = reservationDao.findAll();

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

            switch (choice) {
                case 1:
                    showDevices(devices);
                    break;
                case 2:
                    showReservations(reservations);
                    break;
                case 3:
                    addReservation(scanner, devices, reservations, reservationService);
                    break;
                case 4:
                    searchReservationsByDeviceId(scanner, reservationDao);
                    break;
                case 5:
                    deleteReservation(scanner, reservations, reservationService);
                    break;
                case 6:
                    updateDeviceStatus(scanner, devices, deviceDao, reservationService);
                    break;
                case 7:
                    searchReservationsByDate(scanner, reservationDao);
                    break;
                case 0:
                    System.out.println("系统已退出");
                    scanner.close();
                    return;
                default:
                    System.out.println("输入错误，请重新输入");
            }

            System.out.println();
        }
    }

    public static void showDevices(ArrayList<Device> devices) {
        System.out.println("实验室设备列表：");
        System.out.println("--------------------");

        for (Device device : devices) {
            device.printInfo();
        }
    }

    public static void showReservations(ArrayList<Reservation> reservations) {
        System.out.println("预约记录列表：");
        System.out.println("--------------------");

        for (Reservation reservation : reservations) {
            reservation.printInfo();
        }
    }

    public static void addReservation(Scanner scanner, ArrayList<Device> devices, ArrayList<Reservation> reservations,
                                      ReservationService reservationService) throws Exception {
        System.out.print("请输入设备编号：");
        int deviceId = scanner.nextInt();

        System.out.print("请输入预约人姓名：");
        String userName = scanner.next();

        System.out.print("请输入预约日期，例如 2026-07-09：");
        String date = scanner.next();

        System.out.print("请输入预约时间段，例如 14:00-16:00：");
        String timeSlot = scanner.next();

        Reservation savedReservation = reservationService.addReservation(devices, reservations, deviceId, userName, date, timeSlot);

        if (savedReservation != null) {
            reservations.add(savedReservation);
            System.out.println("预约新增成功");
        }
    }

    public static void searchReservationsByDeviceId(Scanner scanner, ReservationDao reservationDao) throws Exception {
        System.out.print("请输入要查询的设备编号：");
        int deviceId = scanner.nextInt();

        ArrayList<Reservation> result = reservationDao.findByDeviceId(deviceId);

        System.out.println("查询结果：");
        System.out.println("--------------------");

        if (result.isEmpty()) {
            System.out.println("该设备暂无预约记录");
            return;
        }

        for (Reservation reservation : result) {
            reservation.printInfo();
        }
    }

    public static void deleteReservation(Scanner scanner, ArrayList<Reservation> reservations,
                                         ReservationService reservationService) throws Exception {
        System.out.print("请输入要删除的预约编号：");
        int reservationId = scanner.nextInt();

        boolean success = reservationService.deleteReservation(reservations, reservationId);

        if (success) {
            System.out.println("预约记录删除成功");
        }
    }

    public static void updateDeviceStatus(Scanner scanner, ArrayList<Device> devices, DeviceDao deviceDao,
                                          ReservationService reservationService) throws Exception {
        System.out.print("请输入要修改状态的设备编号：");
        int deviceId = scanner.nextInt();

        Device selectedDevice = reservationService.findDeviceById(devices, deviceId);

        if (selectedDevice == null) {
            System.out.println("设备不存在，修改失败");
            return;
        }

        System.out.println("当前设备状态：" + selectedDevice.getStatus());
        System.out.print("请输入新的设备状态，例如 可预约 或 维修中：");
        String newStatus = scanner.next();

        deviceDao.updateStatus(deviceId, newStatus);
        selectedDevice.setStatus(newStatus);

        System.out.println("设备状态修改成功");
    }

    public static void searchReservationsByDate(Scanner scanner, ReservationDao reservationDao) throws Exception {
        System.out.print("请输入要查询的预约日期，例如 2026-07-09：");
        String date = scanner.next();

        ArrayList<Reservation> result = reservationDao.findByDate(date);

        System.out.println("查询结果：");
        System.out.println("--------------------");

        if (result.isEmpty()) {
            System.out.println("该日期暂无预约记录");
            return;
        }

        for (Reservation reservation : result) {
            reservation.printInfo();
        }
    }
}