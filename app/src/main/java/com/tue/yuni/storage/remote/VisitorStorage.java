package com.tue.yuni.storage.remote;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tue.yuni.models.MenuItem;
import com.tue.yuni.models.canteen.Canteen;
import com.tue.yuni.models.review.CanteenReview;
import com.tue.yuni.models.review.MenuItemReview;
import com.tue.yuni.storage.RemoteStorage;
import com.tue.yuni.storage.parser.CanteenParser;
import com.tue.yuni.storage.parser.MenuItemParser;
import com.tue.yuni.storage.parser.ReviewParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VisitorStorage {

    private String baseUrl;
    private RequestQueue queue;

    public VisitorStorage(String baseUrl, RequestQueue queue) {
        this.baseUrl = baseUrl;
        this.queue = queue;
    }

    /**
     * Gets a list of all canteens.
     *
     * @param canteensDataHandler Success handler
     * @param errorHandler        Error handler
     */
    public void getCanteens(final RemoteStorage.CanteensDataHandler canteensDataHandler, final RemoteStorage.ErrorHandler errorHandler) {
        queue.add(
                new JsonArrayRequest(
                        Request.Method.GET,
                        baseUrl + "/canteens",
                        null,
                        response -> {
                            List<Canteen> canteens = new ArrayList<>();
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    canteens.add(CanteenParser.parse(response.getJSONObject(i)));
                                }

                                canteensDataHandler.onReceive(canteens);
                            } catch (JSONException e) {
                                errorHandler.onError(e);
                            }
                        },
                        errorHandler::onError
                )
        );
    }

    /**
     * Gets a specific canteen.
     *
     * @param id           Id
     * @param handler      Success handler
     * @param errorHandler Error handler
     */
    public void getCanteen(int id, RemoteStorage.CanteenDataHandler handler, RemoteStorage.ErrorHandler errorHandler) {
        queue.add(
                new JsonObjectRequest(
                        Request.Method.GET,
                        baseUrl + "/canteens/" + id,
                        null,
                        response -> {
                            try {
                                handler.onReceive(CanteenParser.parse(response));
                            } catch (JSONException e) {
                                errorHandler.onError(e);
                            }
                        },
                        errorHandler::onError
                )
        );
    }

    /**
     * Fetches a list of menu item reviews from storage.
     *
     * @param menuItemId   Menu item id
     * @param handler      Success handler
     * @param errorHandler Error handler
     */
    public void getMenuItemReviews(
            int menuItemId,
            RemoteStorage.MenuItemReviewsDataHandler handler,
            RemoteStorage.ErrorHandler errorHandler
    ) {
        queue.add(
                new JsonArrayRequest(
                        Request.Method.GET,
                        baseUrl + "/menu_items/" + menuItemId + "/reviews",
                        null,
                        response -> {
                            try {
                                List<MenuItemReview> reviews = new ArrayList<>();
                                for (int i = 0; i < response.length(); i++) {
                                    reviews.add(ReviewParser.parseMenuItemReview(response.getJSONObject(i)));
                                }

                                handler.onReceive(reviews);
                            } catch (JSONException e) {
                                errorHandler.onError(e);
                            }
                        },
                        errorHandler::onError
                )
        );
    }

    /**
     * Adds a menu item review to the storage. Note that a review cannot exceed 200 characters.
     *
     * @param menuItemId   Menu item id
     * @param rating       Rating, 1 <= rating <= 5
     * @param description  Description, at most 200 characters
     * @param errorHandler Error handler
     */
    public void createMenuItemReview(
            int menuItemId,
            float rating,
            String description,
            RemoteStorage.RequestCompletedHandler handler,
            RemoteStorage.ErrorHandler errorHandler
    ) {
        // Build post data
        JSONObject data = new JSONObject();
        try {
            data.put("rating", rating);
            data.put("description", description);
        } catch (JSONException e) {
            errorHandler.onError(e);

            return;
        }

        // Send request
        queue.add(
                new JsonObjectRequest(
                        Request.Method.POST,
                        baseUrl + "/menu_items/" + menuItemId + "/reviews",
                        data,
                        response -> handler.onCompleted(),
                        errorHandler::onError
                )
        );
    }

    /**
     * Fetches a list of canteen reviews from storage.
     *
     * @param canteenId    Canteen id
     * @param handler      Success handler
     * @param errorHandler Error handler
     */
    public void getCanteenReviews(
            int canteenId,
            RemoteStorage.CanteenReviewsDataHandler handler,
            RemoteStorage.ErrorHandler errorHandler
    ) {
        queue.add(
                new JsonArrayRequest(
                        Request.Method.GET,
                        baseUrl + "/canteens/" + canteenId + "/reviews",
                        null,
                        response -> {
                            try {
                                List<CanteenReview> reviews = new ArrayList<>();
                                for (int i = 0; i < response.length(); i++) {
                                    reviews.add(ReviewParser.parseCanteenReview(response.getJSONObject(i)));
                                }

                                handler.onReceive(reviews);
                            } catch (JSONException e) {
                                errorHandler.onError(e);
                            }
                        },
                        errorHandler::onError
                )
        );
    }

    /**
     * Adds a canteen review to the storage. Note that a review cannot exceed 200 characters.
     *
     * @param canteenId    Canteen id
     * @param rating       Rating, 1 <= rating <= 5
     * @param description  Description, at most 200 characters
     * @param errorHandler Error handler
     */
    public void createCanteenReview(
            int canteenId,
            float rating,
            String description,
            RemoteStorage.RequestCompletedHandler handler,
            RemoteStorage.ErrorHandler errorHandler
    ) {
        // Build post data
        JSONObject data = new JSONObject();
        try {
            data.put("rating", rating);
            data.put("description", description);
        } catch (JSONException e) {
            errorHandler.onError(e);

            return;
        }

        // Send request
        queue.add(
                new JsonObjectRequest(
                        Request.Method.POST,
                        baseUrl + "/canteens/" + canteenId + "/reviews",
                        data,
                        response -> handler.onCompleted(),
                        errorHandler::onError
                )
        );
    }

    /**
     * Fetches a list of all available menu items.
     *
     * @param handler      Success handler
     * @param errorHandler Error handler
     */
    public void getAllMenuItems(RemoteStorage.MenuItemsDataHandler handler, RemoteStorage.ErrorHandler errorHandler) {
        queue.add(
                new JsonArrayRequest(
                        Request.Method.GET,
                        baseUrl + "/menu/all",
                        null,
                        response -> {
                            try {
                                List<MenuItem> menuItems = new ArrayList<>();
                                for (int i = 0; i < response.length(); i++) {
                                    menuItems.add(MenuItemParser.parse(response.getJSONObject(i)));
                                }

                                handler.onReceive(menuItems);
                            } catch (JSONException e) {
                                errorHandler.onError(e);
                            }
                        },
                        errorHandler::onError
                )
        );
    }

    /**
     * Adds a new entry to the busyness database for a specific canteen
     *
     * @param canteenId    Canteen id
     * @param handler      Success handler
     * @param errorHandler Error handler
     */
    public void createBusynessEntry(
            int canteenId,
            RemoteStorage.RequestCompletedHandler handler,
            RemoteStorage.ErrorHandler errorHandler
    ) {
        queue.add(
                new JsonObjectRequest(
                        Request.Method.POST,
                        baseUrl + "/canteens/" + canteenId + "/busyness",
                        null,
                        response -> handler.onCompleted(),
                        errorHandler::onError
                )
        );
    }
}
