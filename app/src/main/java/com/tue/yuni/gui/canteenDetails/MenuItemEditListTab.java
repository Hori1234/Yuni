package com.tue.yuni.gui.canteenDetails;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tue.yuni.R;
import com.tue.yuni.gui.editCanteenDialog.MenuDialogContent;
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

public class MenuItemEditListTab extends Fragment implements AdapterView.OnItemClickListener, View.OnTouchListener, MenuDialogContent, RemoteStorage.MenuItemsDataHandler
        , RemoteStorage.ErrorHandler {
    private String category;
    private ListView listView;
    private MenuItemEditListViewAdapter listAdapter;
    private List<ExtendedMenuItem> menuItems;
    private List<MenuItem> allItems;
    private Canteen canteen;
    // Related to adding a new menuItem to the Canteen
    List<MenuItem> menuItemsToAdd;
    private int itemToAdd = -1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
                itemToAdd = -1;
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getContext().getString(R.string.selectMenuItem));
                // Remove menuItems already in the Canteen
                menuItemsToAdd = new ArrayList<>(allItems.size());
                boolean match;
                for (MenuItem item : allItems) {
                    match = false;
                    if (item.getCategory().equals(category)) {
                        for (MenuItem preExistingItem : menuItems) {
                            // Remove already added items to the canteen
                            if (item.getId() == preExistingItem.getId())
                                match = true;
                        }
                        if (!match) menuItemsToAdd.add(item);
                    }
                }
                // Sort items alphabetically
                menuItemsToAdd.sort(new MenuItem.CustomComparator());
                // Create Array of Names
                final String[] menuItemsToAddNames = new String[menuItemsToAdd.size()];
                for (int i = 0; i < menuItemsToAdd.size(); i++) menuItemsToAddNames[i] = menuItemsToAdd.get(i).getName();
                // Set Adapter
                builder.setSingleChoiceItems(menuItemsToAddNames, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        itemToAdd = which;
                    }
                });
                // Set Confirm Button
                builder.setPositiveButton(getContext().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (itemToAdd != - 1) {
                            HashMap<Day, Boolean> dayMap = new HashMap<>();
                            dayMap.put(Day.MONDAY, true);
                            dayMap.put(Day.TUESDAY, true);
                            dayMap.put(Day.WEDNESDAY, true);
                            dayMap.put(Day.THURSDAY, true);
                            dayMap.put(Day.FRIDAY, true);
                            dayMap.put(Day.SATURDAY, true);
                            dayMap.put(Day.SUNDAY, true);
                            RemoteStorage.get().addItemToMenu(canteen.getId(), menuItemsToAdd.get(itemToAdd).getId(), new Schedule(dayMap), PasswordStorage.get().getPassword(), () -> {
                                ExtendedMenuItem newMenuItem = new ExtendedMenuItem(
                                        menuItemsToAdd.get(itemToAdd).getId(),
                                        menuItemsToAdd.get(itemToAdd).getName(),
                                        menuItemsToAdd.get(itemToAdd).getDescription(),
                                        menuItemsToAdd.get(itemToAdd).getCategory(),
                                        0.0f,
                                        Availability.IN_STOCK,
                                        canteen.getId(),
                                        new Schedule(dayMap)
                                        );
                                menuItems.add(newMenuItem);
                                menuItems.sort(new MenuItem.CustomComparator());
                                listAdapter.notifyDataSetChanged();
                                // Delete Data
                                menuItemsToAdd = null;
                                itemToAdd = -1;
                                // Close dialog
                                dialog.dismiss();
                            }, (error) -> {
                                // Delete Data
                                menuItemsToAdd = null;
                                itemToAdd = -1;
                                // Close dialog
                                dialog.dismiss();
                            });
                        } else {
                            dialog.dismiss();
                        }
                    }
                });
                builder.setNegativeButton(getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(false);
                builder.show();
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
            menuItems.sort(new MenuItem.CustomComparator());
        }
        if (args != null && args.containsKey("canteen")) {
            canteen = args.getParcelable("canteen");
        }
        if (args != null && args.containsKey("category")) {
            category = args.getString("category");
        }
    }


    @Override
    public void onChangeMenuItem(ExtendedMenuItem menuItem) {
        if (NetworkService.networkAvailabilityHandler(getActivity().getApplicationContext())) {
            //todo: test this check
            RemoteStorage.get().removeItemFromMenu(menuItem.getMenuId(), PasswordStorage.get().getPassword(), () -> {
                menuItems.remove(menuItem);
                listAdapter.notifyDataSetChanged();
            }, this);
        }
    }

    @Override
    public void onChangeMenuItem(ExtendedMenuItem menuItem, Schedule schedule) {
        if (NetworkService.networkAvailabilityHandler(getActivity().getApplicationContext())) {
            //todo: test this check
            RemoteStorage.get().updateMenuItemSchedule(PasswordStorage.get().getPassword(), menuItem.getMenuId(), schedule, () -> {
                menuItem.setSchedule(schedule);
            }, this);
        }
    }

    @Override
    public void onChangeMenuItem(ExtendedMenuItem menuItem, Availability availability) {
        if (NetworkService.networkAvailabilityHandler(getActivity().getApplicationContext())) {
            //todo: test this check
            RemoteStorage.get().updateMenuItemAvailability(PasswordStorage.get().getPassword(), menuItem.getMenuId(), availability, () -> {
                menuItem.setAvailability(availability);
            }, this);
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
    public void onReceive(List<MenuItem> menuItems) {
        this.allItems = menuItems;
    }
}
