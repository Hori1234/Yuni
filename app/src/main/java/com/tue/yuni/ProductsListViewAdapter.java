package com.tue.yuni;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class ProductsListViewAdapter extends BaseAdapter {
    private Context ctx;
    private List<Product> products;
    private int extendedViewItem = -1;

    public ProductsListViewAdapter(Context ctx, List<Product> products) {
        this.ctx = ctx;
        this.products = products;
    }

    public void setExtendedViewItem(int extendedViewItem) {
        if (extendedViewItem == this.extendedViewItem)
            this.extendedViewItem = -1;
        else
            this.extendedViewItem = extendedViewItem;
        this.notifyDataSetChanged();
    }

    public int getExtendedViewItem() {
        return extendedViewItem;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return products.get(position).ID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate Layout for each list row
        if (convertView == null) {
            // Check if the current list Item needs to be a Default or Extended Layout
            if (position != extendedViewItem)   // Default Layout
                convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_product, parent, false);
            else                                // Extended Layout
                convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_product_extended, parent, false);
        } else {
            // Check if the current list Item needs to be a Default or Extended Layout
            if (position == extendedViewItem) {
                // If there is no ImageView it means we need to instantiate the Extended Layout
                if (convertView.findViewById(R.id.productImage) == null)
                    convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_product_extended, parent, false);
            } else {
                // If there is a ImageView it means we need to instantiate the Default Layout
                if (convertView.findViewById(R.id.productImage) != null)
                    convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_product, parent, false);
            }
        }

        // Setup the Item parameters
        // Default View
        ((TextView) convertView.findViewById(R.id.productName)).setText(products.get(position).Name);
        ((RatingBar) convertView.findViewById(R.id.productRating)).setRating(products.get(position).Rating);
        ((AvailabilityIndicator) convertView.findViewById(R.id.productAvailability)).setAvailability(products.get(position).Availability);
        ((TextView) convertView.findViewById(R.id.productPrice)).setText(String.format("%.2f€", products.get(position).Price));
        // Extended View
        if (position == extendedViewItem) {
            // Item Image
            ((ImageView) convertView.findViewById(R.id.productImage)).setImageResource(products.get(position).Picture);
            // Reviews List
            LinearLayout reviewsContainer = convertView.findViewById(R.id.productReviews);
            if (products.get(position).Reviews != null && products.get(position).Reviews.size() > 0) {
                for (int i = 0; i < Math.min(2, products.get(position).Reviews.size()); i++) {
                    View view = LayoutInflater.from(ctx).inflate(R.layout.layout_product_review, null);
                    ((TextView) view.findViewById(R.id.reviewText)).setText(products.get(position).Reviews.get(i).Text);
                    ((RatingBar) view.findViewById(R.id.reviewRating)).setRating(products.get(position).Reviews.get(i).Rating);
                    reviewsContainer.addView(view);
                }
            } else {
                reviewsContainer.addView(createTextView(ctx, "No Reviews Available!"));
            }
        }

        // Return the instantiated row
        return convertView;
    }

    private TextView createTextView(Context ctx, String text) {
        TextView textView = new TextView(ctx);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setText(text);
        return textView;
    }
}
