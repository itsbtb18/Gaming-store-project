package com.btbmina.gamestore.classes;

import java.util.ArrayList;
import java.util.List;
public class Cart {
    private int userId;
    private List<Game> games;

    public Cart(int userId) {
        this.userId = userId;
        this.games = new ArrayList<>();
    }

    public int getUserId() {
        return userId;
    }

    public List<Game> getGames() {
        return games;
    }

    public void addGame(Game game) {
        if (!games.contains(game)) {
            games.add(game);
        }
    }

    public void removeGame(Game game) {
        games.remove(game);
    }

    public void clearCart() {
        games.clear();
    }

    public double getTotalPrice() {
        return games.stream().mapToDouble(Game::getPrice).sum();
    }

    @Override
    public String toString() {
        return "Cart{userId=" + userId + ", games=" + games + '}';
    }
}

