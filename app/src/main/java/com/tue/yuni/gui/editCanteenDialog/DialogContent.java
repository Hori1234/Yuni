package com.tue.yuni.gui.editCanteenDialog;

import com.tue.yuni.models.Availability;
import com.tue.yuni.models.ExtendedMenuItem;
import com.tue.yuni.models.Schedule;

public interface DialogContent {
    void onChangeMenuItem(int changeType, ExtendedMenuItem menuItem, Schedule schedule, Availability availability);
}
