package com.tue.yuni.gui.editCanteenDialog;

import com.tue.yuni.models.Schedule;

public interface DialogContent {
    void onChangeMenuItem(int changeType, Schedule schedule, int availability);
}
