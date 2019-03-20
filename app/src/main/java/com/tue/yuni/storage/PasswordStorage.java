package com.tue.yuni.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class PasswordStorage {
    private static PasswordStorage passwordStorage = null;

    private String passwordStorageName;
    private SharedPreferences passwordSharedPreference;

    /**
     * create a password storage handler
     *
     * @param context current context
     */
    private PasswordStorage(Context context) {
        this.passwordStorageName = "passwordEmployeeYuni";
        this.passwordSharedPreference = context.getSharedPreferences(passwordStorageName, Context.MODE_PRIVATE);
    }

    /**
     * ToDo
     * @return
     */
    public static void initialize(Context ctx){
        if (passwordStorage == null) {
            synchronized (FavouriteStorage.class){
                passwordStorage = new PasswordStorage(ctx);
            }
        }
    }

    /**
     * ToDo
     * @return
     */
    public static PasswordStorage get(){
        if (passwordStorage == null) throw new IllegalStateException("FavouriteStorage not Initialized");
        return passwordStorage;
    }

    /**
     * set the Password
     *
     * @param password the Password
     */
    public void setPassword(String password) {
        SharedPreferences.Editor editor = passwordSharedPreference.edit();
        editor.putString("password", password);
        editor.apply();
    }

    /**
     * remove the Password
     */
    public void removePassword() {
        SharedPreferences.Editor editor = passwordSharedPreference.edit();
        editor.remove("password");
        editor.apply();
    }


    /**
     * given an identifier check if it is in Password
     *
     * @param password the Password
     * @return true if the id is in favor
     */
    public Boolean checkPassword(String password) {
        if (passwordSharedPreference.getString("password", "no password here").equals(password)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * get the pasword
     */

    public String getPassword() {
        return passwordSharedPreference.getString("password","");
    }

    /**
     * clear the Password
     */
    public void resetPassword() {
        SharedPreferences.Editor editor = passwordSharedPreference.edit();
        editor.clear();
        editor.apply();
    }
}
