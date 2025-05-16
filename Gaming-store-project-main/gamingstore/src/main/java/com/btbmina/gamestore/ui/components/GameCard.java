package com.btbmina.gamestore.ui.components;
import com.btbmina.gamestore.ui.dialogs.PurchaseDialog;
import com.btbmina.gamestore.classes.Game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameCard extends JPanel {
    private final JLabel imageLabel;
    private final JLabel titleLabel;
    private final JLabel priceLabel;
    private final Game game;
    private final int userId;

    public GameCard(Game game, int userId, ImageIcon image) {
        this.game = game;
        this.userId = userId;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(180, 260));
        setBackground(new Color(35, 25, 65));
        setBorder(BorderFactory.createLineBorder(new Color(90, 60, 150), 2));

        imageLabel = new JLabel(image);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        titleLabel = new JLabel(game.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        priceLabel = new JLabel(String.format("%.2f €", game.getPrice()));
        priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        priceLabel.setForeground(new Color(200, 160, 255));
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel textPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(priceLabel);

        // Add purchase button
        JButton buyButton = new JButton("Buy Now");
        buyButton.setBackground(new Color(90, 60, 150));
        buyButton.setForeground(Color.WHITE);
        buyButton.setFocusPainted(false);
        buyButton.addActionListener(e -> handleGamePurchase());
        textPanel.add(buyButton);

        add(imageLabel, BorderLayout.CENTER);
        add(textPanel, BorderLayout.SOUTH);

        // Add hover effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(new Color(120, 90, 180), 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(new Color(90, 60, 150), 2));
            }
        });
    }

    private void handleGamePurchase() {
        Window window = SwingUtilities.getWindowAncestor(this);
        Frame parent = window instanceof Frame ? (Frame) window : null;

        PurchaseDialog purchaseDialog = new PurchaseDialog(parent, game, userId);
        purchaseDialog.setVisible(true);
    }

    // Add getters if needed
    public Game getGame() {
        return game;
    }
}