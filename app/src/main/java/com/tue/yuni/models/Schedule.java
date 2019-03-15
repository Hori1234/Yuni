package com.tue.yuni.models;

public class Schedule {

    private boolean availableOnMonday;
    private boolean availableOnTuesday;
    private boolean availableOnWednesday;
    private boolean availableOnThursday;
    private boolean availableOnFriday;
    private boolean availableOnSaturday;
    private boolean availableOnSunday;

    public Schedule(
            boolean availableOnMonday,
            boolean availableOnTuesday,
            boolean availableOnWednesday,
            boolean availableOnThursday,
            boolean availableOnFriday,
            boolean availableOnSaturday,
            boolean availableOnSunday
    ) {
        this.availableOnMonday = availableOnMonday;
        this.availableOnTuesday = availableOnTuesday;
        this.availableOnWednesday = availableOnWednesday;
        this.availableOnThursday = availableOnThursday;
        this.availableOnFriday = availableOnFriday;
        this.availableOnSaturday = availableOnSaturday;
        this.availableOnSunday = availableOnSunday;
    }

    public boolean isAvailableOnMonday() {
        return availableOnMonday;
    }

    public boolean isAvailableOnTuesday() {
        return availableOnTuesday;
    }

    public boolean isAvailableOnWednesday() {
        return availableOnWednesday;
    }

    public boolean isAvailableOnThursday() {
        return availableOnThursday;
    }

    public boolean isAvailableOnFriday() {
        return availableOnFriday;
    }

    public boolean isAvailableOnSaturday() {
        return availableOnSaturday;
    }

    public boolean isAvailableOnSunday() {
        return availableOnSunday;
    }
}
