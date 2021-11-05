package com.example.ohthmhyh;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class EditProfileFragment extends DialogFragment {

    interface ProfileListener {
        void updateProfileCallback(User user);
    }

    // instance variables
    private ProfileListener listener;
    User user;


    /**
     * An empty constructor for the fragment.
     */
    public EditProfileFragment(){}


    /**
     * Make the fragment, taking in a user object. The user object is used to prefill
     * the views in the edit fragment.
     * @param user
     */
    public EditProfileFragment(User user){
        this.user = user;
    }


    /**
     * Use this to set the user profile update listener. This is used to force the
     * calling fragment to update its views, and to return the user object.
     * @param listener
     */
    public void setOnProfileUpdatelistener(ProfileListener listener){
        this.listener = listener;
    }


    /**
     * Override the onDismiss function to use the onDismissListener set by the
     * setOnDismissListener function.
     */
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) {
            listener.updateProfileCallback(user);
        }
    }


    /**
     * Runs when this dialog fragment is created. This method prefills the views based on
     * the data in the User object set at instantiation time.
     * @param savedInstanceState The state of the calling activity
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // get the view for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_user, null);

        // get the views in the activity
        EditText nameET = view.findViewById(R.id.profile_enter_name);
        EditText usernameET = view.findViewById(R.id.profile_enter_username);
        EditText bioET = view.findViewById(R.id.profile_enter_bio);

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
                        // update the user object
                        user.setUsername(usernameET.getText().toString());
                        user.setName(nameET.getText().toString());
                        user.setBio(bioET.getText().toString());
                    }
                })
                .setNegativeButton("Exit", null)
                .create();
    }

}

