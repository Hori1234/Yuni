package com.tue.yuni;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class ProductReviewsListViewAdapter extends BaseAdapter {
    private Context ctx;
    private List<ProductReview> reviews;

    public ProductReviewsListViewAdapter(Context ctx, List<ProductReview> reviews) {
        this.ctx = ctx;
        this.reviews = reviews;
    }

    @Override
    public int getCount() {
        if (reviews != null)
            return Math.min(reviews.size(), 10);    // Show only latest 10 Reviews
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return reviews.get(position).ID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate Layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_product_review, parent, false);
        }

        ((TextView)  convertView.findViewById(R.id.reviewText)).setText(reviews.get(position).Text);
        ((RatingBar) convertView.findViewById(R.id.reviewRating)).setRating(reviews.get(position).Rating);

        return convertView;
    }
}
