package com.btbmina.gamestore.service;

import com.btbmina.gamestore.DB.DatabaseConnection;
import com.btbmina.gamestore.classes.Game;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryService {
    private Connection connection;

    public LibraryService() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add a game to the user's library
    public boolean addGameToLibrary(int userId, Game game) {
        String query = "INSERT INTO library (user_id, game_id) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, game.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Remove a game from the user's library
    public boolean removeGameFromLibrary(int userId, int gameId) {
        String query = "DELETE FROM library WHERE user_id = ? AND game_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, gameId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all games in a user's library
    public List<Game> getGamesInLibrary(int userId) {
        List<Game> games = new ArrayList<>();
        String query = "SELECT g.id, g.title, g.description, g.price, g.category, g.rating, g.system_requirements " +
                "FROM games g INNER JOIN library l ON g.id = l.game_id WHERE l.user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                int gameId = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                String category = resultSet.getString("category");
                double rating = resultSet.getDouble("rating");
                String systemRequirements = resultSet.getString("system_requirements");

                Game game = new Game(gameId, title, description, price, category, rating, systemRequirements);
                games.add(game);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return games;
    }

    // Check if a game is in a user's library
    public boolean isGameInLibrary(int userId, int gameId) {
        String query = "SELECT COUNT(*) FROM library WHERE user_id = ? AND game_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, gameId);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
