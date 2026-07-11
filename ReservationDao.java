import java.sql.Connection;
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

        Connection connection = DbUtil.getConnection(password);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM reservation");

        while (resultSet.next()) {
            Reservation reservation = createReservationFromResultSet(resultSet);
            reservations.add(reservation);
        }

        resultSet.close();
        statement.close();
        connection.close();

        return reservations;
    }

    public ArrayList<Reservation> findByDeviceId(int deviceId) throws Exception {
        ArrayList<Reservation> reservations = new ArrayList<>();

        Connection connection = DbUtil.getConnection(password);

        String sql = "SELECT * FROM reservation WHERE device_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, deviceId);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Reservation reservation = createReservationFromResultSet(resultSet);
            reservations.add(reservation);
        }

        resultSet.close();
        preparedStatement.close();
        connection.close();

        return reservations;
    }

    public ArrayList<Reservation> findByDate(String date) throws Exception {
        ArrayList<Reservation> reservations = new ArrayList<>();

        Connection connection = DbUtil.getConnection(password);

        String sql = "SELECT * FROM reservation WHERE reservation_date = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, date);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Reservation reservation = createReservationFromResultSet(resultSet);
            reservations.add(reservation);
        }

        resultSet.close();
        preparedStatement.close();
        connection.close();

        return reservations;
    }

    public int insert(Reservation reservation) throws Exception {
        Connection connection = DbUtil.getConnection(password);

        String[] timeParts = reservation.getTimeSlot().split("-");
        String startTime = timeParts[0] + ":00";
        String endTime = timeParts[1] + ":00";

        String sql = "INSERT INTO reservation "
                + "(device_id, user_name, reservation_date, start_time, end_time) "
                + "VALUES (?, ?, ?, ?, ?)";

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

    public void deleteById(int reservationId) throws Exception {
        Connection connection = DbUtil.getConnection(password);

        String sql = "DELETE FROM reservation WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, reservationId);

        preparedStatement.executeUpdate();

        preparedStatement.close();
        connection.close();
    }

    private Reservation createReservationFromResultSet(ResultSet resultSet) throws Exception {
        int id = resultSet.getInt("id");
        int deviceId = resultSet.getInt("device_id");
        String userName = resultSet.getString("user_name");
        String date = resultSet.getDate("reservation_date").toString();

        String startTime = resultSet.getTime("start_time").toString().substring(0, 5);
        String endTime = resultSet.getTime("end_time").toString().substring(0, 5);
        String timeSlot = startTime + "-" + endTime;

        return new Reservation(id, deviceId, userName, date, timeSlot);
    }
}