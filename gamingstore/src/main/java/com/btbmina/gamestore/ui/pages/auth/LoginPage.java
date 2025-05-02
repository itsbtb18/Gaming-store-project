package com.btbmina.gamestore.ui.pages.auth;

import com.btbmina.gamestore.Util.ColorScheme;
import com.btbmina.gamestore.ui.components.*;
import com.btbmina.gamestore.ui.pages.main.HomePage;
import com.btbmina.gamestore.DB.UserDB; // Assurez-vous que cette classe est bien importée.

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginPage extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage() {
        setTitle("Gaming Store - Login");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(ColorScheme.DARK_BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Logo
        JLabel logoLabel = new JLabel("Login to Your Account");
        logoLabel.setForeground(ColorScheme.PRIMARY_PURPLE);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Input Fields
        usernameField = createTextField();
        passwordField = createPasswordField();

        // Login Button
        PurpleButton loginButton = new PurpleButton("Login");
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Signup Link
        JButton signupLink = new JButton("Don't have an account? Sign Up");
        signupLink.setBorderPainted(false);
        signupLink.setContentAreaFilled(false);
        signupLink.setFocusPainted(false);
        signupLink.setForeground(ColorScheme.SECONDARY_PURPLE);
        signupLink.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        signupLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupLink.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Assemble
        mainPanel.add(logoLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        addFormField(mainPanel, "Username", usernameField);
        addFormField(mainPanel, "Password", passwordField);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(loginButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(signupLink);

        // Login button action
        loginButton.addActionListener(e -> {
            // Get input values
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Both fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Don't continue if fields are empty
            }

            // Check login credentials (you need to write this method in UserDB)
            try {
                if (UserDB.checkCredentials(username, password)) {
                    System.out.println("Login successful");
                    new HomePage();
                    dispose(); // pour fermer la fenêtre de login
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "An error occurred while logging in. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Signup link action
        signupLink.addActionListener(e -> {
            System.out.println("Signup link clicked");
            new SignupPage(); // Go to signup page
            dispose();         // Close login page
        });

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
