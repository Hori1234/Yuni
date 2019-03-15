package com.tue.yuni.gui.canteenDetails;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tue.yuni.gui.review.FeedbackDialog;
import com.tue.yuni.models.Product;
import com.tue.yuni.R;

import java.util.List;

public class MenuItemListTab extends Fragment implements AdapterView.OnItemClickListener, View.OnTouchListener, FeedbackDialog.DialogContent {
    private ListView listView;
    private MenuItemListViewAdapter listAdapter;
    private List<Product> products;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_products, container, false);
        // Get ListView
        listView = view.findViewById(R.id.productsList);
        // General ListView Settings
        listView.setFastScrollEnabled(false);
        listView.setFastScrollAlwaysVisible(false);
        // List View Adapter
        listAdapter = new MenuItemListViewAdapter(getContext(), products, this);
        listView.setAdapter(listAdapter);
        // List Item Click
        listView.setOnItemClickListener(this);
        // Fix Scrolling of reviews List inside List View Item
        listView.setOnTouchListener(this);
        // Restore List State
        if (savedInstanceState != null){
            listAdapter.setExtendedViewItem(savedInstanceState.getInt("ExtendedItem"));
            listView.scrollTo(0, savedInstanceState.getInt("ScrollY"));
        }
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listAdapter.setExtendedViewItem(position);
        // Ensure the extended item will fully be in view
        listView.smoothScrollToPosition(position);
    }

    @SuppressLint("ClickableViewAccessibility")
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

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        // Read Arguments From Bundle
        if (args != null) {
            if (args.containsKey("productsByCategory"))
                products = args.getParcelableArrayList("productsByCategory");
        }
    }

    @Override
    public void onSendReview(float rating, String reviewText) {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("ScrollY", listView.getScrollY());
        outState.putInt("ExtendedItem", listAdapter.getExtendedViewItem());
    }
}
