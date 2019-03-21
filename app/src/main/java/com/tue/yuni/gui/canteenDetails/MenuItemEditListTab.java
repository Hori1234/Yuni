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
import com.tue.yuni.gui.editCanteenDialog.DialogContent;
import com.tue.yuni.models.ExtendedMenuItem;
import com.tue.yuni.models.MenuItem;
import com.tue.yuni.models.Schedule;
import com.tue.yuni.storage.PasswordStorage;
import com.tue.yuni.storage.RemoteStorage;

import java.util.ArrayList;
import java.util.List;

public class MenuItemEditListTab extends Fragment implements AdapterView.OnItemClickListener, View.OnTouchListener, DialogContent, RemoteStorage.ErrorHandler,RemoteStorage.RequestCompletedHandler{
    private ListView listView;
    private MenuItemEditListViewAdapter listAdapter;
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
        listAdapter = new MenuItemEditListViewAdapter(getContext(), menuItems, this);
        listView.setAdapter(listAdapter);
        // List Item Click
        listView.setOnItemClickListener(this);
        // Fix Scrolling of reviews List inside List View Item
        listView.setOnTouchListener(this);
        // Restore List State
        if (savedInstanceState != null){
            listView.scrollTo(0, savedInstanceState.getInt("ScrollY"));
        }
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Ensure the extended item will fully be in view
        listView.smoothScrollToPosition(position);
        //TODO enter edit dish
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
        if (args != null && args.containsKey("menuItemsByCategory")) {
            menuItems = args.getParcelableArrayList("menuItemsByCategory");
        }
    }

//    @Override
//    public void onSetStarred(float rating, String reviewText) {
//
//    }


    @Override
    public void onChangeMenuItem(int changeType, ExtendedMenuItem menuItem, Schedule schedule, int availability) {
        switch(changeType){
            case 0:
                //TODO
                break;
            case 1:
                //TODO
                RemoteStorage.get().removeItemFromMenu(menuItem.getMenuId(),PasswordStorage.get().getPassword(),this,this);
                break;
            case 2:
                RemoteStorage.get().updateMenuItemSchedule(PasswordStorage.get().getPassword(),menuItem.getMenuId(),schedule,this,this);
                break;
        }

    }




    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("ScrollY", listView.getScrollY());
    }

    @Override
    public void onError(Exception e) {
        //TODO
    }

    @Override
    public void onCompleted() {
        //TODO
    }
}
