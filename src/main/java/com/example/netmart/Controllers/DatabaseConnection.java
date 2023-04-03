package com.example.netmart.Controllers;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/netmart";
        String user = "root";
        String password = "network123";
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }
}
