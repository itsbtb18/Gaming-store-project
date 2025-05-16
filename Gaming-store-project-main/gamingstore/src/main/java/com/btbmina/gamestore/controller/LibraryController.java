package com.btbmina.gamestore.controller;

import com.btbmina.gamestore.classes.Game;
import com.btbmina.gamestore.service.LibraryService;

import java.util.List;

public class LibraryController {
    private LibraryService libraryService;

    public LibraryController() {
        this.libraryService = new LibraryService();
    }

    // Ajouter un jeu à la bibliothèque d’un utilisateur
    public boolean addGameToLibrary(int userId, Game game) {
        return libraryService.addGameToLibrary(userId, game);
    }

    // Supprimer un jeu de la bibliothèque d’un utilisateur
    public boolean removeGameFromLibrary(int userId, int gameId) {
        return libraryService.removeGameFromLibrary(userId, gameId);
    }

    // Obtenir tous les jeux de la bibliothèque d’un utilisateur
    public List<Game> getUserLibrary(int userId) {
        return libraryService.getGamesInLibrary(userId);
    }

    // Vérifier si un jeu est déjà présent dans la bibliothèque de l’utilisateur
    public boolean isGameInLibrary(int userId, int gameId) {
        return libraryService.isGameInLibrary(userId, gameId);
    }
}
