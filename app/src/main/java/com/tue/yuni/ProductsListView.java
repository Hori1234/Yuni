package com.tue.yuni;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tue.yuni.ListView_Adapters.ProductsListViewAdapter;

import java.util.List;

public class ProductsListView extends Fragment {
    private ListView listView;
    private ProductsListViewAdapter listAdapter;
    private List<Product> products;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_products, container, false);
        // Get ListView
        listView = view.findViewById(R.id.productsList);
        // General ListView Settings
        listView.setFastScrollEnabled(false);
        listView.setFastScrollAlwaysVisible(false);
        // List View Adapter
        listAdapter = new ProductsListViewAdapter(getContext(), products);
        listView.setAdapter(listAdapter);
        // List Item Click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listAdapter.setExtendedViewItem(position);
                // Ensure the extended item will fully be in view
                listView.smoothScrollToPosition(position);
            }
        });
        // Fix Scrolling of Reviews List inside List View Item
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
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
        return view;
    }

    public void setup(List<Product> products) {
        this.products = products;
    }
}
