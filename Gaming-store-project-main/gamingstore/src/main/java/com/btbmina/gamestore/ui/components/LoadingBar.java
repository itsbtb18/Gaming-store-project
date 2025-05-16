package com.btbmina.gamestore.ui.components;

import javax.swing.*;
import java.awt.*;

public class LoadingBar extends JProgressBar {
    public LoadingBar() {
        setIndeterminate(true);
        setForeground(new Color(180, 130, 255));
        setBackground(new Color(40, 30, 70));
        setBorder(BorderFactory.createEmptyBorder());
    }
}