package com.tue.yuni.gui.editCanteenDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.tue.yuni.R;
import com.tue.yuni.models.ExtendedMenuItem;
import com.tue.yuni.models.MenuItem;
import com.tue.yuni.models.canteen.Canteen;

public class addDialog {
    private Context ctx;
    private AlertDialog dialog;
    private AddDialogContent parent;
    private MenuItem menuItem;
    private Canteen canteen;
    private TextView addSure;

    /*
     *A dialog conforming whether you want to add a menuItem to the current menu
     * needs a menuItem and a canteen to add it to
     * and a parent where the actual posting will be handled
     */
    public addDialog(@NonNull Context ctx) {
        this.ctx = ctx;
    }

    @SuppressWarnings({"all"})
    public void show(AddDialogContent parent, MenuItem menuItem, Canteen canteen){
        // Instantiate dialog only if it doesn't already exist
        if (dialog == null) {
            //retrieve the menuItem to be added
            this.menuItem=menuItem;
            // where this needs to be hadled
            this.parent = parent;
            //to what canteen it needs to be applied
            this.canteen = canteen;

            // Create Alert Dialog
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
            // Inflate Alert Dialog View
            View view = LayoutInflater.from(ctx).inflate(R.layout.layout_add_menu_item, null);

            alertDialog.setView(view);
            // Get View UI Elements
            Button addButton = view.findViewById(R.id.addDish);

            // this Text View
//            addSure = view.findViewById(R.id.certaintyAdd);
//            addSure.setText("add " + menuItem.getName()+ " to "+ canteen.getName());
            
            // Show Alert Dialog
            dialog = alertDialog.show();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            // add button behavior
            addButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    // dismiis the dialog
                    dismiss();
                    // Notify the parent of the feedback given
                    parent.onAddMenuItem(menuItem, canteen);
                }
            });
            //Cancel button to dismiss the current dialog
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
