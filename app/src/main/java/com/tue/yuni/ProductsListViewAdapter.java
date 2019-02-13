package com.tue.yuni;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Inflate Layout for each list row
        if (convertView == null) {
            // Default Layout
            convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_product, parent, false);
            // Check if the current list Item needs to the extended view
            if (position == extendedViewItem) {   // Default Layout
                LinearLayout extensionLayout = convertView.findViewById(R.id.extendedDetails);
                extensionLayout.addView(LayoutInflater.from(ctx).inflate(R.layout.layout_product_extended, null,false));
            }
        } else {
            LinearLayout extensionLayout = convertView.findViewById(R.id.extendedDetails);
            // Check if the current list Item needs to be a Default or Extended Layout
            if (position == extendedViewItem && extensionLayout.getChildCount() == 0) {
                extensionLayout.addView(LayoutInflater.from(ctx).inflate(R.layout.layout_product_extended, null,false));
            } else if (position != extendedViewItem && extensionLayout.getChildCount() > 0){
                extensionLayout.removeAllViews();
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
            ImageView imageView = convertView.findViewById(R.id.productImage);
            imageView.setTag(products.get(position).ID);
            new AsyncImageViewLoader(ctx, products.get(position), imageView).execute();
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageDialog(products.get(position));
                }
            });
            // Reviews List
            LinearLayout reviewsContainer = convertView.findViewById(R.id.productReviews);
            // Check whether or not there is at least 1 review to display
            if (products.get(position).Reviews != null && products.get(position).Reviews.size() > 0) {
                // Display at most 2 Reviews
                for (int i = 0; i < Math.min(2, products.get(position).Reviews.size()); i++) {
                    View view = LayoutInflater.from(ctx).inflate(R.layout.layout_product_review_mini, null);
                    ((TextView) view.findViewById(R.id.reviewText)).setText(products.get(position).Reviews.get(i).Text);
                    ((RatingBar) view.findViewById(R.id.reviewRating)).setRating(products.get(position).Reviews.get(i).Rating);
                    reviewsContainer.addView(view);
                }
                // Display View More button if there are more than 2 reviews
                if (products.get(position).Reviews.size() > 2) {
                    Button viewMoreReviews = new Button(ctx,null, 0, R.style.Widget_AppCompat_Button_Borderless_Colored);
                    // Focus Settings necessary to keep list item clickable
                    viewMoreReviews.setFocusable(false);
                    viewMoreReviews.setFocusableInTouchMode(false);
                    // Setup Button Text
                    viewMoreReviews.setText("View more");
                    // Add button to the layout
                    reviewsContainer.addView(viewMoreReviews);
                    // Setup Button On Click
                    viewMoreReviews.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ReviewsDialog(products.get(position));
                        }
                    });
                }
            } else {
                reviewsContainer.addView(createTextView(ctx, "No Reviews Available!"));
            }
        }

        // Return the instantiated row
        return convertView;
    }

    private void ImageDialog(Product product){
        // Create Alert Dialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
        // Inflate Alert Dialog View
        final View view = LayoutInflater.from(ctx).inflate(R.layout.layout_product_image_dialog, null);
        alertDialog.setView(view);
        // Load Image
        ImageView imageView = view.findViewById(R.id.imageContainer);
        imageView.setTag(product.ID);
        new AsyncImageViewLoader(ctx, product, imageView).execute();
        // Show Alert Dialog
        final AlertDialog dialog = alertDialog.show();
        // Setup Alert Dialog Layout Params
        int size;
        if (Resources.getSystem().getDisplayMetrics().widthPixels > Resources.getSystem().getDisplayMetrics().heightPixels)
            size = Resources.getSystem().getDisplayMetrics().heightPixels - 100;
        else
            size = Resources.getSystem().getDisplayMetrics().widthPixels - 100;
        dialog.getWindow().setLayout(size, size);
        // Make Alert Dialog Background Invisible
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Make Dialog Close on Touch
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dialog.dismiss();
                return false;
            }
        });
    }

    private void ReviewsDialog(Product product) {
        // Create Alert Dialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
        // Setup List Adapter
        alertDialog.setAdapter(new ProductReviewsListViewAdapter(ctx, product), null);
        // Show Dialog
        alertDialog.show();
    }

    private TextView createTextView(Context ctx, String text) {
        TextView textView = new TextView(ctx);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setText(text);
        return textView;
    }
}
