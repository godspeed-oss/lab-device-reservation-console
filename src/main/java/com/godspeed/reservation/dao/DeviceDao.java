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
    private String password;

    public DeviceDao(String password) {
        this.password = password;
    }

    public ArrayList<Device> findAll() throws Exception {
        ArrayList<Device> devices = new ArrayList<>();

        Connection connection = DbUtil.getConnection(password);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM device");

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String type = resultSet.getString("type");
            String status = resultSet.getString("status");

            Device device = new Device(id, name, type, status);
            devices.add(device);
        }

        resultSet.close();
        statement.close();
        connection.close();

        return devices;
    }

    public void updateStatus(int deviceId, String newStatus) throws Exception {
        Connection connection = DbUtil.getConnection(password);

        String sql = "UPDATE device SET status = ? WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, newStatus);
        preparedStatement.setInt(2, deviceId);

        preparedStatement.executeUpdate();

        preparedStatement.close();
        connection.close();
    }
}