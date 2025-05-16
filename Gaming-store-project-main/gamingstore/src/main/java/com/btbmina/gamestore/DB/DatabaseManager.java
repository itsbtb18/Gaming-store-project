package com.btbmina.gamestore.DB;
import com.btbmina.gamestore.DB.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
public class DatabaseManager {

        /**
         * Initialise la connexion à la base de données.
         * À appeler au démarrage de l'application.
         */
        public static void connect() {
            try {
                Connection conn = DatabaseConnection.getInstance().getConnection();
                if (conn != null) {
                    System.out.println("🔗 Base de données connectée via DatabaseManager.");
                }
            } catch (SQLException e) {
                System.err.println(" Impossible d'établir la connexion via DatabaseManager.");
                e.printStackTrace();
            }
        }

        /**
         * Retourne la connexion actuelle.
         * @return la connexion JDBC active
         */
        public static Connection getConnection() {
            try {
                return DatabaseConnection.getInstance().getConnection();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la récupération de la connexion !");
                e.printStackTrace();
                return null;
            }
        }

        public static void disconnect() {
            DatabaseConnection.getInstance().closeConnection();
        }

        public static boolean isConnected() {
            return DatabaseConnection.getInstance().isConnected();
        }
    }