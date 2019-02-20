package com.tue.yuni.models;

public enum Day {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    private String description;

    Day(String description) {
        this.description = description;
    }

    public String asString() {
        return description;
    }
}