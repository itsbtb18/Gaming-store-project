package com.btbmina.gamestore.ui.pages.main;

import com.btbmina.gamestore.animation.AnimationManager;
import com.btbmina.gamestore.ui.AppTheme;
import com.btbmina.gamestore.ui.FontManager;
import com.btbmina.gamestore.ui.pages.auth.LoginPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

/**
 * Loading page - first screen shown when the application starts
 * Features animated logo and progress bar
 */
public class LoadingPage extends JFrame {
    private JPanel mainPanel;
    private JLabel logoLabel;
    private JLabel titleLabel;
    private JProgressBar progressBar;
    private Timer loadingTimer;
    private int loadingProgress = 0;

    public LoadingPage() {
        setTitle("BTB-Mina Game Store");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true); // No window decorations for modern look
        setShape(new RoundRectangle2D.Double(0, 0, 900, 600, 15, 15)); // Rounded corners

        initComponents();
        startLoadingAnimation();
    }

    private void initComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(AppTheme.BG_DARK);

        // Center panel for logo and title
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(AppTheme.BG_DARK);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(100, 0, 100, 0));

        // Logo (replace with your actual logo path)
        ImageIcon logoIcon = createLogoIcon();
        logoLabel = new JLabel(logoIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        titleLabel = new JLabel("BTB-MINA GAME STORE");
        titleLabel.setFont(FontManager.getTitle(36));
        titleLabel.setForeground(AppTheme.PRIMARY_PURPLE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Your Ultimate Gaming Destination");
        subtitleLabel.setFont(FontManager.getLight(18));
        subtitleLabel.setForeground(AppTheme.TEXT_LIGHT_GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add to center panel with spacing
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(logoLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(subtitleLabel);
        centerPanel.add(Box.createVerticalGlue());

        // Bottom panel for progress bar
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(AppTheme.BG_DARK);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 50));

        // Custom progress bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setUI(new ModernProgressBarUI());
        progressBar.setStringPainted(false);
        progressBar.setBackground(AppTheme.BG_MEDIUM);
        progressBar.setForeground(AppTheme.PRIMARY_PURPLE);
        progressBar.setPreferredSize(new Dimension(getWidth(), 8));
        progressBar.setBorderPainted(false);

        JLabel loadingLabel = new JLabel("Loading...");
        loadingLabel.setFont(FontManager.getRegular(14));
        loadingLabel.setForeground(AppTheme.TEXT_LIGHT_GRAY);

        bottomPanel.add(loadingLabel, BorderLayout.NORTH);
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.CENTER);
        bottomPanel.add(progressBar, BorderLayout.SOUTH);

        // Add panels to main panel
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);
    }

    private ImageIcon createLogoIcon() {
        // Create a placeholder logo - in a real app, load your actual logo
        int width = 150;
        int height = 150;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Set rendering hints for better quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Draw rounded rectangle as background
        g2d.setColor(AppTheme.PRIMARY_DARK);
        g2d.fill(new RoundRectangle2D.Double(0, 0, width, height, 30, 30));

        // Draw game controller icon
        g2d.setColor(AppTheme.PRIMARY_PURPLE);
        // Draw a simple controller shape
        int padding = 30;
        g2d.fillRoundRect(padding, padding + 20, width - (padding * 2), 40, 20, 20);
        g2d.fillOval(padding + 15, padding + 10, 30, 30);
        g2d.fillOval(width - padding - 45, padding + 10, 30, 30);
        g2d.fillOval(padding + 15, padding + 40, 30, 30);
        g2d.fillOval(width - padding - 45, padding + 40, 30, 30);

        g2d.dispose();

        return new ImageIcon(image);
    }

    private void startLoadingAnimation() {
        // Start a timer to simulate loading progress
        loadingTimer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadingProgress += 1;
                progressBar.setValue(loadingProgress);

                if (loadingProgress >= 100) {
                    loadingTimer.stop();
                    finishLoading();
                }
            }
        });
        loadingTimer.start();
    }

    private void finishLoading() {
        // Transition to login page after brief pause
        Timer transitionTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginPage loginPage = new LoginPage();
                loginPage.setVisible(true);
                dispose(); // Close loading page
            }
        });
        transitionTimer.setRepeats(false);
        transitionTimer.start();
    }

    // Custom UI for modern progress bar
    private static class ModernProgressBarUI extends BasicProgressBarUI {
        @Override
        protected void paintDeterminate(Graphics g, JComponent c) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = progressBar.getWidth();
            int height = progressBar.getHeight();

            // Draw background
            g2d.setColor(progressBar.getBackground());
            g2d.fillRoundRect(0, 0, width, height, height, height);

            // Calculate filled width
            int fillWidth = (int) (progressBar.getPercentComplete() * width);

            // Draw filled progress
            g2d.setColor(progressBar.getForeground());
            g2d.fillRoundRect(0, 0, fillWidth, height, height, height);

            g2d.dispose();
        }
    }

    // For testing the loading page standalone
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            LoadingPage loadingPage = new LoadingPage();
            loadingPage.setVisible(true);
        });
    }
}