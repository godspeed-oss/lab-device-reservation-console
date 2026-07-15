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
        String sql = "SELECT id, name, type, status FROM device ORDER BY id";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            ArrayList<Device> devices = new ArrayList<>();

            while (resultSet.next()) {
                devices.add(mapToDevice(resultSet));
            }

            return devices;
        }
    }

    public Device findById(int id) throws Exception {
        String sql = "SELECT id, name, type, status FROM device WHERE id = ?";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToDevice(resultSet);
                }

                return null;
            }
        }
    }

    public ArrayList<Device> findByName(String keyword) throws Exception {
        String sql = "SELECT id, name, type, status FROM device WHERE name LIKE ? ORDER BY id";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, "%" + keyword + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                ArrayList<Device> devices = new ArrayList<>();

                while (resultSet.next()) {
                    devices.add(mapToDevice(resultSet));
                }

                return devices;
            }
        }
    }

    public ArrayList<Device> findByNameKeyword(String keyword) throws Exception {
        return findByName(keyword);
    }

    public ArrayList<Device> findByStatus(String status) throws Exception {
        String sql = "SELECT id, name, type, status FROM device WHERE status = ? ORDER BY id";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, status);

            try (ResultSet resultSet = statement.executeQuery()) {
                ArrayList<Device> devices = new ArrayList<>();

                while (resultSet.next()) {
                    devices.add(mapToDevice(resultSet));
                }

                return devices;
            }
        }
    }

    public int add(Device device) throws Exception {
        String sql = "INSERT INTO device (name, type, status) VALUES (?, ?, ?)";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, device.getName());
            statement.setString(2, device.getType());
            statement.setString(3, device.getStatus());

            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }

                return 0;
            }
        }
    }

    public boolean update(Device device) throws Exception {
        String sql = "UPDATE device SET name = ?, type = ?, status = ? WHERE id = ?";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, device.getName());
            statement.setString(2, device.getType());
            statement.setString(3, device.getStatus());
            statement.setInt(4, device.getId());

            return statement.executeUpdate() > 0;
        }
    }

    public boolean updateStatus(int id, String status) throws Exception {
        String sql = "UPDATE device SET status = ? WHERE id = ?";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, status);
            statement.setInt(2, id);

            return statement.executeUpdate() > 0;
        }
    }

    public boolean deleteById(int id) throws Exception {
        String sql = "DELETE FROM device WHERE id = ?";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    public int countAll() throws Exception {
        String sql = "SELECT COUNT(*) AS count FROM device";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            resultSet.next();
            return resultSet.getInt("count");
        }
    }

    public int countByStatus(String status) throws Exception {
        String sql = "SELECT COUNT(*) AS count FROM device WHERE status = ?";

        try (
                Connection connection = DbUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, status);

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt("count");
            }
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