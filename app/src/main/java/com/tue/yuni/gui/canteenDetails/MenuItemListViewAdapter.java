package com.tue.yuni.gui.canteenDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tue.yuni.gui.review.ReviewListViewAdapter;
import com.tue.yuni.gui.util.AvailabilityIndicator;
import com.tue.yuni.gui.review.FeedbackDialog;
import com.tue.yuni.models.Product;
import com.tue.yuni.R;

import java.util.List;

public class MenuItemListViewAdapter extends BaseAdapter {
    private Context ctx;
    private List<Product> products;
    private int extendedViewItem = -1;
    private FeedbackDialog.DialogContent parent;
    private MenuItemExtension extendedItem;

    public MenuItemListViewAdapter(Context ctx, List<Product> products, FeedbackDialog.DialogContent parent) {
        this.ctx = ctx;
        this.products = products;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        // Inflate Layout for each list row
        if (convertView == null) {
            // Default Layout
            convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_menu_item, parent, false);
            // Check if the current list Item needs to the extended view
            if (position == extendedViewItem) {   // Default Layout
                convertView = extendedItem.getView(products.get(position), convertView);
            }
            // ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.productName = convertView.findViewById(R.id.productName);
            viewHolder.productRating = convertView.findViewById(R.id.productRating);
            viewHolder.productAvailability = convertView.findViewById(R.id.productAvailability);
            viewHolder.extendButton = convertView.findViewById(R.id.extendView);
            viewHolder.extensionLayout = convertView.findViewById(R.id.extendedDetails);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
            // Check if the current list Item needs to be a Default or Extended Layout
            if (position == extendedViewItem && viewHolder.extensionLayout.getChildCount() == 0) {
                convertView = extendedItem.getView(products.get(position), convertView);
            } else if (position != extendedViewItem && viewHolder.extensionLayout.getChildCount() > 0){
                viewHolder.extensionLayout.removeAllViews();
            }
        }

        // Setup the Item parameters
        // Default View
        viewHolder.productName.setText(products.get(position).name);
        viewHolder.productRating.setRating(products.get(position).rating);
        viewHolder.productAvailability.setAvailability(products.get(position).availability);
        if (viewHolder.extendButton .getVisibility() == View.INVISIBLE && position != extendedViewItem) {
            viewHolder.extendButton .setVisibility(View.VISIBLE);
        }

        // Return the instantiated row
        return convertView;
    }
    /*
    Required for Performance Optimization
     */
    private static class ViewHolder {
        private TextView productName;
        private RatingBar productRating;
        private AvailabilityIndicator productAvailability;
        private ImageView extendButton;
        private LinearLayout extensionLayout;
    }
}
