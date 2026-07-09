import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Device> devices = new ArrayList<>();

        devices.add(new Device(1, "显微镜", "光学设备", "可预约"));
        devices.add(new Device(2, "3D打印机", "制造设备", "维修中"));
        devices.add(new Device(3, "高速离心机", "实验设备", "可预约"));

        System.out.println("实验室设备列表：");
        System.out.println("--------------------");

        for (Device device : devices) {
            device.printInfo();
        }
    }
}