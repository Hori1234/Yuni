package com.tue.yuni.storage.parser;

import com.tue.yuni.models.Location;
import com.tue.yuni.models.canteen.Canteen;
import com.tue.yuni.models.canteen.OperatingTimes;

import org.json.JSONException;
import org.json.JSONObject;

public class CanteenParser {

    /**
     * Parses JSON into a canteen object.
     *
     * @param data JSON data
     * @return Canteen object
     * @throws JSONException If json is not in the correct format
     */
    public static Canteen parse(JSONObject data) throws JSONException {
        return new Canteen(
                data.getInt("id"),
                data.getString("name"),
                data.getString("description"),
                OperatingTimes.fromStorage(data.getJSONObject("operating_times")),
                new Location(), // TODO: Location
                data.getString("building"),
                0, // TODO: Image resource id
                2.5f,
                3
        );
    }
}
