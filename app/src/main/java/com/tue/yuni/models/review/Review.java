package com.tue.yuni.models.review;

public class Review {

    protected final int id;
    protected final float rating;
    protected final String description;
    protected final String createdAt;

    /**
     * @param id          Id
     * @param rating      Rating
     * @param description Description
     * @param createdAt   Created at
     */
    public Review(int id, float rating, String description, String createdAt) {
        this.id = id;
        this.rating = rating;
        this.description = description;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public float getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
