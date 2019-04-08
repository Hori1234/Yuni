package com.tue.yuni.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ExtendedMenuItem extends MenuItem implements Parcelable {
    private int menuId;
    private Schedule schedule;
    private Availability availability;

    public ExtendedMenuItem(
            int id,
            String name,
            String description,
            String category,
            float rating,
            Availability availability,
            int menuId,
            Schedule schedule
    ) {
        super(id, name, description, category, rating);
        this.menuId = menuId;
        this.schedule = schedule;
        this.availability = availability;
    }
    public int getId() {
        return id;
    }

    public int getMenuId() {
        return menuId;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Availability getAvailability() {
        return availability;
    }

    /**
     * Parcelable Implementation
     */
    protected ExtendedMenuItem(Parcel in) {
        super(in);
        menuId = in.readInt();
        schedule = in.readParcelable(Schedule.class.getClassLoader());
        availability = Availability.valueOf(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(menuId);
        dest.writeParcelable(schedule, 0);
        dest.writeString(availability.name());
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
