package com.lab.reservation.dao;

import com.lab.reservation.entity.Device;
import com.lab.reservation.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DeviceDao {

    public ArrayList<Device> findAll() throws Exception {
        ArrayList<Device> devices = new ArrayList<>();

        String sql = "SELECT id, name, type, status FROM device ORDER BY id";

        try (
                Connection connection = DbUtil.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ) {
            while (resultSet.next()) {
                devices.add(mapToDevice(resultSet));
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
                    return mapToDevice(resultSet);
                }
            }
        }

        return null;
    }

    public ArrayList<Device> findByNameKeyword(String keyword) throws Exception {
        ArrayList<Device> devices = new ArrayList<>();

        String sql = "SELECT id, name, type, status FROM device WHERE name LIKE ? ORDER BY id";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, "%" + keyword + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    devices.add(mapToDevice(resultSet));
                }
            }
        }

        return devices;
    }

    public ArrayList<Device> findByStatus(String status) throws Exception {
        ArrayList<Device> devices = new ArrayList<>();

        String sql = "SELECT id, name, type, status FROM device WHERE status = ? ORDER BY id";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, status);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    devices.add(mapToDevice(resultSet));
                }
            }
        }

        return devices;
    }

    public int add(Device device) throws Exception {
        String sql = "INSERT INTO device (name, type, status) VALUES (?, ?, ?)";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setString(1, device.getName());
            preparedStatement.setString(2, device.getType());
            preparedStatement.setString(3, device.getStatus());

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

    public boolean update(Device device) throws Exception {
        String sql = "UPDATE device SET name = ?, type = ?, status = ? WHERE id = ?";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, device.getName());
            preparedStatement.setString(2, device.getType());
            preparedStatement.setString(3, device.getStatus());
            preparedStatement.setInt(4, device.getId());

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        }
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

    public boolean deleteById(int id) throws Exception {
        String sql = "DELETE FROM device WHERE id = ?";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, id);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        }
    }

    private Device mapToDevice(ResultSet resultSet) throws Exception {
        return new Device(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("type"),
                resultSet.getString("status")
        );
    }
}