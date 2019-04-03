package com.tue.yuni.services.location;

import com.tue.yuni.models.Location;

public interface LocationReceivedListener {
    public void onLocationReceived(Location location);
}
