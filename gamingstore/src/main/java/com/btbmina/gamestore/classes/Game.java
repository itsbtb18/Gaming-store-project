package com.btbmina.gamestore.classes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un jeu dans le système avec ses détails et exigences système
 */
public class Game {
    private int gameId;
    private String title;
    private String description;
    private double price;
    private LocalDate releaseDate;
    private String developer;
    private String publisher;
    private byte[] coverImage;
    private String trailerUrl;

    // Propriétés liées qui seront chargées au besoin
    private SystemRequirements systemRequirement;
    private List<GameCategory> categories = new ArrayList<>();
    private List<Rating> ratings = new ArrayList<>();

    // Score moyen calculé à partir des ratings
    private double averageRating;

    // Constructeur pour un nouveau jeu
    public Game(int title, String description, String price, double releaseDate,
                double developer, String publisher) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.releaseDate = releaseDate;
        this.developer = developer;
        this.publisher = publisher;
    }

    // Constructeur complet pour le chargement depuis la base de données
    public Game(int gameId, String title, String description, double price,
                LocalDate releaseDate, String developer, String publisher,
                byte[] coverImage, String trailerUrl) {
        this.gameId = gameId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.releaseDate = releaseDate;
        this.developer = developer;
        this.publisher = publisher;
        this.coverImage = coverImage;
        this.trailerUrl = trailerUrl;
    }

    // Getters et Setters
    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public byte[] getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(byte[] coverImage) {
        this.coverImage = coverImage;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public SystemRequirements getSystemRequirement() {
        return systemRequirement;
    }

    public void setSystemRequirement(SystemRequirements systemRequirement) {
        this.systemRequirement = systemRequirement;
    }

    public List<GameCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<GameCategory> categories) {
        this.categories = categories;
    }

    public void addCategory(GameCategory category) {
        if (!categories.contains(category)) {
            categories.add(category);
        }
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
        calculateAverageRating();
    }

    public void addRating(Rating rating) {
        if (!ratings.contains(rating)) {
            ratings.add(rating);
            calculateAverageRating();
        }
    }

    public double getAverageRating() {
        return averageRating;
    }

    // Calcule la note moyenne à partir de la liste des évaluations
    private void calculateAverageRating() {
        if (ratings.isEmpty()) {
            this.averageRating = 0;
            return;
        }

        double sum = 0;
        for (Rating rating : ratings) {
            sum += rating.getScore();
        }
        this.averageRating = sum / ratings.size();
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameId=" + gameId +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", developer='" + developer + '\'' +
                ", publisher='" + publisher + '\'' +
                ", avgRating=" + averageRating +
                '}';
    }
}