package com.tue.yuni.storage;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.tue.yuni.models.MenuItem;
import com.tue.yuni.models.canteen.Canteen;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RemoteStorage {

    private static RemoteStorage instance;

    private final String BASE_URL = "http://116.203.117.175/api";

    private RequestQueue queue;

    private RemoteStorage() {
        // Intentionally left blank to provide singleton implementation
    }

    public static RemoteStorage get(Context context) {
        if (instance == null) {
            instance = new RemoteStorage();
            instance.initialise(context);
        }

        return instance;
    }

    public void initialise(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    /*
     * Publicly available methods
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
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            canteenDataHandler.onReceive(null);
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
        public void onError(VolleyError error);
    }

    public interface CanteenDataHandler {
        public void onReceive(List<Canteen> canteens);
    }
}
