package com.btbmina.gamestore.ui.components;

import com.btbmina.gamestore.DB.GameDB;
import com.btbmina.gamestore.Util.ColorScheme;
import com.btbmina.gamestore.classes.Game;
import com.btbmina.gamestore.ui.FontManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class SearchBar extends JTextField {
    private JPopupMenu searchResults;
    private Timer searchTimer;
    private boolean isSearching = false;
    private JButton viewAllButton; // Button to view all search results

    public SearchBar() {
        setupAppearance();
        setupSearchFunctionality();
    }

    private void setupAppearance() {
        setPreferredSize(new Dimension(300, 35));
        setFont(FontManager.getRegular(14));
        // Plus claire pour meilleure visibilité du texte
        setForeground(ColorScheme.TEXT_PRIMARY);
        // Arrière-plan plus clair pour meilleure visibilité
        setBackground(new Color(150, 150, 180));
        setCaretColor(ColorScheme.TEXT_PRIMARY);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.ACCENT_COLOR, 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        // Placeholder text
        setPlaceholder("Search games...");
    }

    private void setupSearchFunctionality() {
        // Create popup menu for search results that appears below the search field
        searchResults = new JPopupMenu();
        // Couleur de fond plus claire pour le menu
        searchResults.setBackground(new Color(120, 120, 180));
        searchResults.setBorder(BorderFactory.createLineBorder(ColorScheme.ACCENT_COLOR, 1));

        // Important: Set the light weight popup property to false
        // This helps with popup visibility in some Swing parent containers
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);

        // Create search timer to avoid excessive database queries
        searchTimer = new Timer(300, e -> performSearch());
        searchTimer.setRepeats(false);

        // Add document listener for text changes
        getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { startSearch(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { startSearch(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { startSearch(); }
        });

        // Add key listener to handle Enter key and navigation
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performSearch();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN && searchResults.isVisible() && searchResults.getComponentCount() > 0) {
                    // Get the first menu item that is enabled
                    for (int i = 0; i < searchResults.getComponentCount(); i++) {
                        Component comp = searchResults.getComponent(i);
                        if (comp instanceof JMenuItem && comp.isEnabled()) {
                            ((JMenuItem) comp).doClick();
                            return;
                        }
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE && searchResults.isVisible()) {
                    // Close the popup on escape
                    searchResults.setVisible(false);
                }
            }
        });

        // Use AWTEventListener to better track clicks outside the popup
        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
            if (event instanceof MouseEvent && event.getID() == MouseEvent.MOUSE_PRESSED) {
                MouseEvent mouseEvent = (MouseEvent) event;
                // Check if click was outside both the search field and popup
                if (!SwingUtilities.isDescendingFrom(mouseEvent.getComponent(), this) &&
                        !SwingUtilities.isDescendingFrom(mouseEvent.getComponent(), searchResults)) {
                    searchResults.setVisible(false);
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK);

        // Make sure the popup appears in the right location when the search bar moves
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                updatePopupPosition();
            }

            @Override
            public void componentResized(ComponentEvent e) {
                updatePopupPosition();
            }
        });
    }

    private void updatePopupPosition() {
        if (searchResults.isVisible()) {
            try {
                Point p = getLocationOnScreen();
                searchResults.setLocation(p.x, p.y + getHeight());
            } catch (IllegalComponentStateException ex) {
                // Component may not be on screen yet
                System.err.println("Could not update popup position: " + ex.getMessage());
            }
        }
    }

    private boolean isPopupClicked() {
        try {
            Point mousePosition = MouseInfo.getPointerInfo().getLocation();
            Point popupLocation = searchResults.getLocationOnScreen();
            Rectangle popupBounds = new Rectangle(
                    popupLocation.x,
                    popupLocation.y,
                    searchResults.getWidth(),
                    searchResults.getHeight()
            );
            return popupBounds.contains(mousePosition);
        } catch (Exception e) {
            System.err.println("Error checking popup click: " + e.getMessage());
            return false;
        }
    }

    private void setPlaceholder(String placeholder) {
        setText(placeholder);
        setForeground(ColorScheme.TEXT_SECONDARY);

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals(placeholder)) {
                    setText("");
                    setForeground(ColorScheme.TEXT_PRIMARY);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty() && !searchResults.isVisible()) {
                    setText(placeholder);
                    setForeground(ColorScheme.TEXT_SECONDARY);
                }
            }
        });
    }

    private void startSearch() {
        // Don't search if the text is the placeholder
        if (getText().equals("Search games...")) {
            return;
        }

        // Restart the timer to reduce database calls
        if (searchTimer.isRunning()) {
            searchTimer.restart();
        } else {
            searchTimer.start();
        }
    }

    private void performSearch() {
        String searchText = getText().trim();

        // Don't search if empty or placeholder
        if (searchText.isEmpty() || searchText.equals("Search games...")) {
            searchResults.setVisible(false);
            return;
        }

        // Set flag to indicate we're searching
        isSearching = true;

        try {
            // Debug output
            System.out.println("Searching for: " + searchText);

            // Execute search query
            List<Game> games = GameDB.searchGames(searchText);

            // Debug output
            System.out.println("Found " + games.size() + " games");

            // Display results
            showSearchResults(games);
        } catch (Exception e) {
            System.err.println("Error searching games: " + e.getMessage());
            e.printStackTrace();

            // Show error in results popup
            searchResults.removeAll();
            JMenuItem errorItem = new JMenuItem("Erreur lors de la recherche");
            errorItem.setFont(FontManager.getRegular(14));
            errorItem.setForeground(new Color(255, 100, 100)); // Rouge plus visible
            errorItem.setBackground(new Color(70, 45, 120));
            errorItem.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
            searchResults.add(errorItem);
            showPopup();
        } finally {
            isSearching = false;
        }
    }

    private void showSearchResults(List<Game> games) {
        // Clear existing items
        searchResults.removeAll();

        if (games.isEmpty()) {
            // Add "No results" item
            JMenuItem noResults = new JMenuItem("Aucun jeu trouvé");
            noResults.setEnabled(false);
            noResults.setFont(FontManager.getRegular(14));
            noResults.setForeground(Color.WHITE); // Texte BLANC comme demandé
            noResults.setBackground(new Color(70, 45, 120));
            noResults.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
            searchResults.add(noResults);
        } else {
            // Add each game as a menu item
            for (Game game : games) {
                JMenuItem item = createResultItem(game);
                searchResults.add(item);
            }

            // Add "View All Results" button
            addViewAllButton(games.size());
        }

        // Show the popup
        showPopup();
    }

    private void addViewAllButton(int resultCount) {
        // Create a panel to hold the button (for better styling)
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(new Color(45, 45, 45)); // Noir claire comme demandé

        // Create the "View All" button
        viewAllButton = new JButton("Voir tous les résultats (" + resultCount + ")");
        viewAllButton.setFont(FontManager.getRegular(14));
        viewAllButton.setForeground(Color.WHITE);
        viewAllButton.setBackground(new Color(45, 45, 45)); // Noir claire comme demandé
        viewAllButton.setBorderPainted(false);
        viewAllButton.setFocusPainted(false);
        viewAllButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        viewAllButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                viewAllButton.setBackground(new Color(60, 60, 60)); // Slightly lighter on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                viewAllButton.setBackground(new Color(45, 45, 45));
            }
        });

        // Action when clicking the button
        viewAllButton.addActionListener(e -> {
            searchResults.setVisible(false);
            // TODO: Navigate to full search results page
            System.out.println("Navigating to full search results for: " + getText());
            // mainFrame.showAllSearchResults(getText());
        });

        buttonPanel.add(viewAllButton, BorderLayout.CENTER);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        // Add to popup as a menu component (not a menu item)
        searchResults.add(buttonPanel);
    }

    private void showPopup() {
        try {
            // Make sure we're on the EDT
            SwingUtilities.invokeLater(() -> {
                try {
                    // Calculate the popup size - width matches search field, height depends on items
                    int itemCount = Math.max(1, searchResults.getComponentCount());
                    int popupHeight = Math.min(itemCount * 40 + 10, 300);
                    searchResults.setPreferredSize(new Dimension(getWidth(), popupHeight));

                    // Ensure the popup is visible and in the right position
                    Point p = getLocationOnScreen();
                    searchResults.setLocation(p.x, p.y + getHeight());
                    searchResults.setVisible(true);

                    // Force component to validate and repaint
                    searchResults.validate();
                    searchResults.repaint();

                    // Request focus back to search field
                    SwingUtilities.invokeLater(this::requestFocusInWindow);
                } catch (Exception e) {
                    System.err.println("Error showing popup from EDT: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            System.err.println("Error showing popup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private JMenuItem createResultItem(Game game) {
        JMenuItem item = new JMenuItem(game.getTitle()) {
            // Override to prevent focus issues
            @Override
            public void requestFocus() {
                // Intentionally empty to prevent focus stealing
            }
        };

        item.setFont(FontManager.getRegular(14)); // Police plus grande pour plus de lisibilité
        item.setForeground(Color.WHITE); // Texte BLANC comme demandé
        item.setBackground(new Color(70, 45, 120)); // Fond plus clair
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(100, 70, 160)), // Séparateur subtil
                BorderFactory.createEmptyBorder(10, 12, 10, 12) // Rembourrage augmenté
        ));
        item.setFocusPainted(false);

        // Action when clicking on a game
        item.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                // Hide the popup
                searchResults.setVisible(false);

                // Set search field text to selected game and restore focus
                setText(game.getTitle());
                requestFocusInWindow();

                // Navigate to game details page (implement this in your application)
                System.out.println("Selected game: " + game.getTitle() + ", ID: " + game.getId());

                // TODO: Replace with your navigation code
                // For example: mainFrame.showGamePage(game.getId());
            });
        });

        // Hover effect - couleur plus vive pour le survol
        item.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                item.setBackground(ColorScheme.SECONDARY_PURPLE);
            }
            public void mouseExited(MouseEvent e) {
                item.setBackground(new Color(70, 45, 120));
            }
        });

        return item;
    }

    // Method to directly show the search results popup (for testing)
    public void showSearchPopup() {
        performSearch();
    }

    // Method to check if database connection is working
    public static void testSearchFunction(String query) {
        try {
            List<Game> games = GameDB.searchGames(query);
            System.out.println("Test search for '" + query + "' found " + games.size() + " games:");
            for (Game game : games) {
                System.out.println(" - " + game.getTitle() + " (ID: " + game.getId() + ")");
            }
        } catch (Exception e) {
            System.err.println("Test search failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}