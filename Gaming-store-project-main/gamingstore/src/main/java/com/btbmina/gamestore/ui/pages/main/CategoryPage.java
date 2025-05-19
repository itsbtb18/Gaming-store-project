package com.btbmina.gamestore.ui.pages.main;

import com.btbmina.gamestore.Util.ColorScheme;
import com.btbmina.gamestore.ui.FontManager;
import com.btbmina.gamestore.ui.components.*;
import com.btbmina.gamestore.classes.Game;
import com.btbmina.gamestore.DB.GameDB;
import com.btbmina.gamestore.classes.User;
import com.btbmina.gamestore.ui.components.MenuBar;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

public class CategoryPage extends JFrame {
    private User currentUser;
    private String categoryName;
    private List<Game> categoryGames;

    // Custom colors for black and purple theme
    private static final Color BACKGROUND_BLACK = new Color(15, 15, 20);
    private static final Color CARD_BACKGROUND = new Color(25, 25, 35);
    private static final Color CARD_HOVER = new Color(35, 30, 50);
    private static final Color ACCENT_PURPLE = new Color(128, 55, 230);
    private static final Color LIGHT_PURPLE = new Color(150, 90, 240);
    private static final Color TEXT_WHITE = new Color(240, 240, 245);
    private static final Color TEXT_GRAY = new Color(180, 180, 195);

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
        // Set up main container with BorderLayout
        JPanel mainContainer = new JPanel(new BorderLayout(0, 0));
        mainContainer.setBackground(BACKGROUND_BLACK);

        // Add title bar at the top
        TitleBar titleBar = new TitleBar(this);
        mainContainer.add(titleBar, BorderLayout.NORTH);

        // Create central panel with BorderLayout
        JPanel centralPanel = new JPanel(new BorderLayout());
        centralPanel.setBackground(BACKGROUND_BLACK);

        // Create menu bar
        MenuBar menuBar = new MenuBar(this, currentUser);
        menuBar.setBorder(new MatteBorder(0, 0, 1, 0, new Color(40, 40, 55)));
        centralPanel.add(menuBar, BorderLayout.NORTH);

        // Main content area
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_BLACK);

        // Add category header
        contentPanel.add(createCategoryHeader(), BorderLayout.NORTH);

        // Add game list or empty state in a scroll pane
        JPanel gamesContainer = categoryGames != null && !categoryGames.isEmpty() 
            ? createGamesList() 
            : createEmptyStatePanel();

        JScrollPane scrollPane = new JScrollPane(gamesContainer);
        scrollPane.setBorder(null);
        scrollPane.setBackground(BACKGROUND_BLACK);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        contentPanel.add(scrollPane, BorderLayout.CENTER);
        centralPanel.add(contentPanel, BorderLayout.CENTER);

        // Add central panel to main container
        mainContainer.add(centralPanel, BorderLayout.CENTER);

        // Set component sizes
        setPreferredSize(new Dimension(1200, 800));
        titleBar.setPreferredSize(new Dimension(getWidth(), 40));
        menuBar.setPreferredSize(new Dimension(getWidth(), 60));

        setContentPane(mainContainer);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createCategoryHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_BLACK);
        headerPanel.setBorder(new EmptyBorder(30, 40, 20, 40));

        JLabel categoryLabel = new JLabel(categoryName);
        categoryLabel.setFont(FontManager.getBold(28));
        categoryLabel.setForeground(TEXT_WHITE);

        headerPanel.add(categoryLabel, BorderLayout.WEST);
        return headerPanel;
    }

    private JPanel createGamesList() {
        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(BACKGROUND_BLACK);
        listContainer.setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40));

        JPanel gamesPanel = new JPanel(new GridLayout(0, 1, 0, 10));
        gamesPanel.setBackground(BACKGROUND_BLACK);

        for (Game game : categoryGames) {
            gamesPanel.add(createGameListItem(game));
        }

        listContainer.add(gamesPanel);
        return listContainer;
    }

    private JPanel createGameListItem(Game game) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(CARD_BACKGROUND);
        item.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(0, 0, 0, 0),
            new CompoundBorder(
                BorderFactory.createLineBorder(new Color(40, 40, 55), 1),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
            )
        ));

        // Create content panel with proper padding
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(CARD_BACKGROUND);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));

        // Game title with custom font
        JLabel nameLabel = new JLabel(game.getTitle());
        nameLabel.setFont(FontManager.getMedium(16));
        nameLabel.setForeground(TEXT_WHITE);

        // Right side panel for price
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setBackground(CARD_BACKGROUND);

        // Price label with accent color
        JLabel priceLabel = new JLabel(String.format("$%.2f", game.getPrice()));
        priceLabel.setFont(FontManager.getBold(16));
        priceLabel.setForeground(ACCENT_PURPLE);

        // Arrow icon for visual indicator
        JLabel arrowLabel = new JLabel(" ›");
        arrowLabel.setFont(FontManager.getBold(18));
        arrowLabel.setForeground(TEXT_GRAY);

        rightPanel.add(priceLabel);
        rightPanel.add(arrowLabel);

        contentPanel.add(nameLabel, BorderLayout.WEST);
        contentPanel.add(rightPanel, BorderLayout.EAST);

        item.add(contentPanel, BorderLayout.CENTER);

        // Add hover effect with smooth transition
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                item.setBackground(CARD_HOVER);
                contentPanel.setBackground(CARD_HOVER);
                rightPanel.setBackground(CARD_HOVER);
                arrowLabel.setForeground(LIGHT_PURPLE);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                item.setBackground(CARD_BACKGROUND);
                contentPanel.setBackground(CARD_BACKGROUND);
                rightPanel.setBackground(CARD_BACKGROUND);
                arrowLabel.setForeground(TEXT_GRAY);
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
        emptyState.setBackground(BACKGROUND_BLACK);
        emptyState.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BACKGROUND_BLACK);

        JLabel iconLabel = new JLabel("🎮");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 72));
        iconLabel.setForeground(ACCENT_PURPLE);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("No games found");
        titleLabel.setFont(FontManager.getBold(24));
        titleLabel.setForeground(TEXT_WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel messageLabel = new JLabel("We'll add more games to this category soon!");
        messageLabel.setFont(FontManager.getRegular(16));
        messageLabel.setForeground(TEXT_GRAY);
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
        UIManager.put("OptionPane.background", CARD_BACKGROUND);
        UIManager.put("Panel.background", CARD_BACKGROUND);
        UIManager.put("OptionPane.messageForeground", TEXT_WHITE);
        UIManager.put("Button.background", ACCENT_PURPLE);
        UIManager.put("Button.foreground", TEXT_WHITE);

        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }
}