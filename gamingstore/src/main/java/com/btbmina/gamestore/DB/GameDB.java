package com.btbmina.gamestore.DB;
import com.btbmina.gamestore.classes.Game;
import com.btbmina.gamestore.classes.User;
import java.sql.*;
public class GameDB {
    private static final String URL = "jdbc:mysql://localhost:3306/btbmina_games";
    private static final String USER = "root";
    private static final String PASSWORD = "2004";

    public static Game getGameById(int gameId) {
        String query = "SELECT * FROM games WHERE game_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, gameId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Game(
                        rs.getInt("game_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getString("category"),
                        rs.getDouble("rating"),
                        rs.getString("system_requirements")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean insertGame(Game game) {
        String query = "INSERT INTO games (title, description, price, category, rating, system_requirements) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, game.getTitle());
            stmt.setString(2, game.getDescription());
            stmt.setDouble(3, game.getPrice());
            stmt.setString(4, game.getCategory());
            stmt.setDouble(5, game.getRating());
            stmt.setString(6, game.getSystemRequirements());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}