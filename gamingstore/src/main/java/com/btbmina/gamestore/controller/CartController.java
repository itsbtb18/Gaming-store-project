package com.btbmina.gamestore.controller;

import com.btbmina.gamestore.service.CartService;
import com.btbmina.gamestore.classes.Cart;
import com.btbmina.gamestore.classes.Game;

public class CartController {
    private final CartService cartService;

    public CartController() {
        this.cartService = new CartService();
    }

    // Ajouter un jeu au panier de l'utilisateur
    public boolean addToCart(int userId, Game game) {
        return cartService.addGameToCart(userId, game);
    }

    // Supprimer un jeu du panier de l'utilisateur
    public boolean removeFromCart(int userId, Game game) {
        return cartService.removeGameFromCart(userId, game);
    }

    // Récupérer le panier d'un utilisateur
    public Cart getCart(int userId) {
        return cartService.getCartForUser(userId);
    }

    // Vider le panier d'un utilisateur
    public boolean clearCart(int userId) {
        return cartService.clearCart(userId);
    }

    // Obtenir le prix total du panier
    public double getTotalPrice(int userId) {
        return cartService.getTotalPrice(userId);
    }
}
