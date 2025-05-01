package com.btbmina.gamestore.ui.components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class ModernScrollPane extends JScrollPane {
    public ModernScrollPane(Component view) {
        super(view);
        getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(100, 60, 160);
                this.trackColor = new Color(30, 20, 50);
            }
        });
        setBorder(BorderFactory.createEmptyBorder());
    }
}
