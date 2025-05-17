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
import java.util.List;
import java.sql.SQLException;

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

        initializeFrame();
        loadGames();
        createContent();
        startEntryAnimations();

        setOpacity(0.0f);
        setVisible(true);

        // Fade-in animation
        Timer fadeTimer = new Timer(20, null);
        float[] alpha = {0.0f};

        fadeTimer.addActionListener(e -> {
            alpha[0] += 0.1f;
            if (alpha[0] >= 1.0f) {
                alpha[0] = 1.0f;
                fadeTimer.stop();
            }
            setOpacity(alpha[0]);
        });

        fadeTimer.start();
    }


    private void initializeFrame() {
        setTitle("Gaming Store - " + categoryName);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);

        // Set fullscreen
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(this);
        } else {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }

    private void loadGames() {
        try {
            // Use GameDB to fetch games by category
            categoryGames = GameDB.getGamesByCategory(categoryName);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Failed to load games", e.getMessage());
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

        // Add MenuBar
        JPanel menuWrapper = new JPanel(new BorderLayout());
        menuWrapper.setBackground(ColorScheme.DARK_BACKGROUND);
        menuWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        menuWrapper.add(new MenuBar(this, currentUser), BorderLayout.CENTER);
        contentPanel.add(menuWrapper);

        // Add category header
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(createCategoryHeader());

        // Add games grid
        if (categoryGames != null && !categoryGames.isEmpty()) {
            contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
            contentPanel.add(createGamesGrid());
        } else {
            contentPanel.add(Box.createRigidArea(new Dimension(0, 50)));
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
        header.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Category title with icon
        JPanel titleWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titleWrapper.setBackground(ColorScheme.DARK_BACKGROUND);

        JLabel iconLabel = new JLabel(getCategoryIcon());
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        iconLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel(categoryName + " Games");
        titleLabel.setFont(FontManager.getBold(32));
        titleLabel.setForeground(Color.WHITE);

        titleWrapper.add(iconLabel);
        titleWrapper.add(titleLabel);

        // Games count
        JLabel countLabel = new JLabel(categoryGames.size() + " games");
        countLabel.setFont(FontManager.getMedium(16));
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
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw background
                g2d.setColor(new Color(30, 30, 40));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                g2d.dispose();
            }
        };
        card.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.setOpaque(false);

        // Game image panel
        JPanel imagePanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    ImageIcon icon = new ImageIcon(getClass().getResource(game.getPath_image()));
                    Image img = icon.getImage();
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    g.setColor(new Color(40, 40, 50));
                    g.fillRect(0, 0, getWidth(), getHeight());
                    g.setColor(Color.WHITE);
                    g.setFont(FontManager.getBold(24));
                    String text = "No Image";
                    FontMetrics fm = g.getFontMetrics();
                    g.drawString(text,
                            (getWidth() - fm.stringWidth(text)) / 2,
                            (getHeight() + fm.getAscent()) / 2);
                }
            }
        };
        imagePanel.setPreferredSize(new Dimension(CARD_WIDTH - 20, 160));

        // Game info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Game title
        JLabel titleLabel = new JLabel(game.getTitle());
        titleLabel.setFont(FontManager.getBold(16));
        titleLabel.setForeground(Color.WHITE);

        // Game price
        JLabel priceLabel = new JLabel(String.format("$%.2f", game.getPrice()));
        priceLabel.setFont(FontManager.getMedium(14));
        priceLabel.setForeground(new Color(130, 90, 210));

        // Rating stars
        JPanel ratingPanel = createRatingStars(game.getRating());

        infoPanel.add(titleLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(ratingPanel);

        card.add(imagePanel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);

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
                // TODO: Navigate to game details page
                System.out.println("Clicked game: " + game.getTitle());
            }
        });

        return card;
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