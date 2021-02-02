package com.tue.yuni.gui.editCanteenDialog;

import com.tue.yuni.models.Availability;
import com.tue.yuni.models.ExtendedMenuItem;
import com.tue.yuni.models.Schedule;

public interface MenuDialogContent {
    void onChangeMenuItem(ExtendedMenuItem menuItem);

    void onChangeMenuItem(ExtendedMenuItem menuItem, Schedule schedule);

    void onChangeMenuItem(ExtendedMenuItem menuItem, Availability availability);
}
