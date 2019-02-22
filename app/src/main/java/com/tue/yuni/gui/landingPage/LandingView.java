package com.tue.yuni.gui.landingPage;

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

import com.tue.yuni.R;

public class LandingView extends Fragment {
    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_landing_page, null);
        tabLayout = view.findViewById(R.id.tabSelector);
        viewPager = view.findViewById(R.id.viewPager);
        // Setup Tabs
        tabLayout.setupWithViewPager(viewPager);
        // Setup View Pager
        viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                switch (i){
                    case 0: return new CampusMapTab();
                    case 1: return new CanteenListTab();
                    case 2: return new HelpTab();
                    default: return null;   // Should never get here
                }
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0: return getResources().getString(R.string.map);
                    case 1: return getResources().getString(R.string.list);
                    case 2: return getResources().getString(R.string.help);
                    default: return null;   // Should never get here
                }
            }
        });
        // Return view
        return view;
    }
}
