package com.btbmina.gamestore;
import com.btbmina.gamestore.DB.DatabaseManager;
import com.btbmina.gamestore.ui.pages.main.LoadingPage;
import javax.swing.*;

public class GameStoreApp {
    public static void main(String[] args) {
        // Set modern look and feel
        SwingUtilities.invokeLater(() -> {
            try {
                // 3. Connexion à la base de données
                DatabaseManager.connect();
                if (!DatabaseManager.isConnected()) {
                    throw new IllegalStateException("La base de données est inaccessible.");
                }

                // 4. Afficher la page de chargement
                LoadingPage loadingPage = new LoadingPage();
                loadingPage.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Erreur critique : impossible de démarrer l'application.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                System.exit(1); // Quitter proprement
            }
        });

        // 5. Fermer proprement la base de données à la fin de l'application
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DatabaseManager.disconnect();
            System.out.println(" Déconnexion réussie de la base de données.");
        }));
    }
}