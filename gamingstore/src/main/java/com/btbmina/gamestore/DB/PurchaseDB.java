package com.btbmina.gamestore.DB;
import com.btbmina.gamestore.classes.Purchase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDB {
    private static final String URL = "jdbc:mysql://localhost:3306/gamestore";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    // ➤ Insérer un achat dans la base de données
    public static boolean insertPurchase(Purchase purchase) {
        String query = "INSERT INTO purchases (user_id, game_id, purchase_date, price, payment_method) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, purchase.getUserId());
            stmt.setInt(2, purchase.getGameId());
            stmt.setTimestamp(3, Timestamp.valueOf(purchase.getPurchaseDate()));
            stmt.setDouble(4, purchase.getPrice());
            stmt.setString(5, purchase.getPaymentMethod());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ➤ Récupérer tous les achats d'un utilisateur
    public static List<Purchase> getPurchasesByUserId(int userId) {
        List<Purchase> purchases = new ArrayList<>();
        String query = "SELECT * FROM purchases WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Purchase purchase = new Purchase(
                        rs.getInt("purchase_id"),
                        rs.getInt("user_id"),
                        rs.getInt("game_id"),
                        rs.getTimestamp("purchase_date").toLocalDateTime(),
                        rs.getDouble("price"),
                        rs.getString("payment_method")
                );
                purchases.add(purchase);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return purchases;
    }
}