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

import com.tue.yuni.R;
import com.tue.yuni.gui.review.FeedbackDialog;
import com.tue.yuni.gui.review.ReviewBox;
import com.tue.yuni.gui.util.AvailabilityIndicator;
import com.tue.yuni.models.Day;
import com.tue.yuni.models.canteen.Canteen;
import com.tue.yuni.models.review.CanteenReview;
import com.tue.yuni.storage.RemoteStorage;

import java.util.ArrayList;
import java.util.List;


public class CanteenInfoTab extends Fragment implements RemoteStorage.CanteenReviewsDataHandler, RemoteStorage.ErrorHandler, View.OnClickListener {
    private Canteen canteen;

    private View view;
    private AvailabilityIndicator busyness;
    private TextView busynessText;
    private TextView[] dayHoursTextView = new TextView[7];
    private TextView descriptionTextView;
    private RatingBar ratingBar;
    private LinearLayout reviewBoxContainer;
    Button leaveReview;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Instantiate View
        view = inflater.inflate(R.layout.layout_canteen_info, null);

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
        reviewBoxContainer = view.findViewById(R.id.reviewsContainer);
        leaveReview = view.findViewById(R.id.sendReview);

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
            if (canteen.getOperatingTimes().isOpen(Day.values()[i])) {
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
        // Reviews
        RemoteStorage.get().getCanteenReviews(canteen.getId(), this, this);
        // Feedback Button
        leaveReview.setOnClickListener(this);

        // Return view
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

    @Override
    public void onClick(View v) {
        if (v == leaveReview) {
            new FeedbackDialog(getContext()).show(new FeedbackDialog.DialogContent() {
                @Override
                public void onSendReview(float rating, String reviewText, int ID) {
                    RemoteStorage.get().createCanteenReview(
                            ID,
                            rating,
                            reviewText,
                            () -> {
                                // TODO
                            },
                            e -> {
                                // ToDo
                            }
                    );
                }
            }, canteen.getId());
        }
    }

    @Override
    public void onReceive(List<CanteenReview> reviews) {
        // Clear Container Content
        reviewBoxContainer.removeAllViews();
        // Add new ReviewBox
        ReviewBox reviewBox = new ReviewBox(getContext(), new ArrayList<>(reviews), (ScrollView) view);
        reviewBoxContainer.addView(reviewBox.getView());
    }

    @Override
    public void onError(Exception e) {

    }
}
