package com.btbmina.gamestore.classes;

public class GameCategory {
    private int categoryId;
    private String name;
    private String description;

    // Constructeur pour une nouvelle catégorie
    public GameCategory(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Constructeur complet pour chargement depuis la base de données
    public GameCategory(int categoryId, String name, String description) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
    }

    // Getters et Setters
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        GameCategory category = (GameCategory) obj;
        return categoryId == category.categoryId;
    }

    @Override
    public int hashCode() {
        return categoryId;
    }
}

