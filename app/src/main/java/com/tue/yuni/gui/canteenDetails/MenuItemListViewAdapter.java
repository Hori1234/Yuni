package com.tue.yuni.gui.canteenDetails;

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

import com.tue.yuni.gui.util.AsyncImageViewLoader;
import com.tue.yuni.gui.util.AvailabilityIndicator;
import com.tue.yuni.gui.review.FeedbackDialog;
import com.tue.yuni.gui.review.ReviewBox;
import com.tue.yuni.models.Product;
import com.tue.yuni.R;

import java.util.List;

public class MenuItemListViewAdapter extends BaseAdapter {
    private Context ctx;
    private List<Product> products;
    private int extendedViewItem = -1;

    public MenuItemListViewAdapter(Context ctx, List<Product> products) {
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
        return products.get(position).id;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Inflate Layout for each list row
        if (convertView == null) {
            // Default Layout
            convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_menu_item, parent, false);
            // Check if the current list Item needs to the extended view
            if (position == extendedViewItem) {   // Default Layout
                extendedListItem(position, convertView, parent);
            }
        } else {
            LinearLayout extensionLayout = convertView.findViewById(R.id.extendedDetails);
            // Check if the current list Item needs to be a Default or Extended Layout
            if (position == extendedViewItem && extensionLayout.getChildCount() == 0) {
                extendedListItem(position, convertView, parent);
            } else if (position != extendedViewItem && extensionLayout.getChildCount() > 0){
                extensionLayout.removeAllViews();
            }
        }

        // Setup the Item parameters
        // Default View
        ((TextView) convertView.findViewById(R.id.productName)).setText(products.get(position).name);
        ((RatingBar) convertView.findViewById(R.id.productRating)).setRating(products.get(position).rating);
        ((AvailabilityIndicator) convertView.findViewById(R.id.productAvailability)).setAvailability(products.get(position).availability);
        if (convertView.findViewById(R.id.extendView).getVisibility() == View.INVISIBLE && position != extendedViewItem) {
            convertView.findViewById(R.id.extendView).setVisibility(View.VISIBLE);
        }

        // Return the instantiated row
        return convertView;
    }

    private void extendedListItem(final int position, View convertView, ViewGroup parent){
        LinearLayout extensionLayout = convertView.findViewById(R.id.extendedDetails);
        extensionLayout.addView(LayoutInflater.from(ctx).inflate(R.layout.layout_menu_item_extension, null,false));
        // Retract Symbol
        if (convertView.findViewById(R.id.extendView).getVisibility() == View.VISIBLE ) {
            convertView.findViewById(R.id.extendView).setVisibility(View.INVISIBLE);
        }
        // Item Image
        ImageView imageView = convertView.findViewById(R.id.productImage);
        imageView.setTag(products.get(position).id);
        new AsyncImageViewLoader(ctx, products.get(position), imageView).execute();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDialog(products.get(position));
            }
        });
        // reviews List
        LinearLayout reviewsContainer = convertView.findViewById(R.id.productReviews);
        // Check whether or not there is at least 1 review to display
        if (products.get(position).reviews != null && products.get(position).reviews.size() > 0) {
            // Display at most 2 reviews
            for (int i = 0; i < Math.min(2, products.get(position).reviews.size()); i++) {
                View view = LayoutInflater.from(ctx).inflate(R.layout.layout_review_mini, null);
                ((TextView) view.findViewById(R.id.reviewText)).setText(products.get(position).reviews.get(i).text);
                ((RatingBar) view.findViewById(R.id.reviewRating)).setRating(products.get(position).reviews.get(i).rating);
                reviewsContainer.addView(view);
            }
            // Display View More button if there are more than 2 reviews
            if (products.get(position).reviews.size() > 2) {
                instantiateButtons(products.get(position), reviewsContainer, true, true);
            } else {
                instantiateButtons(products.get(position), reviewsContainer, true, false);
            }
        } else {
            reviewsContainer.addView(createTextView(ctx, "No reviews Available!"));
            instantiateButtons(products.get(position), reviewsContainer, true, false);
        }
    }

    private void ImageDialog(Product product){
        // Create Alert Dialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
        // Inflate Alert Dialog View
        final View view = LayoutInflater.from(ctx).inflate(R.layout.layout_menu_item_image_dialog, null);
        alertDialog.setView(view);
        // Load Image
        ImageView imageView = view.findViewById(R.id.imageContainer);
        imageView.setTag(product.id);
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

    private void instantiateButtons(final Product product, final LinearLayout container, boolean displayFeedback, boolean displayViewMore){
        LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        if (displayFeedback) {
            Button leaveReview = new Button(ctx,null, 0, R.style.Widget_AppCompat_Button_Colored);
            leaveReview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
            // Focus Settings necessary to keep list item clickable
            leaveReview.setFocusable(false);
            leaveReview.setFocusableInTouchMode(false);
            // Setup Button text
            leaveReview.setText(ctx.getString(R.string.Feedback));
            // Add button to the layout
            linearLayout.addView(leaveReview);
            // Setup Button On Click
            leaveReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new FeedbackDialog(ctx).show(new FeedbackDialog.DialogContent() {
                        @Override
                        public void onSendReview(float rating, String reviewText) {
                            // Handle Review Contents
                        }
                    });
                }
            });
        }
        if (displayViewMore) {
            Button viewMoreReviews = new Button(ctx,null, 0, R.style.Widget_AppCompat_Button_Colored);
            viewMoreReviews.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
            // Focus Settings necessary to keep list item clickable
            viewMoreReviews.setFocusable(false);
            viewMoreReviews.setFocusableInTouchMode(false);
            // Setup Button text
            viewMoreReviews.setText(ctx.getString(R.string.view_more));
            // Add button to the layout
            linearLayout.addView(viewMoreReviews);
            // Setup Button On Click
            viewMoreReviews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create Alert Dialog
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
                    alertDialog.setView(new ReviewBox(ctx, product.reviews).getView());
                    alertDialog.show();
                }
            });
        }
        container.addView(linearLayout);
    }

    private TextView createTextView(Context ctx, String text) {
        TextView textView = new TextView(ctx);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setText(text);
        return textView;
    }
}
