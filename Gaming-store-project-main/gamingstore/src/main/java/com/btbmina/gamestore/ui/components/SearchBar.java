package com.btbmina.gamestore.ui.components;
import com.btbmina.gamestore.DB.UserDB;
import com.btbmina.gamestore.classes.User;
import com.btbmina.gamestore.ui.pages.main.GamePage;
import com.btbmina.gamestore.DB.GameDB;
import com.btbmina.gamestore.Util.ColorScheme;
import com.btbmina.gamestore.classes.Game;
import com.btbmina.gamestore.ui.FontManager;
import com.btbmina.gamestore.ui.pages.main.GamePage;

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

        setForeground(ColorScheme.TEXT_PRIMARY);

        setBackground(new Color(150, 150, 180));
        setCaretColor(ColorScheme.TEXT_PRIMARY);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorScheme.ACCENT_COLOR, 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        setPlaceholder("Search games...");
    }

    private void setupSearchFunctionality() {

        searchResults = new JPopupMenu();

        searchResults.setBackground(new Color(120, 120, 180));
        searchResults.setBorder(BorderFactory.createLineBorder(ColorScheme.ACCENT_COLOR, 1));

        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);

        searchTimer = new Timer(300, e -> performSearch());
        searchTimer.setRepeats(false);


        getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { startSearch(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { startSearch(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { startSearch(); }
        });

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

        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
            if (event instanceof MouseEvent && event.getID() == MouseEvent.MOUSE_PRESSED) {
                MouseEvent mouseEvent = (MouseEvent) event;
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

        if (getText().equals("Search games...")) {
            return;
        }

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

        isSearching = true;

        try {

            System.out.println("Searching for: " + searchText);


            List<Game> games = GameDB.searchGames(searchText);


            System.out.println("Found " + games.size() + " games");

            showSearchResults(games);
        } catch (Exception e) {
            System.err.println("Error searching games: " + e.getMessage());
            e.printStackTrace();

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

            JMenuItem noResults = new JMenuItem("Aucun jeu trouvé");
            noResults.setEnabled(false);
            noResults.setFont(FontManager.getRegular(14));
            noResults.setForeground(Color.WHITE); // Texte BLANC comme demandé
            noResults.setBackground(new Color(70, 45, 120));
            noResults.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
            searchResults.add(noResults);
        } else {

            for (Game game : games) {
                JMenuItem item = createResultItem(game);
                searchResults.add(item);
            }

            addViewAllButton(games.size());
        }

        showPopup();
    }

    private void addViewAllButton(int resultCount) {

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(new Color(45, 45, 45)); // Noir claire comme demandé

        viewAllButton = new JButton("Voir tous les résultats (" + resultCount + ")");
        viewAllButton.setFont(FontManager.getRegular(14));
        viewAllButton.setForeground(Color.WHITE);
        viewAllButton.setBackground(new Color(45, 45, 45)); // Noir claire comme demandé
        viewAllButton.setBorderPainted(false);
        viewAllButton.setFocusPainted(false);
        viewAllButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

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

        viewAllButton.addActionListener(e -> {
            searchResults.setVisible(false);

            System.out.println("Navigating to full search results for: " + getText());

        });

        buttonPanel.add(viewAllButton, BorderLayout.CENTER);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        searchResults.add(buttonPanel);
    }

    private void showPopup() {
        try {

            SwingUtilities.invokeLater(() -> {
                try {

                    int itemCount = Math.max(1, searchResults.getComponentCount());
                    int popupHeight = Math.min(itemCount * 40 + 10, 300);
                    searchResults.setPreferredSize(new Dimension(getWidth(), popupHeight));

                    Point p = getLocationOnScreen();
                    searchResults.setLocation(p.x, p.y + getHeight());
                    searchResults.setVisible(true);

                    searchResults.validate();
                    searchResults.repaint();


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
            @Override
            public void requestFocus() {
                // Prevent focus stealing
            }
        };


        item.setFont(FontManager.getRegular(14));
        item.setForeground(Color.BLACK);
        item.setBackground(new Color(200, 200, 200));
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        item.setFocusPainted(false);


        item.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                try {

                    searchResults.setVisible(false);


                    setText(game.getTitle());


                    Window window = SwingUtilities.getWindowAncestor(SearchBar.this);
                    if (window instanceof JFrame) {
                        ((JFrame) window).dispose();


                        User currentUser = getCurrentUser();
                        if (currentUser == null) {
                            throw new IllegalStateException("No user logged in");
                        }

                        GamePage gamePage = new GamePage(game, currentUser);
                        gamePage.setUndecorated(true);


                        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                        GraphicsDevice gd = ge.getDefaultScreenDevice();
                        if (gd.isFullScreenSupported()) {
                            gd.setFullScreenWindow(gamePage);
                        } else {
                            gamePage.setExtendedState(JFrame.MAXIMIZED_BOTH);
                            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                            gamePage.setSize(screenSize.width, screenSize.height);
                            gamePage.setLocationRelativeTo(null);
                        }

                        gamePage.setVisible(true);
                    }
                } catch (Exception ex) {
                    System.err.println("Error navigating to game: " + ex.getMessage());
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            SwingUtilities.getWindowAncestor(SearchBar.this),
                            "Error loading game page: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            });
        });


        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                item.setBackground(new Color(220, 220, 220));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                item.setBackground(new Color(200, 200, 200));
            }
        });

        return item;
    }

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
            private User getCurrentUser() {
                try {
                    return UserDB.getCurrentUser();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
}