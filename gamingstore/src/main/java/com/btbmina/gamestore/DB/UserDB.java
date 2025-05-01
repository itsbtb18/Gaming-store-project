package com.btbmina.gamestore.DB;

import com.btbmina.gamestore.classes.User;

import java.sql.Connection;
import java.sql.*;

public class UserDB{
    private static final String URL = "jdbc:mysql://localhost:3306/gamestore"; // Adjust based on your DB
    private static final String USER = "root"; // DB username
    private static final String PASSWORD = "password"; // DB password

    public static User getUserById(int userId) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("user_id"), rs.getString("username"),
                        rs.getString("email"), rs.getString("password_hash"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean insertUser(User user) {
        String query = "INSERT INTO users (username, email, password_hash) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Additional methods for updating and deleting users can be added similarly.

}
