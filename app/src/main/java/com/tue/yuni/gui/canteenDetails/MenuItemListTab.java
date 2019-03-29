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

import com.tue.yuni.R;
import com.tue.yuni.gui.review.FeedbackDialog;
import com.tue.yuni.models.ExtendedMenuItem;
import com.tue.yuni.models.MenuItem;
import com.tue.yuni.services.network.NetworkService;
import com.tue.yuni.storage.RemoteStorage;

import java.util.List;

public class MenuItemListTab extends Fragment implements AdapterView.OnItemClickListener, View.OnTouchListener, FeedbackDialog.DialogContent {
    private ListView listView;
    private MenuItemListViewAdapter listAdapter;
    private List<ExtendedMenuItem> menuItems;

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
        listAdapter = new MenuItemListViewAdapter(getContext(), menuItems, this);
        listView.setAdapter(listAdapter);
        // List Item Click
        listView.setOnItemClickListener(this);
        // Fix Scrolling of reviews List inside List View Item
        listView.setOnTouchListener(this);
        // Restore List State
        if (savedInstanceState != null) {
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
        switch (event.getAction()) {
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
        if (args != null && args.containsKey("menuItemsByCategory")) {
            menuItems = args.getParcelableArrayList("menuItemsByCategory");
        }
    }

    @Override
    public void onSendReview(float rating, String reviewText, int ID) {
        if (NetworkService.networkAvailabilityHandler(getActivity().getApplicationContext())) {
            RemoteStorage.get().createMenuItemReview(
                    ID,
                    rating,
                    reviewText,
                    () -> {
                        if (menuItems.get(listAdapter.getExtendedViewItem()).getId() == ID) {
                            listAdapter.forceMenuItemExtensionReviewsUpdate();
                        }
                    },
                    e -> {
                        // ToDo
                    }
            );
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("ScrollY", listView.getScrollY());
        outState.putInt("ExtendedItem", listAdapter.getExtendedViewItem());
    }
}
