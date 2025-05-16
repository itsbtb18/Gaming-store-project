package com.btbmina.gamestore.service;

import com.btbmina.gamestore.DB.DatabaseConnection;
import com.btbmina.gamestore.classes.Game;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameService {
    private Connection connection;

    public GameService() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add a new game to the database
    public boolean addGame(Game game) {
        String query = "INSERT INTO games (title, description, price, category, rating, system_requirements, path_image) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, game.getTitle());
            stmt.setString(2, game.getDescription());
            stmt.setDouble(3, game.getPrice());
            stmt.setString(4, game.getCategory());
            stmt.setDouble(5, game.getRating());
            stmt.setString(6, game.getSystemRequirements());
            stmt.setString(7, game.getPath_image());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update game details in the database
    public boolean updateGame(Game game) {
        String query = "UPDATE games SET title = ?, description = ?, price = ?, category = ?, rating = ?, system_requirements = ?, path_image = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, game.getTitle());
            stmt.setString(2, game.getDescription());
            stmt.setDouble(3, game.getPrice());
            stmt.setString(4, game.getCategory());
            stmt.setDouble(5, game.getRating());
            stmt.setString(6, game.getSystemRequirements());
            stmt.setString(7, game.getPath_image());
            stmt.setInt(8, game.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a game from the database by its ID
    public boolean deleteGame(int gameId) {
        String query = "DELETE FROM games WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, gameId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get a game by its ID
    public Game getGameById(int gameId) {
        String query = "SELECT * FROM games WHERE id = ?";
        Game game = null;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, gameId);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                game = new Game(
                        gameId,
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getDouble("price"),
                        resultSet.getString("category"),
                        resultSet.getDouble("rating"),
                        resultSet.getString("system_requirements"),
                        resultSet.getString("path_image")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return game;
    }

    // Get all games from the database
    public List<Game> getAllGames() {
        List<Game> games = new ArrayList<>();
        String query = "SELECT * FROM games";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Game game = new Game(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getString("category"),
                        rs.getDouble("rating"),
                        rs.getString("system_requirements"),
                        rs.getString("path_image")
                );
                games.add(game);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return games;
    }
}
