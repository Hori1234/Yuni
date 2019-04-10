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


/*
 * Changes the availability of a menuItem in a certain canteen
 * ExtebdedMenuItem holdsboth canteen and dish ids
 * parent handles the actual posting of the availability
 *
 */
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

            /*
             * each Radio button when pressed clears everything and then toggles itself to be certain
             * only one is pressed at any given time
             */
            inStockButton = view.findViewById(R.id.inStockButton);
            inStockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    availabilityGroup.clearCheck();
                    inStockButton.toggle();
                }
            });
            /*
             * each Radio button when pressed clears everything and then toggles itself to be certain
             * only one is pressed at any given time
             */
            lowStockButton = view.findViewById(R.id.lowStockButton);
            lowStockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    availabilityGroup.clearCheck();
                    lowStockButton.toggle();
                }
            });
            /*
             * each Radio button when pressed clears everything and then toggles itself to be certain
             * only one is pressed at any given time
             */
            noStockButton = view.findViewById(R.id.outStockButton);
            noStockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    availabilityGroup.clearCheck();
                    noStockButton.toggle();
                }
            });

            // toggle the correct button given the menuItem that was given as input
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


    /*
     * Notify the parent of the availability given
     */
    @Override
    public void onClick(View v) {
        //retrieve the id of the checked button
        int radioButtonId = availabilityGroup.getCheckedRadioButtonId();
        // retrieve the view at this ID
        View radioButton = availabilityGroup.findViewById(radioButtonId);
        //check what position this view at in the group
        int idx = availabilityGroup.indexOfChild(radioButton);
        Availability availability = null;

        // Set availability to be passed based on the position of the checked button
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

        // pass the availability and extended menu item to parent to handle posting
        parent.onChangeMenuItem(menuItem, availability);
    }
}
