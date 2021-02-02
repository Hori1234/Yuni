package com.tue.yuni.gui.editMenu;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tue.yuni.R;
import com.tue.yuni.gui.addItemToTheListMenu.MenuItemFragment;
import com.tue.yuni.gui.util.CustomFragment;
import com.tue.yuni.models.MenuItem;
import com.tue.yuni.services.network.NetworkService;
import com.tue.yuni.storage.RemoteStorage;

import java.util.ArrayList;
import java.util.List;

public class MenuEditView extends Fragment implements View.OnClickListener {
    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<String> menuItemCategories;
    private List<List<MenuItem>> menuItemsByCategory;
    private FloatingActionButton btn;
    private int pageToRestore = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_menu_edit_view, null);
        btn = view.findViewById(R.id.btnAddItem);
        tabLayout = view.findViewById(R.id.tabSelector);
        viewPager = view.findViewById(R.id.viewPager);
        // Setup button
        btn.setOnClickListener(this);
        // Setup Tabs
        tabLayout.setupWithViewPager(viewPager);
        // Setup View Pager
        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                // Create Arguments
                Bundle arguments = new Bundle();
                arguments.putParcelableArrayList("menuItemsByCategory", (ArrayList<MenuItem>) menuItemsByCategory.get(i));
                arguments.putStringArray("categories", menuItemCategories.toArray(new String[0]));
                arguments.putInt("Index", i);
                // Instantiate MenuItemListTab Fragment and Pass Arguments
                MenuEditCategoryList productsListView = new MenuEditCategoryList();
                productsListView.setArguments(arguments);
                return productsListView;
            }

            @Override
            public int getCount() {
                return menuItemsByCategory != null ? menuItemCategories.size() : 0;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return menuItemCategories != null ? menuItemCategories.get(position) : "";
            }
        });
        // Check if this is a new instance or not
        if (savedInstanceState != null) {
            pageToRestore = savedInstanceState.getInt("CurrentPage");
            Log.d("Restoring", "Page To Restore: " + pageToRestore);
        }
        // Get Menu Data
        requestUpdate();
        // Return view
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == btn) {
            if (NetworkService.networkAvailabilityHandler(getActivity().getApplicationContext())){
                // Save ViewPager Page Index
                pageToRestore = viewPager.getCurrentItem();
                // Open Fragment to Edit / Add menuItem
                transitionToEditMenuItem();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Fragment Variables on Screen Rotation
        Log.d("Restoring", "Page Saved: " + viewPager.getCurrentItem());
        outState.putInt("CurrentPage", viewPager.getCurrentItem());
    }

    private void transitionToEditMenuItem() {
        // Create MenuItemFragment
        MenuItemFragment menuItemFragment = new MenuItemFragment();
        // Create Arguments
        Bundle arguments = new Bundle();
        if (menuItemCategories != null)
            arguments.putStringArray("categories", menuItemCategories.toArray(new String[0]));
        arguments.putString("category", (String) viewPager.getAdapter().getPageTitle(viewPager.getCurrentItem()));
        menuItemFragment.setArguments(arguments);
        // Transition to Fragment
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.addToBackStack("MenuEditView");
        ft.replace(R.id.content, menuItemFragment);
        ft.commit();
    }

    /**
     * When a update is requested
     */
    private void requestUpdate() {
        RemoteStorage.get().getAllMenuItems(new RemoteStorage.MenuItemsDataHandler() {
            @Override
            public void onReceive(List<MenuItem> menuItems) {
                processMenuData(menuItems);
                List<Fragment> frags = getChildFragmentManager().getFragments();
                for (int i = 0; i < frags.size(); i++) {
                    CustomFragment cFrag = (CustomFragment) frags.get(i);
                    // Get Frag Menu Items by Category index
                    int index = cFrag.getParameters().getInt("Index");
                    // Update Fragment
                    Bundle arguments = new Bundle();
                    arguments.putParcelableArrayList("menuItemsByCategory", (ArrayList<MenuItem>) menuItemsByCategory.get(index));
                    arguments.putStringArray("categories", menuItemCategories.toArray(new String[0]));
                    arguments.putInt("Index", index);
                    cFrag.update(arguments);
                }
                // Update ViewPager
                viewPager.getAdapter().notifyDataSetChanged();
                // Restore ViewPager current Page
                viewPager.setCurrentItem(pageToRestore);
            }
        }, new RemoteStorage.ErrorHandler() {
            @Override
            public void onError(Exception e) {
                // ToDo
            }
        });
    }

    /**
     * Process the menu items to be placed in categories
     *
     * @param menuItems Raw menu items
     */
    private void processMenuData(List<MenuItem> menuItems) {
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
            // Ensure menuItems are sorted alphabetically by Name
            menuItemsByCategory.get(i).sort(new MenuItem.CustomComparator());
        }
    }
}
