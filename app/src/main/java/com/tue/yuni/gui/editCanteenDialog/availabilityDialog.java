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
import com.tue.yuni.models.Availability;
import com.tue.yuni.models.ExtendedMenuItem;

public class availabilityDialog implements View.OnClickListener {
    private Context ctx;
    private AlertDialog dialog;
    private MenuDialogContent parent;
    private ExtendedMenuItem menuItem;

    RadioGroup availabilityGroup;
    RadioButton inStockButton;
    RadioButton lowStockButton;
    RadioButton noStockButton;

    public availabilityDialog(@NonNull Context ctx) {
        this.ctx = ctx;
    }

    @SuppressWarnings({"all"})
    public void show(MenuDialogContent parent, ExtendedMenuItem menuItem){
        // Instantiate dialog only if it doesn't already exist
        if (dialog == null) {
            this.menuItem = menuItem;
            this.parent = parent;
            // Create Alert Dialog
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
            // Inflate Alert Dialog View
            View view = LayoutInflater.from(ctx).inflate(R.layout.layout_set_availability_menu_item, null);
            alertDialog.setView(view);
            // Get View UI Elements
            Button sendAvailibilityButton = view.findViewById(R.id.setAvailability);

            availabilityGroup = view.findViewById(R.id.availabilityGroup);

            inStockButton = view.findViewById(R.id.inStockButton);
            inStockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    availabilityGroup.clearCheck();
                    inStockButton.toggle();
                }
            });
            lowStockButton = view.findViewById(R.id.lowStockButton);
            lowStockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    availabilityGroup.clearCheck();
                    lowStockButton.toggle();
                }
            });
            noStockButton = view.findViewById(R.id.outStockButton);
            noStockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    availabilityGroup.clearCheck();
                    noStockButton.toggle();
                }
            });

            switch(menuItem.getAvailability()){
                case IN_STOCK:
                    inStockButton.toggle();
                    break;
                case LOW_STOCK:
                    lowStockButton.toggle();
                    break;
                case OUT_OF_STOCK:
                    noStockButton.toggle();
                    break;
                default:
                    inStockButton.toggle();
                    break;

            }

            // Show Alert Dialog
            dialog = alertDialog.show();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            // Setup Event
            sendAvailibilityButton.setOnClickListener(this);
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
        int radioButtonId = availabilityGroup.getCheckedRadioButtonId();
        View radioButton = availabilityGroup.findViewById(radioButtonId);
        int idx = availabilityGroup.indexOfChild(radioButton);
        Availability availability = null;

        switch (idx) {
            case 0:
                availability = Availability.OUT_OF_STOCK;
                break;
            case 1:
                availability = Availability.IN_STOCK;
                break;
            case 2:
                availability = Availability.LOW_STOCK;
                break;
        }
        dismiss();

        parent.onChangeMenuItem(menuItem, availability);
    }
}
