package com.btbmina.gamestore.ui.dialogs;

import com.btbmina.gamestore.classes.Game;
import com.btbmina.gamestore.service.PurchaseService;
import javax.swing.*;
import java.awt.*;

public class PurchaseDialog extends JDialog {
    private final Game game;
    private final int userId;
    private final PurchaseService purchaseService;

    public PurchaseDialog(Frame parent, Game game, int userId) {
        super(parent, "Purchase Game", true);
        this.game = game;
        this.userId = userId;
        this.purchaseService = new PurchaseService();

        setupUI();
        pack();
        setLocationRelativeTo(parent);
    }

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Game: " + game.getTitle());
        JLabel priceLabel = new JLabel(String.format("Price: %.2f €", game.getPrice()));

        JButton confirmButton = new JButton("Confirm Purchase");
        JButton cancelButton = new JButton("Cancel");

        confirmButton.addActionListener(e -> processPurchase());
        cancelButton.addActionListener(e -> dispose());

        mainPanel.add(titleLabel);
        mainPanel.add(priceLabel);
        mainPanel.add(confirmButton);
        mainPanel.add(cancelButton);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void processPurchase() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Do you want to proceed with the purchase?",
                "Confirm Purchase",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            if (purchaseService.processPurchase(userId, game.getId(), game.getPrice())) {
                JOptionPane.showMessageDialog(this,
                        "Purchase successful! The game has been added to your library.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Purchase failed. Please try again later.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
