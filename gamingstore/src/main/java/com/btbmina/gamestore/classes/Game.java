package com.btbmina.gamestore.classes;

/**
 * Représente un jeu dans le système avec ses détails et exigences système
 */
public class Game {
    private int id;
    private String title;
    private String description;
    private double price;
    private String category;
    private double rating;
    private String systemRequirements; // Ajouté

    public Game(int id, String title, String description, double price, String category, double rating, String systemRequirements) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.rating = rating;
        this.systemRequirements = systemRequirements;
    }

    public Game(String title, String description, double price, String category, double rating, String systemRequirements) {
        this(0, title, description, price, category, rating, systemRequirements);
    }

    public Game(int gameId, String gameName, double gamePrice) {
        this.id = gameId;
        this.title = gameName;
        this.price = gamePrice;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getRating() { return rating; }
    public void setRating(double rating) {
        if (rating < 0.0 || rating > 5.0)
            throw new IllegalArgumentException("La note doit être entre 0.0 et 5.0");
        this.rating = rating;
    }

    public String getSystemRequirements() {
        return systemRequirements;
    }

    public void setSystemRequirements(String systemRequirements) {
        this.systemRequirements = systemRequirements;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", rating=" + rating +
                ", systemRequirements='" + systemRequirements + '\'' +
                '}';
    }
}
