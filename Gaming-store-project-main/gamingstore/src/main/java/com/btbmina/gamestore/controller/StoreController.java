package com.btbmina.gamestore.controller;

import com.btbmina.gamestore.classes.Game;
import com.btbmina.gamestore.classes.User;
import com.btbmina.gamestore.service.GameService;
import com.btbmina.gamestore.service.LibraryService;

import javax.swing.*;
import java.util.List;

public class StoreController {
    private final GameService gameService;
    private final LibraryService libraryService;
    private final User currentUser;
    private final DefaultListModel<String> gameListModel;
    private List<Game> games;

    public StoreController(GameService gameService, LibraryService libraryService, User currentUser, DefaultListModel<String> gameListModel) {
        this.gameService = gameService;
        this.libraryService = libraryService;
        this.currentUser = currentUser;
        this.gameListModel = gameListModel;
        loadGames();
    }

    public void loadGames() {
        games = gameService.getAllGames();
        gameListModel.clear();
        for (Game game : games) {
            gameListModel.addElement(game.getTitle() + " | " + game.getPrice() + " DA");
        }
    }

    public void handleGameSelection(int selectedIndex) {
        if (selectedIndex < 0 || selectedIndex >= games.size()) {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner un jeu.");
            return;
        }

        Game selectedGame = games.get(selectedIndex);

        boolean alreadyOwned = libraryService.isGameInLibrary(currentUser.getUserId(), selectedGame.getId());

        if (alreadyOwned) {
            JOptionPane.showMessageDialog(null, "Ce jeu est déjà dans votre bibliothèque.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null,
                "Voulez-vous acheter ce jeu ?\n" + selectedGame.getTitle() + " - " + selectedGame.getPrice() + " DA",
                "Confirmer l'achat",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = libraryService.addGameToLibrary(currentUser.getUserId(), selectedGame);
            if (success) {
                JOptionPane.showMessageDialog(null, "Jeu ajouté à votre bibliothèque !");
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout du jeu.");
            }
        }
    }
}
