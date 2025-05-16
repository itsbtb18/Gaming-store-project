package com.btbmina.gamestore.ui.pages.auth;

import com.btbmina.gamestore.ui.FontManager;
import com.btbmina.gamestore.Util.ColorScheme;
import com.btbmina.gamestore.ui.components.PurpleButton;
import com.btbmina.gamestore.ui.components.TitleBar;
import com.btbmina.gamestore.ui.pages.main.HomePage;
import com.btbmina.gamestore.DB.UserDB;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.sql.SQLException;

public class LoginPage extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private Image backgroundImage;

    public LoginPage() {
        loadBackgroundImage();
        initializeFrame();
        createMainContent();
    }

    // ✅ Corrected and simplified image loader
    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(getClass().getClassLoader().getResource("assets/images/bg_login.jpg"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Failed to load background image: " + e.getMessage());
        }
    }

    private void initializeFrame() {
        setTitle("Gaming Store - Login");
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

    // ✅ NO overlay — just clean image background
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
        setupLoginForm(contentPanel);
        return contentPanel;
    }

    private void setupLoginForm(JPanel container) {
        JPanel formPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Semi-transparent panel (optional – you can remove this if not needed)
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
        formPanel.setPreferredSize(new Dimension(400, 500));

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
        JLabel titleLabel = new JLabel("Welcome Back");
        titleLabel.setFont(FontManager.getTitle(32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Login to your account");
        subtitleLabel.setFont(FontManager.getRegular(16));
        subtitleLabel.setForeground(ColorScheme.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameField = createStyledField();
        passwordField = createStyledPasswordField();

        PurpleButton loginButton = new PurpleButton("LOGIN");
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        loginButton.setFont(FontManager.getBold(16));
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton signupLink = createSignupLink();
        signupLink.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(subtitleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setOpaque(false);
        fieldsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldsPanel.setMaximumSize(new Dimension(360, 200));

        addFormField(fieldsPanel, "USERNAME", usernameField);
        addFormField(fieldsPanel, "PASSWORD", passwordField);

        panel.add(fieldsPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(loginButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(signupLink);

        setupLoginAction(loginButton);
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

    private JButton createSignupLink() {
        JButton signupLink = new JButton("Don't have an account? Sign Up");
        signupLink.setBorderPainted(false);
        signupLink.setContentAreaFilled(false);
        signupLink.setFocusPainted(false);
        signupLink.setForeground(ColorScheme.TEXT_SECONDARY);
        signupLink.setFont(FontManager.getRegular(14));
        signupLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupLink.setAlignmentX(Component.LEFT_ALIGNMENT);

        signupLink.addActionListener(e -> {
            new SignupPage();
            dispose();
        });

        return signupLink;
    }

    private void setupLoginAction(JButton loginButton) {
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                showError("Please fill in all fields");
                return;
            }

            try {
                if (UserDB.checkCredentials(username, password)) {
                    new HomePage();
                    dispose();
                } else {
                    showError("Invalid username or password");
                }
            } catch (SQLException ex) {
                showError("Connection error. Please try again.");
                ex.printStackTrace();
            }
        });
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
