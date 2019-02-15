package com.tue.yuni.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tue.yuni.Review;
import com.tue.yuni.R;
import com.tue.yuni.ListView_Adapters.ReviewsListViewAdapter;

import java.util.List;

public class ReviewsDialog {
    private Context ctx;
    private AlertDialog dialog;
    private List<Review> reviews;
    private ReviewsListViewAdapter adapter;
    private ListView reviewsListView;
    private Button prev, next;

    public ReviewsDialog(Context ctx, List<Review> reviews) {
        this.ctx = ctx;
        this.reviews = reviews;
    }

    public void show(){
        // Create Alert Dialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
        // Inflate Alert Dialog View
        final View view = LayoutInflater.from(ctx).inflate(R.layout.layout_reviews, null);
        alertDialog.setView(view);
        // List View
        reviewsListView = view.findViewById(R.id.reviewsList);
        adapter = new ReviewsListViewAdapter(ctx, reviews, 10);
        reviewsListView.setAdapter(adapter);
        // Buttons
        if (reviews != null && reviews.size() > 10) {
            LinearLayout buttonsContainer = ((LinearLayout)view.findViewById(R.id.buttonsContainer));
            prev = new Button(ctx, null, 0, R.style.Widget_AppCompat_Button_Borderless_Colored);
            prev.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
            prev.setText("Prev");
            next = new Button(ctx, null, 0, R.style.Widget_AppCompat_Button_Borderless_Colored);
            next.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
            next.setText("Next");
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
                    next.setVisibility(View.VISIBLE);
                    if (adapter.getStartItem() == 0)
                        prev.setVisibility(View.INVISIBLE);
                }
            });
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.setStartItem(adapter.getStartItem() + 10);
                    adapter.notifyDataSetChanged();
                    prev.setVisibility(View.VISIBLE);
                    if ((reviews.size() - adapter.getStartItem()) < 10)
                        next.setVisibility(View.INVISIBLE);
                }
            });
        }
        // Show Alert Dialog
        dialog = alertDialog.show();
    }
}
