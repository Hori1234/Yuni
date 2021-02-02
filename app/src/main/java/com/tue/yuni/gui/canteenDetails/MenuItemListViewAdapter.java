package com.tue.yuni.gui.canteenDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.tue.yuni.gui.util.TrafficLightIndicator;
import com.tue.yuni.gui.review.FeedbackDialog;
import com.tue.yuni.models.ExtendedMenuItem;
import com.tue.yuni.models.MenuItem;
import com.tue.yuni.R;
import com.tue.yuni.storage.FavouriteStorage;

import java.util.List;

public class MenuItemListViewAdapter extends BaseAdapter {
    private Context ctx;
    private List<ExtendedMenuItem> menuItems;
    private int extendedViewItem = -1;
    private FeedbackDialog.DialogContent parent;
    // Extension Related
    private MenuItemExtension extendedItem;
    private boolean forceReviewsUpdate = false;

    public MenuItemListViewAdapter(Context ctx, List<ExtendedMenuItem> menuItems, FeedbackDialog.DialogContent parent) {
        this.ctx = ctx;
        this.menuItems = menuItems;
        this.parent = parent;
        extendedItem = new MenuItemExtension(ctx, parent);
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

    public void forceMenuItemExtensionReviewsUpdate(){
        forceReviewsUpdate = true;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return menuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return menuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return menuItems.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        // Inflate Layout for each list row
        if (convertView == null) {
            // Default Layout
            convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_menu_item, parent, false);
            // Check if the current list Item needs to the extended view
            if (position == extendedViewItem) {   // Default Layout
                convertView = extendedItem.getView(menuItems.get(position), convertView);
            }
            // ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.menuItemName = convertView.findViewById(R.id.productName);
            viewHolder.menuItemRating = convertView.findViewById(R.id.productRating);
            viewHolder.menuItemAvailability = convertView.findViewById(R.id.productAvailability);
            viewHolder.extendButton = convertView.findViewById(R.id.extendView);
            viewHolder.extensionLayout = convertView.findViewById(R.id.extendedDetails);
            viewHolder.favoriteButton = convertView.findViewById(R.id.buttonFavorite);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
            // Check if the current list Item needs to be a Default or Extended Layout
            if (position == extendedViewItem){
                if (viewHolder.extensionLayout.getChildCount() == 0)
                    convertView = extendedItem.getView(menuItems.get(position), convertView);
                else if (forceReviewsUpdate) {
                    convertView = extendedItem.reviewsUpdate(menuItems.get(position), convertView);
                    forceReviewsUpdate = false;
                }
            } else if (position != extendedViewItem && viewHolder.extensionLayout.getChildCount() > 0){
                viewHolder.extensionLayout.removeAllViews();
            }
        }

        // Setup the Item parameters
        // Default View
        viewHolder.menuItemName.setText(menuItems.get(position).getName());
        viewHolder.menuItemRating.setRating(menuItems.get(position).getRating());
        viewHolder.menuItemAvailability.setState(menuItems.get(position).getAvailability());
        if (viewHolder.extendButton .getVisibility() == View.INVISIBLE && position != extendedViewItem) {
            viewHolder.extendButton .setVisibility(View.VISIBLE);
        }
        viewHolder.favoriteButton.setChecked(FavouriteStorage.get().checkFavorite(menuItems.get(position).getId()));
        viewHolder.favoriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    FavouriteStorage.get().setFavorite(menuItems.get(position).getId());
                } else {
                    FavouriteStorage.get().removeFavorite(menuItems.get(position).getId());
                }
            }
        });

        // Return the instantiated row
        return convertView;
    }
    /*
    Required for Performance Optimization
     */
    private static class ViewHolder {
        private TextView menuItemName;
        private RatingBar menuItemRating;
        private TrafficLightIndicator menuItemAvailability;
        private ToggleButton favoriteButton;

        private ImageView extendButton;
        private LinearLayout extensionLayout;
    }
}
