package com.btbmina.gamestore.ui.pages.main;

import com.btbmina.gamestore.Util.ColorScheme;
import com.btbmina.gamestore.classes.User;
import com.btbmina.gamestore.ui.components.ModernScrollBarUI;
import com.btbmina.gamestore.ui.components.PurpleButton;
import com.btbmina.gamestore.ui.components.TitleBar;
import com.btbmina.gamestore.ui.components.MenuBar;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartPage extends JFrame {
    private JPanel cartItemsPanel;
    private JLabel totalPriceLabel;
    private JLabel itemCountLabel;
    private double totalPrice = 0.0;
    private int itemCount = 0;
    private ArrayList<CartItem> cartItems = new ArrayList<>();
    private final User currentUser;
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
    private final Color HIGHLIGHT_COLOR = new Color(187, 134, 252, 50);

    // Inner class to represent a cart item
    private class CartItem {
        String title;
        double price;
        JPanel panel;

        public CartItem(String title, double price, JPanel panel) {
            this.title = title;
            this.price = price;
            this.panel = panel;
        }
    }

    public CartPage(User currentUser) {
        this.currentUser = currentUser;
        setUndecorated(true);
        initializeFrame();
        createContent();
        pack();
        setSize(1100, 700);
        setLocationRelativeTo(null);
    }

    private void initializeFrame() {
        setTitle("Gaming Store - Cart");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(new Color(0, 0, 0, 0));
    }

    private void createContent() {
        // Main container with a 1-pixel purple border
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(ColorScheme.DARK_BACKGROUND);
        mainContainer.setBorder(new LineBorder(ColorScheme.PRIMARY_PURPLE, 1));

        // Add TitleBar
        TitleBar titleBar = new TitleBar(this);
        titleBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.PRIMARY_PURPLE));
        mainContainer.add(titleBar, BorderLayout.NORTH);

        // Content wrapper (includes menu and main content)
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(ColorScheme.DARK_BACKGROUND);

        // Add MenuBar immediately below TitleBar
        MenuBar menuBar = new MenuBar(this, currentUser);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(80, 80, 80)));
        contentWrapper.add(menuBar, BorderLayout.NORTH);

        // Main content area
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(ColorScheme.DARK_BACKGROUND);

        createUI(mainContentPanel);
        contentWrapper.add(mainContentPanel, BorderLayout.CENTER);

        mainContainer.add(contentWrapper, BorderLayout.CENTER);
        setContentPane(mainContainer);
    }

    private void createUI(JPanel contentPanel) {
        contentPanel.setLayout(new BorderLayout(0, 0));
        contentPanel.setBackground(ColorScheme.DARK_BACKGROUND);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Create header panel with gradient
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Create gradient from dark purple to darker background
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(90, 30, 120),
                        w, h, ColorScheme.MEDIUM_BACKGROUND
                );

                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);

                g2d.dispose();
            }
        };

        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        headerPanel.setPreferredSize(new Dimension(0, 80));

        // Stylish header with icon
        JPanel titleWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titleWrapper.setOpaque(false);

        // Cart icon
        JLabel cartIcon = new JLabel("\uD83D\uDED2"); // Unicode shopping cart
        cartIcon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 28));
        cartIcon.setForeground(Color.WHITE);

        // Title with custom font
        JLabel titleLabel = new JLabel("YOUR SHOPPING CART");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        titleWrapper.add(cartIcon);
        titleWrapper.add(titleLabel);

        // Item count on the right
        itemCountLabel = new JLabel("0 items");
        itemCountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        itemCountLabel.setForeground(new Color(180, 180, 180));

        headerPanel.add(titleWrapper, BorderLayout.WEST);
        headerPanel.add(itemCountLabel, BorderLayout.EAST);

        // Main cart content area with shadow effect
        JPanel cartContentPanel = new JPanel(new BorderLayout());
        cartContentPanel.setBackground(ColorScheme.DARK_BACKGROUND);
        cartContentPanel.setBorder(new CompoundBorder(
                BorderFactory.createEmptyBorder(15, 20, 15, 20),
                new ShadowBorder(ColorScheme.DARK_BACKGROUND, 5)
        ));

        // Initialize cart items panel with proper spacing
        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        cartItemsPanel.setBackground(new Color(25, 25, 30));
        cartItemsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Custom scroll pane with styled scrollbar
        JScrollPane scrollPane = new JScrollPane(cartItemsPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(new Color(25, 25, 30));
        scrollPane.getViewport().setBackground(new Color(25, 25, 30));

        // Customize scrollbar
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUI(new ModernScrollBarUI());
        verticalScrollBar.setUnitIncrement(16);
        verticalScrollBar.setBackground(new Color(25, 25, 30));

        // Hide horizontal scrollbar
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Create checkout panel
        JPanel checkoutPanel = createCheckoutPanel();

        // Add components to cart content panel
        cartContentPanel.add(scrollPane, BorderLayout.CENTER);
        cartContentPanel.add(checkoutPanel, BorderLayout.SOUTH);

        // Add empty cart message (will be hidden when items are added)
        JPanel emptyCartPanel = createEmptyCartPanel();
        cartContentPanel.add(emptyCartPanel, BorderLayout.NORTH);

        // Add all components to the main content panel
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        contentPanel.add(cartContentPanel, BorderLayout.CENTER);

        // Add cart items after totalPriceLabel is initialized
        addCartItem("Cyberpunk 2077: Ultimate Edition", 59.99);
        addCartItem("Elden Ring", 49.99);
        addCartItem("Starfield", 69.99);

        // Hide empty cart message when we have items
        emptyCartPanel.setVisible(false);
    }

    private JPanel createEmptyCartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 35));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        JLabel messageLabel = new JLabel("Your cart is empty", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        messageLabel.setForeground(new Color(150, 150, 150));

        JLabel subMessageLabel = new JLabel("Browse our store to add games to your cart", SwingConstants.CENTER);
        subMessageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subMessageLabel.setForeground(new Color(120, 120, 120));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(new Color(30, 30, 35));
        textPanel.add(messageLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        textPanel.add(subMessageLabel);

        panel.add(textPanel, BorderLayout.CENTER);

        // Browse button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(30, 30, 35));

        PurpleButton browseButton = new PurpleButton("Browse Store");
        browseButton.setPreferredSize(new Dimension(150, 40));
        buttonPanel.add(browseButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.setPreferredSize(new Dimension(0, 180));

        return panel;
    }

    public void addCartItem(String title, double price) {
        JPanel itemPanel = new JPanel(new BorderLayout(15, 0));
        itemPanel.setBackground(new Color(35, 35, 40));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        itemPanel.setPreferredSize(new Dimension(itemPanel.getPreferredSize().width, 100));

        // Add rounded corners and border
        itemPanel.setBorder(new CompoundBorder(
                new RoundedBorder(8, new Color(50, 50, 55)),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        // Add hover effect
        itemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                itemPanel.setBackground(new Color(45, 45, 50));
                itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                itemPanel.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                itemPanel.setBackground(new Color(35, 35, 40));
                itemPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                itemPanel.repaint();
            }
        });

        // Game image placeholder (left side)
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(60, 60, 65));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);

                // Draw placeholder icon
                g2d.setColor(new Color(90, 90, 95));
                int iconSize = 24;
                g2d.fillRect(getWidth()/2 - iconSize/2, getHeight()/2 - iconSize/2, iconSize, iconSize);

                g2d.dispose();
            }
        };
        imagePanel.setPreferredSize(new Dimension(70, 70));

        // Info panel (center)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(35, 35, 40));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Title with custom font
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Digital product label
        JPanel digitalLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 3));
        digitalLabelPanel.setBackground(new Color(35, 35, 40));
        digitalLabelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel digitalLabel = new JLabel("DIGITAL DOWNLOAD");
        digitalLabel.setForeground(new Color(187, 134, 252));
        digitalLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        digitalLabelPanel.add(digitalLabel);

        infoPanel.add(titleLabel);
        infoPanel.add(digitalLabelPanel);

        // Right panel with price and remove button
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(35, 35, 40));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Price
        JLabel priceLabel = new JLabel(currencyFormatter.format(price));
        priceLabel.setForeground(Color.WHITE);
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        priceLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // Remove button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 5));
        buttonPanel.setBackground(new Color(35, 35, 40));
        buttonPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JButton removeButton = new JButton("Remove");
        removeButton.setForeground(new Color(200, 80, 80));
        removeButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        removeButton.setBorder(null);
        removeButton.setContentAreaFilled(false);
        removeButton.setFocusPainted(false);
        removeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect to remove button
        removeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                removeButton.setForeground(new Color(255, 100, 100));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                removeButton.setForeground(new Color(200, 80, 80));
            }
        });

        removeButton.addActionListener(e -> removeCartItem(itemPanel, price));

        buttonPanel.add(removeButton);

        rightPanel.add(priceLabel);
        rightPanel.add(buttonPanel);

        // Add all components to item panel
        itemPanel.add(imagePanel, BorderLayout.WEST);
        itemPanel.add(infoPanel, BorderLayout.CENTER);
        itemPanel.add(rightPanel, BorderLayout.EAST);

        cartItemsPanel.add(itemPanel);
        cartItemsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Update cart totals
        totalPrice += price;
        itemCount++;
        updateTotalPrice();
        updateItemCount();

        // Store the cart item
        cartItems.add(new CartItem(title, price, itemPanel));
    }

    private void removeCartItem(JPanel itemPanel, double price) {
        int index = -1;
        for (int i = 0; i < cartItemsPanel.getComponentCount(); i++) {
            if (cartItemsPanel.getComponent(i) == itemPanel) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            cartItemsPanel.remove(itemPanel);
            if (index + 1 < cartItemsPanel.getComponentCount()) {
                cartItemsPanel.remove(index); // Remove spacer too
            }
            totalPrice -= price;
            itemCount--;
            updateTotalPrice();
            updateItemCount();
            cartItemsPanel.revalidate();
            cartItemsPanel.repaint();
        }
    }

    private void updateItemCount() {
        itemCountLabel.setText(itemCount + (itemCount == 1 ? " item" : " items"));
    }

    private JPanel createCheckoutPanel() {
        // Create a panel with gradient background
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Create gradient from dark purple to black
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(40, 20, 60),
                        w, h, new Color(20, 20, 25)
                );

                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);

                g2d.dispose();
            }
        };

        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        panel.setPreferredSize(new Dimension(0, 100));

        // Summary panel on the left
        JPanel summaryPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        summaryPanel.setOpaque(false);

        JLabel summaryLabel = new JLabel("ORDER SUMMARY");
        summaryLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        summaryLabel.setForeground(new Color(200, 200, 200));

        totalPriceLabel = new JLabel();
        totalPriceLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        totalPriceLabel.setForeground(Color.WHITE);
        updateTotalPrice();

        summaryPanel.add(summaryLabel);
        summaryPanel.add(totalPriceLabel);

        // Checkout button with glow effect
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton checkoutButton = new GlowButton("PROCEED TO CHECKOUT", ColorScheme.PRIMARY_PURPLE);
        checkoutButton.setPreferredSize(new Dimension(230, 50));
        buttonPanel.add(checkoutButton);

        panel.add(summaryPanel, BorderLayout.WEST);
        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }

    private void updateTotalPrice() {
        totalPriceLabel.setText(currencyFormatter.format(totalPrice));
    }

    // Custom rounded border
    private class RoundedBorder extends LineBorder {
        private int radius;

        public RoundedBorder(int radius, Color color) {
            super(color);
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getLineColor());
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
    }

    // Custom glow button
    private class GlowButton extends JButton {
        private boolean isHovered = false;
        private Color buttonColor;

        public GlowButton(String text, Color color) {
            super(text);
            this.buttonColor = color;
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            setContentAreaFilled(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw button background with gradient
            GradientPaint gp;
            if (isHovered) {
                // Safely increase color values
                int r = Math.min(255, buttonColor.getRed() + 20);
                int green = Math.min(255, buttonColor.getGreen() + 20);
                int b = Math.min(255, buttonColor.getBlue() + 20);
                
                gp = new GradientPaint(
                        0, 0,
                        buttonColor,
                        getWidth(), getHeight(),
                        new Color(r, green, b)
                );

                // Add glow effect when hovered
                for (int i = 0; i < 5; i++) {
                    g2.setColor(new Color(
                        buttonColor.getRed(), 
                        buttonColor.getGreen(), 
                        buttonColor.getBlue(), 
                        50 - i * 10
                    ));
                    g2.setStroke(new BasicStroke(i * 2));
                    g2.drawRoundRect(i, i, getWidth() - 1 - i * 2, getHeight() - 1 - i * 2, 10, 10);
                }
            } else {
                // Safely decrease color values
                int r = Math.max(0, buttonColor.getRed() - 20);
                int green = Math.max(0, buttonColor.getGreen() - 20);
                int b = Math.max(0, buttonColor.getBlue() - 20);
                
                gp = new GradientPaint(
                        0, 0,
                        buttonColor,
                        getWidth(), getHeight(),
                        new Color(r, green, b)
                );
            }

            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

            // Draw border
            g2.setColor(new Color(255, 255, 255, 40));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // Custom shadow border
    private class ShadowBorder extends AbstractBorder {
        private Color shadowColor;
        private int shadowSize;

        public ShadowBorder(Color shadowColor, int shadowSize) {
            this.shadowColor = shadowColor;
            this.shadowSize = shadowSize;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(shadowSize, shadowSize, shadowSize, shadowSize);
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Top shadow
            g2.setPaint(new GradientPaint(
                    x, y, new Color(0, 0, 0, 50),
                    x, y + shadowSize, new Color(0, 0, 0, 0)));
            g2.fillRect(x, y, width, shadowSize);

            // Left shadow
            g2.setPaint(new GradientPaint(
                    x, y, new Color(0, 0, 0, 50),
                    x + shadowSize, y, new Color(0, 0, 0, 0)));
            g2.fillRect(x, y, shadowSize, height);

            // Bottom shadow
            g2.setPaint(new GradientPaint(
                    x, y + height - shadowSize, new Color(0, 0, 0, 0),
                    x, y + height, new Color(0, 0, 0, 30)));
            g2.fillRect(x, y + height - shadowSize, width, shadowSize);

            // Right shadow
            g2.setPaint(new GradientPaint(
                    x + width - shadowSize, y, new Color(0, 0, 0, 0),
                    x + width, y, new Color(0, 0, 0, 30)));
            g2.fillRect(x + width - shadowSize, y, shadowSize, height);

            g2.dispose();
        }
    }

    }