package com.tue.yuni.gui.landingPage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tue.yuni.R;
import com.tue.yuni.gui.canteenDetails.MenuItemListViewAdapter;
import com.tue.yuni.models.canteen.Canteen;

public class CanteenListTab extends Fragment implements AdapterView.OnItemClickListener {
    private ListView listView;
    private CanteenListAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_landing_page_canteen_list, null);  // Place Holder
        // Get ListView
        listView = view.findViewById(R.id.canteenList);
        // General ListView Settings
        listView.setFastScrollEnabled(false);
        listView.setFastScrollAlwaysVisible(false);
        // List View Adapter
        //listAdapter = new CanteenListAdapter(getContext(), null);
        //listView.setAdapter(listAdapter);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
