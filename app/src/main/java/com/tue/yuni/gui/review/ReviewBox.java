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
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.tue.yuni.models.review.Review;
import com.tue.yuni.R;

import java.util.List;

public class ReviewBox implements View.OnClickListener, View.OnTouchListener {
    private Context ctx;
    private List<Review> reviews;
    private ReviewListViewAdapter adapter;
    private ListView reviewsListView;
    private Button prev, next;
    private int itemsToList;
    private ScrollView scrollView;

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
    }

    @SuppressWarnings({"all"})
    public View getView(){
        // Inflate Alert Dialog View
        View view = LayoutInflater.from(ctx).inflate(R.layout.layout_reviews, null);
        // List View
        reviewsListView = view.findViewById(R.id.reviewsList);
        adapter = new ReviewListViewAdapter(ctx, reviews, itemsToList);
        reviewsListView.setAdapter(adapter);
        // Buttons
        if (reviews != null && reviews.size() > 10) {
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
            prev.setVisibility(View.INVISIBLE);
            if (reviews.size() < 10)
                next.setVisibility(View.INVISIBLE);
        }
        // Size List appropriately if inside a scrollview
        resizeListView();
        // Return View
        return view;
    }

    private void resizeListView(){
        final int UNBOUNDED = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = 0;
        for (int i = 0; i < reviewsListView.getCount(); i++) {
            View child = reviewsListView.getAdapter().getView(i, null, reviewsListView);
            child.measure(UNBOUNDED, UNBOUNDED);
            height += child.getMeasuredHeight();
        }
        reviewsListView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height + 18));
    }

    @Override
    public void onClick(View v) {
        if (v.equals(next)){
            adapter.setStartItem(adapter.getStartItem() + itemsToList);
            prev.setVisibility(View.VISIBLE);
            if ((reviews.size() - adapter.getStartItem()) < itemsToList)
                next.setVisibility(View.INVISIBLE);
        }
        else if (v.equals(prev)) {
            adapter.setStartItem(adapter.getStartItem() - itemsToList);
            next.setVisibility(View.VISIBLE);
            if (adapter.getStartItem() == 0)
                prev.setVisibility(View.INVISIBLE);
        }
        // Notify Adapter
        adapter.notifyDataSetChanged();
        // Scroll to top
        reviewsListView.smoothScrollToPosition(0);
        // If ScrollView exists
        if (scrollView != null) {
            // Resize ListView
            resizeListView();
            // Scroll to top of the list with the ScrollView
            reviewsListView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.smoothScrollTo(0, reviewsListView.getTop() + scrollView.getHeight());
                }
            });
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
