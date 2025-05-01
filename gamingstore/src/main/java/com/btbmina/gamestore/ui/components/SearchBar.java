package com.btbmina.gamestore.ui.components;

import javax.swing.*;
import java.awt.*;

public class SearchBar extends JTextField {
    public SearchBar() {
        setPreferredSize(new Dimension(200, 30));
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setForeground(Color.WHITE);
        setBackground(new Color(60, 40, 90));
        setCaretColor(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 70, 160), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    }
}
