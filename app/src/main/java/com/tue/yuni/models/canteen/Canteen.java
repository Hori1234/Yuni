package com.tue.yuni.models.canteen;

import com.tue.yuni.models.Location;

public class Canteen {

    private int id;
    private String name;
    private String description;
    private OperatingTimes operatingTimes;
    private Location location;
    private int imageResourceId;

    public Canteen(
            int id,
            String name,
            String description,
            OperatingTimes operatingTimes,
            Location location,
            int imageResourceId
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.operatingTimes = operatingTimes;
        this.location = location;
        this.imageResourceId = imageResourceId;
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

    public int getImageResourceId() {
        return imageResourceId;
    }
}
