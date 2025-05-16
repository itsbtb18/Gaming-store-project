package com.btbmina.gamestore.ui.pages.auth;

import com.btbmina.gamestore.Util.ColorScheme;
import com.btbmina.gamestore.ui.FontManager;
import com.btbmina.gamestore.ui.components.PurpleButton;
import com.btbmina.gamestore.ui.components.TitleBar;
import com.btbmina.gamestore.ui.pages.main.HomePage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.sql.*;

public class SignupPage extends JFrame {

    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private Image backgroundImage;

    public SignupPage() {
        loadBackgroundImage();
        initializeFrame();
        createMainContent();
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(getClass().getClassLoader().getResource("assets/images/bg_login.jpg"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Failed to load background image: " + e.getMessage());
        }
    }

    private void initializeFrame() {
        setTitle("Gaming Store - Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setUndecorated(true);
    }

    private void createMainContent() {
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.add(new TitleBar(this), BorderLayout.NORTH);
        mainContainer.add(createContentPanel(), BorderLayout.CENTER);
        setContentPane(mainContainer);
        setVisible(true);
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        contentPanel.setLayout(new GridBagLayout());
        setupSignupForm(contentPanel);
        return contentPanel;
    }

    private void setupSignupForm(JPanel container) {
        JPanel formPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 160));
                RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(
                        0, 0, getWidth(), getHeight(), 20, 20
                );
                g2d.fill(roundedRectangle);

                g2d.setColor(new Color(255, 255, 255, 50));
                g2d.setStroke(new BasicStroke(1f));
                g2d.draw(roundedRectangle);
            }
        };

        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
        formPanel.setPreferredSize(new Dimension(400, 600));

        addFormComponents(formPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        container.add(formPanel, gbc);
    }

    private void addFormComponents(JPanel panel) {
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(FontManager.getTitle(32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Sign up to get started");
        subtitleLabel.setFont(FontManager.getRegular(16));
        subtitleLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameField = createStyledField();
        emailField = createStyledField();
        passwordField = createStyledPasswordField();
        confirmPasswordField = createStyledPasswordField();

        PurpleButton signupButton = new PurpleButton("SIGN UP");
        signupButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        signupButton.setFont(FontManager.getBold(16));
        signupButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton loginLink = new JButton("Already have an account? Login");
        loginLink.setBorderPainted(false);
        loginLink.setContentAreaFilled(false);
        loginLink.setFocusPainted(false);
        loginLink.setForeground(ColorScheme.TEXT_SECONDARY);
        loginLink.setFont(FontManager.getRegular(14));
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.setAlignmentX(Component.LEFT_ALIGNMENT);

        loginLink.addActionListener(e -> {
            new LoginPage();
            dispose();
        });

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setOpaque(false);
        fieldsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldsPanel.setMaximumSize(new Dimension(360, 300));

        addFormField(fieldsPanel, "USERNAME", usernameField);
        addFormField(fieldsPanel, "EMAIL", emailField);
        addFormField(fieldsPanel, "PASSWORD", passwordField);
        addFormField(fieldsPanel, "CONFIRM PASSWORD", confirmPasswordField);

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(subtitleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));
        panel.add(fieldsPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(signupButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(loginLink);

        setupSignupAction(signupButton);
    }

    private JTextField createStyledField() {
        JTextField field = new JTextField();
        configureField(field);
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        configureField(field);
        return field;
    }

    private void configureField(JTextField field) {
        field.setMaximumSize(new Dimension(360, 35));
        field.setBackground(ColorScheme.LIGHT_BACKGROUND);
        field.setForeground(ColorScheme.TEXT_PRIMARY);
        field.setCaretColor(Color.WHITE);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setFont(FontManager.getRegular(14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 30), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        field.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                field.setBackground(new Color(45, 45, 65));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                field.setBackground(ColorScheme.LIGHT_BACKGROUND);
            }
        });
    }

    private void addFormField(JPanel parent, String labelText, JComponent field) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.setMaximumSize(new Dimension(360, 70));

        JLabel label = new JLabel(labelText);
        label.setForeground(ColorScheme.TEXT_SECONDARY);
        label.setFont(FontManager.getRegular(14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        wrapper.add(label);
        wrapper.add(Box.createRigidArea(new Dimension(0, 5)));
        wrapper.add(field);

        parent.add(wrapper);
        parent.add(Box.createRigidArea(new Dimension(0, 15)));
    }

    private void setupSignupAction(JButton signupButton) {
        signupButton.addActionListener(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showError("All fields must be filled!");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showError("Passwords do not match!");
                return;
            }

            if (addUserToDatabase(username, email, password)) {
                new HomePage();
                dispose();
            } else {
                showError("Failed to create account. Try again.");
            }
        });
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private boolean addUserToDatabase(String username, String email, String password) {
        String dbUrl = "jdbc:mysql://sql7.freesqldatabase.com:3306/sql7779083";
        String dbUser = "sql7779083";
        String dbPassword = "Hdm6dRtXQF";
        String insertSQL = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(insertSQL)) {

            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
