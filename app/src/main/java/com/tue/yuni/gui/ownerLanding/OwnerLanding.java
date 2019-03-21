package com.tue.yuni.gui.ownerLanding;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tue.yuni.R;
import com.tue.yuni.gui.ownerLogin.OwnerLogin;


public class OwnerLanding extends Fragment {
    String selectedCanteen;
    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        if (args != null){
            if (args.containsKey("name")){
                selectedCanteen = args.getString("name");
                //the name of the canteen the owner selected will be fetched
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_ownerlanding, null);
        TextView textviewcanteen = view.findViewById(R.id.textViewCanteen);
        textviewcanteen.setText(selectedCanteen);
        //the textview at the top of the
        //button for the menu
        view.findViewById(R.id.buttonMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Transition to Fragment
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack("OwnerLanding");
                ft.replace(R.id.content, new OwnerLogin());
                //todo: link to correct destination
                ft.commit();
                // getActivity().onBackPressed();
            }
        });

        //button for the information
        view.findViewById(R.id.buttonInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Transition to Fragment
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack("OwnerLanding");
                ft.replace(R.id.content, new OwnerLogin());
                //todo: replace with correct destination
                ft.commit();
            }
        });

        //button for the reviews
        view.findViewById(R.id.buttonReviews).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Transition to Fragment
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack("OwnerLanding");
                ft.replace(R.id.content, new OwnerLogin());
                //todo: link to correct destiantion
                ft.commit();
            }
        });

        //button for the statistics
        view.findViewById(R.id.buttonStatistics).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Transition to Fragment
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack("OwnerLanding");
                ft.replace(R.id.content, new OwnerLogin());
                //todo: link to correct destination
                ft.commit();
            }

        });

        //button for switching canteens goes to login so an employee can switch canteens
        view.findViewById(R.id.buttonSwitch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Transition to Fragment
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack("OwnerLanding");
                ft.replace(R.id.content, new OwnerLogin());
                ft.commit();
            }
        });
        return view;
    }


}
