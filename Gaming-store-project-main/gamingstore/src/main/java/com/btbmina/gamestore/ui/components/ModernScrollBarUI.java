package com.btbmina.gamestore.ui.components;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import javax.swing.*;

public class ModernScrollBarUI extends BasicScrollBarUI {
    @Override
    protected void configureScrollBarColors() {
        this.thumbColor = new Color(100, 60, 160);  // Purple thumb
        this.trackColor = new Color(30, 20, 50);    // Dark track
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    private JButton createZeroButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }
}
