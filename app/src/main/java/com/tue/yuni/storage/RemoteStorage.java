package com.tue.yuni.storage;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tue.yuni.models.Availability;
import com.tue.yuni.models.MenuItem;
import com.tue.yuni.models.Schedule;
import com.tue.yuni.models.canteen.Canteen;
import com.tue.yuni.models.canteen.OperatingTimes;
import com.tue.yuni.models.review.CanteenReview;
import com.tue.yuni.models.review.MenuItemReview;
import com.tue.yuni.storage.remote.OwnerStorage;
import com.tue.yuni.storage.remote.VisitorStorage;

import org.json.JSONObject;

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

    private VisitorStorage visitorStorage;
    private OwnerStorage ownerStorage;

    private RemoteStorage(Context context) {
        this.queue = Volley.newRequestQueue(context);
        this.visitorStorage = new VisitorStorage(BASE_URL, queue);
        this.ownerStorage = new OwnerStorage(BASE_URL, queue);
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
        visitorStorage.getCanteens(canteensDataHandler, errorHandler);
    }

    /**
     * Gets a specific canteen.
     *
     * @param id           Id
     * @param handler      Success handler
     * @param errorHandler Error handler
     */
    public void getCanteen(int id, CanteenDataHandler handler, ErrorHandler errorHandler) {
        visitorStorage.getCanteen(id, handler, errorHandler);
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
        visitorStorage.getMenuItemReviews(menuItemId, handler, errorHandler);
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
        visitorStorage.createMenuItemReview(menuItemId, rating, description, handler, errorHandler);
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
        visitorStorage.getCanteenReviews(canteenId, handler, errorHandler);
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
        visitorStorage.createCanteenReview(canteenId, rating, description, handler, errorHandler);
    }

    /**
     * Fetches a list of all available menu items.
     *
     * @param handler      Success handler
     * @param errorHandler Error handler
     */
    public void getAllMenuItems(MenuItemsDataHandler handler, ErrorHandler errorHandler) {
        visitorStorage.getAllMenuItems(handler, errorHandler);
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
        visitorStorage.createBusynessEntry(canteenId, handler, errorHandler);
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
        ownerStorage.authenticate(password, authenticateHandler, errorHandler);
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
        ownerStorage.createMenuItem(password, name, description, category, handler, errorHandler);
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
        ownerStorage.updateMenuItem(password, menuItemId, name, description, category, handler, errorHandler);
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
        ownerStorage.addItemToMenu(canteenId, menuItemId, schedule, password, handler, errorHandler);
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
        ownerStorage.removeItemFromMenu(menuId, password, handler, errorHandler);
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
        ownerStorage.updateCanteen(password, canteenId, name, description, operatingTimes, handler, errorHandler);
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
        ownerStorage.updateMenuItemSchedule(password, menuId, schedule, handler, errorHandler);
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
        ownerStorage.updateMenuItemAvailability(password, menuId, availability, handler, errorHandler);
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
            RequestCompletedHandler handler,
            ErrorHandler errorHandler
    ) {
        ownerStorage.removeMenuItem(password, menuItemId, handler, errorHandler);
    }

    public interface ErrorHandler {
        void onError(Exception e);
    }

    public interface RequestCompletedHandler {
        void onCompleted();
    }

    public interface CanteensDataHandler {
        void onReceive(List<Canteen> canteens);
    }

    public interface AuthenticateHandler {
        void onReceive(boolean authenticated);
    }

    public interface CanteenDataHandler {
        void onReceive(Canteen canteen);
    }

    public interface MenuItemReviewsDataHandler {
        void onReceive(List<MenuItemReview> reviews);
    }

    public interface CanteenReviewsDataHandler {
        void onReceive(List<CanteenReview> reviews);
    }

    public interface MenuItemsDataHandler {
        void onReceive(List<MenuItem> menuItems);
    }
}
