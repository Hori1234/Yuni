package com.tue.yuni.storage.parser;

import com.tue.yuni.models.Availability;
import com.tue.yuni.models.ExtendedMenuItem;
import com.tue.yuni.models.MenuItem;
import com.tue.yuni.models.Schedule;

import org.json.JSONException;
import org.json.JSONObject;

public class MenuItemParser {

    public static MenuItem parse(JSONObject data) throws JSONException {
        return new MenuItem(
                data.getInt("id"),
                data.getString("name"),
                data.getString("description"),
                data.getString("category"),
                Double.isNaN(data.optDouble("rating"))
                        ? 0
                        : (float) data.getDouble("rating")
        );
    }

    public static ExtendedMenuItem parseExtended(JSONObject data) throws JSONException {
        return new ExtendedMenuItem(
                data.getInt("id"),
                data.getString("name"),
                data.getString("description"),
                data.getString("category"),
                Double.isNaN(data.optDouble("rating"))
                        ? 0
                        : (float) data.getDouble("rating"),
                Availability.valueOf(data.getString("availability")),
                data.getInt("menu_id"),
                Schedule.fromStorage(data.getJSONObject("schedule"))
        );
    }
}
