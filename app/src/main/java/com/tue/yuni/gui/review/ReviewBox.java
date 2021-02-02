package com.tue.yuni.gui.review;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tue.yuni.models.review.Review;
import com.tue.yuni.R;

import java.util.List;

public class ReviewBox implements View.OnClickListener, View.OnTouchListener {
    private View view;
    private Context ctx;
    private List<Review> reviews;
    private ReviewListViewAdapter adapter;
    private ListView reviewsListView;
    private Button prev, next;
    private int itemsToList;
    private ScrollView scrollView;

    // ScrollView Mode Only
    private View itemsView[];
    private LinearLayout container;
    private RatingBar itemRating[];
    private TextView itemText[];
    private int startItem = 0;


    public ReviewBox(Context ctx, List<Review> reviews) {
        this.ctx = ctx;
        this.reviews = reviews;
        itemsToList = 10;
    }

    public ReviewBox(Context ctx, List<Review> reviews, @NonNull ScrollView scrollView) {
        this.ctx = ctx;
        this.reviews = reviews;
        this.scrollView = scrollView;
        itemsToList = 10;

        itemsView = new View[itemsToList];
        itemRating = new RatingBar[itemsToList];
        itemText = new TextView[itemsToList];
    }

    @SuppressWarnings({"all"})
    public View getView(){
        // Inflate Alert Dialog View
        view = LayoutInflater.from(ctx).inflate(R.layout.layout_reviews, null);
        // List View
        if (scrollView == null) {
            reviewsListView = view.findViewById(R.id.reviewsList);
            adapter = new ReviewListViewAdapter(ctx, reviews, itemsToList);
            reviewsListView.setAdapter(adapter);
        } else {
            container = view.findViewById(R.id.reviewsContainer);
            // Remove ListView
            container.removeView(view.findViewById(R.id.reviewsList));
            // Instantiate all reviews view
            for (int i = 0; i < itemsToList; i++) {
                itemsView[i] = LayoutInflater.from(ctx).inflate(R.layout.layout_review, null);
                itemRating[i] = itemsView[i].findViewById(R.id.reviewRating);
                itemText[i] = itemsView[i].findViewById(R.id.reviewText);
                container.addView(itemsView[i], i);
            }
            setListItems(false);
        }

        // Create and Setup Buttons
        setButton();

        // Return View
        return view;
    }

    public void setReviewsPage(int page){
        startItem = page * itemsToList;
        if (!(reviews != null && startItem < reviews.size())) {
            startItem = 0;
        }
    }

    public int getReviewsPage(){
        return (startItem / itemsToList);
    }

    private void setListItems(boolean scroll){
        if (scrollView != null) {
            for (int i = 0; i < itemsView.length; i++) {
                if ((startItem + i) < reviews.size()) {
                    itemsView[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    itemsView[i].setVisibility(View.VISIBLE);
                    itemRating[i].setRating(reviews.get(startItem + i).getRating());
                    itemText[i].setText(reviews.get(startItem + i).getDescription());
                    itemText[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                } else {
                    itemsView[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
                    itemsView[i].setVisibility(View.INVISIBLE);
                }
            }
            // Scroll to top of the list with the ScrollView
            if (scroll) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        itemsView[0].getParent().requestChildFocus(itemsView[0], itemsView[0]);
                        //scrollView.smoothScrollTo(0, container.getTop() + scrollView.getHeight());
                    }
                });
            }
        } else {
            // Set Top Item
            adapter.setStartItem(startItem + itemsToList);
            // Notify Adapter
            adapter.notifyDataSetChanged();
            // Scroll to top
            if (scroll) {
                reviewsListView.smoothScrollToPosition(0);
            }
        }
    }

    private void setButton(){
        // Buttons
        if (reviews != null && reviews.size() > itemsToList) {
            LinearLayout buttonsContainer = view.findViewById(R.id.buttonsContainer);
            // Create Previous Button
            prev = new Button(ctx, null, 0, R.style.Widget_AppCompat_Button_Borderless_Colored);
            prev.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
            prev.setText(ctx.getString(R.string.prev));
            prev.setOnClickListener(this);
            // Create Next Button
            next = new Button(ctx, null, 0, R.style.Widget_AppCompat_Button_Borderless_Colored);
            next.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
            next.setText(ctx.getString(R.string.next));
            next.setOnClickListener(this);
            // Add button to the button container
            buttonsContainer.addView(prev);
            buttonsContainer.addView(next);
            // Setup buttons visibility
            if (startItem == 0)
                prev.setVisibility(View.INVISIBLE);
            if ((startItem + itemsToList) >= reviews.size())
                next.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(next)){
            startItem += itemsToList;
            setListItems(true);
            // Buttons Visibility
            prev.setVisibility(View.VISIBLE);
            if ((reviews.size() - startItem) < itemsToList)
                next.setVisibility(View.INVISIBLE);
        }
        else if (v.equals(prev)) {
            startItem -= itemsToList;
            setListItems(true);
            // Buttons Visibility
            next.setVisibility(View.VISIBLE);
            if (startItem == 0)
                prev.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Must Disable ListView scrolling if it is inside a ScrollView
        if (scrollView != null) {
            return (event.getAction() == MotionEvent.ACTION_MOVE);
        }
        return false;
    }
}
