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
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tue.yuni.R;

import java.util.ArrayList;

public class scheduleDialog implements View.OnClickListener{
    private Context ctx;
    private AlertDialog dialog;
    private DialogContent parent;

    public scheduleDialog(@NonNull Context ctx) {
        this.ctx = ctx;
    }

    @SuppressWarnings({"all"})
    public void show(){
        // Instantiate dialog only if it doesn't already exist
        if (dialog == null) {
//            this.parent = parent;
            // Create Alert Dialog
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
            // Inflate Alert Dialog View
            View view = LayoutInflater.from(ctx).inflate(R.layout.layout_edit, null);
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
        q.add(1);
        ArrayList<ArrayList<Integer>> w= new ArrayList<>();
        w.add(q);
        parent.onSetSchedule(w);
    }



    public interface DialogContent {
        void onSetSchedule(ArrayList<ArrayList<Integer>> schedule);
    }
}
