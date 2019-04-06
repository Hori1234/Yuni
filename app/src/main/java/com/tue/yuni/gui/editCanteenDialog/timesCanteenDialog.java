package com.tue.yuni.gui.editCanteenDialog;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.tue.yuni.R;
import com.tue.yuni.models.Day;
import com.tue.yuni.models.ExtendedMenuItem;
import com.tue.yuni.models.Schedule;
import com.tue.yuni.models.canteen.Canteen;
import com.tue.yuni.models.canteen.OperatingTimes;
import com.tue.yuni.storage.RemoteStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class timesCanteenDialog implements View.OnClickListener {
    private Context ctx;
    private AlertDialog dialog;
    private Canteen canteen;
    private CanteenDialogContent parent;


    EditText[][] dayTimes;
    OperatingTimes times;

    public timesCanteenDialog(@NonNull Context ctx) {
        this.ctx = ctx;
    }

    @SuppressWarnings({"all"})
    public void show(CanteenDialogContent parent, Canteen canteen){
        dayTimes = new EditText[7][2];
        // Instantiate dialog only if it doesn't already exist
        if (dialog == null) {
            this.parent = parent;
            this.canteen = canteen;
            // Create Alert Dialog
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
            // Inflate Alert Dialog View
            View view = LayoutInflater.from(ctx).inflate(R.layout.layout_canteen_info_edit_times, null);
            alertDialog.setView(view);
            // Get View UI Elements

            dayTimes[0][0]  =  view.findViewById(R.id.mondayMorningEdit);
            dayTimes[0][1]  =  view.findViewById(R.id.mondayEveningEdit);
            dayTimes[1][0]  =  view.findViewById(R.id.tuesdayMorningEdit);
            dayTimes[1][1]  =  view.findViewById(R.id.tuesdayEveningEdit);
            dayTimes[2][0]  =  view.findViewById(R.id.wednesdayMorningEdit);
            dayTimes[2][1]  =  view.findViewById(R.id.wednesdayEveningEdit);
            dayTimes[3][0]  =  view.findViewById(R.id.thursdayMorningEdit);
            dayTimes[3][1]  =  view.findViewById(R.id.thursdayEveningEdit);
            dayTimes[4][0]  =  view.findViewById(R.id.fridayMorningEdit);
            dayTimes[4][1]  =  view.findViewById(R.id.fridayEveningEdit);
            dayTimes[5][0]  =  view.findViewById(R.id.saturdayMorningEdit);
            dayTimes[5][1]  =  view.findViewById(R.id.saturdayEveningEdit);
            dayTimes[6][0]  =  view.findViewById(R.id.sundayMorningEdit);
            dayTimes[6][1]  =  view.findViewById(R.id.sundayEveningEdit);

            times =  canteen.getOperatingTimes();
            for(int d = 0; d < 7; d++){
                if(times.isOpen(Day.values()[d])) {
                    dayTimes[d][0].setText(times.getOpeningTime(Day.values()[d]) / 100 + ":" + times.getOpeningTime(Day.values()[d]) % 100);
                    dayTimes[d][0].setOnClickListener(this);
                    dayTimes[d][1].setText(times.getClosingTime(Day.values()[d]) / 100 + ":" + times.getClosingTime(Day.values()[d]) % 100);
                    dayTimes[d][0].setOnClickListener(this);
                }else{
                    dayTimes[d][0].setText("00:00");
                    dayTimes[d][0].setOnClickListener(this);
                    dayTimes[d][1].setText("00:00");
                    dayTimes[d][0].setOnClickListener(this);
                }
            }


            Button sendTimesButton = view.findViewById(R.id.setSchedule);
            sendTimesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<Day, Integer> opening = new HashMap<>();
                    dismiss();
                    String[] time;
                    int timeOpen;
                    int timeClose;
                    for(int d = 0; d < 7; d++){
                        time = dayTimes[d][0].getText().toString().split(":");
                        timeOpen = Integer.valueOf(time[0])*100+Integer.valueOf(time[1]);

                        time = dayTimes[d][1].getText().toString().split(":");
                        timeClose = Integer.valueOf(time[0])*100+Integer.valueOf(time[1]);

                        if(timeOpen!=timeClose) {
                            times.setOpeningTime(Day.values()[d], timeOpen);
                            times.setClosingTime(Day.values()[d], timeClose);
                        }else{
                            if(times.isOpen(Day.values()[d])){
                                times.removeDay(Day.values()[d]);
                            }
                        }


                    }
                    parent.onChangeCanteen(times);
                }
            });

            Button cancelButton = view.findViewById(R.id.cancelButton);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });



            // Show Alert Dialoga
            dialog = alertDialog.show();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            // Setup Event

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
        for(int day = 0; day < 7; day++){
            for(int time = 0; time < 2;time++){
                if(v.equals(dayTimes[day][time])){
                    int hourOfDay, minute;
                    if (time == 0) {
                        hourOfDay   = 0;
                        minute      = 0;
                    } else {
                        hourOfDay   = times.getClosingTime(Day.values()[day])/100;
                        minute      = times.getClosingTime(Day.values()[day])%100;
                    }
                    final int d = day, t = time;
                    TimePickerDialog timePickerDialog = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            dayTimes[d][t].setText(hourOfDay + ":" + minute);
                        }
                    }, hourOfDay, minute, true);

                }

            }
        }
    }
}
