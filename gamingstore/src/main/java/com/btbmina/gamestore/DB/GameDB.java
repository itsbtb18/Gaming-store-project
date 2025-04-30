package com.btbmina.gamestore.DB;
import com.btbmina.gamestore.classes.Game;
import com.btbmina.gamestore.classes.User;

import java.sql.*;
private static final String URL = "jdbc:mysql://localhost:3306/gamestore"; // Adjust based on your DB
private static final String USER = "root"; // DB username
private static final String PASSWORD = "password"; // DB password

public static Game getGameById(int gameId) {
    String query = "SELECT * FROM games WHERE game_id = ?";
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, gameId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new Game(rs.getInt("game_id"), rs.getString("title"), rs.getString("description"),
                    rs.getDouble("price"), rs.getDouble("rating"), rs.getString("system_requirements"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

public static boolean insertGame(Game game) {
    String query = "INSERT INTO games (title, description, price, rating, system_requirements) VALUES (?, ?, ?, ?, ?)";
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, game.getTitle());
        stmt.setString(2, game.getDescription());
        stmt.setDouble(3, game.getPrice());
        stmt.setDouble(4, game.getRating());
        stmt.setString(5, game.getSystemRequirements());
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

// Additional methods for updating and deleting games can be added similarly.
}