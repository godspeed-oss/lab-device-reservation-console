package com.godspeed.reservation.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DbUtil {
    private static final String CONFIG_FILE = "/db.properties";

    private static String url;
    private static String user;
    private static String password;

    static {
        try (InputStream inputStream = DbUtil.class.getResourceAsStream(CONFIG_FILE)) {
            if (inputStream == null) {
                throw new RuntimeException("未找到数据库配置文件：" + CONFIG_FILE);
            }

            Properties properties = new Properties();
            properties.load(inputStream);

            url = properties.getProperty("db.url");
            user = properties.getProperty("db.user");
            password = properties.getProperty("db.password");

            if (url == null || user == null || password == null) {
                throw new RuntimeException("数据库配置不完整，请检查 db.properties");
            }
        } catch (Exception e) {
            throw new RuntimeException("读取数据库配置失败", e);
        }
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(url, user, password);
    }
}