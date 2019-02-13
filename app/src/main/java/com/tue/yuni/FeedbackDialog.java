package com.tue.yuni;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class FeedbackDialog {
    private Context ctx;
    private AlertDialog dialog;

    public FeedbackDialog(Context ctx) {
        this.ctx = ctx;
    }

    public void show(final DialogContent onClick){
        // Create Alert Dialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
        // Inflate Alert Dialog View
        final View view = LayoutInflater.from(ctx).inflate(R.layout.layout_feedback, null);
        alertDialog.setView(view);
        // Show Alert Dialog
        dialog = alertDialog.show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        // Handle the rating text
        final TextView ratingText = view.findViewById(R.id.reviewRatingText);
        final RatingBar ratingBar = view.findViewById(R.id.reviewRating);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating == 0)
                    ratingText.setText("Avoid");
                else if (rating <= 1)
                    ratingText.setText("Very Bad");
                else if (rating <= 2)
                    ratingText.setText("Bad");
                else if (rating <= 3)
                    ratingText.setText("Good");
                else if (rating <= 4)
                    ratingText.setText("Great");
                else
                    ratingText.setText("Awesome, loved it");
            }
        });
        // Send Feedback Button
        Button sendReview = view.findViewById(R.id.sendReview);
        sendReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onSendReview(ratingBar.getRating(), ratingText.getText().toString());
                dialog.dismiss();
            }
        });
    }

    interface DialogContent {
        void onSendReview(float rating, String reviewText);
    }
}
