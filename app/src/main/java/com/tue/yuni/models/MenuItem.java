package com.tue.yuni.models;

public class MenuItem {
    protected final int id;
    protected final String name;
    protected final String description;
    protected final String category;
    protected final float rating;
    protected final int availability;

    public MenuItem(
            int id,
            String name,
            String description,
            String category,
            float rating,
            int availability
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.rating = rating;
        this.availability = availability;
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

    public int getAvailability() {
        return availability;
    }
}
