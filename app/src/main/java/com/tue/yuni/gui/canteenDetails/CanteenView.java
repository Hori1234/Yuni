package com.tue.yuni.gui.canteenDetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.tue.yuni.models.Product;
import com.tue.yuni.R;

import java.util.ArrayList;
import java.util.List;

public class CanteenView extends Fragment {
    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<String> productCategories;
    private List<List<Product>> productsByCategory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_canteen, null);
        tabLayout = view.findViewById(R.id.tabSelector);
        viewPager = view.findViewById(R.id.viewPager);
        // Setup Tabs
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.info)));
        for (int i = 0; i < productCategories.size(); i++){
            tabLayout.addTab(tabLayout.newTab().setText(productCategories.get(i)));
        }
        // Setup View Pager
        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return productsByCategory.size() + 1;
            }

            @Override
            public Fragment getItem(int i) {
                switch(i){
                    case 0:
                        // Info Page
                        return new CanteenInfoTab();
                    default:
                        // Create Arguments
                        Bundle arguments = new Bundle();
                        arguments.putParcelableArrayList("productsByCategory", (ArrayList<Product>) productsByCategory.get(i - 1));
                        // Instantiate Fragment and Pass Arguments
                        MenuItemListTab productsListView = new MenuItemListTab();
                        productsListView.setArguments(arguments);
                        return productsListView;
                }
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0: return getString(R.string.info);
                    default: return productCategories.get(position - 1);
                }
            }
        });
        // Return view
        return view;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        // Read Arguments From Bundle
        if (args != null) {
            if (args.containsKey("productCategories")) {
                productCategories = args.getStringArrayList("productCategories");
                productsByCategory = new ArrayList<>();
                for (int i = 0; i < productCategories.size(); i++) {
                    productsByCategory.add(args.<Product>getParcelableArrayList("productsByCategory" + i));
                }
            }
        }
    }
}
