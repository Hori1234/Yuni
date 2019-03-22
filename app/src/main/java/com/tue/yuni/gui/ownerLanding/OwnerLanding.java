package com.tue.yuni.gui.ownerLanding;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tue.yuni.R;
import com.tue.yuni.gui.canteenDetails.MenuItemEditListTab;
import com.tue.yuni.gui.ownerLogin.OwnerLogin;
import com.tue.yuni.models.ExtendedMenuItem;
import com.tue.yuni.models.canteen.Canteen;
import com.tue.yuni.storage.RemoteStorage;

import java.util.ArrayList;


public class OwnerLanding extends Fragment implements View.OnClickListener {
    Canteen canteen;
    Button menu, info, reviews, statistics, switchCanteen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Instantiate new View
        View view = inflater.inflate(R.layout.layout_ownerlanding, null);

        // Get UI Elements
        TextView textViewCanteen = view.findViewById(R.id.textViewCanteen);
        menu = view.findViewById(R.id.buttonMenu);
        info = view.findViewById(R.id.buttonInfo);
        reviews = view.findViewById(R.id.buttonReviews);
        statistics = view.findViewById(R.id.buttonStatistics);
        switchCanteen = view.findViewById(R.id.buttonSwitch);

        // Setup UI
        textViewCanteen.setText(canteen.getName());
        //button for the menu
        menu.setOnClickListener(this);
        //button for the information
        info.setOnClickListener(this);
        //button for the reviews
        reviews.setOnClickListener(this);
        //button for the statistics
        statistics.setOnClickListener(this);
        //button for switching canteens goes to login so an employee can switch canteens
        switchCanteen.setOnClickListener(this);

        // Return View
        return view;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        if (args != null && args.containsKey("Canteen")){
            canteen = args.getParcelable("Canteen");
        }
    }

    @Override
    public void onClick(View v) {
        if (v == menu) {
            // Setup Arguments
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("menuItems",  new ArrayList<>(canteen.getMenuItems()));
            // Instantiate Fragment
            MenuItemEditListTab menuItemEditListTab = new MenuItemEditListTab();
            menuItemEditListTab.setArguments(bundle);
            // Transition to Fragment
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.addToBackStack("OwnerLanding");
            ft.replace(R.id.content, menuItemEditListTab);
            ft.commit();
        } else if (v == info) {
            // Transition to Fragment
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.addToBackStack("OwnerLanding");
            ft.replace(R.id.content, new OwnerLogin());
            //todo: replace with correct destination
            ft.commit();
        } else if (v == reviews) {
            // Transition to Fragment
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.addToBackStack("OwnerLanding");
            ft.replace(R.id.content, new OwnerLogin());
            //todo: link to correct destiantion
            ft.commit();
        } else if (v == statistics) {
            // Transition to Fragment
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.addToBackStack("OwnerLanding");
            ft.replace(R.id.content, new OwnerLogin());
            //todo: link to correct destination
            ft.commit();
        } else if (v == switchCanteen) {
            // Transition to Fragment
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.addToBackStack("OwnerLanding");
            ft.replace(R.id.content, new OwnerLogin());
            ft.commit();
        }
    }
}
