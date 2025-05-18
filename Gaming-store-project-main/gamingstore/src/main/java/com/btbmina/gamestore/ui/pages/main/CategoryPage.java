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

public class CategoryPage extends JFrame {
    private User currentUser;
    private String categoryName;
    private List<Game> categoryGames;

    public CategoryPage(String categoryName, User currentUser) {
        this.categoryName = categoryName;
        this.currentUser = currentUser;

        setUndecorated(true);

        initializeFrame();
        loadGames();
        createContent();
    }

    private void initializeFrame() {
        setTitle("Gaming Store - " + categoryName);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void loadGames() {
        try {
            categoryGames = GameDB.getGamesByCategory(categoryName);
            if (categoryGames.isEmpty()) {
                System.out.println("No games found for category: " + categoryName);
            } else {
                System.out.println("Loaded " + categoryGames.size() + " games for category: " + categoryName);
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

        mainContainer.add(new TitleBar(this), BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ColorScheme.DARK_BACKGROUND);

        JPanel menuWrapper = new JPanel(new BorderLayout());
        menuWrapper.setBackground(ColorScheme.DARK_BACKGROUND);
        menuWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        MenuBar menuBar = new MenuBar(this, currentUser);

        menuWrapper.add(menuBar, BorderLayout.CENTER);
        contentPanel.add(menuWrapper);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Spacing
        contentPanel.add(createCategoryHeader());

        if (categoryGames != null && !categoryGames.isEmpty()) {
            contentPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Spacing
            contentPanel.add(createGamesList());
        } else {
            contentPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Spacing
            contentPanel.add(createEmptyStatePanel());
        }

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
        header.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        JPanel titleWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titleWrapper.setBackground(ColorScheme.DARK_BACKGROUND);

        JLabel iconLabel = new JLabel(getCategoryIcon());
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel(categoryName + " Games");
        titleLabel.setFont(FontManager.getBold(28));
        titleLabel.setForeground(Color.WHITE);

        titleWrapper.add(iconLabel);
        titleWrapper.add(titleLabel);

        JLabel countLabel = new JLabel(categoryGames.size() + " games");
        countLabel.setFont(FontManager.getMedium(14));
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

    private JPanel createGamesList() {
        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(ColorScheme.DARK_BACKGROUND);
        listContainer.setBorder(BorderFactory.createEmptyBorder(0, 40, 20, 40));

        // Create a list panel to hold all game items
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(ColorScheme.DARK_BACKGROUND);

        for (Game game : categoryGames) {
            listPanel.add(createGameListItem(game));
            listPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between items
        }

        listContainer.add(listPanel);
        return listContainer;
    }

    private JPanel createGameListItem(Game game) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(new Color(30, 30, 40));
        item.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel nameLabel = new JLabel(game.getTitle());
        nameLabel.setFont(FontManager.getMedium(18));
        nameLabel.setForeground(Color.WHITE);

        JLabel priceLabel = new JLabel(String.format("$%.2f", game.getPrice()));
        priceLabel.setFont(FontManager.getBold(16));
        priceLabel.setForeground(new Color(130, 90, 210));

        item.add(nameLabel, BorderLayout.WEST);
        item.add(priceLabel, BorderLayout.EAST);

        // Add hover effect
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                item.setBackground(new Color(40, 40, 60));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                item.setBackground(new Color(30, 30, 40));
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                navigateToGame(game.getId());
            }
        });

        return item;
    }

    private void navigateToGame(int gameId) {
        SwingUtilities.invokeLater(() -> {
            try {
                Game selectedGame = GameDB.getGameById(gameId);
                if (selectedGame == null) {
                    showError("Error", "Game not found");
                    return;
                }

                dispose();

                GamePage gamePage = new GamePage(selectedGame, currentUser);

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

    private JPanel createEmptyStatePanel() {
        JPanel emptyState = new JPanel(new GridBagLayout());
        emptyState.setBackground(ColorScheme.DARK_BACKGROUND);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(ColorScheme.DARK_BACKGROUND);

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

    private void showError(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
}