package com.tue.yuni.services.mapper;

import com.tue.yuni.R;
import com.tue.yuni.gui.util.TrafficLightIndicator;

public class BusynessMapper {

    private static final int LOW = 2;    // 0 <= n <= 2 -> low busyness
    private static final int MEDIUM = 5; // 2 <  n <= 5 -> Medium busyness

    public static TrafficLightIndicator.State getState(int busyness) {
        if (busyness <= LOW) {
            return TrafficLightIndicator.State.GREEN;
        } else if (busyness <= MEDIUM) {
            return TrafficLightIndicator.State.YELLOW;
        } else {
            return TrafficLightIndicator.State.RED;
        }
    }

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
