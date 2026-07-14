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

public class ReservationDao {

    public ArrayList<Reservation> findAll() throws Exception {
        ArrayList<Reservation> reservations = new ArrayList<>();

        String sql = "SELECT id, device_id, user_name, reservation_date, start_time, end_time FROM reservation ORDER BY reservation_date, start_time, id";

        try (
                Connection connection = DbUtil.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ) {
            while (resultSet.next()) {
                reservations.add(mapToReservation(resultSet));
            }
        }

        return reservations;
    }

    public Reservation findById(int id) throws Exception {
        String sql = "SELECT id, device_id, user_name, reservation_date, start_time, end_time FROM reservation WHERE id = ?";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToReservation(resultSet);
                }
            }
        }

        return null;
    }

    public ArrayList<Reservation> findByDeviceId(int deviceId) throws Exception {
        ArrayList<Reservation> reservations = new ArrayList<>();

        String sql = "SELECT id, device_id, user_name, reservation_date, start_time, end_time FROM reservation WHERE device_id = ? ORDER BY reservation_date, start_time, id";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, deviceId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    reservations.add(mapToReservation(resultSet));
                }
            }
        }

        return reservations;
    }

    public boolean existsByDeviceId(int deviceId) throws Exception {
        String sql = "SELECT COUNT(*) FROM reservation WHERE device_id = ?";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, deviceId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    public ArrayList<Reservation> findByDate(String reservationDate) throws Exception {
        ArrayList<Reservation> reservations = new ArrayList<>();

        String sql = "SELECT id, device_id, user_name, reservation_date, start_time, end_time FROM reservation WHERE reservation_date = ? ORDER BY start_time, id";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setDate(1, Date.valueOf(reservationDate));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    reservations.add(mapToReservation(resultSet));
                }
            }
        }

        return reservations;
    }

    public int countAll() throws Exception {
        String sql = "SELECT COUNT(*) FROM reservation";

        try (
                Connection connection = DbUtil.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }

        return 0;
    }

    public int add(Reservation reservation) throws Exception {
        String sql = "INSERT INTO reservation (device_id, user_name, reservation_date, start_time, end_time) VALUES (?, ?, ?, ?, ?)";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setInt(1, reservation.getDeviceId());
            preparedStatement.setString(2, reservation.getUserName());
            preparedStatement.setDate(3, Date.valueOf(reservation.getReservationDate()));
            preparedStatement.setTime(4, Time.valueOf(reservation.getStartTime() + ":00"));
            preparedStatement.setTime(5, Time.valueOf(reservation.getEndTime() + ":00"));

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                return -1;
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

            return -1;
        }
    }

    public boolean update(Reservation reservation) throws Exception {
        String sql = "UPDATE reservation SET device_id = ?, user_name = ?, reservation_date = ?, start_time = ?, end_time = ? WHERE id = ?";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, reservation.getDeviceId());
            preparedStatement.setString(2, reservation.getUserName());
            preparedStatement.setDate(3, Date.valueOf(reservation.getReservationDate()));
            preparedStatement.setTime(4, Time.valueOf(reservation.getStartTime() + ":00"));
            preparedStatement.setTime(5, Time.valueOf(reservation.getEndTime() + ":00"));
            preparedStatement.setInt(6, reservation.getId());

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        }
    }

    public boolean deleteById(int id) throws Exception {
        String sql = "DELETE FROM reservation WHERE id = ?";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, id);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        }
    }

    private Reservation mapToReservation(ResultSet resultSet) throws Exception {
        return new Reservation(
                resultSet.getInt("id"),
                resultSet.getInt("device_id"),
                resultSet.getString("user_name"),
                resultSet.getDate("reservation_date").toString(),
                resultSet.getTime("start_time").toString(),
                resultSet.getTime("end_time").toString()
        );
    }
}