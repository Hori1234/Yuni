package com.tue.yuni;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class ReviewsListViewAdapter extends BaseAdapter {
    private Context ctx;
    private List<ProductReview> reviews;
    private int startItem, itemsToList;

    public ReviewsListViewAdapter(Context ctx, List<ProductReview> reviews, int itemsToList) {
        this.ctx = ctx;
        this.reviews = reviews;
        this.itemsToList = itemsToList;
    }

    public int getStartItem() {
        return startItem;
    }

    public void setStartItem(int startItem) {
        this.startItem = startItem;
    }

    @Override
    public int getCount() {
        if (reviews != null) {
            if (startItem + itemsToList < reviews.size())
                return itemsToList;
            else
                return (reviews.size() - startItem);
        }
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(startItem + position);
    }

    @Override
    public long getItemId(int position) {
        return reviews.get(startItem + position).ID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate Layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_product_review, parent, false);
        }

        ((TextView)  convertView.findViewById(R.id.reviewText)).setText(reviews.get(startItem + position).Text);
        ((RatingBar) convertView.findViewById(R.id.reviewRating)).setRating(reviews.get(startItem + position).Rating);

        return convertView;
    }
}
