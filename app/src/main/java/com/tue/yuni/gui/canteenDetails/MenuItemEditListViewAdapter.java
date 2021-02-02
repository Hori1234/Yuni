package com.tue.yuni.gui.canteenDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tue.yuni.R;
import com.tue.yuni.gui.editCanteenDialog.MenuDialogContent;
import com.tue.yuni.gui.editCanteenDialog.availabilityDialog;
import com.tue.yuni.gui.editCanteenDialog.deleteDialog;
import com.tue.yuni.gui.editCanteenDialog.scheduleMenuDialog;
import com.tue.yuni.models.ExtendedMenuItem;

import java.util.List;

public class MenuItemEditListViewAdapter extends BaseAdapter {
    private Context ctx;
    private List<ExtendedMenuItem> menuItems;


    private MenuDialogContent parentTab;

    public MenuItemEditListViewAdapter(Context ctx, List<ExtendedMenuItem> menuItems, MenuDialogContent parent) {
        this.parentTab = parent;
        this.ctx = ctx;
        this.menuItems = menuItems;
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
            convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_canteen_menu_item_edit, parent, false);

            // ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.menuItemName = convertView.findViewById(R.id.productName);
            viewHolder.availabilityButton =  convertView.findViewById(R.id.availibilityButton);
            viewHolder.scheduleButton = convertView.findViewById(R.id.sheduleButton);
            viewHolder.deleteButton = convertView.findViewById(R.id.deleteMenuItem);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // Setup the Item parameters
        // Default View
        viewHolder.menuItemName.setText(menuItems.get(position).getName());




        viewHolder.availabilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new availabilityDialog(ctx).show(parentTab,menuItems.get(position));
            }
        });


        viewHolder.scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new scheduleMenuDialog(ctx).show(parentTab, menuItems.get(position));
            }
        });


        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new deleteDialog(ctx).show(parentTab,menuItems.get(position));
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
//        private ImageButton starredButton;
        private ImageButton availabilityButton;
        private ImageButton scheduleButton;
        private ImageButton deleteButton;

    }


}
