package com.btbmina.gamestore.ui.pages.main;

import com.btbmina.gamestore.Util.ColorScheme;
import com.btbmina.gamestore.ui.FontManager;
import com.btbmina.gamestore.ui.components.*;
import com.btbmina.gamestore.classes.Game;
import com.btbmina.gamestore.DB.GameDB;
import com.btbmina.gamestore.classes.User;
import com.btbmina.gamestore.ui.components.MenuBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.io.File;

public class CategoryPage extends JFrame {
    private User currentUser;
    private String categoryName;
    private List<Game> categoryGames;
    private static final int CARDS_PER_ROW = 3;
    private static final int CARD_WIDTH = 280;
    private static final int CARD_HEIGHT = 320;
    private static final int CARD_SPACING = 20;

    public CategoryPage(String categoryName, User currentUser) {
        this.categoryName = categoryName;
        this.currentUser = currentUser;

        // Set undecorated before making the frame displayable
        setUndecorated(true);

        initializeFrame();
        loadGames();
        createContent();

        // Don't set visible here - let the caller handle it
    }

    private void initializeFrame() {
        setTitle("Gaming Store - " + categoryName);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Don't set fullscreen here - let the caller handle it
        // The frame configuration will be done by the calling code
    }

    private void loadGames() {
        try {
            categoryGames = GameDB.getGamesByCategory(categoryName);
            if (categoryGames.isEmpty()) {
                System.out.println("No games found for category: " + categoryName);
            } else {
                System.out.println("Loaded " + categoryGames.size() + " games for category: " + categoryName);
                // Print first game details for debugging
                Game firstGame = categoryGames.get(0);
                System.out.println("First game: " + firstGame.getTitle() + ", Image: " + firstGame.getPath_image());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Failed to load games", e.getMessage());
            categoryGames = new ArrayList<>(); // Initialize empty list on error
        }
    }

    private void createContent() {
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(ColorScheme.DARK_BACKGROUND);

        // Add TitleBar
        mainContainer.add(new TitleBar(this), BorderLayout.NORTH);

        // Create content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ColorScheme.DARK_BACKGROUND);

        // Add MenuBar with REDUCED height
        JPanel menuWrapper = new JPanel(new BorderLayout());
        menuWrapper.setBackground(ColorScheme.DARK_BACKGROUND);
        // Removed vertical padding to reduce height
        menuWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Create a compact menu bar
        MenuBar menuBar = new MenuBar(this, currentUser);
        // You might need to modify your MenuBar class to have a method like this
        // If it doesn't exist, you'll need to modify MenuBar class too
        // menuBar.setCompactMode(true);

        menuWrapper.add(menuBar, BorderLayout.CENTER);
        contentPanel.add(menuWrapper);

        // Add category header
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Reduced spacing
        contentPanel.add(createCategoryHeader());

        // Add games grid
        if (categoryGames != null && !categoryGames.isEmpty()) {
            contentPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Reduced spacing
            contentPanel.add(createGamesGrid());
        } else {
            contentPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Reduced spacing
            contentPanel.add(createEmptyStatePanel());
        }

        // Add scroll pane with modern scrollbar
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(ColorScheme.DARK_BACKGROUND);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainContainer.add(scrollPane, BorderLayout.CENTER);
        setContentPane(mainContainer);
    }

    private JPanel createCategoryHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ColorScheme.DARK_BACKGROUND);
        // Reduced vertical padding
        header.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        // Category title with icon
        JPanel titleWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titleWrapper.setBackground(ColorScheme.DARK_BACKGROUND);

        JLabel iconLabel = new JLabel(getCategoryIcon());
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32)); // Reduced font size
        iconLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel(categoryName + " Games");
        titleLabel.setFont(FontManager.getBold(28)); // Reduced font size
        titleLabel.setForeground(Color.WHITE);

        titleWrapper.add(iconLabel);
        titleWrapper.add(titleLabel);

        // Games count
        JLabel countLabel = new JLabel(categoryGames.size() + " games");
        countLabel.setFont(FontManager.getMedium(14)); // Reduced font size
        countLabel.setForeground(new Color(200, 200, 200));
        countLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        header.add(titleWrapper, BorderLayout.CENTER);
        header.add(countLabel, BorderLayout.SOUTH);

        return header;
    }

    private String getCategoryIcon() {
        return switch (categoryName.toLowerCase()) {
            case "action" -> "🎯";
            case "adventure" -> "🗺";
            case "rpg" -> "⚔";
            case "strategy" -> "🎲";
            case "sports" -> "⚽";
            case "racing" -> "🏎";
            case "shooter" -> "🎮";
            default -> "🎲";
        };
    }

    private JPanel createGamesGrid() {
        int totalWidth = CARD_WIDTH * CARDS_PER_ROW + CARD_SPACING * (CARDS_PER_ROW - 1);

        JPanel gridContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        gridContainer.setBackground(ColorScheme.DARK_BACKGROUND);

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(ColorScheme.DARK_BACKGROUND);
        grid.setPreferredSize(new Dimension(totalWidth, -1));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(CARD_SPACING/2, CARD_SPACING/2, CARD_SPACING/2, CARD_SPACING/2);

        for (Game game : categoryGames) {
            grid.add(createGameCard(game), gbc);

            gbc.gridx++;
            if (gbc.gridx == CARDS_PER_ROW) {
                gbc.gridx = 0;
                gbc.gridy++;
            }
        }

        gridContainer.add(grid);
        return gridContainer;
    }

    private JPanel createGameCard(Game game) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        card.setBackground(new Color(30, 30, 40));
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Game image panel
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(CARD_WIDTH - 20, 200));
        imageLabel.setBackground(new Color(40, 40, 50));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setOpaque(true);

        // Fixed image loading with better error handling
        try {
            String imagePath = game.getPath_image();
            System.out.println("Attempting to load image from: " + imagePath);

            // Try loading from resources first
            ImageIcon icon = null;

            // First attempt: Use getResource
            if (imagePath != null && !imagePath.isEmpty()) {
                java.net.URL imageUrl = getClass().getResource(imagePath);
                if (imageUrl != null) {
                    icon = new ImageIcon(imageUrl);
                    System.out.println("Loaded image from resources: " + imageUrl);
                } else {
                    System.out.println("Resource not found, trying absolute path: " + imagePath);
                    // Second attempt: Try as absolute file path
                    File imageFile = new File(imagePath);
                    if (imageFile.exists()) {
                        icon = new ImageIcon(imageFile.getAbsolutePath());
                        System.out.println("Loaded image from file: " + imageFile.getAbsolutePath());
                    }
                }
            }

            if (icon != null && icon.getIconWidth() > 0) {
                Image img = icon.getImage();
                Image scaledImg = img.getScaledInstance(CARD_WIDTH - 20, 200, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImg));
            } else {
                throw new Exception("Failed to load valid image");
            }
        } catch (Exception e) {
            System.out.println("Failed to load image for game: " + game.getTitle() + " - Error: " + e.getMessage());
            imageLabel.setText("No Image");
            imageLabel.setFont(FontManager.getMedium(14));
            imageLabel.setForeground(Color.WHITE);
        }

        // Game info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(30, 30, 40));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Title
        JLabel titleLabel = new JLabel(game.getTitle());
        titleLabel.setFont(FontManager.getBold(16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Price
        JLabel priceLabel = new JLabel(String.format("$%.2f", game.getPrice()));
        priceLabel.setFont(FontManager.getMedium(14));
        priceLabel.setForeground(new Color(130, 90, 210));
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(titleLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(priceLabel);

        // Add hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(130, 90, 210), 2),
                        BorderFactory.createEmptyBorder(8, 8, 8, 8)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                navigateToGame(game.getId());
            }
        });

        card.add(imageLabel, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);

        return card;
    }

    private void navigateToGame(int gameId) {
        SwingUtilities.invokeLater(() -> {
            try {
                Game selectedGame = GameDB.getGameById(gameId);
                if (selectedGame == null) {
                    showError("Error", "Game not found");
                    return;
                }

                // Close current window
                dispose();

                // Create and configure game page
                GamePage gamePage = new GamePage(selectedGame, currentUser);

                // Set fullscreen
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice gd = ge.getDefaultScreenDevice();

                if (gd.isFullScreenSupported()) {
                    gd.setFullScreenWindow(gamePage);
                } else {
                    gamePage.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                    gamePage.setSize(screenSize.width, screenSize.height);
                    gamePage.setLocationRelativeTo(null);
                }

                gamePage.setVisible(true);

            } catch (Exception ex) {
                ex.printStackTrace();
                showError("Error", "Failed to open game page: " + ex.getMessage());
            }
        });
    }

    private JPanel createRatingStars(double rating) {
        JPanel starsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        starsPanel.setOpaque(false);

        int fullStars = (int) rating;
        boolean hasHalfStar = rating % 1 >= 0.5;

        for (int i = 0; i < 5; i++) {
            String star;
            if (i < fullStars) {
                star = "★"; // Full star
            } else if (i == fullStars && hasHalfStar) {
                star = "⯨"; // Half star
            } else {
                star = "☆"; // Empty star
            }

            JLabel starLabel = new JLabel(star);
            starLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
            starLabel.setForeground(new Color(255, 215, 0)); // Gold color
            starsPanel.add(starLabel);
        }

        return starsPanel;
    }

    private JPanel createEmptyStatePanel() {
        JPanel emptyState = new JPanel(new GridBagLayout());
        emptyState.setBackground(ColorScheme.DARK_BACKGROUND);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(ColorScheme.DARK_BACKGROUND);

        // Empty state illustration
        JLabel iconLabel = new JLabel("🎮");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 72));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("No games found");
        titleLabel.setFont(FontManager.getBold(24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel messageLabel = new JLabel("We'll add more games to this category soon!");
        messageLabel.setFont(FontManager.getRegular(16));
        messageLabel.setForeground(new Color(200, 200, 200));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(iconLabel);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        content.add(titleLabel);
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        content.add(messageLabel);

        emptyState.add(content);
        return emptyState;
    }

    private void startEntryAnimations() {
        // Implement fade-in and slide animations similar to HomePage
        setOpacity(0.0f);
        Timer fadeTimer = new Timer(10, new ActionListener() {
            float opacity = 0.0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                if (opacity >= 1.0f) {
                    opacity = 1.0f;
                    ((Timer)e.getSource()).stop();
                }
                setOpacity(opacity);
            }
        });
        fadeTimer.start();
    }

    private void showError(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
}