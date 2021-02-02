package com.tue.yuni.storage.remote;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tue.yuni.models.Availability;
import com.tue.yuni.models.Schedule;
import com.tue.yuni.models.canteen.OperatingTimes;
import com.tue.yuni.storage.RemoteStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OwnerStorage {
    private String baseUrl;
    private RequestQueue queue;

    public OwnerStorage(String baseUrl, RequestQueue queue) {
        this.baseUrl = baseUrl;
        this.queue = queue;
    }

    /**
     * @param password            Password
     * @param authenticateHandler Authentication handler
     * @param errorHandler        Error handler
     */
    public void authenticate(
            String password,
            RemoteStorage.AuthenticateHandler authenticateHandler,
            RemoteStorage.ErrorHandler errorHandler
    ) {
        authenticatedObjectRequest(
                Request.Method.GET,
                baseUrl + "/authenticate",
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
            RemoteStorage.RequestCompletedHandler handler,
            RemoteStorage.ErrorHandler errorHandler
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
                baseUrl + "/menu_items",
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
            RemoteStorage.RequestCompletedHandler handler,
            RemoteStorage.ErrorHandler errorHandler
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
                baseUrl + "/menu_items/" + menuItemId,
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
            RemoteStorage.RequestCompletedHandler handler,
            RemoteStorage.ErrorHandler errorHandler
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
                baseUrl + "/menu",
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
            RemoteStorage.RequestCompletedHandler handler,
            RemoteStorage.ErrorHandler errorHandler
    ) {
        authenticatedObjectRequest(
                Request.Method.DELETE,
                baseUrl + "/menu/" + menuId,
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
            RemoteStorage.RequestCompletedHandler handler,
            RemoteStorage.ErrorHandler errorHandler
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
                baseUrl + "/canteens/" + canteenId,
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
            RemoteStorage.RequestCompletedHandler handler,
            RemoteStorage.ErrorHandler errorHandler
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
                baseUrl + "/menu/" + menuId + "/schedule",
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
            RemoteStorage.RequestCompletedHandler handler,
            RemoteStorage.ErrorHandler errorHandler
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
                baseUrl + "/menu/" + menuId + "/availability",
                data,
                response -> handler.onCompleted(),
                errorHandler::onError,
                password
        );
    }

    /**
     * Removes a menu item from the database along with all reviews and mappings between canteens
     *
     * @param password     Owner password
     * @param menuItemId   Menu item id
     * @param handler      Success handler
     * @param errorHandler Error handler
     */
    public void removeMenuItem(
            String password,
            int menuItemId,
            RemoteStorage.RequestCompletedHandler handler,
            RemoteStorage.ErrorHandler errorHandler
    ) {
        authenticatedObjectRequest(
                Request.Method.DELETE,
                baseUrl + "/menu_items/" + menuItemId,
                null,
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
}
