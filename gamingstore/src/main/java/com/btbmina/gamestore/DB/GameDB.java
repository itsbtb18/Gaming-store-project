package com.btbmina.gamestore.DB;
import com.btbmina.gamestore.classes.Game;
import com.btbmina.gamestore.classes.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameDB {
    private static final String URL = "jdbc:mysql://sql7.freesqldatabase.com:3306/sql7779083";
    private static final String USER = "sql7779083";
    private static final String PASSWORD = "Hdm6dRtXQF";

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
                        rs.getString("system_requirements"),
                        rs.getString("path_image")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static List<Game> searchGames(String query) throws SQLException {
        List<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM games WHERE title LIKE ? LIMIT 10";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + query + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Game game = new Game(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getDouble("price"),
                            rs.getString("category"),
                            rs.getDouble("rating"),
                            rs.getString("system_requirements")
                    );
                    games.add(game);
                }
            }
        }
        return games;
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