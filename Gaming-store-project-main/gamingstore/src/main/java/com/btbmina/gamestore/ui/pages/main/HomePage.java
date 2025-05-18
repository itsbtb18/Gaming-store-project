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
    private float carouselTransitionAlpha = 1.0f;
    private GamePromotion previousPromotion = null;
    private Timer transitionTimer;
    private JFrame mainFrame;
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
                    800, 400, new Color(13, 17, 23)
            );
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, 800, 400);

            g2d.setColor(new Color(255, 255, 255, 40));
            for (int i = 0; i < 20; i++) {
                int x = (int)(Math.random() * 800);
                int y = (int)(Math.random() * 400);
                int size = (int)(Math.random() * 5) + 1;
                g2d.fillRect(x, y, size, size);
            }

            g2d.dispose();
            return new ImageIcon(placeholder);
        }
    }

    public HomePage() {
        this.mainFrame = this; // Set the mainFrame reference
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

        mainContainer.add(new TitleBar(this), BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ColorScheme.DARK_BACKGROUND);

        JPanel menuWrapper = new JPanel(new BorderLayout());
        menuWrapper.setBackground(ColorScheme.DARK_BACKGROUND);
        menuWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        menuWrapper.add(new MenuBar(this, currentUser), BorderLayout.CENTER);
        contentPanel.add(menuWrapper);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(createSearchSection());

        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(createCarouselSection());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(createCategoriesSection());

        JScrollPane scrollPane = createScrollPane(contentPanel);
        mainContainer.add(scrollPane, BorderLayout.CENTER);

        setContentPane(mainContainer);
    }

    private JPanel createSearchSection() {
        JPanel searchSection = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchSection.setBackground(ColorScheme.DARK_BACKGROUND);

        SearchBar searchBar = new SearchBar();

        searchBar.setPreferredSize(new Dimension(600, 40));

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

        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        return scrollPane;
    }

    private JPanel createCarouselSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(ColorScheme.DARK_BACKGROUND);
        section.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));


        JLabel titleLabel = new JLabel("Featured Games");
        titleLabel.setFont(FontManager.getTitle(24));
        titleLabel.setForeground(Color.WHITE);

        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));

        section.add(titleLabel, BorderLayout.NORTH);

        carouselPanel = createCarouselPanel();
        carouselPanel.setPreferredSize(new Dimension(0, 400)); // Increased height
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
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                    GamePromotion current = promotions.get(currentImageIndex);

                    if (carouselTransitionAlpha < 1.0f && previousPromotion != null) {

                        if (previousPromotion.image != null) {
                            Image prevImg = previousPromotion.image.getImage();
                            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f - carouselTransitionAlpha));
                            g2d.drawImage(prevImg, 0, 0, getWidth(), getHeight(), this);
                        }
                    }

                    if (current.image != null) {
                        Image img = current.image.getImage();
                        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, carouselTransitionAlpha));
                        g2d.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                    }

                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

                    Paint originalPaint = g2d.getPaint();

                    GradientPaint overlay = new GradientPaint(
                            0, getHeight() - 200,
                            new Color(0, 0, 0, 0),
                            0, getHeight(),
                            new Color(13, 17, 23, 230)
                    );
                    g2d.setPaint(overlay);
                    g2d.fillRect(0, getHeight() - 200, getWidth(), 200);

                    GradientPaint leftOverlay = new GradientPaint(
                            0, 0,
                            new Color(0, 0, 0, 180),
                            300, 0,
                            new Color(0, 0, 0, 0)
                    );
                    g2d.setPaint(leftOverlay);
                    g2d.fillRect(0, 0, 300, getHeight());

                    g2d.setPaint(originalPaint);

                    drawGameInfo(g2d, current);
                    drawNavigationDots(g2d);

                    g2d.setColor(new Color(ColorScheme.PRIMARY_PURPLE.getRed(),
                            ColorScheme.PRIMARY_PURPLE.getGreen(),
                            ColorScheme.PRIMARY_PURPLE.getBlue(), 40));
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
                }
            }
        };
    }

    private void drawGameInfo(Graphics2D g2d, GamePromotion game) {

        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRoundRect(30, carouselPanel.getHeight() - 130, 500, 100, 15, 15);

        g2d.setFont(FontManager.getBold(36)); // Increased size

        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.drawString(game.title, 42, carouselPanel.getHeight() - 80);

        g2d.setColor(Color.WHITE);
        g2d.drawString(game.title, 40, carouselPanel.getHeight() - 82);

        g2d.setFont(FontManager.getRegular(18)); // Increased size
        g2d.setColor(new Color(220, 220, 220));
        g2d.drawString(game.description, 40, carouselPanel.getHeight() - 45);

        drawPriceInfo(g2d, game);

        g2d.setColor(ColorScheme.PRIMARY_PURPLE);
        g2d.fillRoundRect(40, carouselPanel.getHeight() - 30, 120, 30, 15, 15);

        g2d.setColor(Color.WHITE);
        g2d.setFont(FontManager.getBold(14));
        String playText = "PLAY NOW";
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(playText);
        g2d.drawString(playText, 40 + (120 - textWidth)/2, carouselPanel.getHeight() - 10);
    }

    private void drawPriceInfo(Graphics2D g2d, GamePromotion game) {
        int priceX = carouselPanel.getWidth() - 220;
        int priceY = carouselPanel.getHeight() - 45;

        g2d.setColor(new Color(0, 0, 0, 150));
        int width = 180;
        int height = 60;
        g2d.fillRoundRect(priceX - 20, priceY - 40, width, height, 15, 15);

        g2d.setColor(new Color(ColorScheme.ACCENT_PINK.getRed(),
                ColorScheme.ACCENT_PINK.getGreen(),
                ColorScheme.ACCENT_PINK.getBlue(), 80));
        g2d.drawRoundRect(priceX - 20, priceY - 40, width, height, 15, 15);

        if (game.discount > 0) {

            g2d.setColor(ColorScheme.ACCENT_PINK);
            String discountText = "-" + (int)(game.discount * 100) + "%";
            g2d.setFont(FontManager.getBold(14));
            FontMetrics fm = g2d.getFontMetrics();
            int discountWidth = fm.stringWidth(discountText) + 20;
            g2d.fillRoundRect(priceX - 10, priceY - 60, discountWidth, 24, 12, 12);

            g2d.setColor(Color.WHITE);
            g2d.drawString(discountText, priceX, priceY - 44);

            String originalPrice = String.format("$%.2f", game.price);
            g2d.setFont(FontManager.getRegular(16));
            g2d.setColor(new Color(180, 180, 180));
            g2d.drawString(originalPrice, priceX, priceY - 10);

            int strikeY = priceY - 14;
            g2d.drawLine(priceX, strikeY, priceX + g2d.getFontMetrics().stringWidth(originalPrice), strikeY);


            double discountedPrice = game.price * (1 - game.discount);
            String priceText = String.format("$%.2f", discountedPrice);
            g2d.setFont(FontManager.getBold(28));

            g2d.setColor(new Color(ColorScheme.ACCENT_PINK.getRed(),
                    ColorScheme.ACCENT_PINK.getGreen(),
                    ColorScheme.ACCENT_PINK.getBlue(), 100));
            g2d.drawString(priceText, priceX + 2, priceY + 20);

            g2d.setColor(ColorScheme.ACCENT_PINK);
            g2d.drawString(priceText, priceX, priceY + 18);
        } else {

            String priceText = String.format("$%.2f", game.price);
            g2d.setFont(FontManager.getBold(28));

            g2d.setColor(new Color(ColorScheme.ACCENT_PINK.getRed(),
                    ColorScheme.ACCENT_PINK.getGreen(),
                    ColorScheme.ACCENT_PINK.getBlue(), 100));
            g2d.drawString(priceText, priceX + 2, priceY + 2);

            g2d.setColor(ColorScheme.ACCENT_PINK);
            g2d.drawString(priceText, priceX, priceY);
        }
    }

    private void drawNavigationDots(Graphics2D g2d) {
        if (promotions == null || promotions.isEmpty()) return;

        int dotSize = 10;  // Larger dots
        int spacing = 20;
        int totalWidth = (promotions.size() * dotSize) + ((promotions.size() - 1) * spacing);
        int startX = (carouselPanel.getWidth() - totalWidth) / 2;
        int y = carouselPanel.getHeight() - 20;

        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRoundRect(startX - 10, y - 5, totalWidth + 20, dotSize + 10, 10, 10);

        for (int i = 0; i < promotions.size(); i++) {
            int x = startX + (i * (dotSize + spacing));

            if (i == currentImageIndex) {

                g2d.setColor(new Color(255, 255, 255, 80));
                g2d.fillOval(x-2, y-2, dotSize+4, dotSize+4);
                g2d.setColor(Color.WHITE);
            } else {

                g2d.setColor(new Color(255, 255, 255, 100));
            }

            g2d.fillOval(x, y, dotSize, dotSize);
        }
    }

    private JPanel createCategoriesSection() {
        JPanel section = new JPanel(new BorderLayout());
        section.setBackground(ColorScheme.DARK_BACKGROUND);
        section.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JLabel titleLabel = new JLabel("Browse Categories");
        titleLabel.setFont(FontManager.getTitle(24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
        section.add(titleLabel, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 4, 15, 15));
        grid.setBackground(ColorScheme.DARK_BACKGROUND);

        String[][] categories = {
                {"Shooter", "🎮"}, {"Action", "🗺"},
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
                        0, 0, new Color(ColorScheme.DARK_BACKGROUND.getRed(),
                        ColorScheme.DARK_BACKGROUND.getGreen(),
                        ColorScheme.DARK_BACKGROUND.getBlue(), 255),
                        getWidth(), getHeight(), new Color(ColorScheme.PRIMARY_PURPLE.getRed(),
                        ColorScheme.PRIMARY_PURPLE.getGreen(),
                        ColorScheme.PRIMARY_PURPLE.getBlue(), 80)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

                g2d.setColor(new Color(255, 255, 255, 20));
                for (int i = 0; i < 10; i++) {
                    int x = (int)(Math.random() * getWidth());
                    int y = (int)(Math.random() * getHeight());
                    int size = (int)(Math.random() * 3) + 1;
                    g2d.fillRect(x, y, size, size);
                }
            }
        };

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(ColorScheme.DARK_BACKGROUND);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(180, 120));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(categoryName);
        nameLabel.setFont(FontManager.getBold(18));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(iconLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(nameLabel);
        card.add(Box.createVerticalGlue());

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
                navigateToCategory(categoryName);
            }
        });

        return card;
    }
    private void navigateToCategory(String categoryName) {
        SwingUtilities.invokeLater(() -> {
            try {

                CategoryPage categoryPage = new CategoryPage(categoryName, currentUser);

                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice gd = ge.getDefaultScreenDevice();

                dispose();

                if (gd.isFullScreenSupported()) {
                    gd.setFullScreenWindow(categoryPage);
                } else {
                    categoryPage.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                    categoryPage.setSize(screenSize.width, screenSize.height);
                    categoryPage.setLocationRelativeTo(null);
                }

                categoryPage.setVisible(true);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        this,
                        "Error loading category page: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
    private void animateHover(JPanel card, boolean hovering) {
        Timer timer = new Timer(15, null); // Faster animation
        float[] alpha = {hovering ? 0f : 1f};

        Color startBg = hovering ? ColorScheme.DARK_BACKGROUND :
                new Color(ColorScheme.PRIMARY_PURPLE.getRed(),
                        ColorScheme.PRIMARY_PURPLE.getGreen(),
                        ColorScheme.PRIMARY_PURPLE.getBlue(), 100);

        Color endBg = hovering ? new Color(ColorScheme.PRIMARY_PURPLE.getRed(),
                ColorScheme.PRIMARY_PURPLE.getGreen(),
                ColorScheme.PRIMARY_PURPLE.getBlue(), 100) :
                ColorScheme.DARK_BACKGROUND;

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

            Color currentColor = interpolateColor(startBg, endBg, alpha[0]);
            card.setBackground(currentColor);

            float scale = hovering ? 1.0f + (0.05f * alpha[0]) : 1.0f - (0.02f * (1.0f - alpha[0]));
            card.setBorder(BorderFactory.createEmptyBorder((int)(15 * scale),
                    (int)(15 * scale),
                    (int)(15 * scale),
                    (int)(15 * scale)));

            card.repaint();
        });

        timer.start();
    }

    private Color interpolateColor(Color start, Color end, float ratio) {
        int red = (int) (start.getRed() * (1 - ratio) + end.getRed() * ratio);
        int green = (int) (start.getGreen() * (1 - ratio) + end.getGreen() * ratio);
        int blue = (int) (start.getBlue() * (1 - ratio) + end.getBlue() * ratio);
        int alpha = (int) (start.getAlpha() * (1 - ratio) + end.getAlpha() * ratio);
        return new Color(red, green, blue, alpha);
    }

    private void initializePromotions() {
        promotions = new ArrayList<>();

        promotions.add(new GamePromotion(
                "Cyberpunk 2077",
                "Experience the future of gaming - 50% OFF!",
                "/assets/images/promos/cyberpunk.jpg",
                59.99,
                0.50
        ));

        promotions.add(new GamePromotion(
                "Elden Ring",
                "Journey through the Lands Between",
                "/assets/images/promos/elden-ring.jpg",
                69.99,
                0.0
        ));

        promotions.add(new GamePromotion(
                "God of War Ragnarök",
                "Epic Norse adventure - 30% OFF",
                "/assets/images/promos/god-of-war.jpg",
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

        slideTimer = new Timer(6000, e -> {
            previousPromotion = promotions.get(currentImageIndex);
            currentImageIndex = (currentImageIndex + 1) % promotions.size();
            startTransitionAnimation();
        });
        slideTimer.start();
    }

    private void startTransitionAnimation() {
        if (transitionTimer != null && transitionTimer.isRunning()) {
            transitionTimer.stop();
        }

        carouselTransitionAlpha = 0.0f;
        final GamePromotion nextPromo = promotions.get(currentImageIndex);
        final BufferedImage currentImage = new BufferedImage(carouselPanel.getWidth(),
                carouselPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = currentImage.createGraphics();
        carouselPanel.paint(g);
        g.dispose();

        transitionTimer = new Timer(16, e -> { // ~60fps animation
            carouselTransitionAlpha += 0.05f;
            if (carouselTransitionAlpha >= 1.0f) {
                carouselTransitionAlpha = 1.0f;
                transitionTimer.stop();
            }

            carouselPanel.repaint(new Rectangle(0, 0, carouselPanel.getWidth(),
                    carouselPanel.getHeight()));
        });
        carouselPanel.addPropertyChangeListener("paintTransition", evt -> {
            Graphics2D g2d = (Graphics2D) evt.getNewValue();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    1.0f - carouselTransitionAlpha));
            g2d.drawImage(currentImage, 0, 0, null);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                    carouselTransitionAlpha));
            if (nextPromo.image != null) {
                g2d.drawImage(nextPromo.image.getImage(), 0, 0,
                        carouselPanel.getWidth(), carouselPanel.getHeight(), null);
            }
        });

        transitionTimer.start();
    }

            private void setupCarouselControls() {
                carouselPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        int x = e.getX();
                        if (x < carouselPanel.getWidth() / 3) {

                            currentImageIndex = (currentImageIndex - 1 + promotions.size()) % promotions.size();
                        } else if (x > (carouselPanel.getWidth() * 2) / 3) {

                            currentImageIndex = (currentImageIndex + 1) % promotions.size();
                        }
                        carouselPanel.repaint();
                    }
                });

                addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                            currentImageIndex = (currentImageIndex - 1 + promotions.size()) % promotions.size();
                            carouselPanel.repaint();
                        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                            currentImageIndex = (currentImageIndex + 1) % promotions.size();
                            carouselPanel.repaint();
                        }
                    }
                });
                setFocusable(true);
            }

            private void cleanup() {
                if (slideTimer != null) {
                    slideTimer.stop();
                }
            }

            @Override
            public void dispose() {
                cleanup();
                super.dispose();
            }


            public void updatePromotions(List<GamePromotion> newPromotions) {
                this.promotions = newPromotions;
                currentImageIndex = 0;
                if (carouselPanel != null) {
                    carouselPanel.repaint();
                }
                if (!promotions.isEmpty()) {
                    startImageSlideshow();
                }
            }

            public static void main(String[] args) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new HomePage();
                });
            }
        }