package com.tue.yuni.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MenuItem implements Parcelable {
    protected final int id;
    protected final String name;
    protected final String description;
    protected final String category;
    protected final float rating;

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

    /**
     * Parcelable Implementation
     */
    protected MenuItem(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.description = in.readString();
        this.category = in.readString();
        this.rating = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeFloat(rating);
    }

    public static final Creator<MenuItem> CREATOR = new Creator<MenuItem>() {
        @Override
        public MenuItem createFromParcel(Parcel source) {
            return new MenuItem(source);
        }

        @Override
        public MenuItem[] newArray(int size) {
            return new MenuItem[size];
        }
    };
}
