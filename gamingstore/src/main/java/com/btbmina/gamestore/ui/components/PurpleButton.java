package com.btbmina.gamestore.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PurpleButton extends JButton {

    private Color baseColor = new Color(138, 43, 226); // Violet
    private Color hoverColor = baseColor.brighter();
    private Color clickColor = baseColor.darker();
    private Color currentColor = baseColor;

    public PurpleButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 16));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                currentColor = hoverColor;
                repaint();
            }

            public void mouseExited(MouseEvent e) {
                currentColor = baseColor;
                repaint();
            }

            public void mousePressed(MouseEvent e) {
                currentColor = clickColor;
                repaint();
            }

            public void mouseReleased(MouseEvent e) {
                currentColor = hoverColor;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(currentColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        super.paintComponent(g);
    }
}
