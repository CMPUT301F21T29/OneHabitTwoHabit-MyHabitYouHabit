package com.example.ohthmhyh.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ohthmhyh.adapters.FriendRequestListAdapter;
import com.example.ohthmhyh.adapters.FriendsListAdapter;
import com.example.ohthmhyh.R;
import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.entities.User;
import com.example.ohthmhyh.activities.EditProfileActivity;
import com.example.ohthmhyh.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * This fragment is used to show the user's data
 */
public class UserFragment extends Fragment {

    /**
     * An empty constructor required for fragments
     */
    public UserFragment() {
        // Required empty public constructor
    }


    /**
     * Runs when the fragment view is created.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        // set up the sign out button
        Button signOutButton = view.findViewById(R.id.button_sign_out);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // The user wants to sign out. Sign the user out and go back to the login activity.
                FirebaseAuth.getInstance().signOut();
                goToLoginActivity();
            }
        });

        // get the user data and put it into the proper views
        DatabaseAdapter dba = DatabaseAdapter.getInstance();
        dba.pullUser(new DatabaseAdapter.ProfileCallback() {
            @Override
            public void onProfileCallback(User user) {
                fillViews(view, user);
            }
        });

        return view;
    }


    /**
     * Fills the views in the fragment that are dependent on the user object
     * that is retrieved from the database.
     * @param view The fragment view being filled
     * @param user The user object used to populate the views
     */
    private void fillViews(View view, User user){

        // get the views
        TextView usernameTV= view.findViewById(R.id.username_TV);
        ListView requestLV = view.findViewById(R.id.friend_request_LV);
        TextView emptyRequestTV = view.findViewById(R.id.empty_request_list);
        ListView friendsLV = view.findViewById(R.id.friends_LV);
        TextView emptyFriendsTV = view.findViewById(R.id.empty_friend_list);
        EditText searchFriendsET = view.findViewById(R.id.friend_Search_ET);
        Button searchBtn = view.findViewById(R.id.send_request_btn);

        // set the views
        usernameTV.setText(user.getUsername());

        //check if there is a bio. If not show a message
        emptyRequestTV.setText("Looks like you're all caught up!");
        emptyFriendsTV.setText("You're not following anyone!");

        // fill the friends list view
        friendsLV.setEmptyView(emptyFriendsTV);
        FriendsListAdapter friendsAdapter = new FriendsListAdapter(getActivity(),
                R.layout.item_friend, user.getFriendList());
        friendsLV.setAdapter(friendsAdapter);

        // fill the friend request list view
        requestLV.setEmptyView(emptyRequestTV);
        FriendRequestListAdapter FRAdapter = new FriendRequestListAdapter(getActivity(),
                R.layout.item_friend_request, user.getFriendRequests());
        FRAdapter.setCustomButtonListener(new FriendRequestListAdapter.buttonListener() {
            @Override
            public void onAcceptClickListener(int position) {
                user.acceptFriendRequest(position);
                friendsAdapter.notifyDataSetChanged();
                FRAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDeclineClickListener(int position) {
                user.denyFriendRequest(position);
                FRAdapter.notifyDataSetChanged();
            }
        });
        requestLV.setAdapter(FRAdapter);

        // send friend request when the button is pressed
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = searchFriendsET.getText().toString();
                searchFriendsET.getText().clear();

                // make sure the username is valid
                DatabaseAdapter.checkUsernameExists(username,
                        new DatabaseAdapter.UsernameCheckCallback() {
                            @Override
                            public void onUsernameCheckCallback(boolean usernameExists) {
                                if(usernameExists){
                                    // send the friend request
                                    user.sendFriendRequest(username);
                                    Toast.makeText(getContext(), "Friend request sent!", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getContext(), "User not found!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

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

