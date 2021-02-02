package com.tue.yuni.gui.editCanteenDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.tue.yuni.R;
import com.tue.yuni.models.canteen.Canteen;

public class descriptionCanteenDialog {
    private Context ctx;
    private AlertDialog dialog;
    private Canteen canteen;
    private CanteenDialogContent parent;

    EditText description;

    public descriptionCanteenDialog(@NonNull Context ctx) {
        this.ctx = ctx;
    }

    @SuppressWarnings({"all"})
    public void show(CanteenDialogContent parent, Canteen canteen){
        // Instantiate dialog only if it doesn't already exist
        if (dialog == null) {
            this.parent = parent;
            this.canteen = canteen;
            // Create Alert Dialog
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
            // Inflate Alert Dialog View
            View view = LayoutInflater.from(ctx).inflate(R.layout.layout_canteen_info_edit_description, null);
            alertDialog.setView(view);
            // Get View UI Elements

            description       =  view.findViewById(R.id.editDescription);
            description.setText(canteen.getDescription());


            Button sendDescriptionButton = view.findViewById(R.id.setDescriptionButton);
            sendDescriptionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dismiss();

                    parent.onChangeCanteen(description.getText().toString());
                }
            });

            Button cancelButton = view.findViewById(R.id.cancelButton);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });



            // Show Alert Dialog
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



}
