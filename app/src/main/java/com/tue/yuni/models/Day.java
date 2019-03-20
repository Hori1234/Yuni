package com.tue.yuni.models;

import java.util.Calendar;

public enum Day {
    MONDAY("MONDAY"),
    TUESDAY("TUESDAY"),
    WEDNESDAY("WEDNESDAY"),
    THURSDAY("THURSDAY"),
    FRIDAY("FRIDAY"),
    SATURDAY("SATURDAY"),
    SUNDAY("SUNDAY");

    private String description;

    Day(String description) {
        this.description = description;
    }

    public String asString() {
        return description;
    }
}