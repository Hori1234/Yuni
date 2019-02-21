package com.tue.yuni.gui.review;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tue.yuni.models.Review;
import com.tue.yuni.R;

import java.util.List;

public class ReviewListViewAdapter extends BaseAdapter {
    private Context ctx;
    private List<Review> reviews;
    private int startItem, itemsToList;

    public ReviewListViewAdapter(Context ctx, List<Review> reviews, int itemsToList) {
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
        return reviews.get(startItem + position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate Layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_review, parent, false);
            convertView.setTag(reviews.get(startItem + position).id);
        } else {
            if ((int)convertView.getTag() != reviews.get(startItem + position).id) {
                convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_review, parent, false);
                convertView.setTag(reviews.get(startItem + position).id);
            }
        }

        TextView reviewTextView = convertView.findViewById(R.id.reviewText);
        reviewTextView.setText(reviews.get(startItem + position).text);
        reviewTextView.setLines(
                calculateNumberOfTextLines(
                        reviewTextView.getPaint(),
                        reviews.get(startItem + position).text,
                        parent.getWidth())
        );

        ((RatingBar) convertView.findViewById(R.id.reviewRating)).setRating(reviews.get(startItem + position).rating);

        return convertView;
    }

    private int calculateNumberOfTextLines(Paint paint, String text, int width){
        String[] splits = text.split("\\r?\\n");
        if (width > 0) {
            int numberOfLines = 0;

            for (int i = 0; i < splits.length; i++) {
                numberOfLines += (int) Math.ceil(paint.measureText(splits[i]) / (float) width);
            }

            return numberOfLines;
        } else {
            return splits.length;
        }
    }
}
