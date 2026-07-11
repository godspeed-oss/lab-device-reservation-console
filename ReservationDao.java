import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ReservationDao {
    private String password;

    public ReservationDao(String password) {
        this.password = password;
    }

    public ArrayList<Reservation> findAll() throws Exception {
        ArrayList<Reservation> reservations = new ArrayList<>();

        String url = "jdbc:mysql://localhost:3306/lab_reservation_db?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8";
        String user = "root";

        Connection connection = DriverManager.getConnection(url, user, password);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM reservation");

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            int deviceId = resultSet.getInt("device_id");
            String userName = resultSet.getString("user_name");
            String date = resultSet.getDate("reservation_date").toString();

            String startTime = resultSet.getTime("start_time").toString().substring(0, 5);
            String endTime = resultSet.getTime("end_time").toString().substring(0, 5);
            String timeSlot = startTime + "-" + endTime;

            Reservation reservation = new Reservation(id, deviceId, userName, date, timeSlot);
            reservations.add(reservation);
        }

        resultSet.close();
        statement.close();
        connection.close();

        return reservations;
    }

    public int insert(Reservation reservation) throws Exception {
        String url = "jdbc:mysql://localhost:3306/lab_reservation_db?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8";
        String user = "root";

        Connection connection = DriverManager.getConnection(url, user, password);

        String[] timeParts = reservation.getTimeSlot().split("-");
        String startTime = timeParts[0] + ":00";
        String endTime = timeParts[1] + ":00";

        String sql = "INSERT INTO reservation (device_id, user_name, reservation_date, start_time, end_time) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setInt(1, reservation.getDeviceId());
        preparedStatement.setString(2, reservation.getUserName());
        preparedStatement.setString(3, reservation.getDate());
        preparedStatement.setString(4, startTime);
        preparedStatement.setString(5, endTime);

        preparedStatement.executeUpdate();

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        int newId = 0;

        if (generatedKeys.next()) {
            newId = generatedKeys.getInt(1);
        }

        generatedKeys.close();
        preparedStatement.close();
        connection.close();

        return newId;
    }
}