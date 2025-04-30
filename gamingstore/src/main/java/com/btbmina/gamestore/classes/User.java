package com.btbmina.gamestore.classes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {
    private int userId;
    private String username;
    private String email;
    private String passwordHash;
    private LocalDateTime registrationDate;
    private boolean isVerified;
    private String verificationToken;
    private byte[] profileImage;
    private LocalDateTime lastLogin;

    // Liste des jeux achetés par l'utilisateur (chargée à la demande)
    private List<Game> ownedGames = new ArrayList<>();

    // Constructeur pour un nouvel utilisateur
    public User(int userId, String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.registrationDate = LocalDateTime.now();
        this.isVerified = false;
    }
    public User(int userId, String username, String email, String passwordHash,
                LocalDateTime registrationDate, boolean isVerified,
                String verificationToken, byte[] profileImage, LocalDateTime lastLogin) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.registrationDate = registrationDate;
        this.isVerified = isVerified;
        this.verificationToken = verificationToken;
        this.profileImage = profileImage;
        this.lastLogin = lastLogin;
    }

    // Getters et Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public List<Game> getOwnedGames() {
        return ownedGames;
    }

    public void setOwnedGames(List<Game> ownedGames) {
        this.ownedGames = ownedGames;
    }

    public void addOwnedGame(Game game) {
        if (!ownedGames.contains(game)) {
            ownedGames.add(game);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", isVerified=" + isVerified +
                '}';
    }
}