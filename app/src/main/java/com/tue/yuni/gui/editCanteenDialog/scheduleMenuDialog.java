package com.tue.yuni.gui.editCanteenDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;

import com.tue.yuni.models.Day;
import com.tue.yuni.models.Schedule;
import com.tue.yuni.R;
import com.tue.yuni.models.ExtendedMenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class scheduleMenuDialog implements View.OnClickListener{
    private Context ctx;
    private AlertDialog dialog;
    private MenuDialogContent parent;
    private ExtendedMenuItem menuItem;

    CheckBox mondayBox;
    CheckBox tuesdayBox;
    CheckBox wednesdayBox;
    CheckBox thursdayBox;
    CheckBox fridayBox;
    CheckBox saturdayBox;
    CheckBox sundayBox;

    public scheduleMenuDialog(@NonNull Context ctx) {
        this.ctx = ctx;
    }

    @SuppressWarnings({"all"})
    public void show(MenuDialogContent parent, ExtendedMenuItem menuItem){
        // Instantiate dialog only if it doesn't already exist
        if (dialog == null) {
            this.menuItem=menuItem;
            this.parent = parent;
            // Create Alert Dialog
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
            // Inflate Alert Dialog View
            View view = LayoutInflater.from(ctx).inflate(R.layout.layout_set_schedule, null);
            alertDialog.setView(view);
            // Get View UI Elements
            Button sendScheduleButton = view.findViewById(R.id.setScheduleButton);
            Schedule schedule = menuItem.getSchedule();
            mondayBox = view.findViewById(R.id.mondayBox);
            tuesdayBox = view.findViewById(R.id.tuesdayBox);
            wednesdayBox = view.findViewById(R.id.wednesdayBox);
            thursdayBox = view.findViewById(R.id.thursdayBox);
            fridayBox = view.findViewById(R.id.fridayBox);
            saturdayBox = view.findViewById(R.id.saturdayBox);
            sundayBox = view.findViewById(R.id.sundayBox);

            mondayBox.setChecked(schedule.getDay(Day.MONDAY));
            tuesdayBox.setChecked(schedule.getDay(Day.TUESDAY));
            wednesdayBox.setChecked(schedule.getDay(Day.WEDNESDAY));
            thursdayBox.setChecked(schedule.getDay(Day.THURSDAY));
            fridayBox.setChecked(schedule.getDay(Day.FRIDAY));
            saturdayBox.setChecked(schedule.getDay(Day.SATURDAY));
            sundayBox.setChecked(schedule.getDay(Day.SUNDAY));




            // Show Alert Dialog
            dialog = alertDialog.show();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            // Setup Event
            sendScheduleButton.setOnClickListener(this);
        }
    }

    public void dismiss() {
        // Dismiss Dialog only if it exists
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void onClick(View v) {
        // Notify the parent of the feedback given
        Map<Day,Boolean> dayMap = new HashMap<>();
        dayMap.put(Day.MONDAY,mondayBox.isChecked());
        dayMap.put(Day.TUESDAY,tuesdayBox.isChecked());
        dayMap.put(Day.WEDNESDAY,wednesdayBox.isChecked());
        dayMap.put(Day.THURSDAY,thursdayBox.isChecked());
        dayMap.put(Day.FRIDAY,fridayBox.isChecked());
        dayMap.put(Day.SATURDAY,saturdayBox.isChecked());
        dayMap.put(Day.SUNDAY,sundayBox.isChecked());

        Schedule schedule = new Schedule(dayMap);

        dismiss();
        ArrayList<Integer> q = new ArrayList<>();

        parent.onChangeMenuItem(menuItem,schedule);
    }
}
