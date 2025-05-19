package com.btbmina.gamestore.ui.pages.main;

import com.btbmina.gamestore.Util.ColorScheme;
import com.btbmina.gamestore.ui.FontManager;
import com.btbmina.gamestore.ui.components.*;
import com.btbmina.gamestore.classes.Game;
import com.btbmina.gamestore.classes.User;
import com.btbmina.gamestore.ui.components.MenuBar;
import com.btbmina.gamestore.ui.components.TitleBar;
import com.btbmina.gamestore.ui.components.ModernScrollBarUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

public class GamePage extends JFrame {
    private final Game game;
    private final User currentUser;
    private BufferedImage gameImage;
    private JLabel imageLabel;
    private static final int IMAGE_HEIGHT = 400;
    private JPanel mainContainer;
    private Timer notificationTimer;
    private JLabel notificationLabel;

    public GamePage(Game game, User currentUser) {
        this.game = game;
        this.currentUser = currentUser;

        System.out.println("Game image path: " + game.getPath_image());
        System.out.println("Working directory: " + System.getProperty("user.dir"));

        initializeFrame();
        loadGameImage();
        createContent();
    }

    private void initializeFrame() {
        setTitle("Gaming Store - " + game.getTitle());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
    }

    private void loadGameImage() {
        try {
            if (game.getPath_image() != null && !game.getPath_image().isEmpty()) {
                String imagePath = game.getPath_image();

                System.out.println("Attempting to load image from: " + imagePath);

                if (!imagePath.startsWith("/")) {
                    imagePath = "/" + imagePath;
                }

                URL resourceUrl = getClass().getResource(imagePath);
                if (resourceUrl != null) {
                    gameImage = ImageIO.read(resourceUrl);
                    System.out.println("Successfully loaded image from resources");
                    return;
                }

                File imageFile = new File("src/main/resources" + imagePath);
                if (imageFile.exists()) {
                    gameImage = ImageIO.read(imageFile);
                    System.out.println("Successfully loaded image from file system");
                    return;
                }

                System.err.println("Could not find image at: " + imagePath);
            }
        } catch (Exception e) {
            System.err.println("Error loading game image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createContent() {
        mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(ColorScheme.DARK_BACKGROUND);

        // Add TitleBar at the top
        mainContainer.add(new TitleBar(this), BorderLayout.NORTH);

        // Create content wrapper for MenuBar and main content
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(ColorScheme.DARK_BACKGROUND);

        // Add MenuBar below TitleBar
        MenuBar menuBar = new MenuBar(this, currentUser);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(80, 80, 80)));
        contentWrapper.add(menuBar, BorderLayout.NORTH);

        // Add main content
        contentWrapper.add(createMainContent(), BorderLayout.CENTER);

        mainContainer.add(contentWrapper, BorderLayout.CENTER);

        setContentPane(mainContainer);
    }
    private JPanel createMainContent() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ColorScheme.DARK_BACKGROUND);
        contentPanel.add(createGameContent());
        return contentPanel;
    }

        private JPanel createGameContent() {
            JPanel container = new JPanel();
            container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
            container.setBackground(ColorScheme.DARK_BACKGROUND);
            container.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));

            JPanel imagePanel = createImagePanel();
            container.add(imagePanel);
            container.add(Box.createRigidArea(new Dimension(0, 30)));

            JPanel infoSection = createInfoSection();
            container.add(infoSection);

            return container;
        }

        private JPanel createImagePanel() {
            JPanel panel = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (gameImage != null) {
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                        double ratio = (double) gameImage.getWidth() / gameImage.getHeight();
                        int width = (int) (IMAGE_HEIGHT * ratio);

                        int x = (getWidth() - width) / 2;
                        g2d.drawImage(gameImage, x, 0, width, IMAGE_HEIGHT, null);

                        GradientPaint gradient = new GradientPaint(
                                0, IMAGE_HEIGHT - 100,
                                new Color(0, 0, 0, 0),
                                0, IMAGE_HEIGHT,
                                ColorScheme.DARK_BACKGROUND
                        );
                        g2d.setPaint(gradient);
                        g2d.fillRect(0, IMAGE_HEIGHT - 100, getWidth(), 100);
                    }
                }
            };
            panel.setPreferredSize(new Dimension(0, IMAGE_HEIGHT));
            panel.setBackground(ColorScheme.DARK_BACKGROUND);
            return panel;
        }

        private JPanel createInfoSection() {
            JPanel section = new JPanel();
            section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
            section.setBackground(ColorScheme.DARK_BACKGROUND);

            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(ColorScheme.DARK_BACKGROUND);

            JLabel titleLabel = new JLabel(game.getTitle());
            titleLabel.setFont(FontManager.getBold(32));
            titleLabel.setForeground(Color.WHITE);
            headerPanel.add(titleLabel, BorderLayout.WEST);

            JLabel priceLabel = new JLabel(String.format("$%.2f", game.getPrice()));
            priceLabel.setFont(FontManager.getBold(28));
            priceLabel.setForeground(new Color(130, 90, 210));
            headerPanel.add(priceLabel, BorderLayout.EAST);

            section.add(headerPanel);
            section.add(Box.createRigidArea(new Dimension(0, 20)));

            JTextArea descriptionArea = new JTextArea(game.getDescription());
            descriptionArea.setFont(FontManager.getRegular(16));
            descriptionArea.setForeground(new Color(200, 200, 200));
            descriptionArea.setBackground(ColorScheme.DARK_BACKGROUND);
            descriptionArea.setWrapStyleWord(true);
            descriptionArea.setLineWrap(true);
            descriptionArea.setEditable(false);
            section.add(descriptionArea);
            section.add(Box.createRigidArea(new Dimension(0, 30)));

            JLabel requirementsTitle = new JLabel("System Requirements");
            requirementsTitle.setFont(FontManager.getBold(20));
            requirementsTitle.setForeground(Color.WHITE);
            section.add(requirementsTitle);
            section.add(Box.createRigidArea(new Dimension(0, 10)));

            JTextArea requirementsArea = new JTextArea(game.getSystemRequirements());
            requirementsArea.setFont(FontManager.getRegular(14));
            requirementsArea.setForeground(new Color(200, 200, 200));
            requirementsArea.setBackground(ColorScheme.DARK_BACKGROUND);
            requirementsArea.setWrapStyleWord(true);
            requirementsArea.setLineWrap(true);
            requirementsArea.setEditable(false);
            section.add(requirementsArea);
            section.add(Box.createRigidArea(new Dimension(0, 30)));

            JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
            buttonsPanel.setBackground(ColorScheme.DARK_BACKGROUND);

            JButton buyButton = createStyledButton("Buy Now", new Color(130, 90, 210));
            JButton cartButton = createStyledButton("Add to Cart", new Color(60, 60, 70));

            buyButton.addActionListener(e -> handlePurchase());
            cartButton.addActionListener(e -> handleAddToCart());

            buttonsPanel.add(buyButton);
            buttonsPanel.add(cartButton);
            section.add(buttonsPanel);

            return section;
        }

    private JButton createStyledButton(String text, Color backgroundColor) {

        Color primaryPurple = new Color(130, 90, 210);  // Main purple
        Color darkPurple = new Color(100, 60, 180);     // Darker purple for secondary button

        Color buttonColor = text.equals("Buy Now") ? primaryPurple : darkPurple;

        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(buttonColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }
        };

        button.setFont(FontManager.getBold(16));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(buttonColor.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(buttonColor);
            }
        });

        return button;
    }
        private void handlePurchase() {
            try {
                showNotification("Game purchased successfully!", new Color(46, 125, 50));
                // Here you can add additional purchase logic
            } catch (Exception e) {
                e.printStackTrace();
                showNotification("Purchase failed!", new Color(200, 50, 50));
            }
        }

        private void handleAddToCart() {
            try {
                CartPage cartPage = new CartPage(currentUser);
                cartPage.addCartItem(game.getTitle(), game.getPrice());
                showNotification("Game added to cart!", new Color(130, 90, 210));
                
                // Update visibility of cart page
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice gd = ge.getDefaultScreenDevice();
                if (gd.isFullScreenSupported()) {
                    gd.setFullScreenWindow(cartPage);
                } else {
                    cartPage.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                    cartPage.setSize(screenSize.width, screenSize.height);
                    cartPage.setLocationRelativeTo(null);
                }
                cartPage.setVisible(true);
                dispose(); // Close the current game page
            } catch (Exception e) {
                e.printStackTrace();
                showNotification("Failed to add game to cart!", new Color(200, 50, 50));
            }
        }

    private void showNotification(String message, Color backgroundColor) {
        JPanel notificationPanel = new JPanel();
        notificationPanel.setBackground(backgroundColor);
        notificationPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        
        JLabel label = new JLabel(message);
        label.setForeground(Color.WHITE);
        label.setFont(FontManager.getBold(14));
        notificationPanel.add(label);
        
        // Add to top of main container
        mainContainer.add(notificationPanel, BorderLayout.SOUTH);
        mainContainer.revalidate();
        
        // Remove notification after delay
        Timer timer = new Timer(3000, e -> {
            mainContainer.remove(notificationPanel);
            mainContainer.revalidate();
            mainContainer.repaint();
        });
        timer.setRepeats(false);
        timer.start();
    }
}