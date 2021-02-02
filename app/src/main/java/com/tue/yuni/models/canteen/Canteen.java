package com.tue.yuni.models.canteen;

import android.os.Parcel;
import android.os.Parcelable;

import com.tue.yuni.models.Day;
import com.tue.yuni.models.ExtendedMenuItem;
import com.tue.yuni.models.Location;

import java.util.Calendar;
import java.util.List;

/**
 * Canteen model
 */
public class Canteen implements Parcelable {

    /**
     * Unique identification number
     */
    private final int id;
    /**
     * Name
     */
    private final String name;
    /**
     * Description
     */
    private String description;
    /**
     * Operating times
     */
    private OperatingTimes operatingTimes;
    /**
     * Location
     */
    private final Location location;
    /**
     * Building
     */
    private final String building;
    /**
     * Image resource ID (NOT IMPLEMENTED)
     */
    private final int imageResourceId;
    /**
     * Average rating
     */
    private float rating;
    /**
     * Busyness
     */
    private final int busyness;
    /**
     * Menu items
     */
    private List<ExtendedMenuItem> menuItems;

    /**
     * @param id              ID
     * @param name            Name
     * @param description     Description
     * @param operatingTimes  Operating times
     * @param location        Location
     * @param building        Building
     * @param imageResourceId Image resouce ID
     * @param rating          Average rating
     * @param busyness        Busyness
     * @param menuItems       Menu items
     */
    public Canteen(
            int id,
            String name,
            String description,
            OperatingTimes operatingTimes,
            Location location,
            String building,
            int imageResourceId,
            float rating,
            int busyness,
            List<ExtendedMenuItem> menuItems
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.operatingTimes = operatingTimes;
        this.location = location;
        this.building = building;
        this.imageResourceId = imageResourceId;
        this.rating = rating;
        this.busyness = busyness;
        this.menuItems = menuItems;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOperatingTimes(OperatingTimes operatingTimes) {
        this.operatingTimes = operatingTimes;
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

    public OperatingTimes getOperatingTimes() {
        return operatingTimes;
    }

    public Location getLocation() {
        return location;
    }

    public String getBuilding() {
        return building;
    }

    public float getRating() {
        return rating;
    }

    public int getBusyness() {
        return busyness;
    }

    /**
     * @deprecated
     */
    public int getImageResourceId() {
        return imageResourceId;
    }

    public List<ExtendedMenuItem> getMenuItems() {
        return menuItems;
    }

    /*
     * Parcelable Implementation
     */

    /**
     * Parcelable constructor
     *
     * @param in Parcel
     */
    protected Canteen(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.description = in.readString();
        this.operatingTimes = in.readParcelable(OperatingTimes.class.getClassLoader());
        this.location = null;   // ToDo
        this.building = in.readString();
        this.imageResourceId = in.readInt();
        this.rating = in.readFloat();
        this.busyness = in.readInt();
        in.readTypedList(menuItems, ExtendedMenuItem.CREATOR);
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
        dest.writeParcelable(operatingTimes, 0);
        //dest.writeParcelable(location, 0); ToDo
        dest.writeString(building);
        dest.writeInt(imageResourceId);
        dest.writeFloat(rating);
        dest.writeInt(busyness);
        dest.writeTypedList(menuItems);
    }

    /**
     * @see android.os.Parcelable.Creator
     */
    public static final Creator<Canteen> CREATOR = new Creator<Canteen>() {
        /**
         * @see android.os.Parcelable.Creator
         */
        @Override
        public Canteen createFromParcel(Parcel source) {
            return new Canteen(source);
        }

        /**
         * @see android.os.Parcelable.Creator
         */
        @Override
        public Canteen[] newArray(int size) {
            return new Canteen[size];
        }
    };

    /**
     * 0 = Closed, 1 = Closing, 2 = Open
     *
     * @param canteen Canteen
     * @return Open status
     */
    public static int getCanteenCurrentOpenStatus(Canteen canteen) {
        // Canteen Status Processing
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK) == 1 ? 6 : calendar.get(Calendar.DAY_OF_WEEK) - 2;
        if (canteen.getOperatingTimes().isOpen(Day.values()[day])) {
            // Get Current Time
            int open = canteen.getOperatingTimes().getOpeningTime(Day.values()[day]);
            int close = canteen.getOperatingTimes().getClosingTime(Day.values()[day]);
            int currentTime = calendar.get(Calendar.HOUR_OF_DAY) * 100 + calendar.get(Calendar.MINUTE);

            // Display Open Or Closed for the canteen
            if (open <= currentTime && currentTime < close) {
                if ((close % 100) - 5 < 0) {
                    int min = 60 + ((close % 100) - 5);
                    close = ((close / 100) - 1) * 100 + min;
                }
                if (currentTime >= close) {
                    return 1;
                } else {
                    return 2;
                }
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }
}
