
package com.btbmina.gamestore.classes;

import java.time.LocalDateTime;

public class Rating {
    private int ratingId;
    private int userId;
    private int gameId;
    private double score; // entre 0 et 5 par exemple
    private String comment;
    private LocalDateTime date;

    public Rating(int ratingId, int userId, int gameId, double score, String comment, LocalDateTime date) {
        this.ratingId = ratingId;
        this.userId = userId;
        this.gameId = gameId;
        this.score = score;
        this.comment = comment;
        this.date = date;
    }

    public Rating(int userId, int gameId, double score, String comment) {
        this(0, userId, gameId, score, comment, LocalDateTime.now());
    }

    // Getters et Setters
    public int getRatingId() {
        return ratingId;
    }

    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        if (score < 0 || score > 5) {
            throw new IllegalArgumentException("La note doit être entre 0 et 5.");
        }
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "ratingId=" + ratingId +
                ", userId=" + userId +
                ", gameId=" + gameId +
                ", score=" + score +
                ", comment='" + comment + '\'' +
                ", date=" + date +
                '}';
    }
}
