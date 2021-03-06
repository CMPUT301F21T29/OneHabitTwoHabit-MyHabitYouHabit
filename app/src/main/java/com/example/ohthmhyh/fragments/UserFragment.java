package com.example.ohthmhyh.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ohthmhyh.R;
import com.example.ohthmhyh.adapters.FriendsRecyclerViewAdapter;
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

    private FriendsRecyclerViewAdapter adapter;
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
    /**
     * Inflates the menu
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.top_nav_refresh_menu, menu);
    }
    /**
     * Calls fillViews to display a user object
     */
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
        EditText searchFriendsET = view.findViewById(R.id.friend_Search_ET);
        Button searchBtn = view.findViewById(R.id.send_request_btn);
        RecyclerView friendsAndRequestsRV = view.findViewById(R.id.friendsAndRequestsRV);

        // set the views
        usernameTV.setText(databaseAdapter.userUsername());

        // set up the friend/friend request recyclerView
        adapter = new FriendsRecyclerViewAdapter(databaseAdapter.userFriendList(),
                databaseAdapter.userFriendRequests());
        adapter.setCustomButtonListener(new FriendsRecyclerViewAdapter.buttonListener() {
            // when the accept friend request button is pressed
            @Override
            public void onAcceptClickListener(int position) {
                databaseAdapter.acceptUserFriendRequest(position);
                adapter.notifyDataSetChanged();
            }
            // when the decline friend request button is pressed
            @Override
            public void onDeclineClickListener(int position) {
                databaseAdapter.denyUserFriendRequest(position);
                adapter.notifyDataSetChanged();
            }
            // when a friend is tapped
            @Override
            public void onFriendClickListener(int position){
                // show a confirmation dialog when deleting
                new AlertDialog.Builder(view.getContext())
                        .setMessage("Remove this friend?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            // when "yes" is pressed, delete the friend
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                databaseAdapter.removeUserFriend(position);
                                adapter.notifyDataSetChanged();
                            }})
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });
        friendsAndRequestsRV.setLayoutManager(new LinearLayoutManager(view.getContext()));
        friendsAndRequestsRV.setHasFixedSize(true);
        friendsAndRequestsRV.setAdapter(adapter);

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

