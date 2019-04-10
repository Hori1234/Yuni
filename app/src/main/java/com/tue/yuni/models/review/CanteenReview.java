package com.tue.yuni.models.review;

/**
 * Canteen review model
 */
public class CanteenReview extends Review {

    /**
     * Canteen id
     */
    private final int canteenId;

    /**
     * @param id          ID
     * @param rating      Rating
     * @param description Description
     * @param createdAt   Created at
     * @param canteenId   Canteen id
     */
    public CanteenReview(int id, float rating, String description, String createdAt, int canteenId) {
        super(id, rating, description, createdAt);

        this.canteenId = canteenId;
    }
}
