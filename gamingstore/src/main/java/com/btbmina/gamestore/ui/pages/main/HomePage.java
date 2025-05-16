package com.btbmina.gamestore.ui.pages.main;

import com.btbmina.gamestore.Util.ColorScheme;
import com.btbmina.gamestore.ui.FontManager;
import com.btbmina.gamestore.ui.components.*;
import com.btbmina.gamestore.classes.User;
import com.btbmina.gamestore.DB.UserDB;
import com.btbmina.gamestore.ui.components.MenuBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
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
            this.price = price;
            this.discount = discount;
            loadImage(imagePath);
        }

        private void loadImage(String imagePath) {
            try {
                URL imageUrl = getClass().getResource(imagePath);
                if (imageUrl != null) {
                    this.image = new ImageIcon(imageUrl);
                } else {
                    File file = new File("src/main/resources" + imagePath);
                    if (file.exists()) {
                        this.image = new ImageIcon(file.getAbsolutePath());
                    } else {
                        System.err.println("Failed to load image: " + imagePath);
                        this.image = createPlaceholderImage();
                    }
                }
            } catch (Exception e) {
                System.err.println("Error loading image: " + e.getMessage());
                this.image = createPlaceholderImage();
            }
        }

        private ImageIcon createPlaceholderImage() {
            BufferedImage placeholder = new BufferedImage(800, 400, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = placeholder.createGraphics();
            GradientPaint gradient = new GradientPaint(
                    0, 0, ColorScheme.PRIMARY_PURPLE,
                    800, 400, ColorScheme.DARK_BACKGROUND
            );
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, 800, 400);
            g2d.dispose();
            return new ImageIcon(placeholder);
        }
    }

    public HomePage() {
        initializeFrame();
        loadUserData();
        createMainContent();
        initializePromotions();
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("Gaming Store - Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);

        // Get screen dimensions
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        if (gd.isFullScreenSupported()) {
            // Enable true full screen mode
            gd.setFullScreenWindow(this);
        } else {
            // Fallback to maximized window if full screen is not supported
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            // Set size to screen size
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setSize(screenSize.width, screenSize.height);
            setLocationRelativeTo(null);
        }
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

        // Create content panel with vertical layout
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ColorScheme.DARK_BACKGROUND);

        // Add MenuBar with spacing
        JPanel menuWrapper = new JPanel(new BorderLayout());
        menuWrapper.setBackground(ColorScheme.DARK_BACKGROUND);
        menuWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        menuWrapper.add(new MenuBar(this, currentUser), BorderLayout.CENTER);
        contentPanel.add(menuWrapper);

        // Add SearchBar section
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(createSearchSection());

        // Add main sections
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(createCarouselSection());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(createCategoriesSection());

        // Add scrollPane with custom scrollbar
        JScrollPane scrollPane = createScrollPane(contentPanel);
        mainContainer.add(scrollPane, BorderLayout.CENTER);

        setContentPane(mainContainer);
    }

    // Add this new method to create the search section
    private JPanel createSearchSection() {
        JPanel searchSection = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchSection.setBackground(ColorScheme.DARK_BACKGROUND);

        SearchBar searchBar = new SearchBar();
        // Set preferred size for the search bar
        searchBar.setPreferredSize(new Dimension(600, 40));

        // Create a wrapper panel for proper alignment
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(ColorScheme.DARK_BACKGROUND);
        wrapper.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        wrapper.add(searchBar, BorderLayout.CENTER);

        searchSection.add(wrapper);
        return searchSection;
    }

    private JScrollPane createScrollPane(JPanel content) {
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.setBackground(ColorScheme.DARK_BACKGROUND);
        return scrollPane;
    }

    private JPanel createCarouselSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(ColorScheme.DARK_BACKGROUND);
        section.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // Title with animation
        JLabel titleLabel = new JLabel("Featured Games");
        titleLabel.setFont(FontManager.getTitle(24));
        titleLabel.setForeground(Color.WHITE);
        section.add(titleLabel, BorderLayout.NORTH);

        // Carousel panel
        carouselPanel = createCarouselPanel();
        carouselPanel.setPreferredSize(new Dimension(0, 300));
        section.add(carouselPanel, BorderLayout.CENTER);

        return section;
    }

    private JPanel createCarouselPanel() {
        return new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (promotions != null && !promotions.isEmpty()) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                    GamePromotion current = promotions.get(currentImageIndex);

                    // Draw game image
                    if (current.image != null) {
                        Image img = current.image.getImage();
                        g2d.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                    }

                    // Draw gradient overlay
                    GradientPaint overlay = new GradientPaint(
                            0, getHeight() - 150,
                            new Color(0, 0, 0, 0),
                            0, getHeight(),
                            new Color(0, 0, 0, 200)
                    );
                    g2d.setPaint(overlay);
                    g2d.fillRect(0, getHeight() - 150, getWidth(), 150);

                    // Draw game info
                    drawGameInfo(g2d, current);
                    drawNavigationDots(g2d);
                }
            }
        };
    }

    private void drawGameInfo(Graphics2D g2d, GamePromotion game) {
        // Title
        g2d.setFont(FontManager.getBold(32));
        g2d.setColor(Color.WHITE);
        g2d.drawString(game.title, 40, carouselPanel.getHeight() - 80);

        // Description
        g2d.setFont(FontManager.getRegular(16));
        g2d.setColor(new Color(200, 200, 200));
        g2d.drawString(game.description, 40, carouselPanel.getHeight() - 45);

        // Price info
        drawPriceInfo(g2d, game);
    }

    private void drawPriceInfo(Graphics2D g2d, GamePromotion game) {
        int priceX = carouselPanel.getWidth() - 180;
        int priceY = carouselPanel.getHeight() - 45;

        if (game.discount > 0) {
            // Original price (struck through)
            String originalPrice = String.format("$%.2f", game.price);
            g2d.setFont(FontManager.getRegular(16));
            g2d.setColor(new Color(200, 200, 200));
            g2d.drawString(originalPrice, priceX, priceY);

            // Strike through line
            int strikeY = priceY - 4;
            g2d.drawLine(priceX, strikeY, priceX + g2d.getFontMetrics().stringWidth(originalPrice), strikeY);

            // Discounted price
            double discountedPrice = game.price * (1 - game.discount);
            String priceText = String.format("$%.2f", discountedPrice);
            g2d.setFont(FontManager.getBold(24));
            g2d.setColor(ColorScheme.ACCENT_PINK);
            g2d.drawString(priceText, priceX + 100, priceY);
        } else {
            // Regular price
            String priceText = String.format("$%.2f", game.price);
            g2d.setFont(FontManager.getBold(24));
            g2d.setColor(ColorScheme.ACCENT_PINK);
            g2d.drawString(priceText, priceX, priceY);
        }
    }

    private void drawNavigationDots(Graphics2D g2d) {
        if (promotions == null || promotions.isEmpty()) return;

        int dotSize = 8;
        int spacing = 20;
        int totalWidth = (promotions.size() * dotSize) + ((promotions.size() - 1) * spacing);
        int startX = (carouselPanel.getWidth() - totalWidth) / 2;
        int y = carouselPanel.getHeight() - 20;

        for (int i = 0; i < promotions.size(); i++) {
            int x = startX + (i * (dotSize + spacing));
            g2d.setColor(i == currentImageIndex ? Color.WHITE : new Color(255, 255, 255, 100));
            g2d.fillOval(x, y, dotSize, dotSize);
        }
    }

    private JPanel createCategoriesSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(ColorScheme.DARK_BACKGROUND);
        section.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Browse Categories");
        titleLabel.setFont(FontManager.getTitle(24));
        titleLabel.setForeground(Color.WHITE);
        section.add(titleLabel, BorderLayout.NORTH);

        // Categories grid
        JPanel grid = new JPanel(new GridLayout(2, 4, 15, 15));
        grid.setBackground(ColorScheme.DARK_BACKGROUND);

        String[][] categories = {
                {"Action", "🎮"}, {"Adventure", "🗺"},
                {"RPG", "⚔"}, {"Strategy", "🎯"},
                {"Sports", "⚽"}, {"Racing", "🏎"},
                {"Indie", "🎨"}, {"Simulation", "🌍"}
        };

        for (String[] category : categories) {
            grid.add(createCategoryCard(category[0], category[1]));
        }

        section.add(Box.createRigidArea(new Dimension(0, 20)), BorderLayout.CENTER);
        section.add(grid, BorderLayout.SOUTH);
        return section;
    }

    private JPanel createCategoryCard(String categoryName, String icon) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(
                        0, 0, ColorScheme.DARK_BACKGROUND,
                        0, getHeight(), ColorScheme.LIGHT_BACKGROUND
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            }
        };

        // Card setup
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(ColorScheme.DARK_BACKGROUND);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(180, 120));

        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Category name
        JLabel nameLabel = new JLabel(categoryName);
        nameLabel.setFont(FontManager.getBold(16));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Layout
        card.add(Box.createVerticalGlue());
        card.add(iconLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(nameLabel);
        card.add(Box.createVerticalGlue());

        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                animateHover(card, true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                animateHover(card, false);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Category clicked: " + categoryName);
                // TODO: Implement category navigation
            }
        });

        return card;
    }

    private void animateHover(JPanel card, boolean hovering) {
        Timer timer = new Timer(20, null);
        float[] alpha = {hovering ? 0f : 1f};
        Color startColor = hovering ? ColorScheme.DARK_BACKGROUND : ColorScheme.LIGHT_BACKGROUND;
        Color endColor = hovering ? ColorScheme.LIGHT_BACKGROUND : ColorScheme.DARK_BACKGROUND;

        timer.addActionListener(e -> {
            if (hovering) {
                alpha[0] += 0.1f;
                if (alpha[0] >= 1f) {
                    alpha[0] = 1f;
                    timer.stop();
                }
            } else {
                alpha[0] -= 0.1f;
                if (alpha[0] <= 0f) {
                    alpha[0] = 0f;
                    timer.stop();
                }
            }

            Color currentColor = interpolateColor(startColor, endColor, alpha[0]);
            card.setBackground(currentColor);
            card.repaint();
        });

        timer.start();
    }

    private Color interpolateColor(Color start, Color end, float ratio) {
        int red = (int) (start.getRed() * (1 - ratio) + end.getRed() * ratio);
        int green = (int) (start.getGreen() * (1 - ratio) + end.getGreen() * ratio);
        int blue = (int) (start.getBlue() * (1 - ratio) + end.getBlue() * ratio);
        return new Color(red, green, blue);
    }

    private void initializePromotions() {
        promotions = new ArrayList<>();

        promotions.add(new GamePromotion(
                "Cyberpunk 2077",
                "Experience the future of gaming - 50% OFF!",
                "/images/promos/cyberpunk.jpg",
                59.99,
                0.50
        ));

        promotions.add(new GamePromotion(
                "Elden Ring",
                "Journey through the Lands Between",
                "/images/promos/elden-ring.jpg",
                69.99,
                0.0
        ));

        promotions.add(new GamePromotion(
                "God of War Ragnarök",
                "Epic Norse adventure - 30% OFF",
                "/images/promos/god-of-war.jpg",
                49.99,
                0.30
        ));

        if (!promotions.isEmpty()) {
            startImageSlideshow();
        }
    }

    private void startImageSlideshow() {
        if (slideTimer != null) {
            slideTimer.stop();
        }

        slideTimer = new Timer(5000, e -> {
            currentImageIndex = (currentImageIndex + 1) % promotions.size();
            if (carouselPanel != null) {
                carouselPanel.repaint();
            }
        });
        slideTimer.start();
    }
}