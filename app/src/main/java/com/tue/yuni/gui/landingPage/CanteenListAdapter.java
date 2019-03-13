package com.tue.yuni.gui.landingPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tue.yuni.R;
import com.tue.yuni.gui.canteenDetails.MenuItemListViewAdapter;
import com.tue.yuni.gui.util.AvailabilityIndicator;
import com.tue.yuni.models.canteen.Canteen;

import java.util.ArrayList;
import java.util.List;

public class CanteenListAdapter extends BaseAdapter {
    private Context ctx;
    private List<Item> listItem;
    private List<Canteen> canteens;

    public CanteenListAdapter(Context ctx, List<Canteen> canteens) {
        this.ctx = ctx;
        this.canteens = canteens;
        buildItemsList();
    }

    private void buildItemsList(){
        List<String> locations = new ArrayList<>();
        for (int i = 0; i < canteens.size(); i++) {
            if (!locations.contains(canteens.get(i).getLocation().toString()))
                locations.add(canteens.get(i).getLocation().toString());
        }
        listItem = new ArrayList<>();
        for (int i = 0; i < locations.size(); i++) {
            listItem.add(new Item(locations.get(0), -1));
            for (int u = 0; u < canteens.size(); u++){
                if (canteens.get(u).getLocation().toString().equals(locations.get(0))){
                    listItem.add(new Item(null, u));
                }
            }
        }
    }

    @Override
    public int getCount() {
        return canteens.size();
    }

    @Override
    public Object getItem(int position) {
        return canteens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return canteens.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        // Inflate Layout for each list row
        if (convertView == null) {
            viewHolder = new ViewHolder();
            // Check if Item is a Location or a Canteen
            if (listItem.get(position).isCanteen()){
                convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_landing_page_canteen_list_item, parent, false);
                viewHolder.textView1 = convertView.findViewById(R.id.canteenName);
                viewHolder.textView2 = convertView.findViewById(R.id.canteenOpenOrClosed);
                viewHolder.canteenFavs = convertView.findViewById(R.id.canteenFavs);
                viewHolder.canteenBusyness = convertView.findViewById(R.id.canteenBusyness);
                viewHolder.canteenRating = convertView.findViewById(R.id.canteenRating);
            } else {
                convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_landing_page_canteen_list_item_header, parent, false);
                viewHolder.textView1 = convertView.findViewById(R.id.locationName);
                viewHolder.textView2 = convertView.findViewById(R.id.locationDistance);
            }
            convertView.setTag(convertView);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
            // Check if the View needs to change type
            if (!viewHolder.isCanteen() && listItem.get(position).isCanteen()) {
                convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_landing_page_canteen_list_item, parent, false);
                viewHolder.textView1 = convertView.findViewById(R.id.canteenName);
                viewHolder.textView2 = convertView.findViewById(R.id.canteenOpenOrClosed);
                viewHolder.canteenFavs = convertView.findViewById(R.id.canteenFavs);
                viewHolder.canteenBusyness = convertView.findViewById(R.id.canteenBusyness);
                viewHolder.canteenRating = convertView.findViewById(R.id.canteenRating);
            } else if (viewHolder.isCanteen() && !listItem.get(position).isCanteen()) {
                convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_landing_page_canteen_list_item_header, parent, false);
                viewHolder.textView1 = convertView.findViewById(R.id.locationName);
                viewHolder.textView2 = convertView.findViewById(R.id.locationDistance);
            }
            convertView.setTag(convertView);
        }
        // Setup the Item parameters
        if (viewHolder.isCanteen()) {
            viewHolder.textView1.setText(canteens.get(listItem.get(position).canteenPosition).getName());
            // viewHolder.textView2.setText(canteens.get(listItem.get(position).canteenPosition).getOperatingTimes()); Further Processing Required
            // Solution for Favs Required
            // Solution for Busyness Required
            // Solution for Canteen Rating Required
        } else {
            viewHolder.textView1.setText(listItem.get(position).getLocation());
            // Solution for Distance Required
        }

        return convertView;
    }

    /*
    Required for Performance Optimization
     */
    private static class ViewHolder {
        private TextView textView1;         // Either canteenName or locationName
        private TextView textView2;         // Either canteenOpenOrClosed or locationDistance
        private TextView canteenFavs;
        private TextView canteenBusyness;
        private RatingBar canteenRating;

        private boolean isCanteen(){
            if (canteenFavs == null) return true;
            return false;
        }
    }

    protected class Item{
        private String location;
        private int canteenPosition;

        public Item(String location, int canteenPosition) {
            this.location = location;
            this.canteenPosition = canteenPosition;
        }

        public String getLocation(){
            return location;
        }

        public boolean isCanteen(){
            if (canteenPosition != -1)
                return false;
            return true;
        }
    }
}
