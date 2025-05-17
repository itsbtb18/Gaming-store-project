package com.btbmina.gamestore.ui.pages.main;

import com.btbmina.gamestore.Util.ColorScheme;
import com.btbmina.gamestore.ui.FontManager;
import com.btbmina.gamestore.ui.components.*;
import com.btbmina.gamestore.classes.Game;
import com.btbmina.gamestore.classes.User;
import com.btbmina.gamestore.ui.components.MenuBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

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
                // First try to load from resources
                try {
                    gameImage = ImageIO.read(getClass().getResource(game.getPath_image()));
                } catch (Exception e) {
                    // If that fails, try loading from file system
                    File imageFile = new File("src/main/resources" + game.getPath_image());
                    if (imageFile.exists()) {
                        gameImage = ImageIO.read(imageFile);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading game image: " + e.getMessage());
        }
    }

        private void createContent() {
            mainContainer = new JPanel(new BorderLayout());
            mainContainer.setBackground(ColorScheme.DARK_BACKGROUND);

            // Add TitleBar at the very top
            add(new TitleBar(this), BorderLayout.NORTH);

            // Create content panel with BoxLayout
            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBackground(ColorScheme.DARK_BACKGROUND);

            // Add MenuBar below TitleBar
            JPanel menuWrapper = new JPanel(new BorderLayout());
            menuWrapper.setBackground(ColorScheme.DARK_BACKGROUND);
            menuWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            menuWrapper.add(new MenuBar(this, currentUser), BorderLayout.CENTER);
            contentPanel.add(menuWrapper);

            // Add game content
            contentPanel.add(createGameContent());

            // Wrap in scroll pane with modern scrollbar
            JScrollPane scrollPane = new JScrollPane(contentPanel);
            scrollPane.setBorder(null);
            scrollPane.setBackground(ColorScheme.DARK_BACKGROUND);
            scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);

            // Add notification label
            notificationLabel = new JLabel();
            notificationLabel.setFont(FontManager.getMedium(16));
            notificationLabel.setForeground(Color.WHITE);
            notificationLabel.setHorizontalAlignment(SwingConstants.CENTER);
            notificationLabel.setVisible(false);

            // Add components to main container
            mainContainer.add(scrollPane, BorderLayout.CENTER);
            mainContainer.add(notificationLabel, BorderLayout.SOUTH);

            setContentPane(mainContainer);
        }

        private JPanel createGameContent() {
            JPanel container = new JPanel();
            container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
            container.setBackground(ColorScheme.DARK_BACKGROUND);
            container.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));

            // Game image
            JPanel imagePanel = createImagePanel();
            container.add(imagePanel);
            container.add(Box.createRigidArea(new Dimension(0, 30)));

            // Game info section
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

                        // Calculate dimensions maintaining aspect ratio
                        double ratio = (double) gameImage.getWidth() / gameImage.getHeight();
                        int width = (int) (IMAGE_HEIGHT * ratio);

                        // Center the image
                        int x = (getWidth() - width) / 2;
                        g2d.drawImage(gameImage, x, 0, width, IMAGE_HEIGHT, null);

                        // Add gradient overlay at bottom
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

            // Title and price row
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

            // Description
            JTextArea descriptionArea = new JTextArea(game.getDescription());
            descriptionArea.setFont(FontManager.getRegular(16));
            descriptionArea.setForeground(new Color(200, 200, 200));
            descriptionArea.setBackground(ColorScheme.DARK_BACKGROUND);
            descriptionArea.setWrapStyleWord(true);
            descriptionArea.setLineWrap(true);
            descriptionArea.setEditable(false);
            section.add(descriptionArea);
            section.add(Box.createRigidArea(new Dimension(0, 30)));

            // System requirements
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

            // Buttons panel
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
            JButton button = new JButton(text) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(getBackground());
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    super.paintComponent(g);
                }
            };

            button.setFont(FontManager.getBold(16));
            button.setForeground(Color.WHITE);
            button.setBackground(backgroundColor);
            button.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));

            return button;
        }

        private void handlePurchase() {
            showNotification("Game purchased successfully!", new Color(46, 125, 50));
        }

        private void handleAddToCart() {
            showNotification("Added to cart!", new Color(130, 90, 210));
        }

        private void showNotification(String message, Color backgroundColor) {
            if (notificationTimer != null && notificationTimer.isRunning()) {
                notificationTimer.stop();
            }

            notificationLabel.setText(message);
            notificationLabel.setBackground(backgroundColor);
            notificationLabel.setOpaque(true);
            notificationLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
            notificationLabel.setVisible(true);

            notificationTimer = new Timer(3000, e -> {
                notificationLabel.setVisible(false);
                ((Timer) e.getSource()).stop();
            });
            notificationTimer.start();
        }
}