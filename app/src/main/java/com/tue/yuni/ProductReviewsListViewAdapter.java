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
    private Product product;

    public ProductReviewsListViewAdapter(Context ctx, Product product) {
        this.ctx = ctx;
        this.product = product;
    }

    @Override
    public int getCount() {
        if (product.Reviews != null)
            return product.Reviews.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return product.Reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return product.Reviews.get(position).ID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate Layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_product_review, parent, false);
        }

        ((TextView)  convertView.findViewById(R.id.reviewText)).setText(product.Reviews.get(position).Text);
        ((RatingBar) convertView.findViewById(R.id.reviewRating)).setRating(product.Reviews.get(position).Rating);

        return convertView;
    }
}
