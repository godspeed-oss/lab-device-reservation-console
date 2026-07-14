package com.godspeed.reservation.dao;

import com.godspeed.reservation.entity.Device;
import com.godspeed.reservation.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DeviceDao {

    public ArrayList<Device> findAll() throws Exception {
        ArrayList<Device> devices = new ArrayList<>();

        String sql = "SELECT id, name, type, status FROM device";

        try (
                Connection connection = DbUtil.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ) {
            while (resultSet.next()) {
                Device device = new Device(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("type"),
                        resultSet.getString("status")
                );
                devices.add(device);
            }
        }

        return devices;
    }

    public Device findById(int id) throws Exception {
        String sql = "SELECT id, name, type, status FROM device WHERE id = ?";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Device(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("type"),
                            resultSet.getString("status")
                    );
                }
            }
        }

        return null;
    }

    public boolean updateStatus(int id, String status) throws Exception {
        String sql = "UPDATE device SET status = ? WHERE id = ?";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, id);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        }
    }
}