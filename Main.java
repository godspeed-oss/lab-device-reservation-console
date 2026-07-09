public class Main {
    public static void main(String[] args) {
        Device device1 = new Device(1, "显微镜", "光学设备", "可预约");
        Device device2 = new Device(2, "3D打印机", "制造设备", "维修中");

        device1.printInfo();
        device2.printInfo();
    }
}
