package com.btbmina.gamestore.ui.pages.main;

import javax.swing.*;
import java.awt.*;
import utils.ColorScheme;
import components.PurpleButton;

public class GamePage extends JPanel {
    private final String gameTitle;
    private final double price;

    public GamePage(String gameTitle, double price) {
        this.gameTitle = gameTitle;
        this.price = price;

        setLayout(new BorderLayout());
        setBackground(ColorScheme.BACKGROUND);

        createUI();
    }

    private void createUI() {
        // Header section with game title and main image
        JPanel headerPanel = createHeaderPanel();

        // Main content section
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ColorScheme.BACKGROUND);

        // Game description
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBackground(ColorScheme.DARK_PURPLE);
        descriptionArea.setForeground(ColorScheme.TEXT_COLOR);
        descriptionArea.setText("Experience an epic gaming adventure...");

        // Game details
        JPanel detailsPanel = createDetailsPanel();

        // Screenshots gallery
        JPanel screenshotsPanel = createScreenshotsPanel();

        // Purchase panel
        JPanel purchasePanel = createPurchasePanel();

        contentPanel.add(detailsPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(descriptionArea);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(screenshotsPanel);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(purchasePanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ColorScheme.DARK_PURPLE);
        panel.setPreferredSize(new Dimension(0, 300));

        JLabel titleLabel = new JLabel(gameTitle);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(ColorScheme.TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(titleLabel, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 0));
        panel.setBackground(ColorScheme.BACKGROUND);

        // Developer info
        addDetailSection(panel, "Developer", "Game Studio");

        // Publisher info
        addDetailSection(panel, "Publisher", "Publishing Co");

        // Release date
        addDetailSection(panel, "Release Date", "2024");

        return panel;
    }

    private void addDetailSection(JPanel container, String title, String content) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(ColorScheme.MEDIUM_PURPLE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(ColorScheme.TEXT_COLOR);
        JLabel contentLabel = new JLabel(content);
        contentLabel.setForeground(ColorScheme.TEXT_COLOR);

        panel.add(titleLabel);
        panel.add(contentLabel);
        container.add(panel);
    }

    private JPanel createScreenshotsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 0));
        panel.setBackground(ColorScheme.BACKGROUND);

        for (int i = 0; i < 4; i++) {
            JPanel screenshotPanel = new JPanel();
            screenshotPanel.setBackground(ColorScheme.MEDIUM_PURPLE);
            screenshotPanel.setPreferredSize(new Dimension(200, 150));
            panel.add(screenshotPanel);
        }

        return panel;
    }

    private JPanel createPurchasePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(ColorScheme.DARK_PURPLE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel priceLabel = new JLabel(String.format("$%.2f", price));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        priceLabel.setForeground(ColorScheme.TEXT_COLOR);

        PurpleButton purchaseButton = new PurpleButton("Add to Cart");
        purchaseButton.setPreferredSize(new Dimension(150, 40));

        panel.add(priceLabel);
        panel.add(Box.createRigidArea(new Dimension(20, 0)));
        panel.add(purchaseButton);

        return panel;
    }
}