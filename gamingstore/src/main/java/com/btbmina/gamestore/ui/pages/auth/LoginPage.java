package ui;

import utils.ColorScheme;
import components.PurpleButton;
import javax.swing.*;
import java.awt.*;

public class StorePanel extends JPanel {
    public StorePanel() {
        setLayout(new BorderLayout());
        setBackground(ColorScheme.BACKGROUND);
        
        // Add search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(ColorScheme.DARK_PURPLE);
        JTextField searchField = new JTextField(30);
        searchPanel.add(searchField);
        searchPanel.add(new PurpleButton("Search"));
        
        // Add game grid
        JPanel gamesGrid = new JPanel(new GridLayout(0, 3, 10, 10));
        gamesGrid.setBackground(ColorScheme.BACKGROUND);
        
        // Add sample games (you would load these from database)
        for (int i = 0; i < 9; i++) {
            gamesGrid.add(createGameCard("Game " + (i + 1)));
        }
        
        JScrollPane scrollPane = new JScrollPane(gamesGrid);
        scrollPane.setBorder(null);
        
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createGameCard(String title) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(ColorScheme.MEDIUM_PURPLE);
        
        // Add game image
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(200, 300));
        card.add(imageLabel, BorderLayout.CENTER);
        
        // Add game title and price
        JPanel info = new JPanel(new BorderLayout());
        info.setBackground(ColorScheme.DARK_PURPLE);
        info.add(new JLabel(title), BorderLayout.CENTER);
        info.add(new PurpleButton("$59.99"), BorderLayout.EAST);
        
        card.add(info, BorderLayout.SOUTH);
        return card;
    }
}