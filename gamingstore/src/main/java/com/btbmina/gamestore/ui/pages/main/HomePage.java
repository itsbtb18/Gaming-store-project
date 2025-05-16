package com.btbmina.gamestore.ui.pages.main;

import com.btbmina.gamestore.Util.ColorScheme;
import com.btbmina.gamestore.ui.FontManager;
import com.btbmina.gamestore.ui.components.PurpleButton;
import com.btbmina.gamestore.ui.components.TitleBar;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Objects;

public class HomePage extends JFrame {
    private Timer slideShowTimer;
    private int currentImageIndex = 0;
    private final String[] featuredGames = {"Game1.jpg", "Game2.jpg", "Game3.jpg"};
    private Image backgroundImage;

    public HomePage() {
        setTitle("Gaming Store - Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true); // Fullscreen with custom decoration
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen

        try {
            backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("/assets/images/home_bg.jpg")));
        } catch (IOException e) {
            System.err.println("Background image not found");
        }

        JPanel layeredPanel = new BackgroundPanel();
        layeredPanel.setLayout(new BorderLayout());

        // Title bar
        layeredPanel.add(new TitleBar(this), BorderLayout.NORTH);

        // Top control bar (Profile dropdown + Cart)
        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        topRightPanel.setOpaque(false);

        // Cart button
        PurpleButton cartButton = new PurpleButton("Cart");
        cartButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Redirecting to Cart...");
        });

        // Profile dropdown
        JButton profileButton = new PurpleButton("Profile \u25BC"); // Down arrow
        JPopupMenu profileMenu = new JPopupMenu();
        JMenuItem profileItem = new JMenuItem("View Profile");
        JMenuItem accountItem = new JMenuItem("Account Management");
        JMenuItem logoutItem = new JMenuItem("Log Out");

        profileMenu.add(profileItem);
        profileMenu.add(accountItem);
        profileMenu.addSeparator();
        profileMenu.add(logoutItem);

        profileButton.addActionListener(e -> profileMenu.show(profileButton, 0, profileButton.getHeight()));

        topRightPanel.add(profileButton);
        topRightPanel.add(cartButton);
        layeredPanel.add(topRightPanel, BorderLayout.EAST);

        // Main content
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);

        // Slideshow
        JPanel slideshowPanel = new JPanel();
        slideshowPanel.setOpaque(false);
        slideshowPanel.setPreferredSize(new Dimension(0, 300));
        mainPanel.add(slideshowPanel, BorderLayout.NORTH);

        // Latest releases & offers
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JPanel latestPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        latestPanel.setOpaque(false);
        for (int i = 0; i < 4; i++) latestPanel.add(createGameCard("Latest Game " + (i + 1)));

        JPanel offersPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        offersPanel.setOpaque(false);
        for (int i = 0; i < 4; i++) offersPanel.add(createGameCard("Special Offer " + (i + 1)));

        addSection(contentPanel, "Latest Releases", latestPanel);
        addSection(contentPanel, "Special Offers", offersPanel);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        layeredPanel.add(mainPanel, BorderLayout.CENTER);
        setContentPane(layeredPanel);

        startSlideshow();
        setVisible(true);
    }

    private void addSection(JPanel container, String title, JPanel content) {
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FontManager.getFont(FontManager.FONT_NAME_BOLD, Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));
        content.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        container.add(titleLabel);
        container.add(content);
    }

    private JPanel createGameCard(String title) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(87, 54, 163));

        JPanel imagePanel = new JPanel();
        imagePanel.setPreferredSize(new Dimension(0, 200));
        imagePanel.setBackground(new Color(48, 25, 52));

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
        slideShowTimer = new Timer(3000, (ActionEvent e) -> {
            currentImageIndex = (currentImageIndex + 1) % featuredGames.length;
            repaint();
        });
        slideShowTimer.start();
    }

    // Custom JPanel with background image
    private class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
