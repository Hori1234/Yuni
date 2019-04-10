package com.tue.yuni.gui.canteenDetails;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tue.yuni.R;
import com.tue.yuni.gui.editCanteenDialog.CanteenDialogContent;
import com.tue.yuni.gui.editCanteenDialog.descriptionCanteenDialog;
import com.tue.yuni.gui.editCanteenDialog.timesCanteenDialog;
import com.tue.yuni.gui.review.ReviewBox;
import com.tue.yuni.gui.util.TrafficLightIndicator;
import com.tue.yuni.models.Day;
import com.tue.yuni.models.canteen.Canteen;
import com.tue.yuni.models.canteen.OperatingTimes;
import com.tue.yuni.models.review.CanteenReview;
import com.tue.yuni.models.review.Review;
import com.tue.yuni.services.mapper.BusynessMapper;
import com.tue.yuni.services.network.NetworkService;
import com.tue.yuni.storage.PasswordStorage;
import com.tue.yuni.storage.RemoteStorage;

import java.util.ArrayList;
import java.util.List;


public class CanteenInfoEditTab extends Fragment implements RemoteStorage.CanteenReviewsDataHandler, RemoteStorage.RequestCompletedHandler,  CanteenDialogContent, RemoteStorage.ErrorHandler, View.OnClickListener {
    private Canteen canteen;

    private View view;
    private TrafficLightIndicator busyness;
    private TextView busynessText;
    private TextView[] dayHoursTextView = new TextView[7];
    private TextView descriptionTextView;
    private LinearLayout reviewBoxContainer;

    ImageButton changeImage;
    ImageButton editCanteenTimes;
    ImageButton editDescription;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Instantiate View
        view = inflater.inflate(R.layout.layout_canteen_info_edit, null);

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

        reviewBoxContainer = view.findViewById(R.id.reviewsContainer);

        changeImage     = view.findViewById(R.id.editCanteenImage);
        editCanteenTimes= view.findViewById(R.id.editCanteenTimes);
        editDescription = view.findViewById(R.id.editCanteenDescription);

        changeImage.setOnClickListener(this);
        editCanteenTimes.setOnClickListener(this);
        editDescription.setOnClickListener(this);

        // Busyness
        busyness.setState(BusynessMapper.getState(canteen.getBusyness()));
        busynessText.setText(getContext().getString(BusynessMapper.getTextResource(canteen.getBusyness())));

        // Opening Hours
        displayOpeningHours();

        // Description
        descriptionTextView.setText(canteen.getDescription());

        // Reviews
        RemoteStorage.get().getCanteenReviews(canteen.getId(), this, this);



        // Return view
        return view;
    }

    private void displayOpeningHours() {
        for (int i = 0; i < 7; i++) {
            // Check if Canteen is open on day i
            if (canteen.getOperatingTimes().isOpen(Day.values()[i])) {
                // Parse Time
                int openTime = canteen.getOperatingTimes().getOpeningTime(Day.values()[i]);
                int closeTime = canteen.getOperatingTimes().getClosingTime(Day.values()[i]);
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
        if (NetworkService.networkAvailabilityHandler(getActivity().getApplicationContext())) {
            if (v == changeImage) {
                //TODO new timesCanteenDialog(getContext()).show(this, canteen);
            } else if (v == editCanteenTimes) {
                new timesCanteenDialog(getContext()).show(this, canteen);

            } else if (v == editDescription) {
                new descriptionCanteenDialog(getContext()).show(this, canteen);
            }
        }
    }

    @Override
    public void onReceive(List<CanteenReview> reviews) {
        // Sort Reviews by Date
        reviews.sort(new Review.CustomComparator());
        // Clear Container Content
        reviewBoxContainer.removeAllViews();
        // Add new ReviewBox
        ReviewBox reviewBox = new ReviewBox(getContext(), new ArrayList<>(reviews), (ScrollView) view);
        reviewBoxContainer.addView(reviewBox.getView());
    }

    @Override
    public void onError(Exception e) {
        //TODO
    }

    @Override
    public void onChangeCanteen(Canteen canteen) {
        //TODO
    }

    @Override
    public void onChangeCanteen(OperatingTimes times){
        RemoteStorage.get().updateCanteen(PasswordStorage.get().getPassword(),canteen.getId(),canteen.getName(),canteen.getDescription(),times,() -> {
            canteen.setOperatingTimes(times);
            displayOpeningHours();
        },this);
    }

    @Override
    public void onChangeCanteen(String description){
        RemoteStorage.get().updateCanteen(PasswordStorage.get().getPassword(),canteen.getId(),canteen.getName(),description,canteen.getOperatingTimes(),() -> {
            canteen.setDescription(description);
            descriptionTextView.setText(canteen.getDescription());
        },this);
    }

    @Override
    public void onCompleted() {
        //TODO
    }
}
