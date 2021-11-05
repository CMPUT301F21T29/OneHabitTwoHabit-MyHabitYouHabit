package com.example.ohthmhyh;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
    private static final int EDITPROFILE = 1;

    private TextView userNameTextView;
    private TextView userUserNameTextView;
    private TextView userBioTextView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseUser user;
    private User user_info;

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
        // Get the user that is currently signed in.
        user = FirebaseAuth.getInstance().getCurrentUser();
        user_info = new User();


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        Button signOutButton = view.findViewById(R.id.button_sign_out);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // The user wants to sign out. Sign the user out and go back to the login activity.
                FirebaseAuth.getInstance().signOut();
                goToLoginActivity();
            }
        });

        user_info.setName("Test");
        user_info.setBio("test");
        user_info.setName("test");

        // Display the user's email.
        //TextView userTextView = view.findViewById(R.id.text_view_user);
        //userTextView.setText(user.getEmail());

        // Display the user's name
        userNameTextView = view.findViewById(R.id.user_name);
        userNameTextView.setText(user.getDisplayName().length() > 0 ? user.getDisplayName() : "Breadfish");

        // Display the user's username
        userUserNameTextView = view.findViewById(R.id.user_username);
        userUserNameTextView.setText(user_info.getUsername());

        // Display the user's biography.
        userBioTextView = view.findViewById(R.id.user_biography);
        userBioTextView.setText(user_info.getBio());

        Button editProfileButton = view.findViewById(R.id.user_editprofile);
        editProfileButton.setOnClickListener((v) -> {
            Intent editProfile = new Intent(getActivity(), EditProfileActivity.class);
            editProfile.putExtra("NAME", user.getDisplayName());
            editProfile.putExtra("USERNAME", user_info.getUsername());
            editProfile.putExtra("BIO", user_info.getBio());
            someActivityResultLauncher.launch(editProfile);
        });


        return view;
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent intent = result.getData();
                        String name = intent.getStringExtra("NAME");
                        String user_name = intent.getStringExtra("USERNAME");
                        String biography = intent.getStringExtra("BIO");

                        user_info.setName(name);
                        user_info.setUsername(user_name);
                        user_info.setBio(biography);

                        //doSomeOperations();
                    }
                }
            });

    private void doSomeOperations() {

    }

    /**
     * Create an intent and start the login activity. (when sign out button pressed)
     */
    private void goToLoginActivity() {
        // Go to the login activity from this fragment.
        Intent loginActivityIntent = new Intent(getActivity(), LoginActivity.class);
        startActivity(loginActivityIntent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}