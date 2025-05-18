package com.btbmina.gamestore.controller;

import com.btbmina.gamestore.classes.Game;
import com.btbmina.gamestore.service.GameService;

import java.util.List;

public class GameController {
    private final GameService gameService;

    public GameController() {
        this.gameService = new GameService();
    }

    public boolean addGame(Game game) {
        return gameService.addGame(game);
    }

    public boolean updateGame(Game game) {
        return gameService.updateGame(game);
    }

    public boolean deleteGame(int gameId) {
        return gameService.deleteGame(gameId);
    }

    public Game getGameById(int gameId) {
        return gameService.getGameById(gameId);
    }

    public List<Game> getAllGames() {
        return gameService.getAllGames();
    }
}
