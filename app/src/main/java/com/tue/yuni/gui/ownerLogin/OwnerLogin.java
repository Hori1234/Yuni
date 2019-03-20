package com.tue.yuni.gui.ownerLogin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tue.yuni.R;
import com.tue.yuni.gui.ownerLanding.OwnerLanding;

public class OwnerLogin extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_canteen_login, null);
        view.findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Transition to Fragment
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack("OwnerLogin");
                //todo: decide wether this should stay added to the back stack
                ft.replace(R.id.content, new OwnerLanding());
                ft.commit();
            }
            //todo: implement password check
        });
        return view;
    }
}
