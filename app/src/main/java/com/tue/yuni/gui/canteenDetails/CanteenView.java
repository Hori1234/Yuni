package com.tue.yuni.gui.canteenDetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tue.yuni.models.ExtendedMenuItem;
import com.tue.yuni.models.MenuItem;
import com.tue.yuni.R;
import com.tue.yuni.models.canteen.Canteen;

import java.util.ArrayList;
import java.util.List;

public class CanteenView extends Fragment {
    private Canteen canteen;
    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<String> menuItemCategories;
    private List<List<ExtendedMenuItem>> menuItemsByCategory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_canteen, null);
        tabLayout = view.findViewById(R.id.tabSelector);
        viewPager = view.findViewById(R.id.viewPager);
        // Process Canteen Data
        processCanteenData();
        // Setup Tabs
        tabLayout.setupWithViewPager(viewPager);
        // Setup View Pager
        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                switch(i){
                    case 0:
                        // Create Arguments
                        Bundle args = new Bundle();
                        args.putParcelable("Canteen", canteen);
                        // Instantiate Info Fragment and Pass Arguments
                        CanteenInfoTab canteenInfoTab = new CanteenInfoTab();
                        canteenInfoTab.setArguments(args);
                        return canteenInfoTab;
                    default:
                        // Create Arguments
                        Bundle arguments = new Bundle();
                        arguments.putParcelableArrayList("menuItemsByCategory", (ArrayList<ExtendedMenuItem>) menuItemsByCategory.get(i - 1));
                        // Instantiate MenuItemListTab Fragment and Pass Arguments
                        MenuItemListTab productsListView = new MenuItemListTab();
                        productsListView.setArguments(arguments);
                        return productsListView;
                }
            }

            @Override
            public int getCount() {
                return menuItemsByCategory != null ? menuItemCategories.size() + 1 : 1;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0: return getString(R.string.info);
                    default: return menuItemCategories.get(position - 1);
                }
            }
        });
        // Check if this is a new instance or not
        if (savedInstanceState != null) {
            viewPager.setCurrentItem(savedInstanceState.getInt("CurrentPage"));
        }
        // Return view
        return view;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        // Read Arguments From Bundle
        if (args != null && args.containsKey("Canteen")) {
            canteen = args.getParcelable("Canteen");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Fragment Variables on Screen Rotation
        outState.putInt("CurrentPage", viewPager.getCurrentItem());
    }

    private void processCanteenData(){
        List<ExtendedMenuItem> menuItems = canteen.getMenuItems();
        // Find All Categories in the Menu
        menuItemCategories = new ArrayList<>();
        for (int i = 0; i < menuItems.size(); i++) {
            if (!menuItemCategories.contains(menuItems.get(i).getCategory())) {
                menuItemCategories.add(menuItems.get(i).getCategory());
            }
        }
        // Separate all MenuItems by Category
        menuItemsByCategory = new ArrayList<>();
        for (int i = 0; i < menuItemCategories.size(); i++) {
            menuItemsByCategory.add(new ArrayList<>());
            for (int u = 0; u < menuItems.size(); u++) {
                if (menuItems.get(u).getCategory().equals(menuItemCategories.get(i))) {
                    menuItemsByCategory.get(i).add(menuItems.get(u));
                }
            }
        }
    }
}
