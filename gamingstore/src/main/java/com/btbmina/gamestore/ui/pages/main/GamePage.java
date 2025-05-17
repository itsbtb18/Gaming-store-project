package com.btbmina.gamestore.ui.pages.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import com.btbmina.gamestore.Util.ColorScheme;
import com.btbmina.gamestore.classes.Game;
import com.btbmina.gamestore.ui.components.PurpleButton;

public class GamePage extends JPanel {
    private final Game game;
    private BufferedImage gameImage;
    private JLabel imageLabel;
    private JPanel screenshotsPanel;
    private Timer animationTimer;
    private final int ANIMATION_DURATION = 300; // milliseconds
    private long animationStartTime;
    private boolean animatingIn = true;

    public GamePage(Game game) {
        this.game = game;

        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARK_BACKGROUND);

        // Start with opacity 0 for animation
        setOpaque(false);

        loadGameImage();
        createUI();
        setupAnimation();
    }

    private void loadGameImage() {
        try {
            if (game.getPath_image() != null && !game.getPath_image().isEmpty()) {
                File imageFile = new File(game.getPath_image());
                if (imageFile.exists()) {
                    gameImage = ImageIO.read(imageFile);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading game image: " + e.getMessage());
        }
    }

    private void setupAnimation() {
        animationStartTime = System.currentTimeMillis();
        animationTimer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsed = System.currentTimeMillis() - animationStartTime;
                float progress = Math.min(1.0f, (float)elapsed / ANIMATION_DURATION);

                if (animatingIn) {
                    setOpacity(progress);
                } else {
                    setOpacity(1.0f - progress);
                }

                if (progress >= 1.0f) {
                    animationTimer.stop();
                    setOpaque(true);
                    repaint();
                }
            }
        });
        animationTimer.start();
    }

    private void setOpacity(float opacity) {
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
        for (Component component : getComponents()) {
            if (component instanceof JComponent) {
                ((JComponent) component).setOpaque(opacity >= 0.99f);
            }
        }
        repaint();
    }

    private void createUI() {
        // Create menu bar
        createMenuBar();

        // Header section with game title and main image
        JPanel headerPanel = createHeaderPanel();

        // Main content section with game info
        JPanel contentPanel = createContentPanel();

        // Purchase panel with pricing and buttons
        JPanel purchasePanel = createPurchasePanel();

        // Create a scroll pane for the content
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(purchasePanel, BorderLayout.SOUTH);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(ColorScheme.DARK_PURPLE);
        menuBar.setBorder(BorderFactory.createEmptyBorder());

        JMenu fileMenu = createMenu("File");
        fileMenu.add(createMenuItem("Home"));
        fileMenu.add(createMenuItem("Library"));
        fileMenu.add(createMenuItem("Exit"));

        JMenu storeMenu = createMenu("Store");
        storeMenu.add(createMenuItem("Browse"));
        storeMenu.add(createMenuItem("Wishlist"));
        storeMenu.add(createMenuItem("Cart"));

        JMenu helpMenu = createMenu("Help");
        helpMenu.add(createMenuItem("About"));
        helpMenu.add(createMenuItem("Support"));

        menuBar.add(fileMenu);
        menuBar.add(storeMenu);
        menuBar.add(helpMenu);

        ((JFrame) SwingUtilities.getWindowAncestor(this)).setJMenuBar(menuBar);
    }

    private JMenu createMenu(String title) {
        JMenu menu = new JMenu(title);
        menu.setForeground(ColorScheme.TEXT_PRIMARY);
        menu.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return menu;
    }

    private JMenuItem createMenuItem(String title) {
        JMenuItem menuItem = new JMenuItem(title);
        menuItem.setBackground(ColorScheme.MEDIUM_BACKGROUND);
        menuItem.setForeground(ColorScheme.TEXT_PRIMARY);
        menuItem.addActionListener(e -> System.out.println(title + " menu item clicked"));
        return menuItem;
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ColorScheme.MEDIUM_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title panel with gradient background
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();

                // Create gradient background
                GradientPaint gradient = new GradientPaint(
                        0, 0, ColorScheme.DARK_PURPLE,
                        getWidth(), getHeight(), ColorScheme.MEDIUM_BACKGROUND
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setOpaque(false);

        // Create title with stylish font
        JLabel titleLabel = new JLabel(game.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // Game image panel
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(ColorScheme.MEDIUM_BACKGROUND);
        imagePanel.setBorder(BorderFactory.createLineBorder(ColorScheme.ACCENT_COLOR, 2));
        imagePanel.setPreferredSize(new Dimension(0, 300));

        if (gameImage != null) {
            imageLabel = new JLabel(new ImageIcon(gameImage.getScaledInstance(400, 250, Image.SCALE_SMOOTH)));
        } else {
            // Placeholder image
            imageLabel = new JLabel("Game Image Not Available");
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        }
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(imagePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ColorScheme.DARK_BACKGROUND);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Game details (rating, category, etc.)
        JPanel detailsPanel = createDetailsPanel();
        contentPanel.add(detailsPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Game description
        JPanel descriptionPanel = createDescriptionPanel();
        contentPanel.add(descriptionPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // System requirements
        JPanel requirementsPanel = createRequirementsPanel();
        contentPanel.add(requirementsPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Screenshots gallery
        screenshotsPanel = createScreenshotsPanel();
        contentPanel.add(screenshotsPanel);

        return contentPanel;
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 15, 0));
        panel.setBackground(ColorScheme.DARK_BACKGROUND);

        // Create each detail section with rounded corners
        addDetailSection(panel, "Category", game.getCategory());

        // Create rating stars
        JPanel ratingPanel = new RoundedPanel(15, ColorScheme.MEDIUM_BACKGROUND);
        ratingPanel.setLayout(new BoxLayout(ratingPanel, BoxLayout.Y_AXIS));
        ratingPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel ratingTitle = new JLabel("Rating");
        ratingTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        ratingTitle.setForeground(ColorScheme.TEXT_PRIMARY);
        ratingTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel starsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        starsPanel.setOpaque(false);

        // Create stars based on rating
        double rating = game.getRating();
        for (int i = 1; i <= 5; i++) {
            JLabel star;
            if (i <= rating) {
                star = new JLabel("★"); // Full star
            } else if (i - 0.5 <= rating) {
                star = new JLabel("✭"); // Half star
            } else {
                star = new JLabel("☆"); // Empty star
            }
            star.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            star.setForeground(ColorScheme.ACCENT_COLOR);
            starsPanel.add(star);
        }

        JLabel ratingValue = new JLabel(String.format("%.1f/5.0", rating));
        ratingValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ratingValue.setForeground(ColorScheme.TEXT_PRIMARY);
        ratingValue.setAlignmentX(Component.CENTER_ALIGNMENT);

        ratingPanel.add(ratingTitle);
        ratingPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        ratingPanel.add(starsPanel);
        ratingPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        ratingPanel.add(ratingValue);

        panel.add(ratingPanel);

        // Price section
        JPanel pricePanel = new RoundedPanel(15, ColorScheme.MEDIUM_BACKGROUND);
        pricePanel.setLayout(new BoxLayout(pricePanel, BoxLayout.Y_AXIS));
        pricePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel priceTitle = new JLabel("Price");
        priceTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        priceTitle.setForeground(ColorScheme.TEXT_PRIMARY);
        priceTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel priceValue = new JLabel(String.format("$%.2f", game.getPrice()));
        priceValue.setFont(new Font("Segoe UI", Font.BOLD, 22));
        priceValue.setForeground(ColorScheme.ACCENT_COLOR);
        priceValue.setAlignmentX(Component.CENTER_ALIGNMENT);

        pricePanel.add(priceTitle);
        pricePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        pricePanel.add(priceValue);

        panel.add(pricePanel);

        return panel;
    }

    private void addDetailSection(JPanel container, String title, String content) {
        JPanel panel = new RoundedPanel(15, ColorScheme.MEDIUM_BACKGROUND);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel contentLabel = new JLabel(content);
        contentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        contentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(contentLabel);

        container.add(panel);
    }

    private JPanel createDescriptionPanel() {
        JPanel panel = new RoundedPanel(15, ColorScheme.MEDIUM_BACKGROUND);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Game Description");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);

        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBackground(ColorScheme.MEDIUM_BACKGROUND);
        descriptionArea.setForeground(ColorScheme.TEXT_PRIMARY);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Set the description from the game object
        descriptionArea.setText(game.getDescription() != null ? game.getDescription() :
                "No description available for this game.");

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(descriptionArea, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRequirementsPanel() {
        JPanel panel = new RoundedPanel(15, ColorScheme.MEDIUM_BACKGROUND);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("System Requirements");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);

        JTextArea requirementsArea = new JTextArea();
        requirementsArea.setEditable(false);
        requirementsArea.setLineWrap(true);
        requirementsArea.setWrapStyleWord(true);
        requirementsArea.setBackground(ColorScheme.MEDIUM_BACKGROUND);
        requirementsArea.setForeground(ColorScheme.TEXT_PRIMARY);
        requirementsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        requirementsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Set the system requirements from the game object
        requirementsArea.setText(game.getSystemRequirements() != null ? game.getSystemRequirements() :
                "No system requirements specified for this game.");

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(requirementsArea, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createScreenshotsPanel() {
        JPanel mainPanel = new RoundedPanel(15, ColorScheme.MEDIUM_BACKGROUND);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Screenshots");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);

        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 0));
        panel.setOpaque(false);

        // Create placeholder screenshots
        for (int i = 0; i < 4; i++) {
            JPanel screenshotPanel = new RoundedPanel(10, ColorScheme.DARK_BACKGROUND);
            screenshotPanel.setPreferredSize(new Dimension(200, 120));

            JLabel screenshotLabel = new JLabel("Screenshot " + (i + 1));
            screenshotLabel.setHorizontalAlignment(SwingConstants.CENTER);
            screenshotLabel.setForeground(ColorScheme.TEXT_SECONDARY);

            screenshotPanel.add(screenshotLabel);
            panel.add(screenshotPanel);
        }

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(panel, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createPurchasePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(ColorScheme.MEDIUM_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        // Left side - Game title
        JLabel gameTitleLabel = new JLabel(game.getTitle());
        gameTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gameTitleLabel.setForeground(ColorScheme.TEXT_PRIMARY);

        // Right side - Price and buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonsPanel.setOpaque(false);

        JLabel priceLabel = new JLabel(String.format("$%.2f", game.getPrice()));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        priceLabel.setForeground(ColorScheme.TEXT_PRIMARY);

        // Create glowing Add to Cart button
        PurpleButton addToCartButton = createAnimatedButton("Add to Cart");
        addToCartButton.setPreferredSize(new Dimension(150, 40));

        // Create Buy Now button
        PurpleButton buyNowButton = createAnimatedButton("Buy Now");
        buyNowButton.setPreferredSize(new Dimension(150, 40));

        // Add action listeners to buttons
        addToCartButton.addActionListener(e -> {
            // Add animation effect
            flashButton(addToCartButton);
            JOptionPane.showMessageDialog(
                    SwingUtilities.getWindowAncestor(this),
                    game.getTitle() + " has been added to your cart!",
                    "Added to Cart",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        buyNowButton.addActionListener(e -> {
            // Add animation effect
            flashButton(buyNowButton);
            JOptionPane.showMessageDialog(
                    SwingUtilities.getWindowAncestor(this),
                    "Proceeding to checkout for " + game.getTitle(),
                    "Checkout",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        buttonsPanel.add(priceLabel);
        buttonsPanel.add(addToCartButton);
        buttonsPanel.add(buyNowButton);

        panel.add(gameTitleLabel, BorderLayout.WEST);
        panel.add(buttonsPanel, BorderLayout.EAST);

        return panel;
    }

    private PurpleButton createAnimatedButton(String text) {
        PurpleButton button = new PurpleButton(text);
        button.setBackground(ColorScheme.BUTTON_NORMAL);
        button.setForeground(ColorScheme.TEXT_PRIMARY);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ColorScheme.BUTTON_HOVER);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(ColorScheme.BUTTON_NORMAL);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(ColorScheme.BUTTON_PRESSED);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(ColorScheme.BUTTON_HOVER);
            }
        });

        return button;
    }

    private void flashButton(JButton button) {
        Color originalColor = button.getBackground();

        Timer timer = new Timer(50, null);
        timer.addActionListener(new ActionListener() {
            private int count = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (count % 2 == 0) {
                    button.setBackground(ColorScheme.ACCENT_COLOR);
                } else {
                    button.setBackground(originalColor);
                }

                count++;
                if (count >= 6) {  // Flash 3 times
                    timer.stop();
                    button.setBackground(originalColor);
                }
            }
        });
        timer.start();
    }

    // Custom rounded panel
    private static class RoundedPanel extends JPanel {
        private int cornerRadius;
        private Color backgroundColor;

        public RoundedPanel(int radius, Color bgColor) {
            super();
            this.cornerRadius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(backgroundColor);
            g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

            g2d.dispose();
        }
    }

    // Custom scrollbar UI
    private static class CustomScrollBarUI extends BasicScrollBarUI {
        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            g.setColor(ColorScheme.DARK_BACKGROUND);
            g.fillRect(r.x, r.y, r.width, r.height);
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            if (r.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }

            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color thumbColor = ColorScheme.ACCENT_COLOR;

            g2d.setColor(thumbColor);
            g2d.fillRoundRect(r.x + 1, r.y + 1, r.width - 2, r.height - 2, 10, 10);

            g2d.dispose();
        }
    }

    // For demonstration, you need to add these colors to your ColorScheme class if they don't exist
    static {
        if (ColorScheme.ACCENT_COLOR == null) {
            try {
                java.lang.reflect.Field accentField = ColorScheme.class.getDeclaredField("ACCENT_COLOR");
                accentField.setAccessible(true);
                accentField.set(null, new Color(187, 134, 252));
            } catch (Exception e) {
                // Fallback if reflection fails
                System.err.println("Could not set ACCENT_COLOR via reflection");
            }
        }

        if (ColorScheme.DARK_PURPLE == null) {
            try {
                java.lang.reflect.Field darkPurpleField = ColorScheme.class.getDeclaredField("DARK_PURPLE");
                darkPurpleField.setAccessible(true);
                darkPurpleField.set(null, new Color(49, 27, 80));
            } catch (Exception e) {
                // Fallback if reflection fails
                System.err.println("Could not set DARK_PURPLE via reflection");
            }
        }

        if (ColorScheme.BUTTON_HOVER == null) {
            try {
                java.lang.reflect.Field buttonHoverField = ColorScheme.class.getDeclaredField("BUTTON_HOVER");
                buttonHoverField.setAccessible(true);
                buttonHoverField.set(null, new Color(156, 90, 240));
            } catch (Exception e) {
                // Fallback if reflection fails
                System.err.println("Could not set BUTTON_HOVER via reflection");
            }
        }

        if (ColorScheme.BUTTON_PRESSED == null) {
            try {
                java.lang.reflect.Field buttonPressedField = ColorScheme.class.getDeclaredField("BUTTON_PRESSED");
                buttonPressedField.setAccessible(true);
                buttonPressedField.set(null, new Color(126, 70, 200));
            } catch (Exception e) {
                // Fallback if reflection fails
                System.err.println("Could not set BUTTON_PRESSED via reflection");
            }
        }

        if (ColorScheme.TEXT_SECONDARY == null) {
            try {
                java.lang.reflect.Field textSecondaryField = ColorScheme.class.getDeclaredField("TEXT_SECONDARY");
                textSecondaryField.setAccessible(true);
                textSecondaryField.set(null, new Color(187, 187, 187));
            } catch (Exception e) {
                // Fallback if reflection fails
                System.err.println("Could not set TEXT_SECONDARY via reflection");
            }
        }
    }
}