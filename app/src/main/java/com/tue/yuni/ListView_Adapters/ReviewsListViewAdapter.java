package com.tue.yuni.ListView_Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tue.yuni.DataStructures.Review;
import com.tue.yuni.R;

import java.util.List;

public class ReviewsListViewAdapter extends BaseAdapter {
    private Context ctx;
    private List<Review> reviews;
    private int startItem, itemsToList;

    public ReviewsListViewAdapter(Context ctx, List<Review> reviews, int itemsToList) {
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
            convertView.setTag(reviews.get(startItem + position).ID);
        } else {
            if ((int)convertView.getTag() != reviews.get(startItem + position).ID) {
                convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_product_review, parent, false);
                convertView.setTag(reviews.get(startItem + position).ID);
            }
        }

        ((TextView)  convertView.findViewById(R.id.reviewText)).setText(reviews.get(startItem + position).Text);
        ((RatingBar) convertView.findViewById(R.id.reviewRating)).setRating(reviews.get(startItem + position).Rating);

        return convertView;
    }
}
