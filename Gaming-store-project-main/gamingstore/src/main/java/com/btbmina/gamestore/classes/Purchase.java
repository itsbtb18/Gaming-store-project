package com.btbmina.gamestore.model;

import java.time.LocalDateTime;

public class Purchase {
    private int id;
    private int userId;
    private int gameId;
    private double price;
    private LocalDateTime purchaseDate;
    private String paymentMethod;
    private String transactionId;

    public Purchase(int userId, int gameId, double price, String transactionId) {
        this.userId = userId;
        this.gameId = gameId;
        this.price = price;
        this.purchaseDate = LocalDateTime.now();
        this.transactionId = transactionId;
    }

    public Purchase(int id, int userId, int gameId, LocalDateTime purchaseDate, double price, String paymentMethod, String transactionId) {
        this.id = id;
        this.userId = userId;
        this.gameId = gameId;
        this.purchaseDate = purchaseDate;
        this.price = price;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public int getGameId() { return gameId; }
    public double getPrice() { return price; }
    public LocalDateTime getPurchaseDate() { return purchaseDate; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getTransactionId() { return transactionId; }
}
