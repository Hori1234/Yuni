package com.tue.yuni.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ExtendedMenuItem extends MenuItem implements Parcelable {
    private int menuId;
    private Schedule schedule;

    public ExtendedMenuItem(
            int id,
            String name,
            String description,
            String category,
            float rating,
            int availability,
            int menuId,
            Schedule schedule
    ) {
        super(id, name, description, category, rating, availability);
        this.menuId = menuId;
        this.schedule = schedule;
    }

    public int getMenuId() {
        return menuId;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    /**
     * Parcelable Implementation
     */
    protected ExtendedMenuItem(Parcel in) {
        super(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(menuId);
        dest.writeParcelable(schedule, 0);
    }

    public static final Creator<ExtendedMenuItem> CREATOR = new Creator<ExtendedMenuItem>() {
        @Override
        public ExtendedMenuItem createFromParcel(Parcel source) {
            return new ExtendedMenuItem(source);
        }

        @Override
        public ExtendedMenuItem[] newArray(int size) {
            return new ExtendedMenuItem[size];
        }
    };
}
