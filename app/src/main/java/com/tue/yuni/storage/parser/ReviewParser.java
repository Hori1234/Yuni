package com.tue.yuni.storage.parser;

import com.tue.yuni.models.review.CanteenReview;
import com.tue.yuni.models.review.MenuItemReview;

import org.json.JSONException;
import org.json.JSONObject;

public class ReviewParser {

    public static MenuItemReview parseMenuItemReview(JSONObject data) throws JSONException {
        return new MenuItemReview(
                data.getInt("id"),
                (float)data.getDouble("rating"),
                data.getString("description"),
                data.getString("created_at"),
                data.getInt("menu_item_id")
        );
    }

    public static CanteenReview parseCanteenReview(JSONObject data) throws JSONException {
        return new CanteenReview(
                data.getInt("id"),
                (float)data.getDouble("rating"),
                data.getString("description"),
                data.getString("created_at"),
                data.getInt("canteen_id")
        );
    }
}
