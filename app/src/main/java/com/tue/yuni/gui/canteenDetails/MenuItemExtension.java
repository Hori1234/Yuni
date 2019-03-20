package com.tue.yuni.gui.canteenDetails;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tue.yuni.R;
import com.tue.yuni.gui.review.FeedbackDialog;
import com.tue.yuni.gui.review.ReviewBox;
import com.tue.yuni.gui.util.AsyncImageViewLoader;
import com.tue.yuni.models.MenuItem;
import com.tue.yuni.models.review.MenuItemReview;
import com.tue.yuni.models.review.Review;
import com.tue.yuni.storage.RemoteStorage;

import java.util.ArrayList;
import java.util.List;

public class MenuItemExtension implements View.OnClickListener, View.OnTouchListener, RemoteStorage.MenuItemReviewsDataHandler, RemoteStorage.ErrorHandler {
    private Context ctx;
    private MenuItem menuItem;
    private AlertDialog imageDialog;
    private View imageDialogView;
    private ImageView imageView;
    private TextView menuItemDescription;

    // Reviews Related
    private List<Review> reviews;
    private LinearLayout reviewsContainer;
    private Button leaveReview;
    private Button viewMoreReviews;
    private FeedbackDialog.DialogContent parent;

    public MenuItemExtension(Context ctx, FeedbackDialog.DialogContent parent){
        this.ctx = ctx;
        this.parent = parent;
    }
    @SuppressWarnings({"all"})
    public View getView(MenuItem menuItem, View convertView) {
        this.menuItem = menuItem;

        LinearLayout extensionLayout = convertView.findViewById(R.id.extendedDetails);
        extensionLayout.addView(LayoutInflater.from(ctx).inflate(R.layout.layout_menu_item_extension, null,false));
        // Retract Symbol
        if (convertView.findViewById(R.id.extendView).getVisibility() == View.VISIBLE ) {
            convertView.findViewById(R.id.extendView).setVisibility(View.INVISIBLE);
        }
        // Item Image
        imageView = convertView.findViewById(R.id.productImage);
        imageView.setTag(menuItem.getId());
        new AsyncImageViewLoader(ctx, menuItem, imageView).execute();
        imageView.setOnClickListener(this);
        // Item Description
        menuItemDescription = convertView.findViewById(R.id.productDescription);
        menuItemDescription.setText(menuItem.getDescription());
        // Reviews
        reviewsContainer = convertView.findViewById(R.id.productReviews);
        RemoteStorage.get().getMenuItemReviews(menuItem.getId(), this, this);

        return convertView;
    }

    @SuppressWarnings({"all"})
    private void imageDialog(MenuItem menuItem){
        // Create Alert Dialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
        // Inflate Alert Dialog View
        imageDialogView = LayoutInflater.from(ctx).inflate(R.layout.layout_menu_item_image_dialog, null);
        alertDialog.setView(imageDialogView);
        // Load Image
        ImageView imageView = imageDialogView.findViewById(R.id.imageContainer);
        imageView.setTag(menuItem.getId());
        new AsyncImageViewLoader(ctx, menuItem, imageView).execute();
        // Show Alert Dialog
        imageDialog = alertDialog.show();
        // Setup Alert Dialog Layout Params
        int size;
        if (Resources.getSystem().getDisplayMetrics().widthPixels > Resources.getSystem().getDisplayMetrics().heightPixels)
            size = Resources.getSystem().getDisplayMetrics().heightPixels - 100;
        else
            size = Resources.getSystem().getDisplayMetrics().widthPixels - 100;
        imageDialog.getWindow().setLayout(size, size);
        // Make Alert Dialog Background Invisible
        imageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Make Dialog Close on Touch
        imageDialogView.setOnTouchListener(this);
    }

    private void createButtonsLayout(LinearLayout container, boolean displayViewMore){
        // Create button Layout Container
        LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        // Feedback Button
        leaveReview = createButton(ctx, ctx.getString(R.string.feedback), linearLayout);
        // View More Reviews Button
        if (displayViewMore) {
            viewMoreReviews = createButton(ctx, ctx.getString(R.string.view_more), linearLayout);
        }
        container.addView(linearLayout);
    }

    private Button createButton(Context ctx, String text, LinearLayout layout){
        // Feedback Button
        Button button = new Button(ctx,null, 0, R.style.Widget_AppCompat_Button_Colored);
        button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        // Focus Settings necessary to keep list item clickable
        button.setFocusable(false);
        button.setFocusableInTouchMode(false);
        // Setup Button text
        button.setText(text);
        // Add button to the layout
        layout.addView(button);
        // Setup Button On Click
        button.setOnClickListener(this);
        // Return Button
        return button;
    }

    private TextView createTextView(Context ctx, String text) {
        TextView textView = new TextView(ctx);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setText(text);
        return textView;
    }

    @Override
    public void onClick(View v) {
        if (v.equals(imageView)) {
            imageDialog(menuItem);
        }
        else if (v.equals(leaveReview)) {
            new FeedbackDialog(ctx).show(parent, menuItem.getId());
        }
        else if (v.equals(viewMoreReviews)) {
            // Create Alert Dialog
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
            alertDialog.setView(new ReviewBox(ctx, reviews).getView());
            alertDialog.show();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.equals(imageDialogView)) {
            imageDialog.dismiss();
        }
        return false;
    }

    @Override
    public void onReceive(List<MenuItemReview> reviews) {
        if (reviews != null && reviews.size() > 0) {
            this.reviews = new ArrayList<>(reviews);
            // Display at most 2 reviews
            for (int i = 0; i < Math.min(2, reviews.size()); i++) {
                View view = LayoutInflater.from(ctx).inflate(R.layout.layout_review_mini, null);
                ((TextView) view.findViewById(R.id.reviewText)).setText(reviews.get(i).getDescription());
                ((RatingBar) view.findViewById(R.id.reviewRating)).setRating(reviews.get(i).getRating());
                reviewsContainer.addView(view);
            }
            // Display View More button if there are more than 2 reviews
            if (reviews.size() > 2) {
                createButtonsLayout(reviewsContainer, true);
            } else {
                createButtonsLayout(reviewsContainer, false);
            }
        } else {
            reviewsContainer.addView(createTextView(ctx, "No reviews Available!"));
            createButtonsLayout(reviewsContainer, false);
        }
    }

    @Override
    public void onError(Exception e) {

    }
}
