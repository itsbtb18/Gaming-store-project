package com.btbmina.gamestore.service;

import com.btbmina.gamestore.classes.User;
import com.btbmina.gamestore.DB.DatabaseConnection;
import javax.swing.*;
import java.sql.*;

public class AuthService {
    private Connection connection;

    public AuthService() {
        try {
            // Get the singleton database connection
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database connection error: " + e.getMessage(),
                    "Connection Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
    // Register a new user
    public boolean registerUser(String username, String email, String password) {
        String query = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error registering user: " + e.getMessage(),
                    "Registration Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Authenticate the user during login
    public User authenticateUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                // User found
                return true;
            } else {
                // Invalid credentials
                JOptionPane.showMessageDialog(null, "Invalid username or password.",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error authenticating user: " + e.getMessage(),
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Get user data by username (to load user profile)
    public User getUserData(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                // Return user object
                return new User(username, email, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching user data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    // Update user password
    public boolean updatePassword(String username, String oldPassword, String newPassword) {
        String query = "UPDATE users SET password = ? WHERE username = ? AND password = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            stmt.setString(3, oldPassword);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating password: " + e.getMessage(),
                    "Password Update Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
