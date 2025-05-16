package com.btbmina.gamestore.ui.components;

import com.btbmina.gamestore.DB.GameDB;
import com.btbmina.gamestore.classes.Game;
import com.btbmina.gamestore.ui.FontManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class SearchBar extends JTextField {
    private JPopupMenu searchResults;
    private Timer searchTimer;

    public SearchBar() {
        setupAppearance();
        setupSearchFunctionality();
    }

    private void setupAppearance() {
        setPreferredSize(new Dimension(300, 35));
        setFont(FontManager.getRegular(14));
        setForeground(Color.WHITE);
        setBackground(new Color(60, 40, 90));
        setCaretColor(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 70, 160), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // Placeholder text
        setPlaceholder("Search games...");
    }

    private void setupSearchFunctionality() {
        searchResults = new JPopupMenu();
        searchResults.setBackground(new Color(60, 40, 90));
        searchResults.setBorder(BorderFactory.createLineBorder(new Color(100, 70, 160), 1));

        // Create search timer to avoid excessive database queries
        searchTimer = new Timer(300, e -> performSearch());
        searchTimer.setRepeats(false);

        // Add document listener for text changes
        getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { startSearch(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { startSearch(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { startSearch(); }
        });

        // Hide results when focus is lost
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                Timer timer = new Timer(200, event -> searchResults.setVisible(false));
                timer.setRepeats(false);
                timer.start();
            }
        });
    }

    private void setPlaceholder(String placeholder) {
        setText(placeholder);
        setForeground(new Color(180, 180, 180));

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals(placeholder)) {
                    setText("");
                    setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(placeholder);
                    setForeground(new Color(180, 180, 180));
                }
            }
        });
    }

    private void startSearch() {
        if (searchTimer.isRunning()) {
            searchTimer.restart();
        } else {
            searchTimer.start();
        }
    }

    private void performSearch() {
        String searchText = getText().trim();
        if (searchText.isEmpty() || searchText.equals("Search games...")) {
            searchResults.setVisible(false);
            return;
        }

        try {
            List<Game> games = GameDB.searchGames(searchText);
            showSearchResults(games);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSearchResults(List<Game> games) {
        searchResults.removeAll();

        if (games.isEmpty()) {
            JMenuItem noResults = new JMenuItem("No games found");
            noResults.setEnabled(false);
            noResults.setForeground(Color.GRAY);
            searchResults.add(noResults);
        } else {
            for (Game game : games) {
                JMenuItem item = createResultItem(game);
                searchResults.add(item);
            }
        }

        searchResults.show(this, 0, getHeight());
        searchResults.setPopupSize(getWidth(), Math.min(games.size() * 40 + 10, 200));
    }

    private JMenuItem createResultItem(Game game) {
        JMenuItem item = new JMenuItem(game.getTitle());
        item.setFont(FontManager.getRegular(13));
        item.setForeground(Color.WHITE);
        item.setBackground(new Color(60, 40, 90));
        item.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        item.addActionListener(e -> {
            // TODO: Navigate to game details page
            System.out.println("Selected game: " + game.getTitle());
        });

        // Hover effect
        item.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                item.setBackground(new Color(80, 55, 120));
            }
            public void mouseExited(MouseEvent e) {
                item.setBackground(new Color(60, 40, 90));
            }
        });

        return item;
    }
}