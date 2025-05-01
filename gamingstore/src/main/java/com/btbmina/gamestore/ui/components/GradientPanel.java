package com.btbmina.gamestore.ui.components;

import javax.swing.*;
import java.awt.*;

public class GradientPanel extends JPanel {
    private final Color startColor = new Color(80, 50, 130);
    private final Color endColor = new Color(30, 20, 60);

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        GradientPaint gp = new GradientPaint(0, 0, startColor, 0, getHeight(), endColor);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();
    }
}
