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
        String query = "INSERT INTO games (title, description, price, category, rating, system_requirements) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, game.getTitle());
            stmt.setString(2, game.getDescription());
            stmt.setDouble(3, game.getPrice());
            stmt.setString(4, game.getCategory());
            stmt.setDouble(5, game.getRating());
            stmt.setString(6, game.getSystemRequirements());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update game details in the database
    public boolean updateGame(Game game) {
        String query = "UPDATE games SET title = ?, description = ?, price = ?, category = ?, rating = ?, system_requirements = ? WHERE game_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, game.getTitle());
            stmt.setString(2, game.getDescription());
            stmt.setDouble(3, game.getPrice());
            stmt.setString(4, game.getCategory());
            stmt.setDouble(5, game.getRating());
            stmt.setString(6, game.getSystemRequirements());
            stmt.setInt(7, game.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a game from the database by its ID
    public boolean deleteGame(int gameId) {
        String query = "DELETE FROM games WHERE game_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, gameId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get a game by its ID
    public Game getGameById(int gameId) {
        String query = "SELECT * FROM games WHERE game_id = ?";
        Game game = null;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, gameId);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                String category = resultSet.getString("category");
                double rating = resultSet.getDouble("rating");
                String systemRequirements = resultSet.getString("system_requirements");

                String pathImage = resultSet.getString("path_image");
                game = new Game(gameId, title, description, price, category, rating, systemRequirements, pathImage);

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
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                String category = resultSet.getString("category");
                double rating = resultSet.getDouble("rating");
                String systemRequirements = resultSet.getString("system_requirements");

                String pathImage = resultSet.getString("path_image");
                Game game = new Game(id, title, description, price, category, rating, systemRequirements, pathImage);

                games.add(game);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return games;
    }
}
