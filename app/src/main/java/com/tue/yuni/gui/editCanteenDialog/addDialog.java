package com.tue.yuni.gui.editCanteenDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

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


    public addDialog(@NonNull Context ctx) {
        this.ctx = ctx;
    }

    @SuppressWarnings({"all"})
    public void show(AddDialogContent parent, MenuItem menuItem, Canteen canteen){
        // Instantiate dialog only if it doesn't already exist
        if (dialog == null) {
            this.menuItem=menuItem;
            this.parent = parent;
            this.canteen = canteen;
            // Create Alert Dialog
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
            // Inflate Alert Dialog View
            View view = LayoutInflater.from(ctx).inflate(R.layout.layout_add_menu_item, null);
            alertDialog.setView(view);
            // Get View UI Elements
            Button deleteButton = view.findViewById(R.id.addDish);
            // Show Alert Dialog
            dialog = alertDialog.show();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            // Setup Events
            deleteButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // Notify the parent of the feedback given

                    dismiss();
                    parent.onAddMenuItem(menuItem, canteen);
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
