package com.tue.yuni.models;

/**
 * Utility class for day object
 */
public enum Day {
    MONDAY("MONDAY"),
    TUESDAY("TUESDAY"),
    WEDNESDAY("WEDNESDAY"),
    THURSDAY("THURSDAY"),
    FRIDAY("FRIDAY"),
    SATURDAY("SATURDAY"),
    SUNDAY("SUNDAY");

    /**
     * Textual description of the enumerate type
     */
    private String description;

    /**
     * @param description Description
     */
    Day(String description) {
        this.description = description;
    }

    /**
     * @return Enum value as string
     */
    public String asString() {
        return description;
    }
}