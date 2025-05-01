package com.btbmina.gamestore.service;

import com.btbmina.gamestore.DB.DatabaseConnection;
import com.btbmina.gamestore.classes.Cart;
import com.btbmina.gamestore.classes.Game;

import java.sql.*;
import java.util.List;

public class PurchaseService {
    private Connection connection;

    public PurchaseService() {
        try {
            this.connection = DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Process the purchase by inserting the games into the purchase table and updating the user's balance
    public boolean processPurchase(Cart cart) {
        List<Game> games = cart.getGames();
        double totalPrice = cart.getTotalPrice();
        int userId = cart.getUserId();

        // Start a transaction to ensure atomicity
        try {
            connection.setAutoCommit(false);  // Disable auto-commit for transaction management

            // Step 1: Insert the purchase record
            String insertPurchaseQuery = "INSERT INTO purchases (user_id, total_price, purchase_date) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertPurchaseQuery, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, userId);
                stmt.setDouble(2, totalPrice);
                stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));  // Current timestamp for purchase date

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    connection.rollback();  // If no rows were affected, rollback the transaction
                    return false;
                }

                // Get the generated purchase ID
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int purchaseId = generatedKeys.getInt(1);

                    // Step 2: Insert each purchased game into the purchase_details table
                    String insertPurchaseDetailQuery = "INSERT INTO purchase_details (purchase_id, game_id) VALUES (?, ?)";
                    try (PreparedStatement detailStmt = connection.prepareStatement(insertPurchaseDetailQuery)) {
                        for (Game game : games) {
                            detailStmt.setInt(1, purchaseId);
                            detailStmt.setInt(2, game.getId());
                            detailStmt.addBatch();  // Add to batch for efficiency
                        }
                        detailStmt.executeBatch();  // Execute batch insert
                    }

                    // Step 3: Commit the transaction
                    connection.commit();
                    cart.clearCart();  // Clear the cart after purchase is successful
                    return true;
                }
            } catch (SQLException e) {
                connection.rollback();  // Rollback if any exception occurs
                e.printStackTrace();
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);  // Restore auto-commit to true
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Get purchase history for a user
    public void getPurchaseHistory(int userId) {
        String query = "SELECT p.id, p.total_price, p.purchase_date, g.title FROM purchases p " +
                "JOIN purchase_details pd ON p.id = pd.purchase_id " +
                "JOIN games g ON pd.game_id = g.id " +
                "WHERE p.user_id = ? ORDER BY p.purchase_date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet resultSet = stmt.executeQuery();

            System.out.println("Purchase History for User " + userId + ":");
            while (resultSet.next()) {
                int purchaseId = resultSet.getInt("id");
                double totalPrice = resultSet.getDouble("total_price");
                Timestamp purchaseDate = resultSet.getTimestamp("purchase_date");
                String gameTitle = resultSet.getString("title");

                System.out.println("Purchase ID: " + purchaseId);
                System.out.println("Game: " + gameTitle);
                System.out.println("Total Price: $" + totalPrice);
                System.out.println("Purchase Date: " + purchaseDate);
                System.out.println("-----------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
