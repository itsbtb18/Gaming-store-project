package com.btbmina.gamestore.ui.components;

import com.btbmina.gamestore.Util.ColorScheme;
import com.btbmina.gamestore.ui.FontManager;
import com.btbmina.gamestore.classes.User;
import com.btbmina.gamestore.ui.pages.main.*;
import com.btbmina.gamestore.ui.pages.auth.LoginPage;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class MenuBar extends JPanel {

    private static final int HEIGHT = 50; // Reduced from 70 to save vertical space
    private static final Color MENU_PURPLE = new Color(76, 40, 130);
    private static final Color HOVER_PURPLE = new Color(96, 53, 162);
    private static final Color ACTIVE_PURPLE = new Color(122, 68, 210);
    private static final Color TEXT_COLOR = new Color(240, 240, 250);
    private static final Color SECONDARY_TEXT = new Color(180, 180, 210);
    private static final Color BORDER_HIGHLIGHT = new Color(140, 90, 220, 80);

    private final JFrame parentFrame;
    private final User currentUser;
    private JButton activeNavButton;

    public MenuBar(JFrame parentFrame, User currentUser) {
        this.parentFrame = parentFrame;
        this.currentUser = currentUser;
        setupPanel();
        createContent();
    }

    private void setupPanel() {
        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARK_BACKGROUND);
        setBorder(new MatteBorder(0, 0, 1, 0, BORDER_HIGHLIGHT));
        setPreferredSize(new Dimension(getWidth(), HEIGHT));
    }

    private void createContent() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        JPanel leftPanel = createNavPanel();

        JPanel rightPanel = createControlsPanel();

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createNavPanel() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        JButton homeButton = createNavButton("Home", HomePage.class);
        setActiveNavButton(homeButton);

        panel.add(Box.createVerticalStrut(HEIGHT));
        panel.add(homeButton);

        return panel;
    }

    private void setActiveNavButton(JButton button) {
        if (activeNavButton != null) {
            activeNavButton.setForeground(TEXT_COLOR);
            activeNavButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 0, 0, ACTIVE_PURPLE),
                    BorderFactory.createEmptyBorder(0, 15, 0, 15)
            ));
        }

        activeNavButton = button;
        activeNavButton.setForeground(ACTIVE_PURPLE);
        activeNavButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, ACTIVE_PURPLE),
                BorderFactory.createEmptyBorder(0, 15, 0, 15)
        ));
    }

    private JPanel createControlsPanel() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);


        JButton cartButton = createControlButton("Cart");
        cartButton.addActionListener(e -> {
            CartPage cartPage = new CartPage(currentUser);
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
            parentFrame.dispose();
        });

        JButton userButton = createControlButton("Account");
        userButton.addActionListener(e -> showUserMenu(userButton));

        panel.add(Box.createHorizontalStrut(10));
        panel.add(Box.createVerticalStrut(HEIGHT)); // Ensure vertical centering
        panel.add(cartButton);
        panel.add(Box.createHorizontalStrut(15)); // Consistent spacing
        panel.add(userButton);
        panel.add(Box.createHorizontalStrut(5));

        return panel;
    }

    private JButton createControlButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();

                // Create gradient background
                GradientPaint gradient = new GradientPaint(
                        0, 0,
                        getModel().isPressed() ? MENU_PURPLE.darker() : MENU_PURPLE,
                        0, height,
                        getModel().isPressed() ? MENU_PURPLE : HOVER_PURPLE
                );

                g2d.setPaint(gradient);
                g2d.fill(new RoundRectangle2D.Float(0, 0, width, height, 16, 16));

                // Add subtle highlight at top
                if (!getModel().isPressed()) {
                    g2d.setColor(new Color(255, 255, 255, 30));
                    g2d.fill(new RoundRectangle2D.Float(1, 1, width-2, height/3, 16, 16));
                }

                g2d.dispose();
                super.paintComponent(g);
            }
        };

        button.setFont(FontManager.getBold(14));
        button.setForeground(TEXT_COLOR);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16)); // Reduced padding
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);

        // Set preferred size for consistent button heights
        button.setPreferredSize(new Dimension(button.getPreferredSize().width, 32));
        button.setMaximumSize(new Dimension(button.getPreferredSize().width, 32));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.repaint();
            }
        });

        return button;
    }

    private JButton createNavButton(String text, Class<?> pageClass) {
        JButton button = new JButton(text);
        button.setFont(FontManager.getBold(14));
        button.setForeground(TEXT_COLOR);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 0, ACTIVE_PURPLE),
                BorderFactory.createEmptyBorder(0, 15, 0, 15)
        ));
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setOpaque(false);

        // Set alignment to center vertically
        button.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Set preferred height for consistent button heights
        button.setPreferredSize(new Dimension(button.getPreferredSize().width, 32));
        button.setMaximumSize(new Dimension(button.getPreferredSize().width, 32));

        button.addActionListener(e -> {
            setActiveNavButton(button);
            navigateTo(pageClass);
        });

        return button;
    }

    private void showUserMenu(Component source) {
        JPopupMenu menu = new JPopupMenu() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(40, 25, 65));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                g2d.dispose();
            }
        };

        menu.setBackground(new Color(40, 25, 65));
        menu.setBorder(new EmptyBorder(5, 0, 5, 0));

        JPanel userInfo = new JPanel(new BorderLayout(10, 5));
        userInfo.setBackground(new Color(50, 35, 80));
        userInfo.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Add user avatar
        JPanel avatarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw circle avatar background
                g2d.setColor(MENU_PURPLE);
                g2d.fillOval(0, 0, getWidth(), getHeight());

                // Draw avatar initials
                g2d.setColor(TEXT_COLOR);
                g2d.setFont(FontManager.getBold(16));
                String initials = String.valueOf(currentUser.getUsername().charAt(0)).toUpperCase();
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(initials);
                int textHeight = fm.getAscent();
                g2d.drawString(initials,
                        (getWidth() - textWidth) / 2,
                        (getHeight() + textHeight - fm.getDescent()) / 2);

                g2d.dispose();
            }
        };
        avatarPanel.setPreferredSize(new Dimension(40, 40));
        avatarPanel.setOpaque(false);

        JPanel detailsPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        detailsPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(currentUser.getUsername());
        JLabel emailLabel = new JLabel(currentUser.getEmail());

        nameLabel.setForeground(TEXT_COLOR);
        emailLabel.setForeground(SECONDARY_TEXT);

        nameLabel.setFont(FontManager.getBold(14));
        emailLabel.setFont(FontManager.getRegular(12));

        detailsPanel.add(nameLabel);
        detailsPanel.add(emailLabel);

        userInfo.add(avatarPanel, BorderLayout.WEST);
        userInfo.add(detailsPanel, BorderLayout.CENTER);

        menu.add(userInfo);
        menu.addSeparator();

        addMenuItem(menu, "Profile", e -> navigateTo(ProfilePage.class));
        addMenuItem(menu, "Library", e -> navigateTo(LibraryPage.class));
        menu.addSeparator();
        addMenuItem(menu, "Log Out", e -> handleLogout());
        menu.show(source, 0, source.getHeight());
    }

    private void addMenuItem(JPopupMenu menu, String text, ActionListener action) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(FontManager.getRegular(13));
        item.setForeground(TEXT_COLOR);
        item.setBackground(new Color(40, 25, 65));
        item.setBorder(new EmptyBorder(10, 20, 10, 20));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item.setOpaque(true);
        item.addActionListener(action);

        item.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                item.setBackground(HOVER_PURPLE);
            }
            public void mouseExited(MouseEvent e) {
                item.setBackground(new Color(40, 25, 65));
            }
        });

        menu.add(item);
    }

    private void navigateTo(Class<?> pageClass) {
        try {
            pageClass.getDeclaredConstructor().newInstance();
            parentFrame.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleLogout() {
        parentFrame.dispose();
        new LoginPage();
    }


    private static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;

        public RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(4, 8, 4, 8);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }
}