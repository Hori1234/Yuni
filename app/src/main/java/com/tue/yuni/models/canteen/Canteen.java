package com.tue.yuni.models.canteen;

import android.os.Parcel;
import android.os.Parcelable;

import com.tue.yuni.models.ExtendedMenuItem;
import com.tue.yuni.models.Location;
import com.tue.yuni.models.MenuItem;

import java.util.List;

public class Canteen implements Parcelable {

    private final int id;
    private final String name;
    private final String description;
    private final OperatingTimes operatingTimes;
    private final Location location;
    private final String building;
    private final int imageResourceId;
    private final float rating;
    private final int busyness;

    private List<ExtendedMenuItem> menuItems;

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

    public int getImageResourceId() {
        return imageResourceId;
    }

    public List<ExtendedMenuItem> getMenuItems() {
        return menuItems;
    }

    /**
     * Parcelable Implementation
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

    @Override
    public int describeContents() {
        return 0;
    }

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

    public static final Creator<Canteen> CREATOR = new Creator<Canteen>() {
        @Override
        public Canteen createFromParcel(Parcel source) {
            return new Canteen(source);
        }

        @Override
        public Canteen[] newArray(int size) {
            return new Canteen[size];
        }
    };
}
