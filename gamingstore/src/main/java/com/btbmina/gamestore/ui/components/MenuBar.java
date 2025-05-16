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
    private static final Color MENU_PURPLE = new Color(87, 54, 163);
    private static final Color HOVER_PURPLE = new Color(108, 67, 200);
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
        panel.add(createNavButton("Home", HomePage.class));
        return panel;
    }

    private JPanel createControlsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panel.setOpaque(false);

        JButton cartButton = createControlButton("Cart");
        cartButton.addActionListener(e -> navigateTo(CartPage.class));

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
        button.setBackground(MENU_PURPLE);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(HOVER_PURPLE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(MENU_PURPLE);
            }
        });

        return button;
    }

    private JButton createNavButton(String text, Class<?> pageClass) {
        JButton button = new JButton(text);
        button.setFont(FontManager.getBold(14));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.addActionListener(e -> navigateTo(pageClass));
        return button;
    }

    private void showUserMenu(Component source) {
        JPopupMenu menu = new JPopupMenu();
        menu.setBackground(MENU_PURPLE);
        menu.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JPanel userInfo = new JPanel(new BorderLayout(10, 5));
        userInfo.setBackground(MENU_PURPLE);
        userInfo.setBorder(new EmptyBorder(15, 20, 15, 20));

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

        addMenuItem(menu, "Profile", e -> navigateTo(ProfilePage.class));
        addMenuItem(menu, "Library", e -> navigateTo(LibraryPage.class));
        menu.addSeparator();
        addMenuItem(menu, "Log Out", e -> handleLogout());

        menu.show(source, 0, source.getHeight());
    }

    private void addMenuItem(JPopupMenu menu, String text, ActionListener action) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(FontManager.getRegular(13));
        item.setForeground(Color.WHITE);
        item.setBackground(MENU_PURPLE);
        item.setBorder(new EmptyBorder(10, 20, 10, 20));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item.setOpaque(true);
        item.addActionListener(action);

        item.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                item.setBackground(HOVER_PURPLE);
            }
            public void mouseExited(MouseEvent e) {
                item.setBackground(MENU_PURPLE);
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