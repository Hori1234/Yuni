package com.tue.yuni.gui.editMenu;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tue.yuni.R;
import com.tue.yuni.models.MenuItem;
import com.tue.yuni.storage.PasswordStorage;
import com.tue.yuni.storage.RemoteStorage;

import java.util.List;

public class MenuEditCategoryListAdapter extends BaseAdapter {
    private Context ctx;
    private List<MenuItem> menuItems;
    // Extension Related

    public MenuEditCategoryListAdapter(Context ctx, List<MenuItem> menuItems) {
        this.ctx = ctx;
        this.menuItems = menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
        notifyDataSetChanged();
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
            convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_menu_item_edit_advanced, parent, false);
            // ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.menuItemName = convertView.findViewById(R.id.productName);
            viewHolder.menuItemRating = convertView.findViewById(R.id.productRating);
            viewHolder.menuItemDescription = convertView.findViewById(R.id.productDescription);
            viewHolder.menuItemRemove = convertView.findViewById(R.id.removeItem);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Setup the Item parameters
        // Default View
        viewHolder.menuItemName.setText(menuItems.get(position).getName());
        viewHolder.menuItemRating.setRating(menuItems.get(position).getRating());
        viewHolder.menuItemDescription.setText(menuItems.get(position).getDescription());
        viewHolder.menuItemRemove.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setMessage(ctx.getString(R.string.confirmDeleteMessage))
                    .setPositiveButton(ctx.getString(R.string.confirm), (dialog, which) -> {
                        // User pressed confirm, remove menu item from database
                        MenuItem item = menuItems.get(position);
                        RemoteStorage.get().removeMenuItem(
                                PasswordStorage.get().getPassword(),
                                item.getId(),
                                this::notifyDataSetChanged,
                                e -> {
                                }
                        );
                    })
                    .setNegativeButton(ctx.getString(R.string.cancel), (dialog, which) -> {
                        // Intentionally left blank
                    });
            builder.create().show();
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
        private TextView menuItemDescription;
        private ImageButton menuItemRemove;
    }
}
