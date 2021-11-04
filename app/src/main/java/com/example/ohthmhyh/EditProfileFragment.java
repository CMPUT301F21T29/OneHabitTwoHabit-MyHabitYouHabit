package com.example.ohthmhyh;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class EditProfileFragment extends DialogFragment {

    interface UpdateProfileListener {
        void updateUserCallback(User user);
    }

    UpdateProfileListener listener;
    User user;

//    @Override
//    public void onAttach(Context context){
//        super.onAttach(context);
//
//        // make sure that the class starting this fragment implements the interface
//        if(context instanceof UpdateProfileListener){
//            // define listener so we can use its methods later
//            Log.e("CONGRATS", "WE HERE!!!!!!!!!!!!!!");
//        }
//        else{
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    public EditProfileFragment(){}

    public EditProfileFragment(User user){
        this.user = user;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // get the view for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_user, null);

        // get the views in the activity
        EditText nameET = view.findViewById(R.id.profile_enter_name);
        EditText usernameET = view.findViewById(R.id.profile_enter_username);
        EditText bioET = view.findViewById(R.id.profile_enter_bio);
        Button pickProfile = view.findViewById(R.id.profile_button_image);
        Button saveBtn = view.findViewById(R.id.profile_button_submit);

        // prefill the edit texts with their current values
        nameET.setText(user.getName());
        usernameET.setText(user.getUsername());
        bioET.setText(user.getBio());

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Edit user profile")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // TODO update the user object here
//                        user.setUsername(usernameET.getText().toString());
//                        user.setName(nameET.getText().toString());
//                        user.setBio(bioET.getText().toString());
//                        listener.updateUserCallback(user);
                    }
                })
                .setNegativeButton("Exit", null)
                .create();
    }

}

