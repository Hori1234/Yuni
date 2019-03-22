package com.tue.yuni.gui.ownerLogin;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tue.yuni.R;
import com.tue.yuni.gui.ownerLanding.OwnerLanding;
import com.tue.yuni.models.canteen.Canteen;
import com.tue.yuni.storage.PasswordStorage;
import com.tue.yuni.storage.RemoteStorage;

import java.util.List;

public class OwnerLogin extends Fragment implements RemoteStorage.CanteensDataHandler, RemoteStorage.ErrorHandler, View.OnClickListener {
    List<Canteen> canteens;
    Spinner canteenSelector;
    EditText editPassword;
    Button loginButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Instantiate new View
        View view = inflater.inflate(R.layout.layout_canteen_login, null);

        // Get UI Elements
        canteenSelector = view.findViewById(R.id.canteenspinner);
        editPassword = (view.findViewById(R.id.PassInput));
        loginButton = view.findViewById(R.id.buttonLogin);

        // Disable Login Button until spinner populated
        loginButton.setOnClickListener(this);
        loginButton.setEnabled(true);

        // Get Canteens
        RemoteStorage.get().getCanteens(this, this);

        // Restore Password in UI if previously logged in
        if ( PasswordStorage.get().getPassword() != "") {
            editPassword.setText(PasswordStorage.get().getPassword());
            editPassword.setEnabled(false);
            //if there is an password in memory, it will be set in the editText, which will be disabled, so the correct password is locked in it
        }

        // Return View
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == loginButton) {
            String passInput = editPassword.getText().toString();
            RemoteStorage.get().authenticate(
                    passInput,
                    authenticated -> {
                        if (authenticated) {
                            PasswordStorage.get().setPassword(passInput);
                            //the password is saved in the storage
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("Canteen", canteens.get(canteenSelector.getSelectedItemPosition()));
                            OwnerLanding landing = new OwnerLanding();
                            landing.setArguments(bundle);
                            //the name and ID of the canteen selected in the spinner is saved to be used later
                            ft.addToBackStack("OwnerLogin");
                            //todo: decide wether this should stay added to the back stack
                            ft.replace(R.id.content, landing);
                            ft.commit();
                            //transition to the ownerlanding fragment if password was correct
                        } else {
                            editPassword.setText("");
                            //password resetted to allow for easy re entering of password
                            editPassword.setEnabled(true);
                            PasswordStorage.get().removePassword();
                            //the password is removed from storage and edit text is enable, this is necessary if a previously correct password is revoked
                            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Incorrect password", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.TOP, 0, 30);
                            toast.show();
                            //a toast will show that the password entered was incorrect
                        }
                    },

                    error -> {
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "An error occurred, please try again, if this keeps happening, contant the developper", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 0, 30);
                        toast.show();
                        //In case of an unexpected error a toast telling the user to try again will appear
                    });
        }
    }

    @Override
    public void onReceive(List<Canteen> canteens) {
        // Save Canteens
        this.canteens = canteens;
        // Setup Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item);
        for (int i = 0; i < canteens.size(); i++) {
            spinnerAdapter.add(canteens.get(i).getName());
        }
        canteenSelector.setAdapter(spinnerAdapter);
        // Enable Login Button
        loginButton.setEnabled(true);
    }

    @Override
    public void onError(Exception e) {

    }
}
