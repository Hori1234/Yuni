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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tue.yuni.R;
import com.tue.yuni.gui.ownerLanding.OwnerLanding;
import com.tue.yuni.storage.PasswordStorage;
import com.tue.yuni.storage.RemoteStorage;

public class OwnerLogin extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_canteen_login, null);
        EditText editPassword = (view.findViewById(R.id.PassInput));
        if ( PasswordStorage.get().getPassword() != "") {
            editPassword.setText(PasswordStorage.get().getPassword());
            editPassword.setEnabled(false);
            //if there is an password in memory, it will be set in the editText, which will be disabled, so the correct password is locked in it
        }
        view.findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passInput = editPassword.getText().toString();
                //the inputted password is fetched as PassInput
                RemoteStorage.get().authenticate(
                        passInput,
                        authenticated -> {
                            if (authenticated) {
                                PasswordStorage.get().setPassword(passInput);
                                //the password is saved in the storage
                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                Spinner selectedCanteen = view.findViewById(R.id.canteenspinner);
                                String canteenName = selectedCanteen.getSelectedItem().toString();
                                Long canteenID = selectedCanteen.getSelectedItemId();
                                Bundle bundle = new Bundle();
                                bundle.putString("name", canteenName);
                                bundle.putLong("id", canteenID);//ToDo
                                OwnerLanding landin = new OwnerLanding();
                                landin.setArguments(bundle);
                                //the name and ID of the canteen selected in the spinner is saved to be used later
                                ft.addToBackStack("OwnerLogin");
                                //todo: decide wether this should stay added to the back stack
                                ft.replace(R.id.content, landin);
                                ft.commit();
                                //transition to the ownerlanding fragment if password was correct

                            }
                            else {
                                editPassword.setText("");
                                //password resetted to allow for easy re entering of password
                                editPassword.setEnabled(true);
                                PasswordStorage.get().removePassword();
                                //the password is removed from storage and edit text is enable, this is necessary if a previously correct password is revoked
                                Toast toast = Toast.makeText(getActivity().getApplicationContext(),"Incorrect password",Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.TOP,0,30);
                                toast.show();
                                //a toast will show that the password entered was incorrect
                            }},

                        error -> {
                            Toast toast = Toast.makeText(getActivity().getApplicationContext(),"An error occurred, please try again, if this keeps happening, contant the developper",Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.TOP,0,30);
                            toast.show();
                            //In case of an unexpected error a toast telling the user to try again will appear
                        });

            }
        });
        return view;
    }
}
