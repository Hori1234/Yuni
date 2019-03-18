package com.tue.yuni.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Schedule implements Parcelable {

    private Map<Day, Boolean> schedule;

    public Schedule(Map<Day, Boolean> schedule) {
        this.schedule = schedule;
    }

    public static Schedule fromStorage(JSONObject data) throws JSONException {
        Map<Day, Boolean> schedule = new HashMap<>();

        for (Day day : Day.values()) {
            schedule.put(day, data.getBoolean(day.name()));
        }

        return new Schedule(schedule);
    }

    /**
     * Parcelable Implementation
     */
    protected Schedule(Parcel in) {
        this.schedule = new HashMap<>();
        in.readMap(this.schedule, Boolean.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(schedule);
    }

    public static final Creator<Schedule> CREATOR = new Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel source) {
            return new Schedule(source);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };
}
