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
}