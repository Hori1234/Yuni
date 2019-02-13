package com.tue.yuni;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

    private ProductsListViewAdapter listAdapter;

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
        listAdapter = new ProductsListViewAdapter(ctx, products);
        listView.setAdapter(listAdapter);
        // List Item Click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listAdapter.setExtendedViewItem(position);
            }
        });
        // Fix Scrolling of Reviews List inside List View Item
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("Products List View", "OnTouch: " + v.getId());
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        listView.requestDisallowInterceptTouchEvent(false);
                        break;
                    case MotionEvent.ACTION_UP:
                        listView.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                v.onTouchEvent(event);
                return true;
            }

        });
    }
}
