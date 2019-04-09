package com.tue.yuni.gui.landingPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tue.yuni.R;
import com.tue.yuni.gui.util.TrafficLightIndicator;
import com.tue.yuni.models.Day;
import com.tue.yuni.models.Location;
import com.tue.yuni.models.canteen.Canteen;
import com.tue.yuni.services.location.LocationService;
import com.tue.yuni.services.mapper.BusynessMapper;
import com.tue.yuni.storage.FavouriteStorage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CanteenListAdapter extends BaseAdapter {
    private Context ctx;
    private List<Item> listItem;
    private List<Canteen> canteens;
    private Location location = null;

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

    public void setLocation(Location location) {
        this.location = location;
        notifyDataSetChanged();
    }

    private void buildItemsList() {
        if (canteens != null) {
            List<String> buildings = new ArrayList<>();
            List<Location> locations = new ArrayList<>();
            // Find all different buildings
            for (int i = 0; i < canteens.size(); i++) {
                if (!buildings.contains(canteens.get(i).getBuilding())) {
                    buildings.add(canteens.get(i).getBuilding());
                    locations.add(canteens.get(i).getLocation());
                }
            }
            listItem = new ArrayList<>();
            // Populate list
            for (int i = 0; i < buildings.size(); i++) {
                listItem.add(new Item(buildings.get(i), locations.get(i), -1));
                // Find all canteens in the same given building
                for (int u = 0; u < canteens.size(); u++) {
                    if (canteens.get(u).getBuilding().equals(buildings.get(i))) {
                        listItem.add(new Item(null, null, u));
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
            if (!listItem.get(position).isCanteen()) {
                convertView = createCanteenHeader(viewHolder, parent);
            } else {
                convertView = createCanteen(viewHolder, parent);
            }
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            // Check if the View needs to change type
            if (viewHolder.isCanteen() && !listItem.get(position).isCanteen()) {
                convertView = createCanteenHeader(viewHolder, parent);
            } else if (!viewHolder.isCanteen() && listItem.get(position).isCanteen()) {
                convertView = createCanteen(viewHolder, parent);
            }
        }

        // Setup the Item parameters
        if (viewHolder.isCanteen()) {
            Canteen canteen = canteens.get(listItem.get(position).canteenPosition);

            viewHolder.textView1.setText(canteen.getName());
            // Canteen Status Processing
            int status = Canteen.getCanteenCurrentOpenStatus(canteens.get(listItem.get(position).canteenPosition));
            if (status == 0) {
                viewHolder.textView2.setText(ctx.getString(R.string.closed));
                viewHolder.textView2.setTextColor(ctx.getColor(R.color.canteenClosed));
            }
            else if (status == 1) {
                viewHolder.textView2.setText(ctx.getString(R.string.closing));
                viewHolder.textView2.setTextColor(ctx.getColor(R.color.canteenClosing));
            }
            else if (status == 2) {
                viewHolder.textView2.setText(ctx.getString(R.string.open));
                viewHolder.textView2.setTextColor(ctx.getColor(R.color.canteenOpen));
            }

            // Favorites Processing
            int favoritesCount = 0;
            for (int i = 0; i < canteen.getMenuItems().size(); i++) {
                if (FavouriteStorage.get().checkFavorite(canteen.getMenuItems().get(i).getId())) {
                    favoritesCount++;
                }
            }
            viewHolder.canteenFavs.setText(ctx.getString(R.string.favoritesAbbreviation) + ": " + favoritesCount);

            // Canteen Busyness Processing
            viewHolder.canteenBusyness.setState(BusynessMapper.getState(canteen.getBusyness()));
            viewHolder.canteenBusynessText.setText(ctx.getString(BusynessMapper.getTextResource(canteen.getBusyness())));

            // Canteen Rating Processing
            viewHolder.canteenRating.setRating(canteen.getRating());
        } else {
            viewHolder.textView1.setText(listItem.get(position).getBuilding());
            // Solution for Distance Required
            if (location != null) {
                int time = (int) Math.round(LocationService.getWalkingTime(listItem.get(position).location, location));
                viewHolder.textView2.setText(time + " " + ctx.getString(R.string.minutes));
            }
        }

        return convertView;
    }

    private View createCanteenHeader(ViewHolder viewHolder, ViewGroup parent) {
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

    private View createCanteen(ViewHolder viewHolder, ViewGroup parent) {
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
        private TrafficLightIndicator canteenBusyness;
        private TextView canteenBusynessText;
        private RatingBar canteenRating;

        private boolean isCanteen() {
            if (canteenFavs == null) return false;
            return true;
        }
    }

    /**
     * Customized List of Buildings and Canteens
     */
    private class Item {
        private String building;
        private Location location;
        private int canteenPosition;

        public Item(String building, Location location, int canteenPosition) {
            this.building = building;
            this.location = location;
            this.canteenPosition = canteenPosition;
        }

        public String getBuilding() {
            return building;
        }

        public Location getLocation() {
            return location;
        }

        public boolean isCanteen() {
            if (canteenPosition == -1)
                return false;
            return true;
        }
    }
}