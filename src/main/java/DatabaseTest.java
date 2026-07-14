import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseTest {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("请在运行时传入 MySQL 密码");
            System.out.println("示例：java -cp \".;lib\\mysql-connector-j-9.7.0.jar\" DatabaseTest your_password");
            return;
        }

        String url = "jdbc:mysql://localhost:3306/lab_reservation_db?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8";
        String user = "root";
        String password = args[0];

        Connection connection = DriverManager.getConnection(url, user, password);

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM device");

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String type = resultSet.getString("type");
            String status = resultSet.getString("status");

            System.out.println(id + " " + name + " " + type + " " + status);
        }

        resultSet.close();
        statement.close();
        connection.close();
    }
}