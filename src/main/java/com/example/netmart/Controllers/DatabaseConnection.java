package com.example.netmart.Controllers;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
//    public Connection databaseLink;
//    public Connection getConnection(){
//        String databaseName = "netmart";
//        String databaseUser = "root";
//        String databasePassword = "network123";
//        String url = "jdbc:mysql://localhost/" + databaseName;
//
//        try{
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
//        } catch(Exception e){
//            e.printStackTrace();
//        }
//
//        return databaseLink;
//    }

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/netmart";
        String user = "root";
        String password = "network123";
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }
}
