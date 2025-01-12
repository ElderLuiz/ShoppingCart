package model.entities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import Exceptions.DbException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/challenge";
    private static final String USER = "root";
    private static final String PASSWORD = "1234567";


    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
        	throw new DbException("Connection failed: "+e.getMessage());
        }
    }

  
    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            System.out.println("Connection successful!");
        } catch (SQLException e) {
        	throw new DbException("Connection failed: " + e.getMessage());
        }
    }
}

