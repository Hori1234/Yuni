package com.tue.yuni.gui.editCanteenDialog;

import com.tue.yuni.models.canteen.Canteen;
import com.tue.yuni.models.canteen.OperatingTimes;

public interface CanteenDialogContent {
    void onChangeCanteen(Canteen canteen);

    void onChangeCanteen(OperatingTimes times);

    void onChangeCanteen(String description);
}


