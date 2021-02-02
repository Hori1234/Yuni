package com.tue.yuni.gui.editCanteenDialog;

import com.tue.yuni.models.Availability;
import com.tue.yuni.models.ExtendedMenuItem;
import com.tue.yuni.models.MenuItem;
import com.tue.yuni.models.Schedule;
import com.tue.yuni.models.canteen.Canteen;

public interface AddDialogContent {
    void onAddMenuItem(MenuItem menuItem, Canteen canteen);
}
