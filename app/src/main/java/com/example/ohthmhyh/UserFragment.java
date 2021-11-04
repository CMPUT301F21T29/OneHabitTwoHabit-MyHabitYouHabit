package com.example.ohthmhyh;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseUser fbUser;
    private User user;
    View view;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user, container, false);


        // TODO: pull the user from the database here!
        user = new User();


        // get the views
        TextView nameTextView = view.findViewById(R.id.user_name);
        TextView userNameTextView = view.findViewById(R.id.user_username);
        TextView userBioTextView = view.findViewById(R.id.user_biography);

        // Set the content of the views
        nameTextView.setText(user.getName());
        userNameTextView.setText(user.getUsername());
        userBioTextView.setText(user.getBio());


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

           new EditProfileFragment(user).show(getActivity().getSupportFragmentManager(), "editUserProfile");

        });

        return view;
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