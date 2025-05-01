package com.btbmina.gamestore.ui.pages.auth;

import com.btbmina.gamestore.Util.ColorScheme;
import com.btbmina.gamestore.ui.components.PurpleButton;

import javax.swing.*;
import java.awt.*;

public class SignupPage extends JFrame {

    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public SignupPage() {
        setTitle("Gaming Store - Sign Up");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(ColorScheme.DARK_BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Logo
        JLabel logoLabel = new JLabel("Create Account");
        logoLabel.setForeground(ColorScheme.PRIMARY_PURPLE);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Input Fields
        usernameField = createTextField();
        emailField = createTextField();
        passwordField = createPasswordField();
        confirmPasswordField = createPasswordField();

        // Signup Button
        PurpleButton signupButton = new PurpleButton("Create Account");
        signupButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        signupButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Login Link
        JButton loginLink = new JButton("Already have an account? Login");
        loginLink.setBorderPainted(false);
        loginLink.setContentAreaFilled(false);
        loginLink.setFocusPainted(false);
        loginLink.setForeground(ColorScheme.SECONDARY_PURPLE);
        loginLink.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Assemble
        mainPanel.add(logoLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        addFormField(mainPanel, "Username", usernameField);
        addFormField(mainPanel, "Email", emailField);
        addFormField(mainPanel, "Password", passwordField);
        addFormField(mainPanel, "Confirm Password", confirmPasswordField);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(signupButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(loginLink);

        add(mainPanel);
        setVisible(true);
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setBackground(ColorScheme.LIGHT_BACKGROUND);
        field.setForeground(ColorScheme.TEXT_PRIMARY);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setBackground(ColorScheme.LIGHT_BACKGROUND);
        field.setForeground(ColorScheme.TEXT_PRIMARY);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return field;
    }

    private void addFormField(JPanel panel, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setForeground(ColorScheme.TEXT_SECONDARY);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
    }
}
