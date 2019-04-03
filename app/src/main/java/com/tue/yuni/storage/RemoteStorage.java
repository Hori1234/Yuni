package com.tue.yuni.storage;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tue.yuni.models.Availability;
import com.tue.yuni.models.MenuItem;
import com.tue.yuni.models.Schedule;
import com.tue.yuni.models.canteen.Canteen;
import com.tue.yuni.models.canteen.OperatingTimes;
import com.tue.yuni.models.review.CanteenReview;
import com.tue.yuni.models.review.MenuItemReview;
import com.tue.yuni.storage.parser.CanteenParser;
import com.tue.yuni.storage.parser.MenuItemParser;
import com.tue.yuni.storage.parser.ReviewParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoteStorage {

    /**
     * Singleton instance object
     */
    private static RemoteStorage instance;
    /**
     * Base url for API
     */
    private final String BASE_URL = "http://116.203.117.175/api";
    /**
     * Volley request queue
     */
    private RequestQueue queue;

    private RemoteStorage(Context context) {
        this.queue = Volley.newRequestQueue(context);
    }

    /**
     * Singleton access point.
     *
     * @return RemoteStorage
     */
    public static RemoteStorage get() {
        if (instance == null) {
            throw new IllegalStateException("Cannot provide uninitialised object without context");
        }

        return instance;
    }

    /**
     * Initialises the remote storage. Must happen before class can be used.
     *
     * @param context Context
     */
    public static void initialise(Context context) {
        instance = new RemoteStorage(context);
    }

    /**
     * Gets a list of all canteens.
     *
     * @param canteensDataHandler Success handler
     * @param errorHandler        Error handler
     */
    public void getCanteens(final CanteensDataHandler canteensDataHandler, final ErrorHandler errorHandler) {
        queue.add(
                new JsonArrayRequest(
                        Request.Method.GET,
                        BASE_URL + "/canteens",
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
    public void getCanteen(int id, CanteenDataHandler handler, ErrorHandler errorHandler) {
        queue.add(
                new JsonObjectRequest(
                        Request.Method.GET,
                        BASE_URL + "/canteens/" + id,
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
            MenuItemReviewsDataHandler handler,
            ErrorHandler errorHandler
    ) {
        queue.add(
                new JsonArrayRequest(
                        Request.Method.GET,
                        BASE_URL + "/menu_items/" + menuItemId + "/reviews",
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
            RequestCompletedHandler handler,
            ErrorHandler errorHandler
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
                        BASE_URL + "/menu_items/" + menuItemId + "/reviews",
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
            CanteenReviewsDataHandler handler,
            ErrorHandler errorHandler
    ) {
        queue.add(
                new JsonArrayRequest(
                        Request.Method.GET,
                        BASE_URL + "/canteens/" + canteenId + "/reviews",
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
            RequestCompletedHandler handler,
            ErrorHandler errorHandler
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
                        BASE_URL + "/canteens/" + canteenId + "/reviews",
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
    public void getAllMenuItems(MenuItemsDataHandler handler, ErrorHandler errorHandler) {
        queue.add(
                new JsonArrayRequest(
                        Request.Method.GET,
                        BASE_URL + "/menu/all",
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
            RequestCompletedHandler handler,
            ErrorHandler errorHandler
    ) {
        queue.add(
                new JsonObjectRequest(
                        Request.Method.POST,
                        BASE_URL + "/canteens/" + canteenId + "/busyness",
                        null,
                        response -> handler.onCompleted(),
                        errorHandler::onError
                )
        );
    }

    /**
     * @param password            Password
     * @param authenticateHandler Authentication handler
     * @param errorHandler        Error handler
     */
    public void authenticate(
            String password,
            AuthenticateHandler authenticateHandler,
            ErrorHandler errorHandler
    ) {
        authenticatedObjectRequest(
                Request.Method.GET,
                BASE_URL + "/authenticate",
                null,
                response -> {
                    try {
                        authenticateHandler.onReceive(response.getBoolean("authenticated"));
                    } catch (JSONException e) {
                        errorHandler.onError(e);
                    }
                },
                errorHandler::onError,
                password
        );
    }

    /**
     * Creates a new menu item in the database.
     *
     * @param password     Owner password
     * @param name         Name of the menu item
     * @param description  Description of the menu item
     * @param category     Category of the menu item
     * @param handler      Success handler
     * @param errorHandler Error handler
     */
    public void createMenuItem(
            String password,
            String name,
            String description,
            String category,
            RequestCompletedHandler handler,
            ErrorHandler errorHandler
    ) {
        // Build data
        JSONObject data = new JSONObject();
        try {
            data.put("name", name);
            data.put("description", description);
            data.put("category", category.toUpperCase());
        } catch (JSONException e) {
            errorHandler.onError(e);

            return;
        }

        // Perform request
        authenticatedObjectRequest(
                Request.Method.POST,
                BASE_URL + "/menu_items",
                data,
                response -> handler.onCompleted(),
                errorHandler::onError,
                password
        );
    }

    /**
     * Updates a menu item.
     *
     * @param password     Owner password
     * @param menuItemId   Menu item id
     * @param name         New name of the menu item
     * @param description  New description of the menu item
     * @param category     New category of the menu item
     * @param handler      Success handler
     * @param errorHandler Error handler
     */
    public void updateMenuItem(
            String password,
            int menuItemId,
            String name,
            String description,
            String category,
            RequestCompletedHandler handler,
            ErrorHandler errorHandler
    ) {
        // Build data
        JSONObject data = new JSONObject();
        try {
            data.put("name", name);
            data.put("description", description);
            data.put("category", category.toUpperCase());
        } catch (JSONException e) {
            errorHandler.onError(e);

            return;
        }

        // Perform request
        authenticatedObjectRequest(
                Request.Method.PATCH,
                BASE_URL + "/menu_items/" + menuItemId,
                data,
                response -> handler.onCompleted(),
                errorHandler::onError,
                password
        );
    }

    /**
     * Adds a menu item to a menu of a canteen.
     *
     * @param canteenId    Canteen id
     * @param menuItemId   Menu item id
     * @param schedule     Schedule
     * @param password     Owner password
     * @param handler      Success handler
     * @param errorHandler Error handler
     */
    public void addItemToMenu(
            int canteenId,
            int menuItemId,
            Schedule schedule,
            String password,
            RequestCompletedHandler handler,
            ErrorHandler errorHandler
    ) {
        // Build post data
        JSONObject data = new JSONObject();
        try {
            data.put("canteen_id", canteenId);
            data.put("menu_item_id", menuItemId);
            data.put("schedule", schedule.toBitmask());
        } catch (JSONException e) {
            errorHandler.onError(e);

            return;
        }

        // Perform request
        authenticatedObjectRequest(
                Request.Method.POST,
                BASE_URL + "/menu",
                data,
                response -> handler.onCompleted(),
                errorHandler::onError,
                password
        );
    }

    /**
     * Removes an item from a canteens menu. Note that this method needs the menu id instead of the
     * menu item id.
     *
     * @param menuId       Menu id
     * @param password     Owner password
     * @param handler      Success handler
     * @param errorHandler Error handler
     */
    public void removeItemFromMenu(
            int menuId,
            String password,
            RequestCompletedHandler handler,
            ErrorHandler errorHandler
    ) {
        authenticatedObjectRequest(
                Request.Method.DELETE,
                BASE_URL + "/menu/" + menuId,
                null,
                response -> handler.onCompleted(),
                errorHandler::onError,
                password
        );
    }

    public void updateCanteen(
            String password,
            int canteenId,
            String name,
            String description,
            OperatingTimes operatingTimes,
            RequestCompletedHandler handler,
            ErrorHandler errorHandler
    ) {
        // Build data
        JSONObject data = new JSONObject();
        try {
            data.put("name", name);
            data.put("description", description);
            data.put("operating_times", operatingTimes.toJson());
        } catch (JSONException e) {
            errorHandler.onError(e);

            return;
        }

        // Perform request
        authenticatedObjectRequest(
                Request.Method.PATCH,
                BASE_URL + "/canteens/" + canteenId,
                data,
                response -> handler.onCompleted(),
                errorHandler::onError,
                password
        );
    }

    /**
     * Update the schedule of a menu item.
     *
     * @param password     Owner password
     * @param menuId       Menu id (note, not menu item id)
     * @param schedule     Schedule
     * @param handler      Success handler
     * @param errorHandler Error handler
     */
    public void updateMenuItemSchedule(
            String password,
            int menuId,
            Schedule schedule,
            RequestCompletedHandler handler,
            ErrorHandler errorHandler
    ) {
        // Build patch data
        JSONObject data = new JSONObject();
        try {
            data.put("schedule", schedule.toBitmask());
        } catch (JSONException e) {
            errorHandler.onError(e);
        }

        // Send request
        authenticatedObjectRequest(
                Request.Method.PATCH,
                BASE_URL + "/menu/" + menuId + "/schedule",
                data,
                response -> handler.onCompleted(),
                errorHandler::onError,
                password
        );
    }

    /**
     * Update the availability of a menu item.
     *
     * @param password     Canteen owner password
     * @param menuId       Menu id (not menu item id)
     * @param availability Availability
     * @param handler      Success handler
     * @param errorHandler Error handler
     */
    public void updateMenuItemAvailability(
            String password,
            int menuId,
            Availability availability,
            RequestCompletedHandler handler,
            ErrorHandler errorHandler
    ) {
        // Build data
        JSONObject data = new JSONObject();
        try {
            data.put("availability", availability.name());
        } catch (JSONException e) {
            errorHandler.onError(e);

            return;
        }

        // Perform request
        authenticatedObjectRequest(
                Request.Method.PATCH,
                BASE_URL + "/menu/" + menuId + "/availability",
                data,
                response -> handler.onCompleted(),
                errorHandler::onError,
                password
        );
    }

    /**
     * Performs an authenticated request by setting the X-api-key header in the request.
     *
     * @param method        HTTP method
     * @param url           Url
     * @param data          Post data
     * @param listener      Success listener
     * @param errorListener Error listener
     * @param password      Password (api-key)
     */
    private void authenticatedObjectRequest(
            int method,
            String url,
            JSONObject data,
            Response.Listener<JSONObject> listener,
            Response.ErrorListener errorListener,
            String password
    ) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, url, data, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("X-api-key", password);

                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }

    public interface ErrorHandler {
        public void onError(Exception e);
    }

    public interface RequestCompletedHandler {
        public void onCompleted();
    }

    public interface CanteensDataHandler {
        public void onReceive(List<Canteen> canteens);
    }

    public interface AuthenticateHandler {
        public void onReceive(boolean authenticated);
    }

    public interface CanteenDataHandler {
        public void onReceive(Canteen canteen);
    }

    public interface MenuItemReviewsDataHandler {
        public void onReceive(List<MenuItemReview> reviews);
    }

    public interface CanteenReviewsDataHandler {
        public void onReceive(List<CanteenReview> reviews);
    }

    public interface MenuItemsDataHandler {
        public void onReceive(List<MenuItem> menuItems);
    }
}
