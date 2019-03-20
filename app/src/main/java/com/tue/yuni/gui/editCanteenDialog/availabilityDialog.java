package com.tue.yuni.gui.editCanteenDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tue.yuni.R;
import com.tue.yuni.models.Schedule;

public class availabilityDialog implements View.OnClickListener{
    private Context ctx;
    private AlertDialog dialog;
    private DialogContent parent;

    RadioGroup availabilityGroup;
    RadioButton inStockButton;
    RadioButton lowStockButton;
    RadioButton noStockButton;

    public availabilityDialog(@NonNull Context ctx) {
        this.ctx = ctx;
    }

    @SuppressWarnings({"all"})
    public void show(DialogContent parent){
        // Instantiate dialog only if it doesn't already exist
        if (dialog == null) {
            this.parent = parent;
            // Create Alert Dialog
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
            // Inflate Alert Dialog View
            View view = LayoutInflater.from(ctx).inflate(R.layout.layout_set_availability_menu_item, null);
            alertDialog.setView(view);
            // Get View UI Elements
            Button sendAvailibilityButton = view.findViewById(R.id.setAvailability);

            availabilityGroup = view.findViewById(R.id.availabilityGroup);

            inStockButton=view.findViewById(R.id.inStockButton);
            inStockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    availabilityGroup.clearCheck();
                    inStockButton.toggle();
                }
            });
            lowStockButton=view.findViewById(R.id.lowStockButton);
            lowStockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    availabilityGroup.clearCheck();
                    lowStockButton.toggle();
                }
            });
            noStockButton=view.findViewById(R.id.outStockButton);
            noStockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    availabilityGroup.clearCheck();
                    lowStockButton.toggle();
                }
            });

            // Show Alert Dialog
            dialog = alertDialog.show();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            // Setup Event
            sendAvailibilityButton.setOnClickListener(this);
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
        int result =availabilityGroup.getCheckedRadioButtonId();

        if(result>=0) {
            dimiss();
            //TODO post
            parent.onChangeMenuItem(0, null, result);
        }
    }
}
