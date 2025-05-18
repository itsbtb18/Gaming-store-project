package com.btbmina.gamestore.ui;

import com.btbmina.gamestore.ui.pages.main.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AppUIManager {
    private static JFrame mainFrame;
    private static CardLayout cardLayout;
    private static JPanel mainPanel;
    private static final Color DARK_PURPLE = new Color(48, 25, 52);
    private static final Color MEDIUM_PURPLE = new Color(87, 54, 163);
    private static final Color LIGHT_PURPLE = new Color(157, 78, 221);

    public static void initialize() {
        try {

            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mainFrame = new JFrame("Gaming Store");
        mainFrame.setSize(1280, 720);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        setupUI();
        mainFrame.setVisible(true);
    }

    private static void setupUI() {

        JPanel sidebar = createSidebar();

        mainPanel.add(new HomePage(), "HOME");
        mainPanel.add(new LibraryPage(), "LIBRARY");
        mainPanel.add(new ProfilePage(), "PROFILE");
        mainPanel.add(new CartPage(), "CART");

        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(sidebar, BorderLayout.WEST);
        mainFrame.add(mainPanel, BorderLayout.CENTER);
    }

    private static JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(DARK_PURPLE);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("Gaming Store");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebar.add(logo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        addNavButton(sidebar, "Home", "HOME");
        addNavButton(sidebar, "Store", "STORE");
        addNavButton(sidebar, "Library", "LIBRARY");
        addNavButton(sidebar, "Profile", "PROFILE");
        addNavButton(sidebar, "Cart", "CART");

        return sidebar;
    }

    private static void addNavButton(JPanel sidebar, String text, String cardName) {
        JPanel button = createAnimatedButton(text);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, cardName);
            }
        });
        sidebar.add(button);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private static JPanel createAnimatedButton(String text) {
        JPanel button = new JPanel();
        button.setMaximumSize(new Dimension(180, 40));
        button.setBackground(MEDIUM_PURPLE);
        button.setLayout(new BorderLayout());

        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.add(label, BorderLayout.CENTER);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(LIGHT_PURPLE);
                animateButton(button, 5);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(MEDIUM_PURPLE);
                animateButton(button, -5);
            }
        });

        return button;
    }

    private static void animateButton(JPanel button, int offset) {
        Timer timer = new Timer(50, null);
        final int[] frame = {0};

        timer.addActionListener(e -> {
            frame[0]++;
            if (frame[0] <= 5) {
                button.setBorder(BorderFactory.createEmptyBorder(0, offset, 0, 0));
                button.revalidate();
            } else {
                timer.stop();
            }
        });

        timer.start();
    }

    public static void showPage(String pageName) {
        cardLayout.show(mainPanel, pageName);
    }

    public static JFrame getMainFrame() {
        return mainFrame;
    }
}
