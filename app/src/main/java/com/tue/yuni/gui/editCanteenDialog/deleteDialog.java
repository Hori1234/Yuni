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

public class deleteDialog implements View.OnClickListener{
    private Context ctx;
    private AlertDialog dialog;
    private DialogContent parent;

    public deleteDialog(@NonNull Context ctx) {
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
            Button deleteButton = view.findViewById(R.id.sendButton);
            // Show Alert Dialog
            dialog = alertDialog.show();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            // Setup Event
            deleteButton.setOnClickListener(this);
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

        //TODO

        dimiss();
        parent.onDelete("hi");
    }



    public interface DialogContent {
        void onDelete(String id);
    }
}
