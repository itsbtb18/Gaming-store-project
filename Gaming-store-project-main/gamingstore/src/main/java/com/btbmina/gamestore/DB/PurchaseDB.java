package com.btbmina.gamestore.DB;

import com.btbmina.gamestore.model.Purchase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDB {

    // Insert a purchase into the database
    public static boolean insertPurchase(Purchase purchase) {
        String query = "INSERT INTO purchases (user_id, game_id, purchase_date, price, payment_method, transaction_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, purchase.getUserId());
            stmt.setInt(2, purchase.getGameId());
            stmt.setTimestamp(3, Timestamp.valueOf(purchase.getPurchaseDate()));
            stmt.setDouble(4, purchase.getPrice());
            stmt.setString(5, purchase.getPaymentMethod());
            stmt.setString(6, purchase.getTransactionId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error inserting purchase: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Get all purchases for a user
    public static List<Purchase> getPurchasesByUserId(int userId) {
        List<Purchase> purchases = new ArrayList<>();
        String query = "SELECT * FROM purchases WHERE user_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Purchase purchase = new Purchase(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("game_id"),
                        rs.getTimestamp("purchase_date").toLocalDateTime(),
                        rs.getDouble("price"),
                        rs.getString("payment_method"),
                        rs.getString("transaction_id")
                );
                purchases.add(purchase);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving purchases: " + e.getMessage());
            e.printStackTrace();
        }

        return purchases;
    }

    // Get a specific purchase by ID
    public static Purchase getPurchaseById(int purchaseId) {
        String query = "SELECT * FROM purchases WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, purchaseId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Purchase(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("game_id"),
                        rs.getTimestamp("purchase_date").toLocalDateTime(),
                        rs.getDouble("price"),
                        rs.getString("payment_method"),
                        rs.getString("transaction_id")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving purchase: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}