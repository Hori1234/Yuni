package com.tue.yuni.Dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tue.yuni.R;

public class FeedbackDialog {
    private Context ctx;
    private AlertDialog dialog;

    public FeedbackDialog(Context ctx) {
        this.ctx = ctx;
    }

    @SuppressLint("ClickableViewAccessibility")
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
        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
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

    public interface DialogContent {
        void onSendReview(float rating, String reviewText);
    }
}
