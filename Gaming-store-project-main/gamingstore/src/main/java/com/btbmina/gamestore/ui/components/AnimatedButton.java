package com.btbmina.gamestore.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AnimatedButton extends JButton {
    private float scale = 1.0f;
    private final Timer animationTimer;
    private boolean hovering = false;

    public AnimatedButton(String text) {
        super(text);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setBackground(new Color(120, 90, 170));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        animationTimer = new Timer(15, e -> {
            float target = hovering ? 1.05f : 1.0f;
            if (Math.abs(scale - target) > 0.01f) {
                scale += (target - scale) * 0.1f;
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovering = true;
                animationTimer.start();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovering = false;
                animationTimer.start();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        g2d.scale(scale, scale);
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, w, h, 30, 30);
        super.paintComponent(g2d);
        g2d.dispose();
    }
}
