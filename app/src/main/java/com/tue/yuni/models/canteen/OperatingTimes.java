package com.tue.yuni.models.canteen;

import com.tue.yuni.models.Day;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OperatingTimes {

    private Map<Day, String> openingTimes;
    private Map<Day, String> closingTimes;

    public OperatingTimes(Map<Day, String> openingTimes, Map<Day, String> closingTimes) {
        this.openingTimes = openingTimes;
        this.closingTimes = closingTimes;
    }

    /**
     * Returns whether there is an opening time for the specified day. If not, it indicates that it
     * is not open at all.
     *
     * @param day Day of the week
     * @return True if it is open, false otherwise
     */
    public boolean isOpen(Day day) {
        return openingTimes.containsKey(day);
    }

    /**
     * Returns the opening time for the specified day. Note that if the canteen is not open, this
     * method will throw an IllegalStateException.
     *
     * @param day Day of the week
     * @return Opening time
     */
    public String getOpeningTime(Day day) {
        if (!openingTimes.containsKey(day)) {
            throw new IllegalStateException(
                    String.format("Opening times map does not contain day of week '%s'", day.asString())
            );
        }

        return openingTimes.get(day);
    }

    /**
     * Returns the closing time for the specified day. Note that if the canteen is not open, this
     * method will throw an IllegalStateException.
     *
     * @param day Day of the week
     * @return Opening time
     */
    public String getClosingTime(Day day) {
        if (!closingTimes.containsKey(day)) {
            throw new IllegalStateException(
                    String.format("Closing times map does not contain day of week '%s'", day.asString())
            );
        }

        return closingTimes.get(day);
    }

    /**
     * Creates a new OperatingTimes object from json data
     *
     * @param data data
     * @return OperatingTimes object
     * @throws JSONException If the data cannot be read
     */
    public static OperatingTimes fromStorage(JSONObject data) throws JSONException {
        Map<Day, String> openingTimes = new HashMap<>();
        Map<Day, String> closingTimes = new HashMap<>();

        for (Day day : Day.values()) {
            if (data.has(day.name())) {
                openingTimes.put(day, data.getJSONObject(day.name()).getString("opening"));
                closingTimes.put(day, data.getJSONObject(day.name()).getString("closing"));
            }
        }

        return new OperatingTimes(openingTimes, closingTimes);
    }
}