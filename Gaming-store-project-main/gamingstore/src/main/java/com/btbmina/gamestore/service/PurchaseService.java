
package com.btbmina.gamestore.service;

import com.btbmina.gamestore.DB.DatabaseManager;
import com.btbmina.gamestore.model.Purchase;
import java.sql.*;
import java.util.UUID;

public class PurchaseService {
    private Connection connection;

    public PurchaseService() {
        this.connection = DatabaseManager.getConnection();
    }

    public boolean processPurchase(int userId, int gameId, double price) {
        String transactionId = generateTransactionId();
        Purchase purchase = new Purchase(userId, gameId, price, transactionId);

        String query = "INSERT INTO purchases (user_id, game_id, price, transaction_id, purchase_date) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);

            stmt.setInt(1, purchase.getUserId());
            stmt.setInt(2, purchase.getGameId());
            stmt.setDouble(3, purchase.getPrice());
            stmt.setString(4, purchase.getTransactionId());
            stmt.setTimestamp(5, Timestamp.valueOf(purchase.getPurchaseDate()));

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                if (addToUserLibrary(userId, gameId)) {
                    connection.commit();
                    return true;
                }
            }

            connection.rollback();
            return false;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean addToUserLibrary(int userId, int gameId) throws SQLException {
        String query = "INSERT INTO user_library (user_id, game_id) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, gameId);
            return stmt.executeUpdate() > 0;
        }
    }

    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }
}