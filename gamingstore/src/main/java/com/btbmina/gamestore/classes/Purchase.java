package com.btbmina.gamestore.classes;
import java.sql.Timestamp;
import java.time.LocalDateTime;
    public class Purchase {
        private int purchaseId;
        private int userId;
        private int gameId;
        private LocalDateTime purchaseDate;
        private double price;
        private String paymentMethod;

        private User user;
        private Game game;

        public Purchase(int userId, int gameId, double price, String paymentMethod) {
            this.userId = userId;
            this.gameId = gameId;
            this.price = price;
            this.paymentMethod = paymentMethod;
            this.purchaseDate = LocalDateTime.now();
        }

        public Purchase(int purchaseId, int userId, int gameId, LocalDateTime purchaseDate,
                        double price, String paymentMethod) {
            this.purchaseId = purchaseId;
            this.userId = userId;
            this.gameId = gameId;
            this.purchaseDate = purchaseDate;
            this.price = price;
            this.paymentMethod = paymentMethod;
        }

        // Getters / Setters
        public int getPurchaseId() { return purchaseId; }
        public void setPurchaseId(int purchaseId) { this.purchaseId = purchaseId; }

        public int getUserId() { return userId; }
        public void setUserId(int userId) { this.userId = userId; }

        public int getGameId() { return gameId; }
        public void setGameId(int gameId) { this.gameId = gameId; }

        public LocalDateTime getPurchaseDate() { return purchaseDate; }
        public void setPurchaseDate(LocalDateTime purchaseDate) { this.purchaseDate = purchaseDate; }

        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }

        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

        public User getUser() { return user; }
        public void setUser(User user) {
            this.user = user;
            this.userId = user.getUserId();
        }

        public Game getGame() { return game; }
        public void setGame(Game game) {
            this.game = game;
            this.gameId = game.getId();
        }

        @Override
        public String toString() {
            return "Purchase{" +
                    "purchaseId=" + purchaseId +
                    ", userId=" + userId +
                    ", gameId=" + gameId +
                    ", purchaseDate=" + purchaseDate +
                    ", price=" + price +
                    ", paymentMethod='" + paymentMethod + '\'' +
                    '}';
        }
    }