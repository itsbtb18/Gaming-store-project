package com.btbmina.gamestore.ui.pages.main;

import utils.ColorScheme;
import components.PurpleButton;
import javax.swing.*;
import java.awt.*;

public class LibraryPage extends JPanel {
    public LibraryPage() {
        setLayout(new BorderLayout());
        setBackground(ColorScheme.BACKGROUND);

        // Create top filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(ColorScheme.DARK_PURPLE);
        filterPanel.add(new JLabel("Filter: "));
        filterPanel.add(new PurpleButton("All"));
        filterPanel.add(new PurpleButton("Installed"));
        filterPanel.add(new PurpleButton("Ready to Install"));

        // Create games list panel
        JPanel gamesPanel = new JPanel();
        gamesPanel.setLayout(new BoxLayout(gamesPanel, BoxLayout.Y_AXIS));
        gamesPanel.setBackground(ColorScheme.BACKGROUND);

        // Add sample games (would be loaded from database)
        for (int i = 0; i < 10; i++) {
            gamesPanel.add(createGameEntry("Game " + (i + 1)));
            gamesPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        JScrollPane scrollPane = new JScrollPane(gamesPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(filterPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createGameEntry(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ColorScheme.MEDIUM_PURPLE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Game icon
        JPanel iconPanel = new JPanel();
        iconPanel.setPreferredSize(new Dimension(70, 70));
        iconPanel.setBackground(ColorScheme.DARK_PURPLE);
        
        // Game info
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(ColorScheme.MEDIUM_PURPLE);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(ColorScheme.TEXT_COLOR);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        infoPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Action buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(ColorScheme.MEDIUM_PURPLE);
        buttonsPanel.add(new PurpleButton("Play"));
        buttonsPanel.add(new PurpleButton("Uninstall"));

        panel.add(iconPanel, BorderLayout.WEST);
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.EAST);

        return panel;
    }
}