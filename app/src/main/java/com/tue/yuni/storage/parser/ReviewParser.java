package com.tue.yuni.storage.parser;

import com.tue.yuni.models.review.CanteenReview;
import com.tue.yuni.models.review.MenuItemReview;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility class to parse reviews from JSON
 */
public class ReviewParser {

    /**
     * Parses a menu item review from JSOn data
     *
     * @param data JSON data
     * @return Menu item review model
     * @throws JSONException If the JSON could not be parsed
     */
    public static MenuItemReview parseMenuItemReview(JSONObject data) throws JSONException {
        return new MenuItemReview(
                data.getInt("id"),
                (float) data.getDouble("rating"),
                data.getString("description"),
                data.getString("created_at"),
                data.getInt("menu_item_id")
        );
    }

    /**
     * Parses a canteen review from JSOn data
     *
     * @param data JSON data
     * @return Canteen review model
     * @throws JSONException If the JSON could not be parsed
     */
    public static CanteenReview parseCanteenReview(JSONObject data) throws JSONException {
        return new CanteenReview(
                data.getInt("id"),
                (float) data.getDouble("rating"),
                data.getString("description"),
                data.getString("created_at"),
                data.getInt("canteen_id")
        );
    }
}
