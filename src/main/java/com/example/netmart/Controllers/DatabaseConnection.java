package com.example.netmart.Controllers;

import java.sql.*;

public class DatabaseConnection {

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/sales?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Africa/Accra";
        String user = "root";
        String password = "manandnation12@SQL";
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }

    public ResultSet getAdminData(String name, String password){
        Statement statement;
        ResultSet resultSet = null;
        try{
            statement = getConnection().createStatement();
            String query = "SELECT * FROM users WHERE name= '" + name + "' AND password = '" + password + "';";
            System.out.println("Query: " + query);
            resultSet = statement.executeQuery(query);
            System.out.println("RS: " + resultSet.toString());
        }catch (Exception ex){
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
        return resultSet;
    }
}
