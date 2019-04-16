package com.tue.yuni.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Extended menu item model as presented in the database
 */
public class ExtendedMenuItem extends MenuItem implements Parcelable {
    /**
     * Menu id, the id of the mapping between a canteen and a menu item
     */
    private int menuId;
    /**
     * Schedule (when is the menu item available at that canteen)
     */
    private Schedule schedule;
    /**
     * Availability, whether the menu item is available at the canteen
     */
    private Availability availability;

    /**
     * @param id           Identification
     * @param name         Name
     * @param description  Description
     * @param category     Category
     * @param rating       Average rating
     * @param availability Availability
     * @param menuId       Menu id
     * @param schedule     Schedule
     */
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

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Availability getAvailability() {
        return availability;
    }

    /*
     * Parcelable Implementation
     */

    /**
     * Parcelable constructor
     *
     * @param in Parcel
     */
    protected ExtendedMenuItem(Parcel in) {
        super(in);
        menuId = in.readInt();
        schedule = in.readParcelable(Schedule.class.getClassLoader());
        availability = Availability.valueOf(in.readString());
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
        super.writeToParcel(dest, flags);
        dest.writeInt(menuId);
        dest.writeParcelable(schedule, 0);
        dest.writeString(availability.name());
    }

    /**
     * @see android.os.Parcelable.Creator
     */
    public static final Creator<ExtendedMenuItem> CREATOR = new Creator<ExtendedMenuItem>() {
        /**
         * @see android.os.Parcelable.Creator
         */
        @Override
        public ExtendedMenuItem createFromParcel(Parcel source) {
            return new ExtendedMenuItem(source);
        }

        /**
         * @see android.os.Parcelable.Creator
         */
        @Override
        public ExtendedMenuItem[] newArray(int size) {
            return new ExtendedMenuItem[size];
        }
    };
}
