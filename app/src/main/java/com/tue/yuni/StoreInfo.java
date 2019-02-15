package com.tue.yuni;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tue.yuni.Dialogs.ReviewsPage;

import java.util.ArrayList;

public class StoreInfo extends Fragment {
    private TextView[] dayHours = new TextView[7];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_store_info, null);
        dayHours[0] = view.findViewById(R.id.mondayHours);
        dayHours[1] = view.findViewById(R.id.tuesdayHours);
        dayHours[2] = view.findViewById(R.id.wednesdayHours);
        dayHours[3] = view.findViewById(R.id.thursdayHours);
        dayHours[4] = view.findViewById(R.id.fridayHours);
        dayHours[5] = view.findViewById(R.id.saturdayHours);
        dayHours[6] = view.findViewById(R.id.sundayHours);

        ReviewsPage reviews = new ReviewsPage(
                getContext(),
                new ArrayList<Review>(){{
                    add(new Review(0, "Hello 1", 1.5f));
                    add(new Review(0, "Hello 2", 1.5f));
                    add(new Review(0, "Hello 3", 1.5f));
                    add(new Review(0, "Hello 4", 1.5f));
                    add(new Review(0, "Hello 5", 1.5f));
                    add(new Review(0, "Hello 6", 1.5f));
                    add(new Review(0, "Hello 7", 1.5f));
                    add(new Review(0, "Hello 8", 1.5f));
                    add(new Review(0, "Hello 9", 1.5f));
                    add(new Review(0, "Hello 10", 1.5f));
                    add(new Review(0, "Hello 11", 1.5f));
                    add(new Review(0, "Hello 12", 1.5f));
                    add(new Review(0, "Hello 13", 1.5f));
                    add(new Review(0, "Hello 14", 1.5f));
                    add(new Review(0, "Hello 15", 1.5f));
                }});
        ((LinearLayout)view.findViewById(R.id.reviewsContainer)).addView(reviews.getView());
        return view;
    }
}
