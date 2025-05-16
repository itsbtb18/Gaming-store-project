package com.btbmina.gamestore.ui.pages.main;

import com.btbmina.gamestore.Util.ColorScheme;
import com.btbmina.gamestore.ui.FontManager;
import com.btbmina.gamestore.ui.components.*;
import com.btbmina.gamestore.DB.UserDB;
import com.btbmina.gamestore.classes.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends JFrame {
    private User currentUser;
    private JPanel carouselPanel;
    private int currentImageIndex = 0;
    private Timer slideTimer;
    private List<GamePromotion> promotions;

    private static class GamePromotion {
        String title;
        String description;
        ImageIcon image;
        double price;
        double discount;

        public GamePromotion(String title, String description, String imagePath, double price, double discount) {
            this.title = title;
            this.description = description;
            this.image = new ImageIcon(getClass().getResource(imagePath));
            this.price = price;
            this.discount = discount;
        }
    }

    public HomePage() {
        initializeFrame();
        loadUserData();
        createMainContent();
        initializePromotions();
        startImageSlideshow();
    }

    private void initializeFrame() {
        setTitle("Gaming Store");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setUndecorated(true);
    }

    private void loadUserData() {
        try {
            currentUser = UserDB.getCurrentUser();
            if (currentUser == null) {
                throw new IllegalStateException("User not logged in");
            }
        } catch (Exception e) {
            e.printStackTrace();
            currentUser = new User("Guest", "guest@example.com");
        }
    }

    private void createMainContent() {
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(ColorScheme.DARK_BACKGROUND);

        // Add TitleBar
        mainContainer.add(new TitleBar(this), BorderLayout.NORTH);

        // Create content panel with scroll
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ColorScheme.DARK_BACKGROUND);

        // Add MenuBar below TitleBar
        contentPanel.add(new com.btbmina.gamestore.ui.components.MenuBar(this, currentUser));


        // Add promotional carousel
        contentPanel.add(createCarouselSection());

        // Add categories
        contentPanel.add(createCategoriesSection());

        // Wrap in ScrollPane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.setBackground(ColorScheme.DARK_BACKGROUND);

        mainContainer.add(scrollPane, BorderLayout.CENTER);
        setContentPane(mainContainer);
    }

    private JPanel createCarouselSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(ColorScheme.DARK_BACKGROUND);
        section.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Featured & On Sale");
        titleLabel.setFont(FontManager.getTitle(24));
        titleLabel.setForeground(Color.WHITE);
        section.add(titleLabel, BorderLayout.NORTH);

        // Carousel
        carouselPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (promotions != null && !promotions.isEmpty()) {
                    GamePromotion current = promotions.get(currentImageIndex);
                    Image img = current.image.getImage();
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);

                    // Add gradient overlay
                    Graphics2D g2d = (Graphics2D) g;
                    GradientPaint gradient = new GradientPaint(
                            0, getHeight() - 100,
                            new Color(0, 0, 0, 0),
                            0, getHeight(),
                            new Color(0, 0, 0, 200)
                    );
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, getHeight() - 100, getWidth(), 100);
                }
            }
        };
        carouselPanel.setPreferredSize(new Dimension(0, 300));
        section.add(carouselPanel, BorderLayout.CENTER);

        return section;
    }

    private JPanel createCategoriesSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(ColorScheme.DARK_BACKGROUND);
        section.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Browse Categories");
        titleLabel.setFont(FontManager.getTitle(24));
        titleLabel.setForeground(Color.WHITE);
        section.add(titleLabel, BorderLayout.NORTH);

        // Categories grid
        JPanel grid = new JPanel(new GridLayout(2, 4, 15, 15));
        grid.setBackground(ColorScheme.DARK_BACKGROUND);

        String[] categories = {
                "Action", "Adventure", "RPG", "Strategy",
                "Sports", "Racing", "Indie", "Simulation"
        };

        for (String category : categories) {
            grid.add(createCategoryCard(category));
        }

        section.add(grid, BorderLayout.CENTER);
        return section;
    }

    private JPanel createCategoryCard(String category) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(
                        0, 0, ColorScheme.ACCENT_PINK,
                        getWidth(), getHeight(), ColorScheme.PRIMARY_PURPLE
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };

        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(category);
        label.setFont(FontManager.getBold(16));
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(label, BorderLayout.CENTER);
        return card;
    }

    private void initializePromotions() {
        promotions = new ArrayList<>();
        promotions.add(new GamePromotion(
                "Cyberpunk 2077",
                "50% OFF - Experience the future today!",
                "/images/promos/cyberpunk.jpg",
                59.99,
                0.5
        ));
        // Add more promotions...
    }

    private void startImageSlideshow() {
        slideTimer = new Timer(5000, e -> {
            currentImageIndex = (currentImageIndex + 1) % promotions.size();
            carouselPanel.repaint();
        });
        slideTimer.start();
    }
}
