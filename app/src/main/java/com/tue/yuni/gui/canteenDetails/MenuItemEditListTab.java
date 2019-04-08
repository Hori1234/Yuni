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
import com.tue.yuni.gui.editCanteenDialog.MenuDialogContent;
import com.tue.yuni.models.Availability;
import com.tue.yuni.models.ExtendedMenuItem;
import com.tue.yuni.models.MenuItem;
import com.tue.yuni.models.Schedule;
import com.tue.yuni.models.canteen.Canteen;
import com.tue.yuni.services.network.NetworkService;
import com.tue.yuni.storage.PasswordStorage;
import com.tue.yuni.storage.RemoteStorage;

import java.util.ArrayList;
import java.util.List;

public class MenuItemEditListTab extends Fragment implements AdapterView.OnItemClickListener, View.OnTouchListener, MenuDialogContent, RemoteStorage.MenuItemsDataHandler
        , RemoteStorage.ErrorHandler, RemoteStorage.RequestCompletedHandler {
    private ListView listView;
    private MenuItemEditListViewAdapter listAdapter;
    private List<ExtendedMenuItem> menuItems;
    private List<MenuItem> allItems;
    private Canteen canteen;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if (getActivity().getWindow().getDecorView().findViewById(R.id.fab) != null){
//            Log.d("btn", "btn found");
//        }
        RemoteStorage.get().getAllMenuItems(this, this);
        View view = inflater.inflate(R.layout.layout_products_edit, container, false);
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
        FloatingActionButton fab = view.findViewById(R.id.addDish);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("menuItems", new ArrayList<>(allItems));
                bundle.putParcelable("canteen", canteen);
                // Instantiate Fragment
                MenuItemAddListTab menuItemAddListTab = new MenuItemAddListTab();
                menuItemAddListTab.setArguments(bundle);
                // Transition to Fragment
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack("OwnerLanding");
                ft.replace(R.id.content, menuItemAddListTab);
                ft.commit();
            }
        });
        // Restore List State
        if (savedInstanceState != null) {
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
        if (args != null && args.containsKey("menuItems")) {
            menuItems = args.getParcelableArrayList("menuItems");
        }
        if (args != null && args.containsKey("canteen")) {
            canteen = args.getParcelable("canteen");
        }
    }


    @Override
    public void onChangeMenuItem(ExtendedMenuItem menuItem) {
        if (NetworkService.networkAvailabilityHandler(getActivity().getApplicationContext())) {
            //todo: test this check
            RemoteStorage.get().removeItemFromMenu(menuItem.getMenuId(), PasswordStorage.get().getPassword(), this, this);
        }
    }

    @Override
    public void onChangeMenuItem(ExtendedMenuItem menuItem, Schedule schedule) {
        if (NetworkService.networkAvailabilityHandler(getActivity().getApplicationContext())) {
            //todo: test this check
            RemoteStorage.get().updateMenuItemSchedule(PasswordStorage.get().getPassword(), menuItem.getMenuId(), schedule, this, this);
        }
    }

    @Override
    public void onChangeMenuItem(ExtendedMenuItem menuItem, Availability availability) {
        if (NetworkService.networkAvailabilityHandler(getActivity().getApplicationContext())) {
            //todo: test this check
            RemoteStorage.get().updateMenuItemAvailability(PasswordStorage.get().getPassword(), menuItem.getMenuId(), availability, this, this);
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


    @Override
    public void onReceive(List<MenuItem> menuItems) {
        this.allItems = menuItems;
    }
}
