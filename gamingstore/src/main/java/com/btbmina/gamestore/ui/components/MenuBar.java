package com.btbmina.gamestore.ui.components;

import com.btbmina.gamestore.Util.ColorScheme;
import com.btbmina.gamestore.ui.FontManager;
import com.btbmina.gamestore.classes.User;
import com.btbmina.gamestore.ui.pages.main.CartPage;
import com.btbmina.gamestore.ui.pages.main.StorePage;
import com.btbmina.gamestore.ui.pages.main.LibraryPage;
import com.btbmina.gamestore.ui.pages.main.HomePage;
import com.btbmina.gamestore.ui.pages.auth.LoginPage;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class MenuBar extends JPanel {
    private static final int HEIGHT = 60;
    private static final Color HOVER_COLOR = new Color(255, 255, 255, 30);
    private final JFrame parentFrame;
    private final User currentUser;

    public MenuBar(JFrame parentFrame, User currentUser) {
        this.parentFrame = parentFrame;
        this.currentUser = currentUser;
        setupPanel();
        createContent();
    }

    private void setupPanel() {
        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARK_BACKGROUND);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(255, 255, 255, 20)));
        setPreferredSize(new Dimension(getWidth(), HEIGHT));
    }

    private void createContent() {
        // Left side - Navigation
        JPanel navPanel = createNavPanel();

        // Right side - Controls
        JPanel controlsPanel = createControlsPanel();

        add(navPanel, BorderLayout.WEST);
        add(controlsPanel, BorderLayout.EAST);
    }

    private JPanel createNavPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);

        // Navigation items
        String[] items = {"Home", "Store", "Library"};
        Class<?>[] pages = {HomePage.class, StorePage.class, LibraryPage.class};

        for (int i = 0; i < items.length; i++) {
            panel.add(createNavButton(items[i], pages[i]));
        }

        return panel;
    }

    private JPanel createControlsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panel.setOpaque(false);

        // Cart button
        JButton cartButton = createIconButton("🛒", "Cart");
        cartButton.addActionListener(e -> navigateTo(CartPage.class));

        // User button
        JButton userButton = createIconButton("👤", "Account");
        userButton.addActionListener(e -> showUserMenu(userButton));

        panel.add(cartButton);
        panel.add(userButton);
        panel.add(Box.createHorizontalStrut(20)); // Padding at the end

        return panel;
    }

    private JButton createNavButton(String text, Class<?> pageClass) {
        JButton button = new JButton(text);
        button.setFont(FontManager.getBold(14));
        button.setForeground(Color.WHITE);
        styleButton(button);

        button.addActionListener(e -> navigateTo(pageClass));

        return button;
    }

    private JButton createIconButton(String icon, String tooltip) {
        JButton button = new JButton(icon);
        button.setFont(FontManager.getRegular(18));
        button.setForeground(Color.WHITE);
        button.setToolTipText(tooltip);
        styleButton(button);

        return button;
    }

    private void styleButton(JButton button) {
        button.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);

        // Hover animation
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                animateBackground(button, HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                animateBackground(button, new Color(0, 0, 0, 0));
            }
        });
    }

    private void showUserMenu(Component source) {
        JPopupMenu menu = new JPopupMenu();
        menu.setBackground(ColorScheme.DARK_BACKGROUND);
        menu.setBorder(new LineBorder(new Color(255, 255, 255, 30)));

        // User info panel
        JPanel userInfo = new JPanel(new GridLayout(2, 1, 0, 5));
        userInfo.setBackground(ColorScheme.DARK_BACKGROUND);
        userInfo.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel nameLabel = new JLabel(currentUser.getUsername());
        JLabel emailLabel = new JLabel(currentUser.getEmail());

        nameLabel.setForeground(Color.WHITE);
        emailLabel.setForeground(ColorScheme.TEXT_SECONDARY);

        nameLabel.setFont(FontManager.getBold(14));
        emailLabel.setFont(FontManager.getRegular(12));

        userInfo.add(nameLabel);
        userInfo.add(emailLabel);

        menu.add(userInfo);
        menu.addSeparator();

        // Menu items
        addMenuItem(menu, "Profile", e -> {/* TODO */});
        addMenuItem(menu, "Account Management", e -> {/* TODO */});
        menu.addSeparator();
        addMenuItem(menu, "Log Out", e -> handleLogout());

        menu.show(source, 0, source.getHeight());
    }

    private void addMenuItem(JPopupMenu menu, String text, ActionListener action) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(FontManager.getRegular(13));
        item.setForeground(Color.WHITE);
        item.setBackground(ColorScheme.DARK_BACKGROUND);
        item.setBorder(new EmptyBorder(10, 20, 10, 20));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item.addActionListener(action);

        // Hover effect
        item.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                item.setBackground(ColorScheme.LIGHT_BACKGROUND);
            }
            public void mouseExited(MouseEvent e) {
                item.setBackground(ColorScheme.DARK_BACKGROUND);
            }
        });

        menu.add(item);
    }

    private void animateBackground(JButton button, Color targetColor) {
        Timer timer = new Timer(20, null);
        float[] alpha = {0f};

        timer.addActionListener(e -> {
            alpha[0] += 0.1f;
            if (alpha[0] >= 1f) {
                alpha[0] = 1f;
                timer.stop();
            }

            Color currentColor = new Color(
                    targetColor.getRed(),
                    targetColor.getGreen(),
                    targetColor.getBlue(),
                    (int)(targetColor.getAlpha() * alpha[0])
            );

            button.setBackground(currentColor);
            button.setOpaque(true);
            button.repaint();
        });

        timer.start();
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
}