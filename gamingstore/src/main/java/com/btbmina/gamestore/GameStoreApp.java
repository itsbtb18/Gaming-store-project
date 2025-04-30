package com.btbmina.gamestore;

import com.btbmina.gamestore.ui.pages.main.LoadingPage;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.formdev.flatlaf.FlatDarkLaf;

/**
 * Main application class for BTB-Mina Game Store application
 * This initializes the Swing desktop application
 */
public class GameStoreApp {
    public static void main(String[] args) {
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            System.err.println("Failed to set look and feel");
        }

        // Launch application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Start with loading page
            LoadingPage loadingPage = new LoadingPage();
            loadingPage.setVisible(true);
        });
    }
}