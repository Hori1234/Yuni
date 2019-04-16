package com.tue.yuni.models;

/**
 * Location model
 */
public class Location {
    /**
     * Longitude
     */
    private final double longitude;
    /**
     * Latitude
     */
    private final double latitude;

    /**
     * @param longitude Longitude
     * @param latitude  Latitude
     */
    public Location(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * @return Longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @return Latitude
     */
    public double getLatitude() {
        return latitude;
    }
}
