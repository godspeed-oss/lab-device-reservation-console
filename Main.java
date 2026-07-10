import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayList<Device> devices = new ArrayList<>();
        ArrayList<Reservation> reservations = new ArrayList<>();

        devices.add(new Device(1, "显微镜", "光学设备", "可预约"));
        devices.add(new Device(2, "3D打印机", "制造设备", "维修中"));
        devices.add(new Device(3, "高速离心机", "实验设备", "可预约"));

        reservations.add(new Reservation(1, 1, "张三", "2026-07-09", "09:00-11:00"));

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("实验室设备预约系统");
            System.out.println("1. 查看设备列表");
            System.out.println("2. 查看预约记录");
            System.out.println("3. 新增预约");
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

        if (hasReservationConflict(reservations, deviceId, date, timeSlot)) {
            System.out.println("该设备在这个日期和时间段已有预约冲突，预约失败");
            return;
        }

        int newId = reservations.size() + 1;
        Reservation reservation = new Reservation(newId, deviceId, userName, date, timeSlot);
        reservations.add(reservation);

        System.out.println("预约新增成功");
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

    public static int convertTimeToMinutes(String time) {
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        return hour * 60 + minute;
    }
}