import java.sql.Connection;
import java.sql.DriverManager;

public class DbUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/lab_reservation_db?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8";
    private static final String USER = "root";

    public static Connection getConnection(String password) throws Exception {
        return DriverManager.getConnection(URL, USER, password);
    }
}