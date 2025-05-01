package com.btbmina.gamestore.ui.components;

import javax.swing.*;
import java.awt.*;

public class GameCard extends JPanel {
    private final JLabel imageLabel;
    private final JLabel titleLabel;
    private final JLabel priceLabel;

    public GameCard(String title, String price, ImageIcon image) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(180, 260));
        setBackground(new Color(35, 25, 65));
        setBorder(BorderFactory.createLineBorder(new Color(90, 60, 150), 2));

        imageLabel = new JLabel(image);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        priceLabel = new JLabel(price);
        priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        priceLabel.setForeground(new Color(200, 160, 255));
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(priceLabel);

        add(imageLabel, BorderLayout.CENTER);
        add(textPanel, BorderLayout.SOUTH);
    }
}