package com.tue.yuni.storage;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FavouriteStorage {
    private static FavouriteStorage favouriteStorage = null;

    private String preferenceStorage;
    private SharedPreferences preferences;


    /**
     * create a favorite storage handler
     *
     * @param context current context
     */
    private FavouriteStorage(Context context) {
        this.preferenceStorage = "favoritesYuni";
        this.preferences = context.getSharedPreferences(preferenceStorage, Context.MODE_PRIVATE);
    }

    /**
     * ToDo
     * @return
     */
    public static void initialize(Context ctx){
        if (favouriteStorage == null) {
            synchronized (FavouriteStorage.class){
                favouriteStorage = new FavouriteStorage(ctx);
            }
        }
    }

    /**
     * ToDo
     * @return
     */
    public static FavouriteStorage get(){
        if (favouriteStorage == null) throw new IllegalStateException("FavouriteStorage not Initialized");
        return favouriteStorage;
    }

    /**
     * set the favorite at given ID to true
     *
     * @param identifier identifier of favorite
     */
    public void setFavorite(int identifier) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(String.valueOf(identifier), true);
        editor.apply();
    }

    /**
     * remove the favorite at given ID
     *
     * @param identifier identifier of favorite
     */
    public void removeFavorite(int identifier) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(String.valueOf(identifier));
        editor.apply();
    }

    /**
     * given a list of identifiers return how many are in favorite
     *
     * @param identifiers list of identifiers
     * @return
     */
    public Integer checkFavoritesCount(List<Integer> identifiers) {
        int count = 0;

        for (int id : identifiers) {
            if (preferences.getBoolean(String.valueOf(id), false)) {
                count++;
            }
        }

        return count;
    }

    /**
     * given an identifier check if it is in favorites
     *
     * @param identifier identifier of favorite
     * @return true if the id is in favor
     */
    public Boolean checkFavorite(int identifier) {
        if (preferences.getBoolean(String.valueOf(identifier), false)) {
            return true;
        }

        return false;
    }

    /**
     * returns a list of favorites
     *
     * @return Integer list
     */
    public ArrayList<Integer> getFavorites() {
        ArrayList<Integer> favorites = new ArrayList<>();
        Map<String, ?> mapping = preferences.getAll();
        for (String key : mapping.keySet()) {
            favorites.add(Integer.valueOf(key));
        }
        return favorites;
    }

    /**
     * clear the favorites
     */
    public void resetFavorites() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
