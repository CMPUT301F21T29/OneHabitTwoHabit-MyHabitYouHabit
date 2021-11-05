package com.example.ohthmhyh;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


/**
 * The fragment is shown on the "user profile" screen and shows things like username, profile
 * picture, bio, etc..
 */
public class UserFragment extends Fragment{
    // instance variables
    View view;
    MainActivity main;


    /**
     * An empty constructor for this fragment. Required for fragments
     */
    public UserFragment() {
        // Required empty public constructor
    }


    /**
     * Runs when this fragment is created. This method is responsible for filling in the various
     * views in the fragment when its created, and managing what happens when buttons are pressed.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user, container, false);
        main = (MainActivity) getActivity();
        updateViews();

        // define what happens when sign-out is clicked
        Button signOutButton = view.findViewById(R.id.button_sign_out);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // The user wants to sign out. Sign the user out and go back to the login activity.
                FirebaseAuth.getInstance().signOut();
                goToLoginActivity();
            }
        });

        // define what happens when the edit profile button is pressed
        Button editProfileButton = view.findViewById(R.id.user_editprofile);
        editProfileButton.setOnClickListener((v) -> {
            // start the fragment for editing the user profile
            EditProfileFragment frag = new EditProfileFragment(main.getUser());
            // when the fragment is done, refresh this view with the new content
            frag.setOnProfileUpdatelistener(new EditProfileFragment.ProfileListener() {
                @Override
                public void updateProfileCallback(User user) {
                    // update the views in this fragment
                    updateViews();
                    // update the master user object
                    main.updateUser(user);
                }
            });

            frag.show(getChildFragmentManager(), "editUserProfile");
        });

        return view;
    }


    /**
     * This method will fill in the views with data from the user object
     */
    private void updateViews(){
        // make sure we have the latest user object
        MainActivity main = (MainActivity) getActivity();

        // get the views
        TextView nameTextView = view.findViewById(R.id.user_name);
        TextView userNameTextView = view.findViewById(R.id.user_username);
        TextView userBioTextView = view.findViewById(R.id.user_biography);

        // Set the content of the views
        nameTextView.setText(main.getUser().getName());
        userNameTextView.setText(main.getUser().getUsername());
        userBioTextView.setText(main.getUser().getBio());
    }


    /**
     * Create an intent and start the login activity. (when sign out button pressed)
     */
    private void goToLoginActivity() {
        // Go to the login activity from this fragment.
        Intent loginActivityIntent = new Intent(getActivity(), LoginActivity.class);
        startActivity(loginActivityIntent);
    }

}

