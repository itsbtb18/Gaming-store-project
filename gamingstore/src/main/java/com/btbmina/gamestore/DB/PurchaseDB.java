package com.btbmina.gamestore.DB;
import com.btbmina.gamestore.classes.Purchase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDB {
    private static final String URL = "jdbc:mysql://localhost:3306/gamestore"; // Adjust based on your DB
    private static final String USER = "root"; // DB username
    private static final String PASSWORD = "password"; // DB password

    public static List<Purchase> getPurchasesByUserId(int userId) {
        String query = "SELECT * FROM purchases WHERE user_id = ?";
        List<Purchase> purchases = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                purchases.add(new Purchase(rs.getInt("purchase_id"), rs.getInt("user_id"),
                        rs.getInt("game_id"), rs.getTimestamp("purchase_date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return purchases;
    }

    public static boolean insertPurchase(Purchase purchase) {
        String query = "INSERT INTO purchases (user_id, game_id) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, purchase.getUserId());
            stmt.setInt(2, purchase.getGameId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Additional methods for retrieving, updating, and deleting purchases can be added similarly.
}