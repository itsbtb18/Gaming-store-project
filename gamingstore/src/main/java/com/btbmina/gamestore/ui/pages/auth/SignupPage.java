package ui;

import utils.ColorScheme;
import components.PurpleButton;
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
        mainPanel.setBackground(ColorScheme.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Logo
        JLabel logoLabel = new JLabel("Create Account");
        logoLabel.setForeground(ColorScheme.LIGHT_PURPLE);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form fields
        usernameField = createTextField("Username");
        emailField = createTextField("Email");
        passwordField = createPasswordField("Password");
        confirmPasswordField = createPasswordField("Confirm Password");

        // Signup button
        PurpleButton signupButton = new PurpleButton("Create Account");
        signupButton.setMaximumSize(new Dimension(300, 40));

        // Login link
        JButton loginLink = new JButton("Already have an account? Login");
        loginLink.setBorderPainted(false);
        loginLink.setContentAreaFilled(false);
        loginLink.setForeground(ColorScheme.LIGHT_PURPLE);
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add components
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
    }

    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField(20);
        field.setMaximumSize(new Dimension(300, 35));
        return field;
    }

    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(20);
        field.setMaximumSize(new Dimension(300, 35));
        return field;
    }

    private void addFormField(JPanel panel, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setForeground(ColorScheme.TEXT_COLOR);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
    }
}