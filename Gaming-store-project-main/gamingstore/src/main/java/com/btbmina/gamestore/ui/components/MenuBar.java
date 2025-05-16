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
        add(createNavPanel(), BorderLayout.WEST);
        add(createControlsPanel(), BorderLayout.EAST);
    }

    private JPanel createNavPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);

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
        JButton cartButton = createControlButton("Cart");
        cartButton.addActionListener(e -> navigateTo(CartPage.class));

        // User button
        JButton userButton = createControlButton("Account");
        userButton.addActionListener(e -> showUserMenu(userButton));

        panel.add(cartButton);
        panel.add(userButton);
        panel.add(Box.createHorizontalStrut(20));

        return panel;
    }

    private JButton createControlButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FontManager.getBold(14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(87, 54, 163));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(108, 67, 200));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(87, 54, 163));
            }
        });

        return button;
    }

    private JButton createNavButton(String text, Class<?> pageClass) {
        JButton button = new JButton(text);
        button.setFont(FontManager.getBold(14));
        button.setForeground(Color.WHITE);
        styleButton(button);
        button.addActionListener(e -> navigateTo(pageClass));
        return button;
    }

    private void styleButton(JButton button) {
        button.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setOpaque(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setOpaque(true);
                button.setBackground(HOVER_COLOR);
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setOpaque(false);
                button.repaint();
            }
        });
    }

    private void showUserMenu(Component source) {
        JPopupMenu menu = new JPopupMenu();
        menu.setBackground(new Color(48, 25, 52));
        menu.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // User info panel
        JPanel userInfo = new JPanel(new BorderLayout(10, 5));
        userInfo.setBackground(new Color(48, 25, 52));
        userInfo.setBorder(new EmptyBorder(15, 20, 15, 20));

        // User details
        JPanel detailsPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        detailsPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(currentUser.getUsername());
        JLabel emailLabel = new JLabel(currentUser.getEmail());

        nameLabel.setForeground(Color.WHITE);
        emailLabel.setForeground(new Color(200, 200, 200));

        nameLabel.setFont(FontManager.getBold(14));
        emailLabel.setFont(FontManager.getRegular(12));

        detailsPanel.add(nameLabel);
        detailsPanel.add(emailLabel);
        userInfo.add(detailsPanel, BorderLayout.CENTER);

        menu.add(userInfo);
        menu.addSeparator();

        // Menu items
        addMenuItem(menu, "Profile", e -> System.out.println("Profile clicked"));
        addMenuItem(menu, "Library", e -> navigateTo(LibraryPage.class));
        addMenuItem(menu, "Settings", e -> System.out.println("Settings clicked"));
        menu.addSeparator();
        addMenuItem(menu, "Log Out", e -> handleLogout());

        menu.show(source, 0, source.getHeight());
    }

    private void addMenuItem(JPopupMenu menu, String text, ActionListener action) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(FontManager.getRegular(13));
        item.setForeground(Color.WHITE);
        item.setBackground(new Color(87, 54, 163));
        item.setBorder(new EmptyBorder(10, 20, 10, 20));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item.setOpaque(true);
        item.addActionListener(action);

        item.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                item.setBackground(new Color(108, 67, 200));
            }
            public void mouseExited(MouseEvent e) {
                item.setBackground(new Color(87, 54, 163));
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
}