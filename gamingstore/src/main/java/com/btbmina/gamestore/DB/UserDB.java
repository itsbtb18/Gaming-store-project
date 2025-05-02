package com.btbmina.gamestore.DB;

import com.btbmina.gamestore.classes.User;

import java.sql.Connection;
import java.sql.*;

public class UserDB{
    private static final String URL = "jdbc:mysql://localhost:3306/btbmina_games"; // Adjust based on your DB
    private static final String USER = "root"; // DB username
    private static final String PASSWORD = "2004"; // DB password

    public static User getUserById(int userId) {
        String query = "SELECT * FROM users WHERE users.user_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password")  // was "password_hash"
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean insertUser(User user) {
        String query = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
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
    public static boolean checkCredentials(String username, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password); // Dans un vrai cas, tu devrais comparer le mot de passe haché

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Retourne true si un utilisateur est trouvé
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Relancer l'exception
        }
    }

    // Additional methods for updating and deleting users can be added similarly.

}
