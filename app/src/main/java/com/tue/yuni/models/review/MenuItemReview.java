package com.tue.yuni.models.review;

public class MenuItemReview extends Review {

    private final int menuItemId;

    public MenuItemReview(int id, int rating, String description, String createdAt, int menuItemId) {
        super(id, rating, description, createdAt);

        this.menuItemId = menuItemId;
    }

    public int getMenuItemId() {
        return menuItemId;
    }
}
