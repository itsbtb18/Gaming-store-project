package com.btbmina.gamestore.ui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * A custom, modern title bar for the game store application
 * with a dark purple theme and smooth interactions.
 */
public class TitleBar extends JPanel {
    // Theme colors
    private static final Color BACKGROUND_COLOR = new Color(38, 14, 64);
    private static final Color HOVER_COLOR = new Color(58, 24, 94);
    private static final Color CLOSE_HOVER_COLOR = new Color(232, 17, 35);
    private static final Color TEXT_COLOR = new Color(230, 230, 250);
    private static final Color ICON_COLOR = new Color(190, 147, 255);

    private final JFrame parent;
    private Point initialClick;
    private final int height = 40; // Taller title bar for modern look
    private final Font iconFont = new Font("Segoe UI Symbol", Font.PLAIN, 14);
    private final Font titleFont = new Font("Segoe UI", Font.BOLD, 14);


    private final String appTitle = "GameStore";
    private final JLabel appIcon;
    private final JLabel titleLabel;

    public TitleBar(JFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setPreferredSize(new Dimension(getWidth(), height));
        setBorder(new EmptyBorder(0, 15, 0, 0));


        appIcon = new JLabel("🎮");
        appIcon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));
        appIcon.setForeground(ICON_COLOR);

        titleLabel = new JLabel(appTitle);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(TEXT_COLOR);


        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(appIcon);
        leftPanel.add(titleLabel);
        add(leftPanel, BorderLayout.WEST);

        JPanel buttonsPanel = createButtonsPanel();
        add(buttonsPanel, BorderLayout.EAST);

        setupWindowDragging();
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonsPanel.setOpaque(false);

        buttonsPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Create maximize/restore button (optional)
        JButton maximizeBtn = createButton("🗖");
        maximizeBtn.addActionListener(e -> {
            if ((parent.getExtendedState() & Frame.MAXIMIZED_BOTH) != 0) {
                parent.setExtendedState(Frame.NORMAL);
                maximizeBtn.setText("🗖");
            } else {
                parent.setExtendedState(Frame.MAXIMIZED_BOTH);
                maximizeBtn.setText("🗗");
            }
        });

        JButton minimizeBtn = createButton("🗕");
        minimizeBtn.addActionListener(e -> parent.setState(Frame.ICONIFIED));

        JButton closeBtn = createButton("✕");
        closeBtn.addActionListener(e -> System.exit(0));

        buttonsPanel.add(minimizeBtn);
        buttonsPanel.add(maximizeBtn);
        buttonsPanel.add(closeBtn);

        return buttonsPanel;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(TEXT_COLOR);
        button.setBackground(BACKGROUND_COLOR);
        button.setFont(iconFont);

        button.setPreferredSize(new Dimension(46, height));

        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setContentAreaFilled(true);
                button.setBackground(text.equals("✕") ? CLOSE_HOVER_COLOR : HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setContentAreaFilled(false);
                button.setBackground(BACKGROUND_COLOR);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(text.equals("✕") ?
                        new Color(190, 0, 20) : new Color(48, 14, 84));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(text.equals("✕") ? CLOSE_HOVER_COLOR : HOVER_COLOR);
            }
        });

        return button;
    }

    private void setupWindowDragging() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                initialClick = null;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (initialClick != null) {
                    int thisX = parent.getLocation().x;
                    int thisY = parent.getLocation().y;

                    int xMoved = e.getX() - initialClick.x;
                    int yMoved = e.getY() - initialClick.y;

                    parent.setLocation(thisX + xMoved, thisY + yMoved);
                }
            }
        });
    }

    public void applyRoundedCorners(int radius) {
        if (parent.isUndecorated()) {
            parent.setShape(new RoundRectangle2D.Double(0, 0,
                    parent.getWidth(), parent.getHeight(), radius, radius));
        }
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();


        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        GradientPaint gradient = new GradientPaint(
                0, 0, BACKGROUND_COLOR,
                0, height, new Color(33, 9, 58)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw a subtle separator line at the bottom
        g2d.setColor(new Color(60, 30, 90));
        g2d.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);

        g2d.dispose();
    }
}