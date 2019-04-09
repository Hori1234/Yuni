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
import com.tue.yuni.gui.addItemToTheListMenu.MenuItemFragment;
import com.tue.yuni.gui.canteenDetails.CanteenInfoEditTab;
import com.tue.yuni.gui.canteenDetails.CanteenOwnerView;
import com.tue.yuni.gui.canteenDetails.MenuItemEditListTab;
import com.tue.yuni.gui.editMenu.MenuEditView;
import com.tue.yuni.gui.ownerLogin.OwnerLogin;
import com.tue.yuni.models.ExtendedMenuItem;
import com.tue.yuni.models.MenuItem;
import com.tue.yuni.models.canteen.Canteen;
import com.tue.yuni.storage.RemoteStorage;

import java.util.ArrayList;
import java.util.List;

//this class handles the landing page for canteen employees
public class OwnerLanding extends Fragment implements View.OnClickListener, RemoteStorage.MenuItemsDataHandler, RemoteStorage.ErrorHandler {
    Canteen canteen;
    List<MenuItem> menuItems;
    Button menu, info, reviews, addItem, switchCanteen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RemoteStorage.get().getAllMenuItems(this,this);

        // Instantiate new View
        View view = inflater.inflate(R.layout.layout_ownerlanding, null);

        // Get UI Elements
        TextView textViewCanteen = view.findViewById(R.id.textViewCanteen);
        menu = view.findViewById(R.id.buttonMenu);
        info = view.findViewById(R.id.buttonInfo);
        reviews = view.findViewById(R.id.buttonReviews);
        addItem = view.findViewById(R.id.buttonAddItem);
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
        addItem.setOnClickListener(this);
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
            bundle.putParcelable("Canteen", canteen);
            bundle.putParcelableArrayList("allMenuItems",  new ArrayList<>(menuItems));
            // Instantiate Fragment
            CanteenOwnerView canteenOwnerView = new CanteenOwnerView();
            canteenOwnerView.setArguments(bundle);
            // Transition to Fragment
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.addToBackStack("OwnerLanding");
            ft.replace(R.id.content, canteenOwnerView);
            ft.commit();
        } else if (v == info) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("Canteen",  canteen);
            CanteenInfoEditTab canteenInfoEditTab = new CanteenInfoEditTab();
            canteenInfoEditTab.setArguments(bundle);
            // Transition to Fragment
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.addToBackStack("OwnerLanding");
            ft.replace(R.id.content, canteenInfoEditTab);
            ft.commit();
        } else if (v == reviews){
            // Transition to Fragment
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.addToBackStack("OwnerLanding");
            ft.replace(R.id.content, new OwnerLogin());
            //todo: link to correct destiantion
            ft.commit();
        } else if (v == addItem) {
            // Transition to Fragment
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.addToBackStack("OwnerLanding");
            ft.replace(R.id.content, new MenuEditView());
            ft.commit();
        } else if (v == switchCanteen) {
            // Transition to Fragment
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.addToBackStack("OwnerLanding");
            ft.replace(R.id.content, new OwnerLogin());
            ft.commit();
        }
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onReceive(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}
