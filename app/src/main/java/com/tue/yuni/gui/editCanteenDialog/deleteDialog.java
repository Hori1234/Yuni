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
import com.tue.yuni.models.ExtendedMenuItem;
import com.tue.yuni.models.Schedule;

public class deleteDialog {
    private Context ctx;
    private AlertDialog dialog;
    private DialogContent parent;
    private ExtendedMenuItem menuItem;


    public deleteDialog(@NonNull Context ctx) {
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
            View view = LayoutInflater.from(ctx).inflate(R.layout.layout_delete_menu_item, null);
            alertDialog.setView(view);
            // Get View UI Elements
            Button deleteButton = view.findViewById(R.id.sendButton);
            // Show Alert Dialog
            dialog = alertDialog.show();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            // Setup Events
            deleteButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // Notify the parent of the feedback given

                    dismiss();
                    parent.onChangeMenuItem(1,menuItem,null,0);
                }
            });
            Button cancelButton = view.findViewById(R.id.cancelButton);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    public void dismiss() {
        // Dismiss Dialog only if it exists
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }


}
