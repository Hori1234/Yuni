package com.tue.yuni.gui.review;

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

public class FeedbackDialog implements View.OnClickListener, View.OnTouchListener{
    private Context ctx;
    private AlertDialog dialog;
    private RatingBar ratingBar;
    private TextView ratingText;
    private EditText reviewText;
    private DialogContent parent;

    public FeedbackDialog(@NonNull Context ctx) {
        this.ctx = ctx;
    }

    @SuppressWarnings({"all"})
    public void show(@NonNull DialogContent parent){
        // Instantiate dialog only if it doesn't already exist
        if (dialog == null) {
            this.parent = parent;
            // Create Alert Dialog
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
            // Inflate Alert Dialog View
            View view = LayoutInflater.from(ctx).inflate(R.layout.layout_feedback, null);
            alertDialog.setView(view);
            // Get View UI Elements
            Button sendReview = view.findViewById(R.id.sendReview);
            ratingBar = view.findViewById(R.id.reviewRating);
            ratingText = view.findViewById(R.id.reviewRatingText);
            reviewText = view.findViewById(R.id.reviewText);
            // Show Alert Dialog
            dialog = alertDialog.show();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            // Setup Event
            ratingBar.setOnTouchListener(this);
            sendReview.setOnClickListener(this);
        }
    }

    public void dimiss() {
        // Dimiss Dialog only if it exists
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void onClick(View v) {
        // Notify the parent of the feedback given
        float rating = ratingBar.getRating();
        String text = reviewText.getText().toString();
        dimiss();
        parent.onSendReview(rating, text);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Change Text depending on the amount of stars
        if (ratingBar.getRating() == 0)
            ratingText.setText(ctx.getString(R.string.stars_0));
        else if (ratingBar.getRating() <= 1)
            ratingText.setText(ctx.getString(R.string.stars_1));
        else if (ratingBar.getRating() <= 2)
            ratingText.setText(ctx.getString(R.string.stars_2));
        else if (ratingBar.getRating() <= 3)
            ratingText.setText(ctx.getString(R.string.stars_3));
        else if (ratingBar.getRating() <= 4)
            ratingText.setText(ctx.getString(R.string.stars_4));
        else
            ratingText.setText(ctx.getString(R.string.stars_5));
        return false;
    }

    public interface DialogContent {
        void onSendReview(float rating, String reviewText);
    }
}
