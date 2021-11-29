package com.example.ohthmhyh.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ohthmhyh.adapters.FriendRequestListAdapter;
import com.example.ohthmhyh.adapters.FriendsListAdapter;
import com.example.ohthmhyh.R;
import com.example.ohthmhyh.adapters.FriendsRecyclerViewAdapter;
import com.example.ohthmhyh.adapters.HabitFeedRecyclerViewAdapter;
import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.entities.User;
import com.example.ohthmhyh.activities.EditProfileActivity;
import com.example.ohthmhyh.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * The Fragment that shows the user's profile. This Fragment's parent Activity is MainActivity and
 * is created when the user clicks on the bottom navigation bar's profile button.
 *
 * There are no outstanding issues that we are aware of.
 */
public class UserFragment extends Fragment {

    private FriendsRecyclerViewAdapter adapter;

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
        EditText searchFriendsET = view.findViewById(R.id.friend_Search_ET);
        Button searchBtn = view.findViewById(R.id.send_request_btn);
        RecyclerView friendsAndRequestsRV = view.findViewById(R.id.friendsAndRequestsRV);

        // set the views
        usernameTV.setText(user.getUsername());

        // set up the feed recyclerView
        adapter = new FriendsRecyclerViewAdapter(user.getFriendList(), user.getFriendRequests());
        friendsAndRequestsRV.setLayoutManager(new LinearLayoutManager(view.getContext()));
        friendsAndRequestsRV.setHasFixedSize(true);
        friendsAndRequestsRV.setAdapter(adapter);

        //check if there is a bio. If not show a message
//        emptyRequestTV.setText("Looks like you're all caught up!");
//        emptyFriendsTV.setText("You're not following anyone!");


//        // When a friend is tapped, ask if they should be removed
//        friendsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView uname = view.findViewById(R.id.item_friend);
//                String username = uname.getText().toString();
//
//                // show a confirmation dialog when deleting
//                new AlertDialog.Builder(view.getContext())
//                        .setMessage("Remove " + username + " as a friend?")
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            // when "yes" is pressed, delete the friend
//                            @Override
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                user.removeFriend(position);
//                                friendsAdapter.notifyDataSetChanged();
//                            }})
//                        .setNegativeButton(android.R.string.no, null)
//                        .show();
//            }
//        });



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

