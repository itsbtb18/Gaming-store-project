package com.btbmina.gamestore.controller;

import com.btbmina.gamestore.classes.Game;
import com.btbmina.gamestore.service.GameService;

import java.util.List;

public class GameController {
    private final GameService gameService;

    public GameController() {
        this.gameService = new GameService();
    }

    // Ajouter un jeu
    public boolean addGame(Game game) {
        return gameService.addGame(game);
    }

    // Mettre à jour un jeu
    public boolean updateGame(Game game) {
        return gameService.updateGame(game);
    }

    // Supprimer un jeu
    public boolean deleteGame(int gameId) {
        return gameService.deleteGame(gameId);
    }

    // Obtenir un jeu par ID
    public Game getGameById(int gameId) {
        return gameService.getGameById(gameId);
    }

    // Obtenir tous les jeux
    public List<Game> getAllGames() {
        return gameService.getAllGames();
    }
}
