package com.tue.yuni;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

public class StoreView extends Fragment {
    View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private List<String> productCategories;             // Might be needed for future use
    private List<List<Product>> productsByCategory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_store, null);
        tabLayout = view.findViewById(R.id.tabSelector);
        viewPager = view.findViewById(R.id.viewPager);
        // Setup Tabs
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.info)));
        // Caused by: java.lang.NullPointerException on productCategories: Attempt to invoke interface method 'int java.util.List.size()' on a null object reference
        // Fix by implementing save instance
        for (int i = 0; i < productCategories.size(); i++){
            tabLayout.addTab(tabLayout.newTab().setText(productCategories.get(i)));
        }
        // Setup View Pager
        viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public int getCount() {
                return productsByCategory.size() + 1;
            }

            @Override
            public Fragment getItem(int i) {
                switch(i){
                    case 0:
                        // Info Page
                        return new StoreInfo();
                    default:
                        ProductsListView productsListView = new ProductsListView();
                        productsListView.setup(productsByCategory.get(i - 1));
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void setup(List<String> productCategories, List<List<Product>> productsByCategory) {
        this.productCategories = productCategories;
        this.productsByCategory = productsByCategory;
    }
}
