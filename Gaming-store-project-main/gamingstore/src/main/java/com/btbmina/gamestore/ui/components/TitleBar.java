package com.btbmina.gamestore.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TitleBar extends JPanel {
    private final JFrame parent;
    private Point initialClick;

    public TitleBar(JFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(new Color(30, 10, 60));
        setPreferredSize(new Dimension(getWidth(), 30));

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonsPanel.setOpaque(false);

        // Minimize button
        JButton minimizeBtn = createButton("─");
        minimizeBtn.addActionListener(e -> parent.setState(Frame.ICONIFIED));

        // Close button
        JButton closeBtn = createButton("×");
        closeBtn.addActionListener(e -> System.exit(0));

        buttonsPanel.add(minimizeBtn);
        buttonsPanel.add(closeBtn);
        add(buttonsPanel, BorderLayout.EAST);

        // Add window dragging functionality
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                int thisX = parent.getLocation().x;
                int thisY = parent.getLocation().y;

                int xMoved = (thisX + e.getX()) - (thisX + initialClick.x);
                int yMoved = (thisY + e.getY()) - (thisY + initialClick.y);

                parent.setLocation(thisX + xMoved, thisY + yMoved);
            }
        });
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(30, 10, 60));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(text.equals("×") ? new Color(232, 17, 35) : new Color(50, 20, 90));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(30, 10, 60));
            }
        });

        return button;
    }
}