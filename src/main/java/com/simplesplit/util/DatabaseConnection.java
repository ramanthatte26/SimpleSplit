package com.simplesplit.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConnection {
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        }

        try {
            Properties prop = new Properties();
            try (InputStream inputStream = DatabaseConnection.class.getClassLoader()
                    .getResourceAsStream("config.properties")) {
                if (inputStream == null) {
                    throw new IOException("Unable to find config.properties");
                }
                prop.load(inputStream);
            }

            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.user");
            String password = prop.getProperty("db.password");

            if (url == null || user == null || password == null) {
                throw new IllegalStateException("Database connection properties are missing");
            }

            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Failed to establish database connection", e);
        }

        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}