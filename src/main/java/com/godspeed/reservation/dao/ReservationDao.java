package com.godspeed.reservation.dao;

import com.godspeed.reservation.entity.Reservation;
import com.godspeed.reservation.util.DbUtil;

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

        String sql = "SELECT id, device_id, user_name, reservation_date, start_time, end_time FROM reservation";

        try (
                Connection connection = DbUtil.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ) {
            while (resultSet.next()) {
                Reservation reservation = new Reservation(
                        resultSet.getInt("id"),
                        resultSet.getInt("device_id"),
                        resultSet.getString("user_name"),
                        resultSet.getDate("reservation_date").toString(),
                        resultSet.getTime("start_time").toString(),
                        resultSet.getTime("end_time").toString()
                );
                reservations.add(reservation);
            }
        }

        return reservations;
    }

    public ArrayList<Reservation> findByDeviceId(int deviceId) throws Exception {
        ArrayList<Reservation> reservations = new ArrayList<>();

        String sql = "SELECT id, device_id, user_name, reservation_date, start_time, end_time FROM reservation WHERE device_id = ?";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, deviceId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Reservation reservation = new Reservation(
                            resultSet.getInt("id"),
                            resultSet.getInt("device_id"),
                            resultSet.getString("user_name"),
                            resultSet.getDate("reservation_date").toString(),
                            resultSet.getTime("start_time").toString(),
                            resultSet.getTime("end_time").toString()
                    );
                    reservations.add(reservation);
                }
            }
        }

        return reservations;
    }

    public ArrayList<Reservation> findByDate(String reservationDate) throws Exception {
        ArrayList<Reservation> reservations = new ArrayList<>();

        String sql = "SELECT id, device_id, user_name, reservation_date, start_time, end_time FROM reservation WHERE reservation_date = ?";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setDate(1, Date.valueOf(reservationDate));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Reservation reservation = new Reservation(
                            resultSet.getInt("id"),
                            resultSet.getInt("device_id"),
                            resultSet.getString("user_name"),
                            resultSet.getDate("reservation_date").toString(),
                            resultSet.getTime("start_time").toString(),
                            resultSet.getTime("end_time").toString()
                    );
                    reservations.add(reservation);
                }
            }
        }

        return reservations;
    }

    public boolean add(Reservation reservation) throws Exception {
        String sql = "INSERT INTO reservation (device_id, user_name, reservation_date, start_time, end_time) VALUES (?, ?, ?, ?, ?)";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, reservation.getDeviceId());
            preparedStatement.setString(2, reservation.getUserName());
            preparedStatement.setDate(3, Date.valueOf(reservation.getReservationDate()));
            preparedStatement.setTime(4, Time.valueOf(reservation.getStartTime() + ":00"));
            preparedStatement.setTime(5, Time.valueOf(reservation.getEndTime() + ":00"));

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
}