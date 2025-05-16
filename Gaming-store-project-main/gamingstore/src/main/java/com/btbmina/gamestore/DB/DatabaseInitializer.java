package com.btbmina.gamestore.DB;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * Class responsible for initializing database schema
 */
public class DatabaseInitializer {

    /**
     * Initialize database schema
     * @return true if initialization was successful, false otherwise
     */
    public static boolean initialize() {
        System.out.println("Starting database initialization...");

        // Then create tables
        if (!createTables()) {
            System.err.println("Failed to create tables!");
            return false;
        }

        System.out.println("Database initialization completed successfully!");
        return true;
    }

    /**
     * Create the database if it doesn't exist
     */

    /**
     * Create tables in the database
     */
    private static boolean createTables() {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {

            // Create users table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "user_id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "username VARCHAR(50) NOT NULL UNIQUE, " +
                            "email VARCHAR(100) NOT NULL UNIQUE, " +
                            "password VARCHAR(255) NOT NULL, " +
                            "registration_date DATETIME, " +
                            "is_verified BOOLEAN DEFAULT FALSE, " +
                            "verification_token VARCHAR(100), " +
                            "profile_image MEDIUMBLOB, " +
                            "last_login DATETIME" +
                            ")"
            );

            // Create games table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS games (" +
                            "game_id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "title VARCHAR(100) NOT NULL, " +
                            "description TEXT, " +
                            "price DOUBLE NOT NULL, " +
                            "category VARCHAR(50), " +
                            "rating DOUBLE DEFAULT 0.0, " +
                            "system_requirements TEXT" +
                            ")"
            );

            // Create purchases table
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS purchases (" +
                            "purchase_id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "user_id INT NOT NULL, " +
                            "game_id INT NOT NULL, " +
                            "registration_date DATETIME, " +
                            "price DOUBLE NOT NULL, " +
                            "payment_method VARCHAR(50), " +
                            "FOREIGN KEY (user_id) REFERENCES users(user_id), " +
                            "FOREIGN KEY (game_id) REFERENCES games(game_id)" +
                            ")"
            );

            System.out.println("Tables created successfully.");
            return true;

        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}