package com.btbmina.gamestore;

import com.btbmina.gamestore.DB.DatabaseConnection;
import com.btbmina.gamestore.DB.DatabaseInitializer;
import com.btbmina.gamestore.DB.DatabaseManager;
import com.btbmina.gamestore.ui.pages.main.LoadingPage;
import javax.swing.*;

public class GameStoreApp {
    public static void main(String[] args) {
        System.out.println("Starting Gaming Store Application...");

        // Set modern look and feel
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                System.out.println("Initializing database...");
                if (DatabaseInitializer.initialize()) {
                    System.out.println("Database initialized successfully!");
                } else {
                    System.err.println("Database initialization failed!");
                    throw new IllegalStateException("Database initialization failed");
                }

                // Connect to database via database manager
                DatabaseManager.connect();
                if (DatabaseManager.isConnected()) {
                    System.out.println("Successfully connected to database!");
                } else {
                    System.err.println("Failed to connect to database!");
                    throw new IllegalStateException("La base de données est inaccessible.");
                }

                // Display the loading page
                LoadingPage loadingPage = new LoadingPage();
                loadingPage.setVisible(true);
                System.out.println("Application UI started.");

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Erreur critique : impossible de démarrer l'application.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                System.exit(1); // Exit gracefully
            }
        });

        // Close database properly when application ends
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DatabaseManager.disconnect();
            System.out.println(" Déconnexion réussie de la base de données.");
        }));
    }
}