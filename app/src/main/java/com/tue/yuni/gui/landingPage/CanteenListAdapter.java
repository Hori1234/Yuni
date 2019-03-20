package com.tue.yuni.gui.landingPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tue.yuni.R;
import com.tue.yuni.gui.util.AvailabilityIndicator;
import com.tue.yuni.models.Day;
import com.tue.yuni.models.canteen.Canteen;
import com.tue.yuni.storage.FavouriteStorage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CanteenListAdapter extends BaseAdapter {
    private Context ctx;
    private List<Item> listItem;
    private List<Canteen> canteens;

    public CanteenListAdapter(Context ctx) {
        this.ctx = ctx;
    }

    public void updateCanteens(List<Canteen> canteens) {
        this.canteens = canteens;
        // Create customized Item List
        buildItemsList();
        // Force ListView refresh
        notifyDataSetChanged();
    }

    public Canteen getCanteen(int listPosition) {
        if (listItem.get(listPosition).isCanteen()) {
            return canteens.get(listItem.get(listPosition).canteenPosition);
        }
        return null;
    }

    private void buildItemsList(){
        if (canteens != null) {
            List<String> locations = new ArrayList<>();
            // Find all different buildings
            for (int i = 0; i < canteens.size(); i++) {
                if (!locations.contains(canteens.get(i).getBuilding())) {
                    locations.add(canteens.get(i).getBuilding());
                }
            }
            listItem = new ArrayList<>();
            // Populate list
            for (int i = 0; i < locations.size(); i++) {
                listItem.add(new Item(locations.get(i), -1));
                // Find all canteens in the same given building
                for (int u = 0; u < canteens.size(); u++) {
                    if (canteens.get(u).getBuilding().equals(locations.get(i))) {
                        listItem.add(new Item(null, u));
                    }
                }
            }
        }
    }

    @Override
    public int getCount() {
        return listItem != null ? listItem.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        // Inflate Layout for each list row
        if (convertView == null) {
            viewHolder = new ViewHolder();
            // Check if Item is a Location or a Canteen
            if (!listItem.get(position).isCanteen()){
                convertView = createCanteenHeader(viewHolder, parent);
            } else {
                convertView = createCanteen(viewHolder, parent);
            }
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
            // Check if the View needs to change type
            if (viewHolder.isCanteen() && !listItem.get(position).isCanteen()) {
                convertView = createCanteenHeader(viewHolder, parent);
            } else if (!viewHolder.isCanteen() && listItem.get(position).isCanteen()) {
                convertView = createCanteen(viewHolder, parent);
            }
        }
        // Setup the Item parameters
        if (viewHolder.isCanteen()) {
            viewHolder.textView1.setText(canteens.get(listItem.get(position).canteenPosition).getName());
            // Canteen Status Processing
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK) == 1 ? 6 : calendar.get(Calendar.DAY_OF_WEEK) - 2;
            if (canteens.get(listItem.get(position).canteenPosition).getOperatingTimes().isOpen(Day.values()[day])) {
                // Get Current Time
                int open = Integer.parseInt(canteens.get(listItem.get(position).canteenPosition).getOperatingTimes().getOpeningTime(Day.values()[day]));
                int close = Integer.parseInt(canteens.get(listItem.get(position).canteenPosition).getOperatingTimes().getClosingTime(Day.values()[day]));
                int currentTime = calendar.get(Calendar.HOUR_OF_DAY) * 100 + calendar.get(Calendar.MINUTE);
                // Display Open Or Closed for the canteen
                if (open <= currentTime && currentTime < close) {
                    if ((close % 100) - 5 < 0) {
                        int min = 60 + ((close % 100) - 5);
                        close = ((close / 100) - 1) * 100 + min;
                    }
                    if (currentTime >= close) {
                        viewHolder.textView2.setText(ctx.getString(R.string.closing));
                        viewHolder.textView2.setTextColor(ctx.getColor(R.color.canteenClosing));
                    } else {
                        viewHolder.textView2.setText(ctx.getString(R.string.open));
                        viewHolder.textView2.setTextColor(ctx.getColor(R.color.canteenOpen));
                    }
                } else {
                    viewHolder.textView2.setText(ctx.getString(R.string.closed));
                    viewHolder.textView2.setTextColor(ctx.getColor(R.color.canteenClosed));
                }
            } else {
                viewHolder.textView2.setText(ctx.getString(R.string.closed));
                viewHolder.textView2.setTextColor(ctx.getColor(R.color.canteenClosed));
            }
            // Favorites Processing
            int favoritesCount = 0;
            for (int i = 0; i < canteens.get(listItem.get(position).canteenPosition).getMenuItems().size(); i++) {
                if (FavouriteStorage.get().checkFavorite(canteens.get(listItem.get(position).canteenPosition).getMenuItems().get(i).getId())){
                    favoritesCount++;
                }
            }
            viewHolder.canteenFavs.setText(ctx.getString(R.string.favoritesAbbreviation) + ": " + favoritesCount);
            // Canteen Busyness Processing
            int busyness = 100 - canteens.get(listItem.get(position).canteenPosition).getBusyness();
            viewHolder.canteenBusyness.setAvailability(busyness);
            if (0 <= busyness && busyness < viewHolder.canteenBusyness.getThreshold(0)) {
                viewHolder.canteenBusynessText.setText(ctx.getString(R.string.busy));
            } else if (viewHolder.canteenBusyness.getThreshold(0) <= busyness && busyness < viewHolder.canteenBusyness.getThreshold(1)) {
                viewHolder.canteenBusynessText.setText(ctx.getString(R.string.moderate));
            } else if (viewHolder.canteenBusyness.getThreshold(1) <= busyness && busyness < viewHolder.canteenBusyness.getThreshold(2)) {
                viewHolder.canteenBusynessText.setText(ctx.getString(R.string.quiet));
            }
            // Canteen Rating Processing
            viewHolder.canteenRating.setRating(canteens.get(listItem.get(position).canteenPosition).getRating());
        } else {
            viewHolder.textView1.setText(listItem.get(position).getLocation());
            // Solution for Distance Required
        }

        return convertView;
    }

    private View createCanteenHeader(ViewHolder viewHolder, ViewGroup parent){
        View view = LayoutInflater.from(ctx).inflate(R.layout.layout_landing_page_canteen_list_item_header, parent, false);
        viewHolder.textView1 = view.findViewById(R.id.locationName);
        viewHolder.textView2 = view.findViewById(R.id.locationDistance);
        viewHolder.canteenFavs = null;
        viewHolder.canteenBusyness = null;
        viewHolder.canteenBusynessText = null;
        viewHolder.canteenRating = null;
        view.setTag(viewHolder);
        return view;
    }

    private View createCanteen(ViewHolder viewHolder, ViewGroup parent){
        View view = LayoutInflater.from(ctx).inflate(R.layout.layout_landing_page_canteen_list_item, parent, false);
        viewHolder.textView1 = view.findViewById(R.id.canteenName);
        viewHolder.textView2 = view.findViewById(R.id.canteenOpenOrClosed);
        viewHolder.canteenFavs = view.findViewById(R.id.canteenFavs);
        viewHolder.canteenBusyness = view.findViewById(R.id.canteenBusyness);
        viewHolder.canteenBusynessText = view.findViewById(R.id.canteenBusynessText);
        viewHolder.canteenRating = view.findViewById(R.id.canteenRating);
        view.setTag(viewHolder);
        return view;
    }

    /**
     * Required for Performance Optimization
     */
    private static class ViewHolder {
        private TextView textView1;         // Either canteenName or locationName
        private TextView textView2;         // Either canteenOpenOrClosed or locationDistance
        private TextView canteenFavs;
        private AvailabilityIndicator canteenBusyness;
        private TextView canteenBusynessText;
        private RatingBar canteenRating;

        private boolean isCanteen(){
            if (canteenFavs == null) return false;
            return true;
        }
    }

    /**
     * Customized List of Buildings and Canteens
     */
    private class Item{
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
            if (canteenPosition == -1)
                return false;
            return true;
        }
    }
}