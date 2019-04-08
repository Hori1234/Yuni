package com.tue.yuni.models.canteen;

import android.os.Parcel;
import android.os.Parcelable;

import com.tue.yuni.models.Day;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OperatingTimes implements Parcelable {

    private Map<Day, Integer> openingTimes;
    private Map<Day, Integer> closingTimes;

    public OperatingTimes(Map<Day, Integer> openingTimes, Map<Day, Integer> closingTimes) {
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
    public int getOpeningTime(Day day) {
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
    public int getClosingTime(Day day) {
        if (!closingTimes.containsKey(day)) {
            throw new IllegalStateException(
                    String.format("Closing times map does not contain day of week '%s'", day.asString())
            );
        }

        return closingTimes.get(day);
    }
    public void setClosingTime(Day day, int time) {
        this.closingTimes.put(day,time);
    }

    public void setOpeningTime(Day day, int time) {
        this.openingTimes.put(day,time);
    }

    public void removeDay(Day day){
        if(openingTimes.containsKey(day)){
            openingTimes.remove(day);
        }
        if(closingTimes.containsKey(day)){
            closingTimes.remove(day);
        }

    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();

        for (Day day : Day.values()) {
            if (openingTimes.containsKey(day)) {
                JSONObject data = new JSONObject();
                data.put("opening", openingTimes.get(day));
                data.put("closing", closingTimes.get(day));

                json.put(day.name(), data);
            }
        }

        return json;
    }

    /**
     * Creates a new OperatingTimes object from json data
     *
     * @param data data
     * @return OperatingTimes object
     * @throws JSONException If the data cannot be read
     */
    public static OperatingTimes fromJson(JSONObject data) throws JSONException {
        Map<Day, Integer> openingTimes = new HashMap<>();
        Map<Day, Integer> closingTimes = new HashMap<>();

        for (Day day : Day.values()) {
            if (data.has(day.name())) {
                openingTimes.put(day, data.getJSONObject(day.name()).getInt("opening"));
                closingTimes.put(day, data.getJSONObject(day.name()).getInt("closing"));
            }
        }

        return new OperatingTimes(openingTimes, closingTimes);
    }

    /**
     * Parcelable Implementation
     */
    protected OperatingTimes(Parcel in) {
        in.readMap(this.openingTimes, Integer.class.getClassLoader());
        in.readMap(this.closingTimes, Integer.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(openingTimes);
        dest.writeMap(closingTimes);
    }

    public static final Creator<OperatingTimes> CREATOR = new Creator<OperatingTimes>() {
        @Override
        public OperatingTimes createFromParcel(Parcel source) {
            return new OperatingTimes(source);
        }

        @Override
        public OperatingTimes[] newArray(int size) {
            return new OperatingTimes[size];
        }
    };
}