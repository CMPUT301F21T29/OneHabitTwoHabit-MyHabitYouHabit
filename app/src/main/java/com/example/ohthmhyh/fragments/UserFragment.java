package com.example.ohthmhyh.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * The Fragment that shows the user's profile. This Fragment's parent Activity is MainActivity and
 * is created when the user clicks on the bottom navigation bar's profile button.
 *
 * There are no outstanding issues that we are aware of.
 */
public class UserFragment extends Fragment {

    private DatabaseAdapter databaseAdapter = DatabaseAdapter.getInstance();
    private View view;

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
        setHasOptionsMenu(true);  // For the refresh button at the top menu bar.

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user, container, false);

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
        fillViews();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.top_nav_refresh_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.button_refresh) {
            // Pull the user's updated friend's list and refresh the feed
            databaseAdapter.pullUser(new DatabaseAdapter.OnLoadedListener() {
                @Override
                public void onLoaded() {
                    fillViews();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Fills the views in the fragment that are dependent on the user object
     * that is retrieved from the database.
     */
    private void fillViews(){

        // get the views
        TextView usernameTV= view.findViewById(R.id.username_TV);
        ListView requestLV = view.findViewById(R.id.friend_request_LV);
        TextView emptyRequestTV = view.findViewById(R.id.empty_request_list);
        ListView friendsLV = view.findViewById(R.id.friends_LV);
        TextView emptyFriendsTV = view.findViewById(R.id.empty_friend_list);
        EditText searchFriendsET = view.findViewById(R.id.friend_Search_ET);
        Button searchBtn = view.findViewById(R.id.send_request_btn);

        // set the views
        usernameTV.setText(databaseAdapter.userUsername());

        //check if there is a bio. If not show a message
        emptyRequestTV.setText("Looks like you're all caught up!");
        emptyFriendsTV.setText("You're not following anyone!");

        // fill the friends list view
        friendsLV.setEmptyView(emptyFriendsTV);
        FriendsListAdapter friendsAdapter = new FriendsListAdapter(getActivity(),
                R.layout.item_friend, databaseAdapter.userFriendList());
        friendsLV.setAdapter(friendsAdapter);

        // When a friend is tapped, ask if they should be removed
        friendsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView uname = view.findViewById(R.id.item_friend);
                String username = uname.getText().toString();

                // show a confirmation dialog when deleting
                new AlertDialog.Builder(view.getContext())
                        .setMessage("Remove " + username + " as a friend?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            // when "yes" is pressed, delete the friend
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                databaseAdapter.removeUserFriend(position);
                                friendsAdapter.notifyDataSetChanged();
                            }})
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });

        // fill the friend request list view
        requestLV.setEmptyView(emptyRequestTV);
        FriendRequestListAdapter FRAdapter = new FriendRequestListAdapter(getActivity(),
                R.layout.item_friend_request, databaseAdapter.userFriendRequests());
        FRAdapter.setCustomButtonListener(new FriendRequestListAdapter.buttonListener() {
            @Override
            public void onAcceptClickListener(int position) {
                databaseAdapter.acceptUserFriendRequest(position);
                friendsAdapter.notifyDataSetChanged();
                FRAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDeclineClickListener(int position) {
                databaseAdapter.denyUserFriendRequest(position);
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
                                    databaseAdapter.sendUserFriendRequest(username);
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
        loginActivityIntent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginActivityIntent);
    }

}

