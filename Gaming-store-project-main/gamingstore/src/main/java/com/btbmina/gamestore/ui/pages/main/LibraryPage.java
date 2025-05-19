package com.btbmina.gamestore.ui.pages.main;

import com.btbmina.gamestore.Util.ColorScheme;
import com.btbmina.gamestore.ui.components.ModernScrollBarUI;
import com.btbmina.gamestore.ui.components.PurpleButton;
import com.btbmina.gamestore.ui.components.TitleBar;
import com.btbmina.gamestore.ui.components.MenuBar;
import com.btbmina.gamestore.DB.UserDB;
import com.btbmina.gamestore.classes.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A fullscreen JFrame that displays the user's game library
 * with a professional black and purple themed UI.
 */
public class LibraryPage extends JFrame {

    private final List<String> gamesList = new ArrayList<>();
    private JPanel mainPanel;
    private JPanel gamesListPanel;
    private JTextField searchField;
    private JComboBox<String> sortComboBox;
    private User currentUser;

    public LibraryPage() {
        try {
            this.currentUser = UserDB.getCurrentUser();
        } catch (Exception e) {
            e.printStackTrace();
            this.currentUser = new User("Guest", "guest@example.com");
        }

        initializeSampleData();
        initializeFrame();
        initializeUI();
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("BTB Mina Game Store - My Library");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(this);
        } else {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setSize(screenSize.width, screenSize.height);
            setLocationRelativeTo(null);
        }
    }

    private void initializeUI() {
        // Main panel with BorderLayout
        mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(ColorScheme.DARK_BACKGROUND);

        // Add TitleBar
        TitleBar titleBar = new TitleBar(this);
        titleBar.setTitle("Library");
        mainPanel.add(titleBar, BorderLayout.NORTH);

        // Content wrapper panel for everything below TitleBar
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(ColorScheme.DARK_BACKGROUND);

        // Add MenuBar below TitleBar
        MenuBar menuBar = new MenuBar(this, currentUser);
        contentWrapper.add(menuBar, BorderLayout.NORTH);

        // Create and add components to a separate content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(ColorScheme.DARK_BACKGROUND);
        addHeader(contentPanel);
        addSidebar(contentPanel);
        addGamesList(contentPanel);
        addFooter(contentPanel);

        contentWrapper.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(contentWrapper, BorderLayout.CENTER);

        // Add main panel to frame
        setContentPane(mainPanel);
    }

    private void initializeSampleData() {
        // Sample game titles - replace with actual games from database
        gamesList.add("Cyberpunk 2077");
        gamesList.add("The Witcher 3: Wild Hunt");
        gamesList.add("Red Dead Redemption 2");
        gamesList.add("Elden Ring");
        gamesList.add("God of War");
        gamesList.add("Horizon Zero Dawn");
        gamesList.add("Control");
        gamesList.add("Death Stranding");
        gamesList.add("Disco Elysium");
        gamesList.add("Hades");
        gamesList.add("Doom Eternal");
        gamesList.add("Hollow Knight");
        gamesList.add("Celeste");
        gamesList.add("Stardew Valley");
        gamesList.add("Baldur's Gate 3");
    }

    private void addHeader(JPanel contentPanel) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ColorScheme.PRIMARY_PURPLE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 70));

        // Title
        JLabel titleLabel = new JLabel("MY GAME LIBRARY");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);

        searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.ACCENT_PURPLE, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        searchField.setBackground(ColorScheme.DARK_BACKGROUND);
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);

        JButton searchButton = new PurpleButton("Search");
        searchButton.setPreferredSize(new Dimension(100, 30));

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        contentPanel.add(headerPanel, BorderLayout.NORTH);
    }

    private void addSidebar(JPanel contentPanel) {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(ColorScheme.MEDIUM_BACKGROUND);
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        sidebarPanel.setPreferredSize(new Dimension(200, getHeight()));

        // Add category buttons
        String[] categories = {"All Games", "Installed", "Not Installed", "Recently Played", "Favorites"};

        for (String category : categories) {
            JButton categoryButton = createSidebarButton(category);
            sidebarPanel.add(categoryButton);
            sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        // Add spacing
        sidebarPanel.add(Box.createVerticalGlue());

        // Add settings button at bottom
        JButton settingsButton = createSidebarButton("Settings");
        sidebarPanel.add(settingsButton);

        contentPanel.add(sidebarPanel, BorderLayout.WEST);
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(170, 40));
        button.setBackground(ColorScheme.MEDIUM_BACKGROUND);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ColorScheme.PRIMARY_PURPLE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(ColorScheme.MEDIUM_BACKGROUND);
            }
        });

        return button;
    }

    private void addGamesList(JPanel contentPanel) {
        // Container panel
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(ColorScheme.DARK_BACKGROUND);
        containerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Filter and sort toolbar
        JPanel toolbarPanel = new JPanel(new BorderLayout());
        toolbarPanel.setBackground(ColorScheme.MEDIUM_BACKGROUND);
        toolbarPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Filter buttons panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setOpaque(false);

        String[] filters = {"All", "Action", "Adventure", "RPG", "Strategy", "Sports", "Simulation"};
        for (String filter : filters) {
            PurpleButton filterButton = new PurpleButton(filter);
            filterButton.setPreferredSize(new Dimension(100, 30));
            filterPanel.add(filterButton);
        }

        // Sort panel
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sortPanel.setOpaque(false);

        JLabel sortLabel = new JLabel("Sort by:");
        sortLabel.setForeground(Color.WHITE);

        String[] sortOptions = {"Name (A-Z)", "Name (Z-A)", "Recently Added", "Size"};
        sortComboBox = new JComboBox<>(sortOptions);
        sortComboBox.setBackground(ColorScheme.DARK_BACKGROUND);
        sortComboBox.setForeground(Color.WHITE);
        sortComboBox.setPreferredSize(new Dimension(150, 30));
        sortComboBox.addActionListener(e -> {
            String selectedOption = (String) sortComboBox.getSelectedItem();
            if (selectedOption != null) {
                sortGamesList(selectedOption);
            }
        });

        sortPanel.add(sortLabel);
        sortPanel.add(sortComboBox);

        toolbarPanel.add(filterPanel, BorderLayout.WEST);
        toolbarPanel.add(sortPanel, BorderLayout.EAST);

        // Games list panel
        gamesListPanel = new JPanel();
        gamesListPanel.setLayout(new BoxLayout(gamesListPanel, BoxLayout.Y_AXIS));
        gamesListPanel.setBackground(ColorScheme.DARK_BACKGROUND);

        refreshGamesList();

        JScrollPane scrollPane = new JScrollPane(gamesListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(ColorScheme.DARK_BACKGROUND);
        
        // Add modern scroll bar UI
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new ModernScrollBarUI());

        // Add components to container
        containerPanel.add(toolbarPanel, BorderLayout.NORTH);
        containerPanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(containerPanel, BorderLayout.CENTER);
    }

    private void refreshGamesList() {
        gamesListPanel.removeAll();

        for (String gameName : gamesList) {
            JPanel gameEntry = createGameEntry(gameName);
            gamesListPanel.add(gameEntry);
            gamesListPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        gamesListPanel.revalidate();
        gamesListPanel.repaint();
    }

    private JPanel createGameEntry(String gameName) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ColorScheme.MEDIUM_BACKGROUND);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 10, 5, 10),
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(70, 70, 70))));

        // Game title
        JLabel titleLabel = new JLabel(gameName);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        // Action buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setOpaque(false);

        PurpleButton playButton = new PurpleButton("PLAY");
        playButton.setPreferredSize(new Dimension(100, 35));
        playButton.addActionListener(e -> handlePlayGame(gameName));

        JButton optionsButton = new JButton("•••");
        optionsButton.setFocusPainted(false);
        optionsButton.setBorderPainted(false);
        optionsButton.setContentAreaFilled(false);
        optionsButton.setForeground(Color.WHITE);
        optionsButton.setFont(new Font("Arial", Font.BOLD, 16));
        optionsButton.addActionListener(e -> showOptionsMenu(optionsButton, gameName));

        buttonsPanel.add(playButton);
        buttonsPanel.add(optionsButton);

        // Add hover effect
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(50, 40, 60));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(ColorScheme.MEDIUM_BACKGROUND);
            }
        });

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(buttonsPanel, BorderLayout.EAST);

        return panel;
    }

    private void handlePlayGame(String gameName) {
        // TODO: Implement game launch logic
        JOptionPane.showMessageDialog(this, 
            "Launching game: " + gameName, 
            "Game Launch", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void showOptionsMenu(JButton optionsButton, String gameName) {
        JPopupMenu menu = new JPopupMenu();
        menu.setBackground(ColorScheme.MEDIUM_BACKGROUND);
        
        JMenuItem[] items = {
            createMenuItem("Install"),
            createMenuItem("Uninstall"),
            createMenuItem("Properties"),
            createMenuItem("Open Game Folder")
        };
        
        for (JMenuItem item : items) {
            menu.add(item);
        }
        
        menu.show(optionsButton, 0, optionsButton.getHeight());
    }

    private JMenuItem createMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setForeground(Color.WHITE);
        item.setBackground(ColorScheme.MEDIUM_BACKGROUND);
        item.addActionListener(e -> handleMenuAction(text));
        
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                item.setBackground(ColorScheme.PRIMARY_PURPLE);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                item.setBackground(ColorScheme.MEDIUM_BACKGROUND);
            }
        });
        
        return item;
    }

    private void handleMenuAction(String action) {
        // TODO: Implement menu actions
        JOptionPane.showMessageDialog(this, 
            "Selected action: " + action, 
            "Menu Action", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void addFooter(JPanel contentPanel) {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(ColorScheme.MEDIUM_BACKGROUND);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        footerPanel.setPreferredSize(new Dimension(getWidth(), 40));

        // Library stats
        JLabel statsLabel = new JLabel("Total Games: " + gamesList.size());
        statsLabel.setForeground(Color.LIGHT_GRAY);

        // Storage info
        JLabel storageLabel = new JLabel("Available Storage: 543.2 GB");
        storageLabel.setForeground(Color.LIGHT_GRAY);

        footerPanel.add(statsLabel, BorderLayout.WEST);
        footerPanel.add(storageLabel, BorderLayout.EAST);

        contentPanel.add(footerPanel, BorderLayout.SOUTH);
    }

    private void sortGamesList(String sortOption) {
        switch (sortOption) {
            case "Name (A-Z)":
                gamesList.sort(String::compareTo);
                break;
            case "Name (Z-A)":
                gamesList.sort((a, b) -> b.compareTo(a));
                break;
            case "Recently Added":
                // For now, just reverse the list as a simple simulation
                java.util.Collections.reverse(gamesList);
                break;
            case "Size":
                // Sort by string length as a simple simulation
                gamesList.sort((a, b) -> Integer.compare(a.length(), b.length()));
                break;
        }
        refreshGamesList();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(LibraryPage::new);
    }
}
