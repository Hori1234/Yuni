package com.tue.yuni.gui.editCanteenDialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.tue.yuni.R;
import com.tue.yuni.models.ExtendedMenuItem;

import java.util.ArrayList;

public class scheduleDialog implements View.OnClickListener{
    private Context ctx;
    private AlertDialog dialog;
    private DialogContent parent;
    private ExtendedMenuItem menuItem;

    public scheduleDialog(@NonNull Context ctx) {
        this.ctx = ctx;
    }

    @SuppressWarnings({"all"})
    public void show(DialogContent parent, ExtendedMenuItem menuItem){
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
            Button sendScheduleButton = view.findViewById(R.id.sendReview);
            // Show Alert Dialog
            dialog = alertDialog.show();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            // Setup Event
            sendScheduleButton.setOnClickListener(this);
        }
    }

    public void dimiss() {
        // Dismiss Dialog only if it exists
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void onClick(View v) {
        // Notify the parent of the feedback given
        //TODO get schedule

        dimiss();
        //TODO pass schedule
        ArrayList<Integer> q = new ArrayList<>();

        parent.onChangeMenuItem(2,menuItem,null,0);
    }
}
