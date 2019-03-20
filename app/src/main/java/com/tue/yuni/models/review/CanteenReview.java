package com.tue.yuni.models.review;

public class CanteenReview extends Review {

    private final int canteenId;

    public CanteenReview(int id, int rating, String description, String createdAt, int canteenId) {
        super(id, rating, description, createdAt);

        this.canteenId = canteenId;
    }

    public int getCanteenId() {
        return canteenId;
    }
}
