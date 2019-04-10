package com.tue.yuni.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Schedule model as presented in the database with utility methods
 */
public class Schedule implements Parcelable {

    /**
     * Mapping between days and booleans (whether the canteen is open or not)
     */
    private Map<Day, Boolean> schedule;

    /**
     * @param schedule Day -> boolean mapping
     */
    public Schedule(Map<Day, Boolean> schedule) {
        this.schedule = schedule;
    }

    /**
     * @param day Day of the week
     * @return Whether the canteen is open for that day
     */
    public boolean getDay(Day day) {
        return schedule.get(day);
    }

    /**
     * Converts the map to a bitmask
     *
     * @return Bitmask string
     */
    public String toBitmask() {
        String bitmask = "";

        bitmask += schedule.getOrDefault(Day.MONDAY, false) ? "1" : "0";
        bitmask += schedule.getOrDefault(Day.TUESDAY, false) ? "1" : "0";
        bitmask += schedule.getOrDefault(Day.WEDNESDAY, false) ? "1" : "0";
        bitmask += schedule.getOrDefault(Day.THURSDAY, false) ? "1" : "0";
        bitmask += schedule.getOrDefault(Day.FRIDAY, false) ? "1" : "0";
        bitmask += schedule.getOrDefault(Day.SATURDAY, false) ? "1" : "0";
        bitmask += schedule.getOrDefault(Day.SUNDAY, false) ? "1" : "0";

        return bitmask;
    }

    /**
     * Reads in a bitmask and converts it into a Schedule object
     *
     * @param data JSON data
     * @return New Schedule object
     * @throws JSONException If the JSON cannot be parsed
     */
    public static Schedule fromStorage(JSONObject data) throws JSONException {
        Map<Day, Boolean> schedule = new HashMap<>();

        for (Day day : Day.values()) {
            schedule.put(day, data.getBoolean(day.name()));
        }

        return new Schedule(schedule);
    }

    /*
     * Parcelable Implementation
     */

    /**
     * Parcelable constructor
     *
     * @param in Parcel
     */
    protected Schedule(Parcel in) {
        this.schedule = new HashMap<>();
        in.readMap(this.schedule, Boolean.class.getClassLoader());
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
        dest.writeMap(schedule);
    }

    /**
     * @see android.os.Parcelable.Creator
     */
    public static final Creator<Schedule> CREATOR = new Creator<Schedule>() {
        /**
         * @see android.os.Parcelable.Creator
         */
        @Override
        public Schedule createFromParcel(Parcel source) {
            return new Schedule(source);
        }

        /**
         * @see android.os.Parcelable.Creator
         */
        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };
}
