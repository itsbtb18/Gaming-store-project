package com.btbmina.gamestore.ui.pages.main;

import javax.swing.*;
import javax.swing.border.*;
import com.btbmina.gamestore.ui.pages.auth.LoginPage;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

import com.btbmina.gamestore.Util.ColorScheme;
import com.btbmina.gamestore.ui.components.*;
import com.btbmina.gamestore.ui.FontManager;
import com.btbmina.gamestore.classes.User;
import com.btbmina.gamestore.DB.UserDB;
import com.btbmina.gamestore.ui.components.MenuBar;

public class ProfilePage extends JFrame {
    private static final int CONTENT_WIDTH = 800;
    private static final int FIELD_HEIGHT = 40;
    private static final Color SECTION_BG = new Color(35, 25, 45);
    private static final Color DANGER_BG = new Color(40, 20, 20);
    private static final Color DANGER_BTN = new Color(200, 30, 30);
    private static final Color DANGER_BTN_HOVER = new Color(220, 40, 40);

    private User currentUser;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;

    public ProfilePage() {
        initializeFrame();
        loadUserData();
        createContent();
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("Gaming Store - Profile");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(this);
        } else {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }

    private void loadUserData() {
        try {
            currentUser = UserDB.getCurrentUser();
            if (currentUser == null) {
                throw new IllegalStateException("No user logged in");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading user data",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            navigateToLogin();
        }
    }

    private void createContent() {
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(ColorScheme.DARK_BACKGROUND);

        // Header section
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ColorScheme.DARK_BACKGROUND);
        header.add(new TitleBar(this), BorderLayout.NORTH);

        JPanel menuWrapper = new JPanel(new BorderLayout());
        menuWrapper.setBackground(ColorScheme.DARK_BACKGROUND);
        menuWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        menuWrapper.add(new MenuBar(this, currentUser), BorderLayout.CENTER);
        header.add(menuWrapper, BorderLayout.CENTER);

        mainContainer.add(header, BorderLayout.NORTH);

        // Main content
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(ColorScheme.DARK_BACKGROUND);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ColorScheme.DARK_BACKGROUND);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
        contentPanel.setPreferredSize(new Dimension(CONTENT_WIDTH, -1));

        // Add sections
        contentPanel.add(createProfileSection());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(createDangerZone());

        centerWrapper.add(contentPanel);

        JScrollPane scrollPane = new JScrollPane(centerWrapper);
        scrollPane.setBorder(null);
        scrollPane.setBackground(ColorScheme.DARK_BACKGROUND);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());

        mainContainer.add(scrollPane, BorderLayout.CENTER);
        setContentPane(mainContainer);
    }

    private JPanel createProfileSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(SECTION_BG);
        section.setBorder(createSectionBorder());

        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(40, 30, 50));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        titlePanel.add(createSectionTitle("Profile Information"), BorderLayout.CENTER);

        // Fields Panel
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBackground(SECTION_BG);
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Username Field
        fieldsPanel.add(createFieldGroup(
                "Username",
                usernameField = createField(currentUser.getUsername()),
                createPurpleButton("Change Username", e -> showChangeUsernameDialog())
        ));
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Email Field
        fieldsPanel.add(createFieldGroup(
                "Email",
                emailField = createField(currentUser.getEmail()),
                createPurpleButton("Change Email", e -> showChangeEmailDialog())
        ));
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Password Field
        passwordField = new JPasswordField("••••••••");
        passwordField.setEditable(false);
        styleField(passwordField);
        fieldsPanel.add(createFieldGroup(
                "Password",
                passwordField,
                createPurpleButton("Change Password", e -> showChangePasswordDialog())
        ));

        section.add(titlePanel);
        section.add(new JSeparator(JSeparator.HORIZONTAL));
        section.add(fieldsPanel);

        return section;
    }

    private JPanel createDangerZone() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(DANGER_BG);
        panel.setBorder(createSectionBorder());

        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(50, 25, 25));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = createSectionTitle("Danger Zone");
        title.setForeground(new Color(255, 100, 100));
        titlePanel.add(title, BorderLayout.CENTER);

        // Content Panel
        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contentPanel.setBackground(DANGER_BG);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Delete Button
        JButton deleteButton = new JButton("Delete Account");
        deleteButton.setFont(FontManager.getBold(14));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBackground(DANGER_BTN);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        deleteButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                deleteButton.setBackground(DANGER_BTN_HOVER);
            }
            public void mouseExited(MouseEvent e) {
                deleteButton.setBackground(DANGER_BTN);
            }
        });

        deleteButton.addActionListener(e -> showDeleteAccountDialog());
        contentPanel.add(deleteButton);

        panel.add(titlePanel);
        panel.add(new JSeparator(JSeparator.HORIZONTAL));
        panel.add(contentPanel);

        return panel;
    }

    private JPanel createFieldGroup(String labelText, JTextField field, JButton button) {
        JPanel group = new JPanel(new BorderLayout(15, 0));
        group.setBackground(SECTION_BG);

        JLabel label = new JLabel(labelText);
        label.setFont(FontManager.getRegular(14));
        label.setForeground(new Color(200, 200, 200));
        label.setPreferredSize(new Dimension(100, FIELD_HEIGHT));

        group.add(label, BorderLayout.WEST);
        group.add(field, BorderLayout.CENTER);
        group.add(button, BorderLayout.EAST);

        return group;
    }

    private JTextField createField(String text) {
        JTextField field = new JTextField(text);
        field.setEditable(false);
        styleField(field);
        return field;
    }

    private JButton createPurpleButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(FontManager.getBold(13));
        button.setForeground(Color.WHITE);
        button.setBackground(ColorScheme.LIGHT_BACKGROUND);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(action);
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ColorScheme.BUTTON_HOVER);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(ColorScheme.LIGHT_BACKGROUND);
            }
        });

        return button;
    }

    private void styleField(JTextField field) {
        field.setPreferredSize(new Dimension(-1, FIELD_HEIGHT));
        field.setBackground(new Color(25, 15, 35));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setFont(FontManager.getRegular(14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 70, 160), 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
    }

    private Border createSectionBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 30), 1),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        );
    }

    private JLabel createSectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FontManager.getBold(24));
        label.setForeground(Color.WHITE);
        return label;
    }

    // ... Dialog methods stay the same as in your current code ...

    private void navigateToLogin() {
        new LoginPage();
        dispose();
    }
    private void showChangeUsernameDialog() {
        String newUsername = JOptionPane.showInputDialog(this, "Enter new username:", "Change Username", JOptionPane.PLAIN_MESSAGE);
        if (newUsername != null && !newUsername.trim().isEmpty()) {
            try {
                UserDB.updateUsername(currentUser.getUserId(), newUsername.trim());
                currentUser.setUsername(newUsername.trim());
                JOptionPane.showMessageDialog(this, "Username updated successfully!");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to update username.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void showChangeEmailDialog() {
        String newEmail = JOptionPane.showInputDialog(this, "Enter new email:", "Change Email", JOptionPane.PLAIN_MESSAGE);
        if (newEmail != null && !newEmail.trim().isEmpty()) {
            try {
                UserDB.updateEmail(currentUser.getUserId(), newEmail.trim());
                currentUser.setEmail(newEmail.trim());
                JOptionPane.showMessageDialog(this, "Email updated successfully!");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to update email.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void showDeleteAccountDialog() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        JLabel confirmLabel = new JLabel("Enter your password to confirm account deletion:");
        JPasswordField passwordField = new JPasswordField();

        panel.add(confirmLabel);
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Delete Account",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String password = new String(passwordField.getPassword());

            try {
                UserDB.deleteAccount(currentUser.getUserId(), password);
                JOptionPane.showMessageDialog(this, "Account deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Optional: Exit or redirect to login
                System.exit(0);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Incorrect password or error deleting account.", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void showChangePasswordDialog() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JPasswordField oldPass = new JPasswordField();
        JPasswordField newPass = new JPasswordField();

        panel.add(new JLabel("Current password:"));
        panel.add(oldPass);
        panel.add(new JLabel("New password:"));
        panel.add(newPass);

        int result = JOptionPane.showConfirmDialog(this, panel, "Change Password", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String oldPassword = new String(oldPass.getPassword());
            String newPassword = new String(newPass.getPassword());

            try {
                if (UserDB.checkCredentials(currentUser.getUsername(), oldPassword)) {
                    UserDB.updatePassword(currentUser.getUserId(), oldPassword,newPassword);
                    JOptionPane.showMessageDialog(this, "Password updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect current password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to update password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



}