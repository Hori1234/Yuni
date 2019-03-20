package com.tue.yuni.gui.ownerLanding;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tue.yuni.R;
import com.tue.yuni.gui.ownerLogin.OwnerLogin;


public class OwnerLanding extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_ownerlanding, null);

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
        return view;
    }

}
