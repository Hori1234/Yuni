package com.tue.yuni.storage;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.tue.yuni.models.Location;
import com.tue.yuni.models.MenuItem;
import com.tue.yuni.models.canteen.Canteen;
import com.tue.yuni.models.canteen.OperatingTimes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
     * @param canteenDataHandler Success handler
     * @param errorHandler       Error handler
     */
    public void getCanteens(final CanteenDataHandler canteenDataHandler, final ErrorHandler errorHandler) {
        queue.add(
                new JsonArrayRequest(
                        Request.Method.GET,
                        BASE_URL + "/canteens",
                        null,
                        response -> {
                            List<Canteen> canteens = new ArrayList<>();
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject canteenObject = response.getJSONObject(i);
                                    Canteen canteen = new Canteen(
                                            canteenObject.getInt("id"),
                                            canteenObject.getString("name"),
                                            canteenObject.getString("description"),
                                            OperatingTimes.fromStorage(canteenObject.getJSONObject("operating_times")),
                                            new Location(), // TODO: Location
                                            canteenObject.getString("building"),
                                            0 // TODO: Image resource id
                                    );

                                    canteens.add(canteen);
                                }

                                canteenDataHandler.onReceive(canteens);
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

    /*
     * Owner (authenticated) methods
     */

    public boolean checkAuthentication(String password) {
        // TODO: Implement method body

        return false;
    }

    public void updateAvailability(
            int menuId,
            boolean availableOnMonday,
            boolean availableOnTuesday,
            boolean availableOnWednesday,
            boolean availableOnThursday,
            boolean availableOnFriday,
            boolean availableOnSaturday,
            boolean availableOnSunday
    ) {
        // TODO: Implement method body
    }

    public interface ErrorHandler {
        public void onError(Exception e);
    }

    public interface CanteenDataHandler {
        public void onReceive(List<Canteen> canteens);
    }
}
