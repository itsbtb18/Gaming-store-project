package com.btbmina.gamestore.ui.pages.auth;

import com.btbmina.gamestore.Util.ColorScheme;
import com.btbmina.gamestore.ui.components.PurpleButton;

import javax.swing.*;
import java.awt.*;

public class LoginPage extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage() {
        setTitle("Gaming Store - Login");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(ColorScheme.DARK_BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 40, 50, 40));

        // Logo
        JLabel logoLabel = new JLabel("Gaming Store");
        logoLabel.setForeground(ColorScheme.PRIMARY_PURPLE);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username Label and Field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        usernameField.setBackground(ColorScheme.LIGHT_BACKGROUND);
        usernameField.setForeground(ColorScheme.TEXT_PRIMARY);
        usernameField.setCaretColor(Color.WHITE);
        usernameField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Password Label and Field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        passwordField.setBackground(ColorScheme.LIGHT_BACKGROUND);
        passwordField.setForeground(ColorScheme.TEXT_PRIMARY);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Login Button
        PurpleButton loginButton = new PurpleButton("Login");
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Signup Link
        JButton signupLink = new JButton("Don't have an account? Sign up");
        signupLink.setForeground(ColorScheme.SECONDARY_PURPLE);
        signupLink.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        signupLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupLink.setBorderPainted(false);
        signupLink.setContentAreaFilled(false);
        signupLink.setFocusPainted(false);
        signupLink.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components
        mainPanel.add(logoLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        mainPanel.add(usernameLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(usernameField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        mainPanel.add(passwordLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(passwordField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        mainPanel.add(loginButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(signupLink);

        add(mainPanel);
        setVisible(true);
    }
}
