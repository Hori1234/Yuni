package com.tue.yuni.gui.canteenDetails;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tue.yuni.R;
import com.tue.yuni.gui.editCanteenDialog.availabilityDialog;
import com.tue.yuni.gui.editCanteenDialog.deleteDialog;
import com.tue.yuni.gui.editCanteenDialog.scheduleDialog;
import com.tue.yuni.models.MenuItem;

import java.util.List;

public class MenuItemEditListViewAdapter extends BaseAdapter {
    private Context ctx;
    private List<MenuItem> menuItems;
    private ImageButton starredButton;
    private ImageButton availabilityButton;
    private ImageButton scheduleButton;
    private ImageButton deleteButton;

    public MenuItemEditListViewAdapter(Context ctx, List<MenuItem> menuItems, deleteDialog.DialogContent parent) {
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
            convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_menu_item, parent, false);


            // ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.menuItemName = convertView.findViewById(R.id.productName);


//            starredButton = convertView.findViewById(R.id.starredButton);
            availabilityButton = convertView.findViewById(R.id.availibilityButton);
            scheduleButton = convertView.findViewById(R.id.sheduleButton);
            deleteButton = convertView.findViewById(R.id.deleteMenuItem);

//            viewHolder.starredButton = this.starredButton;
            viewHolder.availabilityButton = this.availabilityButton;
            viewHolder.scheduleButton = this.scheduleButton;
            viewHolder.deleteButton = this.deleteButton;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // Setup the Item parameters
        // Default View
        viewHolder.menuItemName.setText(menuItems.get(position).getName());


//        viewHolder.starredButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setStarred();
//            }
//        });


        viewHolder.availabilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new availabilityDialog(ctx).show();
            }
        });


        viewHolder.scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new scheduleDialog(ctx).show();
            }
        });


        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new deleteDialog(ctx).show();
            }
        });





        // Return the instantiated row
        return convertView;
    }

    /**
     * TODO
     */
    private void setStarred() {

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
