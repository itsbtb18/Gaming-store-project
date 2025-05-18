package com.btbmina.gamestore.controller;

import com.btbmina.gamestore.classes.Game;
import com.btbmina.gamestore.service.LibraryService;

import java.util.List;

public class LibraryController {
    private LibraryService libraryService;

    public LibraryController() {
        this.libraryService = new LibraryService();
    }

    public boolean addGameToLibrary(int userId, Game game) {
        return libraryService.addGameToLibrary(userId, game);
    }


    public boolean removeGameFromLibrary(int userId, int gameId) {
        return libraryService.removeGameFromLibrary(userId, gameId);
    }

    public List<Game> getUserLibrary(int userId) {
        return libraryService.getGamesInLibrary(userId);
    }


    public boolean isGameInLibrary(int userId, int gameId) {
        return libraryService.isGameInLibrary(userId, gameId);
    }
}
