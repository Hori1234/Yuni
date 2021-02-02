package com.tue.yuni.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

/**
 * Menu item model as presented in the database
 */
public class MenuItem implements Parcelable {
    /**
     * Unique identification number
     */
    protected final int id;
    /**
     * Menu item name
     */
    protected final String name;
    /**
     * Menu item description, a short description what the menu item is
     */
    protected final String description;
    /**
     * The category the menu item is in
     */
    protected final String category;
    /**
     * The average rating for this menu item
     */
    protected final float rating;

    /**
     * @param id          ID
     * @param name        Name
     * @param description Description
     * @param category    Category
     * @param rating      Average rating
     */
    public MenuItem(
            int id,
            String name,
            String description,
            String category,
            float rating
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public float getRating() {
        return rating;
    }

    /*
     * Parcelable Implementation
     */

    /**
     * Parcelable constructor
     *
     * @param in Parcel
     */
    protected MenuItem(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.description = in.readString();
        this.category = in.readString();
        this.rating = in.readFloat();
    }

    /**
     * @see Parcelable
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @see Parcelable
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeFloat(rating);
    }

    /**
     * @see android.os.Parcelable.Creator
     */
    public static final Creator<MenuItem> CREATOR = new Creator<MenuItem>() {

        /**
         * @see android.os.Parcelable.Creator
         */
        @Override
        public MenuItem createFromParcel(Parcel source) {
            return new MenuItem(source);
        }

        /**
         * @see android.os.Parcelable.Creator
         */
        @Override
        public MenuItem[] newArray(int size) {
            return new MenuItem[size];
        }
    };

    /**
     * Custom comparator for menu items to sort menu items by name
     */
    public static class CustomComparator implements Comparator<MenuItem> {
        @Override
        public int compare(MenuItem o1, MenuItem o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    }
}
