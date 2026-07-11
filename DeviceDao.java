import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DeviceDao {
    private String password;

    public DeviceDao(String password) {
        this.password = password;
    }

    public ArrayList<Device> findAll() throws Exception {
        ArrayList<Device> devices = new ArrayList<>();

        String url = "jdbc:mysql://localhost:3306/lab_reservation_db?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8";
        String user = "root";

        Connection connection = DriverManager.getConnection(url, user, password);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM device");

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String type = resultSet.getString("type");
            String status = resultSet.getString("status");

            Device device = new Device(id, name, type, status);
            devices.add(device);
        }

        resultSet.close();
        statement.close();
        connection.close();

        return devices;
    }
}