package com.tue.yuni.gui.canteenDetails;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tue.yuni.gui.review.FeedbackDialog;
import com.tue.yuni.gui.review.ReviewBox;
import com.tue.yuni.R;
import com.tue.yuni.models.Review;

import java.util.ArrayList;

public class CanteenInfoTab extends Fragment {
    private TextView[] dayHoursTextView = new TextView[7];
    private TextView descriptionTextView;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.layout_canteen_info, null);
        dayHoursTextView[0] = view.findViewById(R.id.mondayHours);
        dayHoursTextView[1] = view.findViewById(R.id.tuesdayHours);
        dayHoursTextView[2] = view.findViewById(R.id.wednesdayHours);
        dayHoursTextView[3] = view.findViewById(R.id.thursdayHours);
        dayHoursTextView[4] = view.findViewById(R.id.fridayHours);
        dayHoursTextView[5] = view.findViewById(R.id.saturdayHours);
        dayHoursTextView[6] = view.findViewById(R.id.sundayHours);
        descriptionTextView = view.findViewById(R.id.descriptionStore);

        // Place Holder Description
        descriptionTextView.setText(
                "The Auditorium canteen has a good selection of daily plates for hungry students." +
                        "There are also plenty of smaller dishes. Together with small grocery items" +
                        "and a coffee maker, everybody should find something for their taste here." +
                        "With plenty of spoots in the auditorium area you are guaranteed to find a " +
                        "place to seat."
        );

        // These will have to be passed as arguments for each different store
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
                }});
        View viewReviews = reviews.getView();
        final ListView reviewsListView = reviews.getReviewsListView();
        // Disable ListView Scrolling
        reviewsListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        // Make Sure ListView Shows all items without scrolling
        View.OnClickListener onPageChanged = new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // Resize ListView To Show All Items when page is changed
                final int UNBOUNDED = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                int height = 0;
                for (int i = 0; i < reviewsListView.getCount(); i++) {
                    View child = reviewsListView.getAdapter().getView(i, null, reviewsListView);
                    child.measure(UNBOUNDED, UNBOUNDED);
                    height += child.getMeasuredHeight();
                }
                reviewsListView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height + 10));
                // Scroll to top of the list if view argument is valid
                if (v != null) {
                    reviewsListView.post(new Runnable() {
                        @Override
                        public void run() {
                            ((ScrollView) view).smoothScrollTo(0, reviewsListView.getTop() + view.getHeight());
                        }
                    });
                }
            }
        };
        reviews.setOnPageChanged(onPageChanged);
        // Initial ListView resize therefore do not scroll to list
        onPageChanged.onClick(null);
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
}
