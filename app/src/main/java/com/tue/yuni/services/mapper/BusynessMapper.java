package com.tue.yuni.services.mapper;

import com.tue.yuni.R;
import com.tue.yuni.gui.util.TrafficLightIndicator;

/**
 * Busyness mapper static utility class
 */
public class BusynessMapper {

    private static final int LOW = 2;    // 0 <= n <= 2 -> low busyness
    private static final int MEDIUM = 5; // 2 <  n <= 5 -> Medium busyness

    /**
     * Maps a busyness value to a traffic light indicator state
     *
     * @param busyness Busyness value
     * @return Traffic light indicator state
     */
    public static TrafficLightIndicator.State getState(int busyness) {
        if (busyness <= LOW) {
            return TrafficLightIndicator.State.GREEN;
        } else if (busyness <= MEDIUM) {
            return TrafficLightIndicator.State.YELLOW;
        } else {
            return TrafficLightIndicator.State.RED;
        }
    }

    /**
     * Maps a busyness value to a string representation
     *
     * @param busyness Busyness value
     * @return String representation
     */
    public static int getTextResource(int busyness) {
        if (busyness <= LOW) {
            return R.string.quiet;
        } else if (busyness <= MEDIUM) {
            return R.string.moderate;
        } else {
            return R.string.busy;
        }
    }
}
