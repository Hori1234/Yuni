package com.tue.yuni.gui.review;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tue.yuni.models.Review;
import com.tue.yuni.R;

import java.util.List;

public class ReviewBox {
    private Context ctx;
    private List<Review> reviews;
    private ReviewListViewAdapter adapter;
    private ListView reviewsListView;
    private Button prev, next;
    private View.OnClickListener onPageChanged;

    public ReviewBox(Context ctx, List<Review> reviews) {
        this.ctx = ctx;
        this.reviews = reviews;
    }

    public View getView(){
        // Inflate Alert Dialog View
        final View view = LayoutInflater.from(ctx).inflate(R.layout.layout_reviews, null);
        // List View
        reviewsListView = view.findViewById(R.id.reviewsList);
        adapter = new ReviewListViewAdapter(ctx, reviews, 10);
        reviewsListView.setAdapter(adapter);
        // Buttons
        if (reviews != null && reviews.size() > 10) {
            LinearLayout buttonsContainer = ((LinearLayout)view.findViewById(R.id.buttonsContainer));
            prev = new Button(ctx, null, 0, R.style.Widget_AppCompat_Button_Borderless_Colored);
            prev.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
            prev.setText(ctx.getString(R.string.prev));
            next = new Button(ctx, null, 0, R.style.Widget_AppCompat_Button_Borderless_Colored);
            next.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
            next.setText(ctx.getString(R.string.next));
            buttonsContainer.addView(prev);
            buttonsContainer.addView(next);
            prev.setVisibility(View.INVISIBLE);
            if (reviews.size() < 10)
                next.setVisibility(View.INVISIBLE);
            prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.setStartItem(adapter.getStartItem() - 10);
                    adapter.notifyDataSetChanged();
                    reviewsListView.smoothScrollToPosition(0);
                    next.setVisibility(View.VISIBLE);
                    if (adapter.getStartItem() == 0)
                        prev.setVisibility(View.INVISIBLE);
                    // Notify Page has changed
                    onPageChanged.onClick(reviewsListView);
                }
            });
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.setStartItem(adapter.getStartItem() + 10);
                    adapter.notifyDataSetChanged();
                    reviewsListView.smoothScrollToPosition(0);
                    prev.setVisibility(View.VISIBLE);
                    if ((reviews.size() - adapter.getStartItem()) < 10)
                        next.setVisibility(View.INVISIBLE);
                    // Notify Page has changed
                    onPageChanged.onClick(reviewsListView);
                }
            });
        }
        // Return View
        return view;
    }

    public ListView getReviewsListView() {
        return reviewsListView;
    }

    public void setOnPageChanged(View.OnClickListener onPageChanged) {
        this.onPageChanged = onPageChanged;
    }
}
