package com.tue.yuni.storage;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tue.yuni.models.MenuItem;
import com.tue.yuni.models.canteen.Canteen;
import com.tue.yuni.storage.parser.CanteenParser;

import org.json.JSONException;

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

    public List<MenuItem> getAllMenuItems() {
        // TODO: Implement method body

        return null;
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

    public interface CanteensDataHandler {
        public void onReceive(List<Canteen> canteens);
    }

    public interface AuthenticateHandler {
        public void onReceive(boolean authenticated);
    }
  
    public interface CanteenDataHandler {
        public void onReceive(Canteen canteen);
    }
}
