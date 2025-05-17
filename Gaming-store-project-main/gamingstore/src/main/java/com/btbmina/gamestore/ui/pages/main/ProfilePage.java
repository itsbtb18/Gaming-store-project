package com.btbmina.gamestore.ui.pages.main;

import com.btbmina.gamestore.DB.UserDB;
import com.btbmina.gamestore.classes.User;
import com.btbmina.gamestore.Util.ColorScheme;
import com.btbmina.gamestore.ui.components.MenuBar;
import com.btbmina.gamestore.ui.components.TitleBar;
import com.btbmina.gamestore.ui.components.ModernScrollBarUI;
import com.btbmina.gamestore.ui.FontManager;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.util.concurrent.ExecutionException;

public class ProfilePage extends JFrame {
    // Constants
    private static final int CONTENT_WIDTH = 700;
    private static final int FIELD_HEIGHT = 40;
    private static final Color ACCENT_COLOR = new Color(120, 80, 200);
    private static final Color ACCENT_COLOR_HOVER = new Color(140, 100, 220);
    private static final Color SECTION_BG = new Color(40, 40, 50);
    private static final Color SECTION_HEADER_BG = new Color(50, 50, 60);
    private static final Color FIELD_BG = new Color(30, 30, 40);
    private static final Color DANGER_BG = new Color(50, 30, 40);
    private static final Color DANGER_HEADER_BG = new Color(60, 35, 45);
    private static final Color DANGER_BTN = new Color(200, 60, 60);
    private static final Color DANGER_BTN_HOVER = new Color(220, 70, 70);

    // Instance variables
    private User currentUser;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPanel mainContentPanel;
    private CardLayout contentCardLayout;

    // For animations
    private Timer fadeInTimer;
    private Timer slideInTimer;
    private float alphaValue = 0.0f;
    private int slideOffset = 100;

    public ProfilePage() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize frame first
        initializeFrame();

        // Load data and create content
        loadUserData();
        createContent();
        startEntryAnimations();

        // Make visible after everything is set up
        SwingUtilities.invokeLater(() -> {
            revalidate();
            repaint();
            setVisible(true);
        });
    }

    private void initializeFrame() {
        setTitle("Gaming Store - Profile");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);

        // Set minimum size first
        setMinimumSize(new Dimension(900, 700));

        // Configure for fullscreen
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        // Set display mode to maximum resolution
        DisplayMode[] modes = gd.getDisplayModes();
        DisplayMode maxMode = null;
        for (DisplayMode mode : modes) {
            if (maxMode == null ||
                    (mode.getWidth() > maxMode.getWidth() && mode.getHeight() > maxMode.getHeight())) {
                maxMode = mode;
            }
        }

        if (maxMode != null) {
            setSize(maxMode.getWidth(), maxMode.getHeight());
        } else {
            // Fallback to screen size
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setSize(screenSize);
        }

        // Center the frame
        setLocationRelativeTo(null);

        // Try fullscreen mode
        if (gd.isFullScreenSupported()) {
            try {
                gd.setFullScreenWindow(this);
            } catch (Exception e) {
                e.printStackTrace();
                // Fallback to maximized state
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        } else {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }
    private void loadUserData() {
        try {
            currentUser = UserDB.getCurrentUser();
            if (currentUser == null) {
                // Create mock user with proper constructor
                currentUser = new User(1, "TestUser", "test@example.com", "password123");
                System.out.println("Created mock user for testing");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Create mock user with proper constructor
            currentUser = new User(1, "TestUser", "test@example.com", "password123");
            System.out.println("Created mock user for testing after exception");
        }
    }

    private void navigateToLogin() {
        // Implementation to navigate to login screen
        dispose();
        // LoginPage loginPage = new LoginPage();
        // loginPage.setVisible(true);
    }
    // Update the createContent() method's scroll pane configuration:
    private JScrollPane createScrollPane(Component view) {
        JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.setBorder(null);
        scrollPane.setBackground(ColorScheme.DARK_BACKGROUND);

        // Configure vertical scrollbar
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        verticalBar.setUI(new ModernScrollBarUI());
        verticalBar.setPreferredSize(new Dimension(8, 0));
        verticalBar.setUnitIncrement(16);
        verticalBar.setOpaque(false);

        // Configure horizontal scrollbar
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Make viewport transparent
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getViewport().setBackground(ColorScheme.DARK_BACKGROUND);

        return scrollPane;
    }
    private void createContent() {
        // DEBUG - check if user is properly loaded
        System.out.println("Creating content with user: " +
                (currentUser != null ? currentUser.getUsername() : "null"));

        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(ColorScheme.DARK_BACKGROUND);

        // Header section with TitleBar and MenuBar
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ColorScheme.DARK_BACKGROUND);
        header.add(new TitleBar(this), BorderLayout.NORTH);

        JPanel menuWrapper = new JPanel(new BorderLayout());
        menuWrapper.setBackground(ColorScheme.DARK_BACKGROUND);
        menuWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        menuWrapper.add(new MenuBar(this, currentUser), BorderLayout.CENTER);
        header.add(menuWrapper, BorderLayout.CENTER);

        mainContainer.add(header, BorderLayout.NORTH);

        // Center content with scroll functionality
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBackground(ColorScheme.DARK_BACKGROUND);

        JPanel centeringPanel = new JPanel(new GridBagLayout());
        centeringPanel.setBackground(ColorScheme.DARK_BACKGROUND);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ColorScheme.DARK_BACKGROUND);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));

        // Important: Set both preferred AND minimum size to ensure visibility
        contentPanel.setPreferredSize(new Dimension(CONTENT_WIDTH, 800));
        contentPanel.setMinimumSize(new Dimension(CONTENT_WIDTH, 800));

        // Add sections to content panel
        contentPanel.add(createAvatarSection());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(createProfileSection());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(createDangerZone());

        centeringPanel.add(contentPanel);
        contentWrapper.add(centeringPanel, BorderLayout.CENTER);

        // Add scroll functionality
        JScrollPane scrollPane = createScrollPane(contentWrapper);
        mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(ColorScheme.DARK_BACKGROUND);
        mainContentPanel.add(scrollPane, BorderLayout.CENTER);

        // Important: Set viewport view to properly handle scrolling
        scrollPane.getViewport().setOpaque(true);
        scrollPane.getViewport().setBackground(ColorScheme.DARK_BACKGROUND);

        // Main content area
        mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(ColorScheme.DARK_BACKGROUND);
        mainContentPanel.add(scrollPane, BorderLayout.CENTER);

        mainContainer.add(mainContentPanel, BorderLayout.CENTER);

        // Set the content pane
        setContentPane(mainContainer);

        // Force validation and repaint
        validate();
        repaint();

        // DEBUG - Print component hierarchy to help diagnose issues
        System.out.println("Component hierarchy:");
        printComponentHierarchy(mainContainer, 0);
    }

    // Debug method to print component hierarchy
    private void printComponentHierarchy(Container container, int level) {
        String indent = " ".repeat(level * 2);
        System.out.println(indent + container.getClass().getSimpleName() +
                " [" + container.getWidth() + "x" + container.getHeight() + "]" +
                " visible: " + container.isVisible());

        for (Component comp : container.getComponents()) {
            if (comp instanceof Container) {
                printComponentHierarchy((Container)comp, level + 1);
            } else {
                System.out.println(indent + "  " + comp.getClass().getSimpleName() +
                        " [" + comp.getWidth() + "x" + comp.getHeight() + "]" +
                        " visible: " + comp.isVisible());
            }
        }
    }
    private JPanel createAvatarSection() {
        JPanel avatarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        avatarPanel.setBackground(ColorScheme.DARK_BACKGROUND);
        avatarPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Create avatar panel with user icon
        JPanel avatarWrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw circle background
                g2d.setColor(ACCENT_COLOR);
                g2d.fillOval(0, 0, getWidth(), getHeight());

                // Load and draw user icon
                try {
                    ImageIcon icon = new ImageIcon(getClass().getResource("/assets/icons/user.png"));
                    Image img = icon.getImage();
                    int size = (int) (getWidth() * 0.6); // Icon takes 60% of the circle
                    int x = (getWidth() - size) / 2;
                    int y = (getHeight() - size) / 2;
                    g2d.drawImage(img, x, y, size, size, null);
                } catch (Exception e) {
                    // Fallback to initial if image fails to load
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(FontManager.getBold(50));
                    String initial = currentUser != null ?
                            currentUser.getUsername().substring(0, 1).toUpperCase() : "?";
                    FontMetrics metrics = g2d.getFontMetrics();
                    int x = (getWidth() - metrics.stringWidth(initial)) / 2;
                    int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
                    g2d.drawString(initial, x, y);
                }

                g2d.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(120, 120);
            }

            @Override
            public Dimension getMinimumSize() {
                return getPreferredSize();
            }

            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }
        };
        avatarWrapper.setOpaque(false);

        // Rest of the avatar section code remains the same
        // ...
        return avatarPanel;
    }
    private JPanel createProfileSection() {
        // DEBUG - Verify this section is being created
        System.out.println("Creating profile section");

        JPanel section = new RoundedPanel(20);
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(SECTION_BG);
        section.setBorder(createSectionBorder());

        // Set explicit size for this panel
        section.setPreferredSize(new Dimension(CONTENT_WIDTH, 300));
        section.setMinimumSize(new Dimension(CONTENT_WIDTH, 300));
        section.setMaximumSize(new Dimension(CONTENT_WIDTH, 300));

        // Title Panel
        JPanel titlePanel = new RoundedPanel(new BorderLayout(), 20, 20, 0, 0);
        titlePanel.setBackground(SECTION_HEADER_BG);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleIcon = new JLabel("⚙️");
        titleIcon.setFont(new Font("Dialog", Font.PLAIN, 24));
        titleIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

        JPanel titleWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titleWrapper.setBackground(SECTION_HEADER_BG);
        titleWrapper.add(titleIcon);
        titleWrapper.add(createSectionTitle("Profile Information"));

        titlePanel.add(titleWrapper, BorderLayout.CENTER);

        // Fields Panel
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBackground(SECTION_BG);
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Get user data with null safety
        String username = currentUser != null ? currentUser.getUsername() : "User";
        String email = currentUser != null ? currentUser.getEmail() : "user@example.com";

        // Username Field
        fieldsPanel.add(createFieldGroup(
                "Username",
                usernameField = createField(username),
                createPurpleButton("Change Username", e -> showChangeUsernameDialog())
        ));
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Email Field
        fieldsPanel.add(createFieldGroup(
                "Email",
                emailField = createField(email),
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
        // DEBUG - Verify this section is being created
        System.out.println("Creating danger zone section");

        JPanel panel = new RoundedPanel(20);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(DANGER_BG);
        panel.setBorder(createSectionBorder());

        // Set explicit size for this panel
        panel.setPreferredSize(new Dimension(CONTENT_WIDTH, 200));
        panel.setMinimumSize(new Dimension(CONTENT_WIDTH, 200));
        panel.setMaximumSize(new Dimension(CONTENT_WIDTH, 200));

        // Title Panel
        JPanel titlePanel = new RoundedPanel(new BorderLayout(), 20, 20, 0, 0);
        titlePanel.setBackground(DANGER_HEADER_BG);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleIcon = new JLabel("⚠️");
        titleIcon.setFont(new Font("Dialog", Font.PLAIN, 24));
        titleIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

        JPanel titleWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titleWrapper.setBackground(DANGER_HEADER_BG);
        titleWrapper.add(titleIcon);

        JLabel title = createSectionTitle("Danger Zone");
        title.setForeground(new Color(255, 100, 100));
        titleWrapper.add(title);

        titlePanel.add(titleWrapper, BorderLayout.CENTER);

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

                // Add shake animation on hover
                new Timer(30, new ActionListener() {
                    private int count = 0;
                    private int direction = 1;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (count >= 5) {
                            ((Timer)e.getSource()).stop();
                            deleteButton.setLocation(deleteButton.getX() - direction * 2, deleteButton.getY());
                            return;
                        }

                        deleteButton.setLocation(deleteButton.getX() + direction * 4, deleteButton.getY());
                        direction *= -1;
                        count++;
                    }
                }).start();
            }

            public void mouseExited(MouseEvent e) {
                deleteButton.setBackground(DANGER_BTN);
            }
        });

        deleteButton.addActionListener(e -> showDeleteAccountDialog());
        contentPanel.add(deleteButton);

        JTextArea warningText = new JTextArea("WARNING: This action cannot be undone. All your data will be permanently deleted.");
        warningText.setEditable(false);
        warningText.setLineWrap(true);
        warningText.setWrapStyleWord(true);
        warningText.setBackground(DANGER_BG);
        warningText.setForeground(new Color(255, 150, 150));
        warningText.setFont(FontManager.getRegular(12));
        warningText.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        warningText.setMaximumSize(new Dimension(CONTENT_WIDTH - 60, 50));

        JPanel warningPanel = new JPanel();
        warningPanel.setLayout(new BoxLayout(warningPanel, BoxLayout.X_AXIS));
        warningPanel.setBackground(DANGER_BG);
        warningPanel.add(warningText);

        JPanel completeContentPanel = new JPanel();
        completeContentPanel.setLayout(new BoxLayout(completeContentPanel, BoxLayout.Y_AXIS));
        completeContentPanel.setBackground(DANGER_BG);
        completeContentPanel.add(contentPanel);
        completeContentPanel.add(warningPanel);

        panel.add(titlePanel);
        panel.add(new JSeparator(JSeparator.HORIZONTAL));
        panel.add(completeContentPanel);

        return panel;
    }

    private JPanel createFieldGroup(String labelText, JTextField field, JButton button) {
        JPanel group = new JPanel(new BorderLayout(15, 0));
        group.setBackground(SECTION_BG);

        JLabel label = new JLabel(labelText);
        label.setFont(FontManager.getBold(14));
        label.setForeground(new Color(200, 200, 200));
        label.setPreferredSize(new Dimension(100, FIELD_HEIGHT));

        group.add(label, BorderLayout.WEST);
        group.add(field, BorderLayout.CENTER);
        group.add(button, BorderLayout.EAST);

        return group;
    }

    private JTextField createField(String text) {
        JTextField field = new RoundedTextField(15);
        field.setText(text);
        field.setEditable(false);
        styleField(field);
        return field;
    }

    private JButton createPurpleButton(String text, ActionListener action) {
        JButton button = new RoundedButton(text, 15);
        button.setFont(FontManager.getBold(13));
        button.setForeground(Color.WHITE);
        button.setBackground(ACCENT_COLOR);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> {
            // Add click animation
            animateButtonClick(button);
            action.actionPerformed(e);
        });

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ACCENT_COLOR_HOVER);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1),
                        BorderFactory.createEmptyBorder(7, 14, 7, 14)
                ));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(ACCENT_COLOR);
                button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            }
        });

        return button;
    }

    private void styleField(JTextField field) {
        field.setPreferredSize(new Dimension(-1, FIELD_HEIGHT));
        field.setBackground(FIELD_BG);
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
                BorderFactory.createLineBorder(new Color(255, 255, 255, 20), 1),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        );
    }

    private JLabel createSectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FontManager.getBold(24));
        label.setForeground(Color.WHITE);
        return label;
    }

    // Animation methods
    private void startEntryAnimations() {
        // Start with full opacity to simplify debugging
        setOpacity(1.0f);

        // Comment out animations for debugging
        /*
        // Fade in animation
        fadeInTimer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alphaValue += 0.05f;
                if (alphaValue >= 1.0f) {
                    alphaValue = 1.0f;
                    fadeInTimer.stop();
                }
                setOpacity(alphaValue);
            }
        });
        fadeInTimer.start();

        // Slide in animation for content
        slideInTimer = new Timer(15, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                slideOffset -= 5;
                if (slideOffset <= 0) {
                    slideOffset = 0;
                    slideInTimer.stop();
                }

                // This will be handled by our custom container that adjusts the position
                mainContentPanel.repaint();
            }
        });
        slideInTimer.start();
        */
    }



    private void animateButtonClick(JButton button) {
        Color originalColor = button.getBackground();
        button.setBackground(new Color(255, 255, 255));

        // Restore original color after flash effect
        new Timer(100, e -> {
            button.setBackground(originalColor);
            ((Timer) e.getSource()).stop();
        }).start();

        // Scale animation - commented out for now as it may cause layout issues
        /*
        new Timer(20, new ActionListener() {
            private int step = 0;
            private final int totalSteps = 5;
            private final float scaleReduction = 0.05f;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (step >= totalSteps * 2) {
                    ((Timer) e.getSource()).stop();
                    return;
                }

                if (step < totalSteps) {
                    // Scale down
                    button.setSize(
                            (int)(button.getWidth() * (1 - scaleReduction)),
                            (int)(button.getHeight() * (1 - scaleReduction))
                    );
                } else {
                    // Scale up
                    button.setSize(
                            (int)(button.getWidth() * (1 + scaleReduction)),
                            (int)(button.getHeight() * (1 + scaleReduction))
                    );
                }

                step++;
                button.revalidate();
            }
        }).start();
        */
    }

    // Custom Components for modern UI
    private class RoundedPanel extends JPanel {
        private int arcWidth = 0;
        private int arcHeight = 0;
        private int topLeftRadius = 0;
        private int topRightRadius = 0;
        private int bottomLeftRadius = 0;
        private int bottomRightRadius = 0;

        public RoundedPanel(int radius) {
            super();
            this.arcWidth = radius;
            this.arcHeight = radius;
            this.topLeftRadius = radius;
            this.topRightRadius = radius;
            this.bottomLeftRadius = radius;
            this.bottomRightRadius = radius;
            setOpaque(false);
        }

        public RoundedPanel(LayoutManager layout, int radius) {
            super(layout);
            this.arcWidth = radius;
            this.arcHeight = radius;
            this.topLeftRadius = radius;
            this.topRightRadius = radius;
            this.bottomLeftRadius = radius;
            this.bottomRightRadius = radius;
            setOpaque(false);
        }

        public RoundedPanel(LayoutManager layout, int topLeft, int topRight, int bottomLeft, int bottomRight) {
            super(layout);
            this.topLeftRadius = topLeft;
            this.topRightRadius = topRight;
            this.bottomLeftRadius = bottomLeft;
            this.bottomRightRadius = bottomRight;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Create a path with different corner radii
            int width = getWidth();
            int height = getHeight();

            Path2D path = new Path2D.Float();

            // Top left corner
            path.moveTo(topLeftRadius, 0);

            // Top edge and top right corner
            path.lineTo(width - topRightRadius, 0);
            path.quadTo(width, 0, width, topRightRadius);

            // Right edge and bottom right corner
            path.lineTo(width, height - bottomRightRadius);
            path.quadTo(width, height, width - bottomRightRadius, height);

            // Bottom edge and bottom left corner
            path.lineTo(bottomLeftRadius, height);
            path.quadTo(0, height, 0, height - bottomLeftRadius);

            // Left edge and top left corner
            path.lineTo(0, topLeftRadius);
            path.quadTo(0, 0, topLeftRadius, 0);

            path.closePath();

            g2.setColor(getBackground());
            g2.fill(path);
            g2.dispose();
        }
    }

    private class RoundedTextField extends JTextField {
        private int radius;

        public RoundedTextField(int radius) {
            super();
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            super.paintComponent(g);
        }
    }

    private class RoundedButton extends JButton {
        private int radius;

        public RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setContentAreaFilled(false);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isPressed()) {
                g2.setColor(getBackground().darker());
            } else if (getModel().isRollover()) {
                g2.setColor(getBackground().brighter());
            } else {
                g2.setColor(getBackground());
            }

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            super.paintComponent(g);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.dispose();
        }
    }

    // Missing dialog methods
    private void showChangeUsernameDialog() {
        JTextField usernameInput = new RoundedTextField(10);
        usernameInput.setText(currentUser.getUsername());
        styleField(usernameInput);
        usernameInput.setEditable(true);

        int result = JOptionPane.showConfirmDialog(this,
                usernameInput,
                "Change Username",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String newUsername = usernameInput.getText().trim();
            if (!newUsername.isEmpty() && !newUsername.equals(currentUser.getUsername())) {
                // Update username logic here
                currentUser.setUsername(newUsername);
                usernameField.setText(newUsername);
                updateUserDisplay();
            }
        }
    }

    private void showChangeEmailDialog() {
        JTextField emailInput = new RoundedTextField(10);
        emailInput.setText(currentUser.getEmail());
        styleField(emailInput);
        emailInput.setEditable(true);

        int result = JOptionPane.showConfirmDialog(this,
                emailInput,
                "Change Email",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String newEmail = emailInput.getText().trim();
            if (!newEmail.isEmpty() && !newEmail.equals(currentUser.getEmail())) {
                // Update email logic here
                currentUser.setEmail(newEmail);
                emailField.setText(newEmail);
            }
        }
    }
    private JPanel createPasswordField(String labelText, JPasswordField passwordField) {
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(120, 25)); // Adjust as needed

        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.add(label, BorderLayout.WEST);
        panel.add(passwordField, BorderLayout.CENTER);

        return panel;
    }

    private class RoundedPasswordField extends JPasswordField {
        private final int radius;

        public RoundedPasswordField(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Fill background
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

            // Draw text
            super.paintComponent(g);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(100, 70, 160));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
            g2.dispose();
        }
    }

    private void showChangePasswordDialog() {
        JPasswordField currentPass = new RoundedPasswordField(10);
        JPasswordField newPass = new RoundedPasswordField(10);
        JPasswordField confirmPass = new RoundedPasswordField(10);

        styleField(currentPass);
        styleField(newPass);
        styleField(confirmPass);

        JPanel panel = new JPanel(new GridLayout(3, 1, 0, 10));
        panel.add(createPasswordField("Current Password:", currentPass));
        panel.add(createPasswordField("New Password:", newPass));
        panel.add(createPasswordField("Confirm Password:", confirmPass));

        int result = JOptionPane.showConfirmDialog(this,
                panel,
                "Change Password",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            // Password change logic here
        }
    }

    private void showDeleteAccountDialog() {
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete your account?\nThis action cannot be undone.",
                "Delete Account",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            // Account deletion logic here
            dispose();
            // Navigate to login or home page
        }
    }

    private void updateUserDisplay() {
        // Update all UI elements displaying user information
        validate();
        repaint();
    }

    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new ProfilePage().setVisible(true);
        });
    }
}