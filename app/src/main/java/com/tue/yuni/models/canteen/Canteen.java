package com.tue.yuni.models.canteen;

import com.tue.yuni.models.Location;

public class Canteen {

    private final int id;
    private final String name;
    private final String description;
    private final OperatingTimes operatingTimes;
    private final Location location;
    private final String building;
    private final int imageResourceId;
    private final float rating;
    private final int busyness;

    public Canteen(
            int id,
            String name,
            String description,
            OperatingTimes operatingTimes,
            Location location,
            String building,
            int imageResourceId,
            float rating,
            int busyness
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
}
