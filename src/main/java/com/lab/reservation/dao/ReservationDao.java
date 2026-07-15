package com.lab.reservation.dao;

import com.lab.reservation.entity.Reservation;
import com.lab.reservation.util.DbUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class ReservationDao {
    public ArrayList<Reservation> findAll() throws Exception {
        String sql = "SELECT id, device_id, user_name, reservation_date, start_time, end_time FROM reservation ORDER BY id";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            ArrayList<Reservation> reservations = new ArrayList<>();

            while (resultSet.next()) {
                reservations.add(mapToReservation(resultSet));
            }

            return reservations;
        }
    }

    public Reservation findById(int id) throws Exception {
        String sql = "SELECT id, device_id, user_name, reservation_date, start_time, end_time FROM reservation WHERE id = ?";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToReservation(resultSet);
                }

                return null;
            }
        }
    }

    public ArrayList<Reservation> findByDeviceId(int deviceId) throws Exception {
        String sql = "SELECT id, device_id, user_name, reservation_date, start_time, end_time FROM reservation WHERE device_id = ? ORDER BY reservation_date, start_time";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, deviceId);

            try (ResultSet resultSet = statement.executeQuery()) {
                ArrayList<Reservation> reservations = new ArrayList<>();

                while (resultSet.next()) {
                    reservations.add(mapToReservation(resultSet));
                }

                return reservations;
            }
        }
    }

    public ArrayList<Reservation> findByDate(String reservationDate) throws Exception {
        String sql = "SELECT id, device_id, user_name, reservation_date, start_time, end_time FROM reservation WHERE reservation_date = ? ORDER BY device_id, start_time";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setDate(1, Date.valueOf(reservationDate));

            try (ResultSet resultSet = statement.executeQuery()) {
                ArrayList<Reservation> reservations = new ArrayList<>();

                while (resultSet.next()) {
                    reservations.add(mapToReservation(resultSet));
                }

                return reservations;
            }
        }
    }

    public int add(Reservation reservation) throws Exception {
        String sql = "INSERT INTO reservation (device_id, user_name, reservation_date, start_time, end_time) VALUES (?, ?, ?, ?, ?)";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setInt(1, reservation.getDeviceId());
            statement.setString(2, reservation.getUserName());
            statement.setDate(3, Date.valueOf(reservation.getDate()));
            statement.setTime(4, Time.valueOf(reservation.getStartTime() + ":00"));
            statement.setTime(5, Time.valueOf(reservation.getEndTime() + ":00"));

            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }

                return 0;
            }
        }
    }

    public boolean update(Reservation reservation) throws Exception {
        String sql = "UPDATE reservation SET device_id = ?, user_name = ?, reservation_date = ?, start_time = ?, end_time = ? WHERE id = ?";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, reservation.getDeviceId());
            statement.setString(2, reservation.getUserName());
            statement.setDate(3, Date.valueOf(reservation.getDate()));
            statement.setTime(4, Time.valueOf(reservation.getStartTime() + ":00"));
            statement.setTime(5, Time.valueOf(reservation.getEndTime() + ":00"));
            statement.setInt(6, reservation.getId());

            return statement.executeUpdate() > 0;
        }
    }

    public boolean deleteById(int id) throws Exception {
        String sql = "DELETE FROM reservation WHERE id = ?";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean hasConflict(int deviceId, String reservationDate, String startTime, String endTime, int excludeReservationId) throws Exception {
        String sql = """
                SELECT COUNT(*) AS count
                FROM reservation
                WHERE device_id = ?
                  AND reservation_date = ?
                  AND id <> ?
                  AND start_time < ?
                  AND end_time > ?
                """;

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, deviceId);
            statement.setDate(2, Date.valueOf(reservationDate));
            statement.setInt(3, excludeReservationId);
            statement.setTime(4, Time.valueOf(endTime + ":00"));
            statement.setTime(5, Time.valueOf(startTime + ":00"));

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt("count") > 0;
            }
        }
    }

    public boolean existsByDeviceId(int deviceId) throws Exception {
        String sql = "SELECT COUNT(*) AS count FROM reservation WHERE device_id = ?";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, deviceId);

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt("count") > 0;
            }
        }
    }

    private Reservation mapToReservation(ResultSet resultSet) throws Exception {
        return new Reservation(
                resultSet.getInt("id"),
                resultSet.getInt("device_id"),
                resultSet.getString("user_name"),
                resultSet.getDate("reservation_date").toString(),
                resultSet.getTime("start_time").toString().substring(0, 5),
                resultSet.getTime("end_time").toString().substring(0, 5)
        );
    }
}