package com.tue.yuni.gui.landingPage;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tue.yuni.R;
import com.tue.yuni.gui.canteenDetails.CanteenView;
import com.tue.yuni.models.canteen.Canteen;
import com.tue.yuni.services.location.LocationService;
import com.tue.yuni.services.network.NetworkService;
import com.tue.yuni.storage.RemoteStorage;

import java.util.List;
import java.util.TimerTask;

public class CanteenListTab extends Fragment implements AdapterView.OnItemClickListener, RemoteStorage.CanteensDataHandler, RemoteStorage.ErrorHandler {
    private ListView listView;
    private CanteenListAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Create View
        View view = inflater.inflate(R.layout.layout_landing_page_canteen_list, null);
        // Get ListView
        listView = view.findViewById(R.id.canteenList);
        // General ListView Settings
        listView.setFastScrollEnabled(false);
        listView.setFastScrollAlwaysVisible(false);
        listView.setOnItemClickListener(this);
        // List View Adapter
        listAdapter = new CanteenListAdapter(getContext());
        // Location
        if (!LocationService.get().requestLocation((location -> listAdapter.setLocation(location)))){
            // Create Runnable that query for location every 250ms until the location is received
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!LocationService.get().requestLocation((location -> listAdapter.setLocation(location))))
                        handler.postDelayed(this, 250);
                }
            }, 250);
        }
        // Set Adapter to ListView
        listView.setAdapter(listAdapter);
        // Query Server for canteens, if network is available
        if (NetworkService.networkAvailabilityHandler(getActivity().getApplicationContext())){
            RemoteStorage.get().getCanteens(this, this);}
        // Return View
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (listAdapter.getCanteen(position) != null) {
            // Create Fragment Itself
            CanteenView canteenView = new CanteenView();
            Bundle args = new Bundle();
            args.putParcelable("Canteen", listAdapter.getCanteen(position));
            canteenView.setArguments(args);
            // Transition to Fragment
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.addToBackStack("LandingPage");
            ft.replace(R.id.content, canteenView);
            ft.commit();
        }
    }

    @Override
    public void onReceive(List<Canteen> canteens) {
        listAdapter.updateCanteens(canteens);
    }

    @Override
    public void onError(Exception e) {

    }
}
