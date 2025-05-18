package com.btbmina.gamestore.ui.pages.main;

import com.btbmina.gamestore.Util.ColorScheme;
import com.btbmina.gamestore.ui.components.PurpleButton;
import javax.swing.*;
import java.awt.*;

public class LibraryPage extends JPanel {
    public LibraryPage() {
        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARK_BACKGROUND);  // Use DARK_BACKGROUND from ColorScheme

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(ColorScheme.PRIMARY_PURPLE);  // Use PRIMARY_PURPLE for filter panel background
        filterPanel.add(new JLabel("Filter: "));
        filterPanel.add(new PurpleButton("All"));
        filterPanel.add(new PurpleButton("Installed"));
        filterPanel.add(new PurpleButton("Ready to Install"));

        JPanel gamesPanel = new JPanel();
        gamesPanel.setLayout(new BoxLayout(gamesPanel, BoxLayout.Y_AXIS));
        gamesPanel.setBackground(ColorScheme.DARK_BACKGROUND);  // Use DARK_BACKGROUND for games list panel

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
        panel.setBackground(ColorScheme.MEDIUM_BACKGROUND);  // Use MEDIUM_BACKGROUND for game entry panel
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel iconPanel = new JPanel();
        iconPanel.setPreferredSize(new Dimension(70, 70));
        iconPanel.setBackground(ColorScheme.PRIMARY_PURPLE);  // Use PRIMARY_PURPLE for icon panel background

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(ColorScheme.MEDIUM_BACKGROUND);  // Use MEDIUM_BACKGROUND for info panel
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);  // Use TEXT_PRIMARY for title text
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        infoPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(ColorScheme.MEDIUM_BACKGROUND);  // Use MEDIUM_BACKGROUND for buttons panel
        buttonsPanel.add(new PurpleButton("Play"));
        buttonsPanel.add(new PurpleButton("Uninstall"));

        panel.add(iconPanel, BorderLayout.WEST);
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.EAST);

        return panel;
    }
}
