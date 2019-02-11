package com.tue.yuni;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class ProductsListView {
    private List<Product> products;
    private ListView listView;
    private Context ctx;

    private BaseAdapter listAdapter;
    private int extendedViewItem = -1;

    public ProductsListView(Context ctx, ListView listView, List<Product> products) {
        this.ctx = ctx;
        this.listView = listView;
        this.products = products;
        setupListView();
    }

    private void setupListView(){
        // General ListView Settings
        listView.setFastScrollEnabled(false);
        listView.setFastScrollAlwaysVisible(false);
        // List View Adapter
        listAdapter = new Products_ListAdapter();
        listView.setAdapter(listAdapter);
        // List Item Click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == extendedViewItem)
                    extendedViewItem = -1;
                else
                    extendedViewItem = position;
                // Force List Refresh
                listAdapter.notifyDataSetChanged();
            }
        });
    }

    private class Products_ListAdapter extends BaseAdapter {
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
            return products.get(position).ID;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Inflate Layout for each list row
            if (convertView == null) {
                // Check if the current list Item needs to be a Default or Extended Layout
                if (position != extendedViewItem)   // Default Layout
                    convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_product, parent, false);
                else                                // Extended Layout
                    convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_product_extended, parent, false);
            } else {
                // Check if the current list Item needs to be a Default or Extended Layout
                if (position == extendedViewItem) {
                    // If there is no ImageView it means we need to instantiate the Extended Layout
                    if (convertView.findViewById(R.id.productImage) == null)
                        convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_product_extended, parent, false);
                }
                else {
                    // If there is a ImageView it means we need to instantiate the Default Layout
                    if (convertView.findViewById(R.id.productImage) != null)
                        convertView = LayoutInflater.from(ctx).inflate(R.layout.layout_product, parent, false);
                }
            }

            // Setup the Item parameters
            // Default View
            ((TextView)convertView.findViewById(R.id.productName)).setText(products.get(position).Name);
            ((RatingBar)convertView.findViewById(R.id.productRating)).setRating(products.get(position).Rating);
            ((AvailabilityIndicator)convertView.findViewById(R.id.productAvailability)).setAvailability(products.get(position).Availability);
            ((TextView)convertView.findViewById(R.id.productPrice)).setText(String.format("%.2f€", products.get(position).Price));
            // Extended View
            if (position == extendedViewItem) {
                // Item Image
                ((ImageView)convertView.findViewById(R.id.productImage)).setImageResource(products.get(position).Picture);
                // Reviews List

            }

            // Return the instantiated row
            return convertView;
        }
    }
}
