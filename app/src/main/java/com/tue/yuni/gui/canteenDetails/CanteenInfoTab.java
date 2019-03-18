package com.tue.yuni.gui.canteenDetails;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tue.yuni.gui.review.FeedbackDialog;
import com.tue.yuni.gui.review.ReviewBox;
import com.tue.yuni.R;
import com.tue.yuni.gui.util.AvailabilityIndicator;
import com.tue.yuni.models.Day;
import com.tue.yuni.models.Review;
import com.tue.yuni.models.canteen.Canteen;

import java.util.ArrayList;


public class CanteenInfoTab extends Fragment {
    private Canteen canteen;
    private AvailabilityIndicator busyness;
    private TextView busynessText;
    private TextView[] dayHoursTextView = new TextView[7];
    private TextView descriptionTextView;
    private RatingBar ratingBar;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Instantiate View
        final View view = inflater.inflate(R.layout.layout_canteen_info, null);
        // Find All UI Elements
        busyness = view.findViewById(R.id.busyness);
        busynessText = view.findViewById(R.id.busynessText);
        dayHoursTextView[0] = view.findViewById(R.id.mondayHours);
        dayHoursTextView[1] = view.findViewById(R.id.tuesdayHours);
        dayHoursTextView[2] = view.findViewById(R.id.wednesdayHours);
        dayHoursTextView[3] = view.findViewById(R.id.thursdayHours);
        dayHoursTextView[4] = view.findViewById(R.id.fridayHours);
        dayHoursTextView[5] = view.findViewById(R.id.saturdayHours);
        dayHoursTextView[6] = view.findViewById(R.id.sundayHours);
        descriptionTextView = view.findViewById(R.id.descriptionStore);
        ratingBar = view.findViewById(R.id.reviewRating);

        // Busyness
        int busynessVal = 100 - canteen.getBusyness();
        busyness.setAvailability(busynessVal);
        if (0 <= busynessVal && busynessVal < busyness.getThreshold(0)) {
            busynessText.setText(getContext().getString(R.string.busy));
        } else if (busyness.getThreshold(0) <= busynessVal && busynessVal < busyness.getThreshold(1)) {
            busynessText.setText(getContext().getString(R.string.moderate));
        } else if (busyness.getThreshold(1) <= busynessVal && busynessVal < busyness.getThreshold(2)) {
            busynessText.setText(getContext().getString(R.string.quiet));
        }
        // Opening Hours
        for (int i = 0; i < 7; i++) {
            // Check if Canteen is open on day i
            if (canteen.getOperatingTimes().isOpen(Day.values()[i])){
                // Parse Time
                int openTime = Integer.parseInt(canteen.getOperatingTimes().getOpeningTime(Day.values()[i]));
                int closeTime = Integer.parseInt(canteen.getOperatingTimes().getClosingTime(Day.values()[i]));
                int hOpen = openTime / 100;
                int mOpen = openTime % 100;
                int hClose = closeTime / 100;
                int mClose = openTime % 100;
                String open, close;
                open = (hOpen > 12) ? (hOpen - 12) + ":" + String.format("%02d", mOpen) + " PM" : hOpen + ":" + String.format("%02d", mOpen) + " AM";
                close = (hClose > 12) ? (hClose - 12) + ":" + String.format("%02d", mClose) + " PM" : hClose + ":" + String.format("%02d", mClose) + " AM";
                // Display Time
                dayHoursTextView[i].setText(open + " - " + close);
            } else {
                dayHoursTextView[i].setText(getContext().getString(R.string.closed));
            }
        }
        // Description
        descriptionTextView.setText(canteen.getDescription());
        // Overall Canteen Rating
        ratingBar.setRating(canteen.getRating());

        // Reviews ToDo
        ReviewBox reviews = new ReviewBox(
                getContext(),
                new ArrayList<Review>(){{
                    add(new Review(0, "Hello 1\naaijfoiahjfohjaophjf", 1.5f));
                    add(new Review(1, "Hello 2", 1.5f));
                    add(new Review(2, "Hello 3", 1.5f));
                    add(new Review(3, "Hello 4", 1.5f));
                    add(new Review(4, "Hello 5", 1.5f));
                    add(new Review(5, "Hello 6", 1.5f));
                    add(new Review(6, "Hello 7", 1.5f));
                    add(new Review(7, "Hello 8", 1.5f));
                    add(new Review(8, "Hello 9", 1.5f));
                    add(new Review(9, "Hello 10", 1.5f));
                    add(new Review(10, "Hello 11", 1.5f));
                    add(new Review(11, "Hello 12\noiahwaopyhgtpoawerh", 1.5f));
                    add(new Review(12, "Hello 13", 1.5f));
                    add(new Review(13, "Hello 14\nhfoeahfpoawohpo\nafjhaoi[fjh", 1.5f));
                    add(new Review(14, "Hello 15", 1.5f));
                    add(new Review(15, "Hello 16 what is this sourcery this is so weird but it works hell yea what is this funkyness test test test", 1.5f)); // This breaks listView height
                    add(new Review(16, "Hello 17", 1.5f));
                    add(new Review(17, "Hello 18", 1.5f));
                    add(new Review(18, "Hello 19\nouwpahgoruhgpo", 1.5f));
                    add(new Review(19, "Hello 20", 1.5f));
                    add(new Review(20, "Hello 21", 1.5f));
                    add(new Review(21, "Hello 22", 1.5f));
                }}, (ScrollView)view);
        View viewReviews = reviews.getView();
        // Add view to the layout
        ((LinearLayout)view.findViewById(R.id.reviewsContainer)).addView(viewReviews);
        // Add Feedback button to layout
        Button leaveReview = new Button(getContext(),null, 0, R.style.Widget_AppCompat_Button_Colored);
        leaveReview.setText(getContext().getString(R.string.feedback));
        leaveReview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        leaveReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FeedbackDialog(getContext()).show(new FeedbackDialog.DialogContent() {
                    @Override
                    public void onSendReview(float rating, String reviewText) {
                        // Handle Review Contents
                    }
                });
            }
        });
        // Add Feedback button to the layout
        ((LinearLayout)view.findViewById(R.id.reviewsContainer)).addView(leaveReview);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // ToDO ListView Position and Reviews page
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        if (args != null && args.containsKey("Canteen")) {
            canteen = args.getParcelable("Canteen");
        }
    }
}
