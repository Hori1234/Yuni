package com.tue.yuni.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Schedule {

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
}
