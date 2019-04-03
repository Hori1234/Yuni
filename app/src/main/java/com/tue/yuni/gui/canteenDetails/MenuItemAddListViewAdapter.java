package com.tue.yuni.gui.canteenDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tue.yuni.R;
import com.tue.yuni.gui.editCanteenDialog.AddDialogContent;
import com.tue.yuni.gui.editCanteenDialog.MenuDialogContent;
import com.tue.yuni.gui.editCanteenDialog.addDialog;
import com.tue.yuni.gui.editCanteenDialog.availabilityDialog;
import com.tue.yuni.gui.editCanteenDialog.deleteDialog;
import com.tue.yuni.gui.editCanteenDialog.scheduleMenuDialog;
import com.tue.yuni.models.ExtendedMenuItem;
import com.tue.yuni.models.MenuItem;
import com.tue.yuni.models.canteen.Canteen;

import java.util.List;

import static java.security.AccessController.getContext;

public class MenuItemAddListViewAdapter extends BaseAdapter {
    private Context ctx;
    private List<MenuItem> menuItems;

    private Canteen canteen;
    private AddDialogContent parentTab;



    public MenuItemAddListViewAdapter(Context ctx, List<MenuItem> menuItems, AddDialogContent parent, Canteen canteen) {
        this.parentTab = parent;
        this.ctx = ctx;
        this.menuItems = menuItems;
        this.canteen = canteen;
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
            convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_menu_item_name, parent, false);

            // ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.menuItemName = convertView.findViewById(R.id.productName);
            viewHolder.menuItemName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new addDialog(ctx).show(parentTab,menuItems.get(position),canteen);
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // Setup the Item parameters
        // Default View
        viewHolder.menuItemName.setText(menuItems.get(position).getName());

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
