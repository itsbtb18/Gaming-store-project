package com.btbmina.gamestore.service;

import com.btbmina.gamestore.DB.DatabaseConnection;
import com.btbmina.gamestore.classes.Cart;
import com.btbmina.gamestore.classes.Game;

import java.sql.*;
import java.util.List;

public class CartService {
    private Connection connection;

    public CartService() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add a game to the user's cart
    public boolean addGameToCart(int userId, Game game) {
        String query = "INSERT INTO cart_items (user_id, game_id) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, game.getId()); // Assuming Game class has a getId method

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeGameFromCart(int userId, Game game) {
        String query = "DELETE FROM cart_items WHERE user_id = ? AND game_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, game.getId()); // Assuming Game class has a getId method

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Cart getCartForUser(int userId) {
        Cart cart = new Cart(userId);
        String query = "SELECT g.id, g.name, g.price FROM cart_items ci JOIN games g ON ci.game_id = g.id WHERE ci.user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                int gameId = resultSet.getInt("id");
                String gameName = resultSet.getString("name");
                double gamePrice = resultSet.getDouble("price");

                // Create the Game object and add it to the cart
                Game game = new Game(gameId, gameName, gamePrice);
                cart.addGame(game);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cart;
    }

    public boolean clearCart(int userId) {
        String query = "DELETE FROM cart_items WHERE user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public double getTotalPrice(int userId) {
        double totalPrice = 0.0;
        String query = "SELECT g.price FROM cart_items ci JOIN games g ON ci.game_id = g.id WHERE ci.user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                totalPrice += resultSet.getDouble("price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalPrice;
    }
}
