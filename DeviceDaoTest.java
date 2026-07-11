import java.util.ArrayList;

public class DeviceDaoTest {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("请在运行时传入 MySQL 密码");
            return;
        }

        DeviceDao deviceDao = new DeviceDao(args[0]);
        ArrayList<Device> devices = deviceDao.findAll();

        for (Device device : devices) {
            device.printInfo();
        }
    }
}