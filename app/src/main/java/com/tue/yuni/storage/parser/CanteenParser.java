package com.tue.yuni.storage.parser;

import com.tue.yuni.models.ExtendedMenuItem;
import com.tue.yuni.models.Location;
import com.tue.yuni.models.canteen.Canteen;
import com.tue.yuni.models.canteen.OperatingTimes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CanteenParser {

    /**
     * Parses JSON into a canteen object.
     *
     * @param data JSON data
     * @return Canteen object
     * @throws JSONException If json is not in the correct format
     */
    public static Canteen parse(JSONObject data) throws JSONException {
        List<ExtendedMenuItem> menuItems = new ArrayList<>();
        JSONArray menuItemsData = data.getJSONArray("menu_items");

        for (int i = 0; i < menuItemsData.length(); i++) {
            menuItems.add(MenuItemParser.parseExtended(menuItemsData.getJSONObject(i)));
        }

        return new Canteen(
                data.getInt("id"),
                data.getString("name"),
                data.getString("description"),
                OperatingTimes.fromJson(data.getJSONObject("operating_times")),
                new Location(), // TODO: Location
                data.getString("building"),
                0, // TODO: Image resource id
                Double.isNaN(data.optDouble("rating"))
                        ? 0
                        : (float) data.getDouble("rating"),
                data.getInt("busyness"),
                menuItems
        );
    }
}
