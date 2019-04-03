package com.tue.yuni.gui.editMenu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tue.yuni.R;
import com.tue.yuni.gui.addItemToTheListMenu.MenuItemFragment;
import com.tue.yuni.gui.util.CustomFragment;
import com.tue.yuni.models.MenuItem;

import java.util.List;

public class MenuEditCategoryList extends Fragment implements AdapterView.OnItemClickListener, View.OnTouchListener, CustomFragment {
    private int index;
    private ListView listView;
    private MenuEditCategoryListAdapter listAdapter;
    private List<MenuItem> menuItems;
    private String[] categories;

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
        listAdapter = new MenuEditCategoryListAdapter(getContext(), menuItems);
        listView.setAdapter(listAdapter);
        // List Item Click
        listView.setOnItemClickListener(this);
        // Fix Scrolling of reviews List inside List View Item
        listView.setOnTouchListener(this);
        // Restore List State
        if (savedInstanceState != null) {
            listView.scrollTo(0, savedInstanceState.getInt("ScrollY"));
        }
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        transitionToEditMenuItem(menuItems.get(position));
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
            categories = args.getStringArray("categories");
            if (listAdapter != null){
                listAdapter.setMenuItems(menuItems);
            }
            if (args.containsKey("Index"))
                index = args.getInt("Index");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("ScrollY", listView.getScrollY());
    }

    private void transitionToEditMenuItem(MenuItem menuItem){
        // Create Fragment
        MenuItemFragment menuItemFragment = new MenuItemFragment();
        // Set Fragment Arguments
        Bundle bundle = new Bundle();
        bundle.putParcelable("menuItem", menuItem);
        bundle.putStringArray("categories", categories);
        menuItemFragment.setArguments(bundle);
        // Transition to Fragment
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.addToBackStack("MenuEditView");
        ft.replace(R.id.content, menuItemFragment);
        ft.commit();
    }

    @Override
    public void update(Bundle bundle) {
        setArguments(bundle);
    }

    @Override
    public Bundle getParameters() {
        Bundle bundle = new Bundle();
        bundle.putInt("Index", index);
        return bundle;
    }
}
