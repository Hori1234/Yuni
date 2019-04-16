package com.tue.yuni.models.review;

/**
 * Menu item review model
 */
public class MenuItemReview extends Review {

    /**
     * Menu item id
     */
    private final int menuItemId;

    /**
     * @param id          ID
     * @param rating      Rating
     * @param description Description
     * @param createdAt   Created at
     * @param menuItemId  Menu item id
     */
    public MenuItemReview(int id, float rating, String description, String createdAt, int menuItemId) {
        super(id, rating, description, createdAt);

        this.menuItemId = menuItemId;
    }
}
