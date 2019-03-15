package com.tue.yuni.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class PasswordStorage {

    private String passwordStorageName;
    private SharedPreferences passwordStorage;

    /**
     * create a password storage handler
     *
     * @param context current context
     */
    public PasswordStorage(Context context) {
        this.passwordStorageName = "passwordEmployeeYuni";
        this.passwordStorage = context.getSharedPreferences(passwordStorageName, Context.MODE_PRIVATE);
    }

    /**
     * set the Password
     *
     * @param password the Password
     */
    public void setPassword(String password) {
        SharedPreferences.Editor editor = passwordStorage.edit();
        editor.putString("password", password);
        editor.apply();
    }

    /**
     * remove the Password
     */
    public void removePassword() {
        SharedPreferences.Editor editor = passwordStorage.edit();
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
        if (passwordStorage.getString("password", "no password here").equals(password)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * clear the Password
     */
    public void resetPassword() {
        SharedPreferences.Editor editor = passwordStorage.edit();
        editor.clear();
        editor.apply();
    }
}
