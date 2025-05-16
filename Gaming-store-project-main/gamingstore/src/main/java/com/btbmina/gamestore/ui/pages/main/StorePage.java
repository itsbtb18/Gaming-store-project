package com.btbmina.gamestore.ui.pages.main;

import com.btbmina.gamestore.Util.ColorScheme;
import com.btbmina.gamestore.ui.components.PurpleButton;
import javax.swing.*;
import java.awt.*;

public class StorePage extends JPanel {
    public StorePage() {
        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARK_BACKGROUND);  // Utilisation de la couleur de fond sombre

        // Add search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(ColorScheme.MEDIUM_BACKGROUND); // Couleur de fond pour la barre de recherche
        JTextField searchField = new JTextField(30);
        searchField.setBackground(ColorScheme.MEDIUM_BACKGROUND);  // Fond du champ de texte
        searchField.setForeground(ColorScheme.TEXT_PRIMARY);  // Couleur du texte dans le champ
        searchPanel.add(searchField);
        searchPanel.add(new PurpleButton("Search"));

        // Add game grid
        JPanel gamesGrid = new JPanel(new GridLayout(0, 3, 10, 10));
        gamesGrid.setBackground(ColorScheme.DARK_BACKGROUND); // Utilisation de la couleur de fond sombre pour la grille

        // Add sample games (you would load these from a database)
        for (int i = 0; i < 9; i++) {
            gamesGrid.add(createGameCard("Game " + (i + 1)));
        }

        JScrollPane scrollPane = new JScrollPane(gamesGrid);
        scrollPane.setBorder(null);

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createGameCard(String title) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(ColorScheme.MEDIUM_BACKGROUND); // Utilisation de la couleur pour les cartes de jeu

        // Add game image (just an empty space for now)
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(200, 300));
        imageLabel.setBackground(ColorScheme.LIGHT_BACKGROUND);  // Fond pour l'image
        card.add(imageLabel, BorderLayout.CENTER);

        // Add game title and price
        JPanel info = new JPanel(new BorderLayout());
        info.setBackground(ColorScheme.MEDIUM_BACKGROUND);  // Fond pour la section info
        info.add(new JLabel(title), BorderLayout.CENTER);
        info.add(new PurpleButton("$59.99"), BorderLayout.EAST);  // Bouton violet pour le prix

        card.add(info, BorderLayout.SOUTH);
        return card;
    }
}
