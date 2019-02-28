package com.tue.yuni.gui.landingPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
        //convertView = LayoutInflater.from(ctx).inflate()
        return null;
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
