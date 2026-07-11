import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("请在运行时传入 MySQL 密码");
            System.out.println("示例：java -cp \".;lib\\mysql-connector-j-9.7.0.jar\" Main your_password");
            return;
        }

        String mysqlPassword = args[0];

        DeviceDao deviceDao = new DeviceDao(mysqlPassword);
ReservationDao reservationDao = new ReservationDao(mysqlPassword);

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
                    addReservation(scanner, devices, reservations);
                    break;
                case 4:
                    searchReservationsByDeviceId(scanner, reservations);
                    break;
                case 5:
                    deleteReservation(scanner, reservations);
                    break;
                case 6:
                    updateDeviceStatus(scanner, devices, deviceDao);
                    break;
                case 7:
                    searchReservationsByDate(scanner, reservations);
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

    public static void addReservation(Scanner scanner, ArrayList<Device> devices, ArrayList<Reservation> reservations) {
        System.out.print("请输入设备编号：");
        int deviceId = scanner.nextInt();

        Device selectedDevice = findDeviceById(devices, deviceId);

        if (selectedDevice == null) {
            System.out.println("设备不存在，预约失败");
            return;
        }

        if (!selectedDevice.getStatus().equals("可预约")) {
            System.out.println("该设备当前状态为：" + selectedDevice.getStatus());
            System.out.println("设备不可预约，预约失败");
            return;
        }

        System.out.print("请输入预约人姓名：");
        String userName = scanner.next();

        System.out.print("请输入预约日期，例如 2026-07-09：");
        String date = scanner.next();

        System.out.print("请输入预约时间段，例如 14:00-16:00：");
        String timeSlot = scanner.next();

        if (!isValidTimeSlot(timeSlot)) {
            System.out.println("时间段格式错误，预约失败");
            return;
        }

        if (hasReservationConflict(reservations, deviceId, date, timeSlot)) {
            System.out.println("该设备在这个日期和时间段已有预约冲突，预约失败");
            return;
        }

        int newId = reservations.size() + 1;
        Reservation reservation = new Reservation(newId, deviceId, userName, date, timeSlot);
        reservations.add(reservation);

        System.out.println("预约新增成功");
    }

    public static void searchReservationsByDeviceId(Scanner scanner, ArrayList<Reservation> reservations) {
        System.out.print("请输入要查询的设备编号：");
        int deviceId = scanner.nextInt();

        boolean found = false;

        System.out.println("查询结果：");
        System.out.println("--------------------");

        for (Reservation reservation : reservations) {
            if (reservation.getDeviceId() == deviceId) {
                reservation.printInfo();
                found = true;
            }
        }

        if (!found) {
            System.out.println("该设备暂无预约记录");
        }
    }

    public static void deleteReservation(Scanner scanner, ArrayList<Reservation> reservations) {
        System.out.print("请输入要删除的预约编号：");
        int reservationId = scanner.nextInt();

        Reservation targetReservation = null;

        for (Reservation reservation : reservations) {
            if (reservation.getId() == reservationId) {
                targetReservation = reservation;
                break;
            }
        }

        if (targetReservation == null) {
            System.out.println("预约记录不存在，删除失败");
            return;
        }

        reservations.remove(targetReservation);
        System.out.println("预约记录删除成功");
    }

    public static void updateDeviceStatus(Scanner scanner, ArrayList<Device> devices, DeviceDao deviceDao) throws Exception {
    System.out.print("请输入要修改状态的设备编号：");
    int deviceId = scanner.nextInt();

    Device selectedDevice = findDeviceById(devices, deviceId);

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

    public static void searchReservationsByDate(Scanner scanner, ArrayList<Reservation> reservations) {
        System.out.print("请输入要查询的预约日期，例如 2026-07-09：");
        String date = scanner.next();

        boolean found = false;

        System.out.println("查询结果：");
        System.out.println("--------------------");

        for (Reservation reservation : reservations) {
            if (reservation.getDate().equals(date)) {
                reservation.printInfo();
                found = true;
            }
        }

        if (!found) {
            System.out.println("该日期暂无预约记录");
        }
    }

    public static Device findDeviceById(ArrayList<Device> devices, int deviceId) {
        for (Device device : devices) {
            if (device.getId() == deviceId) {
                return device;
            }
        }

        return null;
    }

    public static boolean hasReservationConflict(ArrayList<Reservation> reservations, int deviceId, String date, String newTimeSlot) {
        for (Reservation reservation : reservations) {
            if (reservation.getDeviceId() == deviceId && reservation.getDate().equals(date)) {
                if (isTimeOverlap(reservation.getTimeSlot(), newTimeSlot)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isTimeOverlap(String existingTimeSlot, String newTimeSlot) {
        String[] existingParts = existingTimeSlot.split("-");
        String[] newParts = newTimeSlot.split("-");

        int existingStart = convertTimeToMinutes(existingParts[0]);
        int existingEnd = convertTimeToMinutes(existingParts[1]);
        int newStart = convertTimeToMinutes(newParts[0]);
        int newEnd = convertTimeToMinutes(newParts[1]);

        return newStart < existingEnd && existingStart < newEnd;
    }

    public static boolean isValidTimeSlot(String timeSlot) {
        String[] parts = timeSlot.split("-");

        if (parts.length != 2) {
            return false;
        }

        try {
            int start = convertTimeToMinutes(parts[0]);
            int end = convertTimeToMinutes(parts[1]);

            return start < end;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidTime(String time) {
        String[] parts = time.split(":");

        if (parts.length != 2) {
            return false;
        }

        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        return hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59;
    }

    public static int convertTimeToMinutes(String time) {
        if (!isValidTime(time)) {
            throw new IllegalArgumentException("Invalid time format");
        }

        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        return hour * 60 + minute;
    }
}