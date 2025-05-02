package com.btbmina.gamestore.ui.pages.main;

import javax.swing.*;
import java.awt.*;

public class HomePage extends JFrame {
    private Timer slideShowTimer;
    private int currentImageIndex = 0;
    private final String[] featuredGames = {"Game1.jpg", "Game2.jpg", "Game3.jpg"};

    public HomePage() {
        setTitle("Gaming Store - Home");
        setSize(1000, 700);
        setLocationRelativeTo(null); // Centrer la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(28, 28, 28));

        // Featured games slideshow
        JPanel slideshowPanel = new JPanel();
        slideshowPanel.setPreferredSize(new Dimension(0, 300));
        slideshowPanel.setBackground(new Color(48, 25, 52));
        mainPanel.add(slideshowPanel, BorderLayout.NORTH);

        // Latest releases
        JPanel latestPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        latestPanel.setBackground(new Color(28, 28, 28));
        for (int i = 0; i < 4; i++) {
            latestPanel.add(createGameCard("Latest Game " + (i + 1)));
        }

        // Special offers
        JPanel offersPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        offersPanel.setBackground(new Color(28, 28, 28));
        for (int i = 0; i < 4; i++) {
            offersPanel.add(createGameCard("Special Offer " + (i + 1)));
        }

        // Combine latest and offers
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(28, 28, 28));

        addSection(contentPanel, "Latest Releases", latestPanel);
        addSection(contentPanel, "Special Offers", offersPanel);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        setContentPane(mainPanel);

        startSlideshow();
        setVisible(true); // N'oublie pas ça !
    }

    private void addSection(JPanel container, String title, JPanel content) {
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));

        container.add(titleLabel);
        content.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        container.add(content);
    }

    private JPanel createGameCard(String title) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(87, 54, 163));

        // Game image placeholder
        JPanel imagePanel = new JPanel();
        imagePanel.setPreferredSize(new Dimension(0, 200));
        imagePanel.setBackground(new Color(48, 25, 52));

        // Game info
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(new Color(87, 54, 163));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel priceLabel = new JLabel("$59.99");
        priceLabel.setForeground(Color.WHITE);
        priceLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        infoPanel.add(titleLabel, BorderLayout.CENTER);
        infoPanel.add(priceLabel, BorderLayout.EAST);

        card.add(imagePanel, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);

        return card;
    }

    private void startSlideshow() {
        slideShowTimer = new Timer(3000, e -> {
            currentImageIndex = (currentImageIndex + 1) % featuredGames.length;
            repaint();
        });
        slideShowTimer.start();
    }
}
