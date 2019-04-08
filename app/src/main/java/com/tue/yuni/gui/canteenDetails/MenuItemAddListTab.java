package com.tue.yuni.gui.canteenDetails;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tue.yuni.R;
import com.tue.yuni.gui.addItemToTheListMenu.MenuItemFragment;
import com.tue.yuni.gui.editCanteenDialog.AddDialogContent;
import com.tue.yuni.gui.editCanteenDialog.MenuDialogContent;
import com.tue.yuni.gui.editMenu.MenuEditView;
import com.tue.yuni.models.Availability;
import com.tue.yuni.models.Day;
import com.tue.yuni.models.ExtendedMenuItem;
import com.tue.yuni.models.MenuItem;
import com.tue.yuni.models.Schedule;
import com.tue.yuni.models.canteen.Canteen;
import com.tue.yuni.services.network.NetworkService;
import com.tue.yuni.storage.PasswordStorage;
import com.tue.yuni.storage.RemoteStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuItemAddListTab extends Fragment implements AdapterView.OnItemClickListener, View.OnTouchListener, AddDialogContent, RemoteStorage.ErrorHandler,RemoteStorage.RequestCompletedHandler{
    private ListView listView;
    private MenuItemAddListViewAdapter listAdapter;
    private List<MenuItem> menuItems;
    private Canteen canteen;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        List<MenuItem> doubles = new ArrayList<>();
        for(ExtendedMenuItem canteenItem :canteen.getMenuItems()){
            for(MenuItem catagoryItem: menuItems){
                if(catagoryItem.getId()==canteenItem.getId()){
                    doubles.add(catagoryItem);
                }
            }
            for(MenuItem dubble: doubles){
                menuItems.remove(dubble);
            }
        }

        View view = inflater.inflate(R.layout.layout_products_edit, container, false);
        // Get ListView
        listView = view.findViewById(R.id.productsList);
        // General ListView Settings
        listView.setFastScrollEnabled(false);
        listView.setFastScrollAlwaysVisible(false);
        // List View Adapter
        listAdapter = new MenuItemAddListViewAdapter(getContext(), menuItems, this,canteen);
        listView.setAdapter(listAdapter);
        // List Item Click
        listView.setOnItemClickListener(this);
        // Fix Scrolling of reviews List inside List View Item
        listView.setOnTouchListener(this);
        // Restore List State
        if (savedInstanceState != null){
            listView.scrollTo(0, savedInstanceState.getInt("ScrollY"));
        }
        FloatingActionButton fab = view.findViewById(R.id.addDish);
        fab.hide();
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
        if (args != null && args.containsKey("menuItems")) {
            menuItems = args.getParcelableArrayList("menuItems");
        }
        if (args != null && args.containsKey("canteen")) {
            canteen = args.getParcelable("canteen");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("ScrollY", listView.getScrollY());
    }

    @Override
    public void onAddMenuItem(MenuItem menuItem, Canteen canteen) {
        if (NetworkService.networkAvailabilityHandler(getActivity().getApplicationContext())){
            //todo: test this check
            HashMap<Day,Boolean> dayMap = new HashMap<>();
            dayMap.put(Day.MONDAY,true);
            dayMap.put(Day.TUESDAY,true);
            dayMap.put(Day.WEDNESDAY,true);
            dayMap.put(Day.THURSDAY,true);
            dayMap.put(Day.FRIDAY,true);
            dayMap.put(Day.SATURDAY,true);
            dayMap.put(Day.SUNDAY,true);
            RemoteStorage.get().addItemToMenu(canteen.getId(),menuItem.getId(),new Schedule(dayMap),PasswordStorage.get().getPassword(),this,this);
        }
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
