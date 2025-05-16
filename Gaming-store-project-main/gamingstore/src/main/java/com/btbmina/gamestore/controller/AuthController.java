package com.btbmina.gamestore.controller;

import com.btbmina.gamestore.service.AuthService;
import com.btbmina.gamestore.classes.User;

public class AuthController {
    private AuthService authService;

    public AuthController() {
        this.authService = new AuthService();
    }

    // Login method should return a User object (not a boolean)
    public User login(String username, String password) {
        // Call the authenticate method from AuthService which should return a User
        return authService.authenticateUser(username, password);
    }

    // Register method (if applicable) should return a boolean to indicate success or failure
    public boolean register(String username, String password, String email) {
        return authService.registerUser(username, password, email);
    }
}
