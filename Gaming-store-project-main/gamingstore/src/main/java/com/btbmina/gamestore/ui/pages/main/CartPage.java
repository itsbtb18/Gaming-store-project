package com.btbmina.gamestore.ui.pages.main;

import javax.swing.*;
import java.awt.*;
import com.btbmina.gamestore.Util.ColorScheme;
import com.btbmina.gamestore.ui.components.PurpleButton;

public class CartPage extends JPanel {
    private JPanel cartItemsPanel;
    private JLabel totalPriceLabel;
    private double totalPrice = 0.0;

    public CartPage() {
        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARK_BACKGROUND);

        createUI();
    }

    private void createUI() {

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ColorScheme.MEDIUM_BACKGROUND);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Shopping Cart");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        cartItemsPanel.setBackground(ColorScheme.DARK_BACKGROUND);

        addCartItem("Game 1", 59.99);
        addCartItem("Game 2", 49.99);
        addCartItem("Game 3", 29.99);

        JScrollPane scrollPane = new JScrollPane(cartItemsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setBackground(ColorScheme.SCROLLBAR_TRACK);
        scrollPane.getVerticalScrollBar().setForeground(ColorScheme.SCROLLBAR_THUMB);

        // Checkout panel
        JPanel checkoutPanel = createCheckoutPanel();

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(checkoutPanel, BorderLayout.SOUTH);
    }

    private void addCartItem(String title, double price) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBackground(ColorScheme.LIGHT_BACKGROUND);
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(ColorScheme.LIGHT_BACKGROUND);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel priceLabel = new JLabel(String.format("$%.2f", price));
        priceLabel.setForeground(ColorScheme.TEXT_SECONDARY);

        infoPanel.add(titleLabel, BorderLayout.WEST);
        infoPanel.add(priceLabel, BorderLayout.EAST);

        PurpleButton removeButton = new PurpleButton("Remove");
        removeButton.addActionListener(e -> removeCartItem(itemPanel, price));

        itemPanel.add(infoPanel, BorderLayout.CENTER);
        itemPanel.add(removeButton, BorderLayout.EAST);

        cartItemsPanel.add(itemPanel);
        cartItemsPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        totalPrice += price;
        updateTotalPrice();
    }

    private void removeCartItem(JPanel itemPanel, double price) {
        int index = -1;
        for (int i = 0; i < cartItemsPanel.getComponentCount(); i++) {
            if (cartItemsPanel.getComponent(i) == itemPanel) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            cartItemsPanel.remove(itemPanel);
            if (index + 1 < cartItemsPanel.getComponentCount()) {
                cartItemsPanel.remove(index); // Remove spacer too
            }
            totalPrice -= price;
            updateTotalPrice();
            cartItemsPanel.revalidate();
            cartItemsPanel.repaint();
        }
    }

    private JPanel createCheckoutPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ColorScheme.PRIMARY_PURPLE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Total price
        totalPriceLabel = new JLabel();
        totalPriceLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalPriceLabel.setForeground(ColorScheme.TEXT_PRIMARY);
        updateTotalPrice();

        PurpleButton checkoutButton = new PurpleButton("Proceed to Checkout");
        checkoutButton.setPreferredSize(new Dimension(200, 40));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(ColorScheme.PRIMARY_PURPLE);
        rightPanel.add(totalPriceLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        rightPanel.add(checkoutButton);

        panel.add(rightPanel, BorderLayout.EAST);
        return panel;
    }

    private void updateTotalPrice() {
        totalPriceLabel.setText(String.format("Total: $%.2f", totalPrice));
    }
}
