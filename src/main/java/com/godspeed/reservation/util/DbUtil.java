package com.godspeed.reservation.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
public class DbUtil {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream inputStream = DbUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("db.properties not found");
            }

            PROPERTIES.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load database configuration", e);
        }
    }

    public static Connection getConnection(String password) throws Exception {
        String url = PROPERTIES.getProperty("db.url");
        String user = PROPERTIES.getProperty("db.user");

        return DriverManager.getConnection(url, user, password);
    }
}