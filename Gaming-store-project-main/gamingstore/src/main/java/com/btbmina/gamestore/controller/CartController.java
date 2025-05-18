package com.btbmina.gamestore.controller;

import com.btbmina.gamestore.service.CartService;
import com.btbmina.gamestore.classes.Cart;
import com.btbmina.gamestore.classes.Game;

public class CartController {
    private final CartService cartService;

    public CartController() {
        this.cartService = new CartService();
    }


    public boolean addToCart(int userId, Game game) {
        return cartService.addGameToCart(userId, game);
    }

    public boolean removeFromCart(int userId, Game game) {
        return cartService.removeGameFromCart(userId, game);
    }

    public Cart getCart(int userId) {
        return cartService.getCartForUser(userId);
    }

    public boolean clearCart(int userId) {
        return cartService.clearCart(userId);
    }

    public double getTotalPrice(int userId) {
        return cartService.getTotalPrice(userId);
    }
}
