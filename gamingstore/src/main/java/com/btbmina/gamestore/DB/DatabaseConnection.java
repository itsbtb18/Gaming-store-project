package com.btbmina.gamestore.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages database connections for the application
 */
public class DatabaseConnection {
    // Database credentials - made public for DatabaseInitializer
    public static final String DATABASE_URL = "jdbc:mysql://sql7.freesqldatabase.com:3306/sql7779083";
    public static final String DATABASE_USER = "sql7779083";
    public static final String DATABASE_PASSWORD = "Hdm6dRtXQF";

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }

    /**
     * Get singleton instance of the DatabaseConnection
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Get a connection to the database
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(
                        DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
                System.out.println("Database connection established.");
            } catch (SQLException e) {
                System.err.println("Failed to connect to database!");
                e.printStackTrace();
                throw e;
            }
        }
        return connection;
    }

    /**
     * Close the database connection
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing database connection");
                e.printStackTrace();
            }
        }
    }

    /**
     * Check if the database connection is active
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}