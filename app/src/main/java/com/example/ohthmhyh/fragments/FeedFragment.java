package com.example.ohthmhyh.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ohthmhyh.R;
import com.example.ohthmhyh.adapters.HabitFeedRecyclerViewAdapter;
import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.database.HabitList;
import com.example.ohthmhyh.entities.Habit;
import com.example.ohthmhyh.entities.User;

import java.util.ArrayList;

/**
 * The Fragment that shows the user's feed. This Fragment's parent Activity is MainActivity and is
 * created when the user clicks on the bottom navigation bar's feed button.
 *
 * There are no outstanding issues that we are aware of.
 */
public class FeedFragment extends Fragment{


    ArrayList<Habit> feedHabits;
    ArrayList<String> usernames;
    HabitFeedRecyclerViewAdapter adapter;

    public FeedFragment() {
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
        feedHabits = new ArrayList<>();
        usernames = new ArrayList<>();

        // Inflate the layout and get views for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        RecyclerView feedRV = view.findViewById(R.id.feed_RV);

        // set up the feed recyclerView
        adapter = new HabitFeedRecyclerViewAdapter(view.getContext(), feedHabits, usernames);
        feedRV.setLayoutManager(new LinearLayoutManager(view.getContext()));
        feedRV.setHasFixedSize(true);
        feedRV.setAdapter(adapter);

        // populate the feed from the database
        buildFeed();

        return view;
    }


    /**
     * Builds up the habit feed from the database.
     */
    public void buildFeed(){
        DatabaseAdapter dba = DatabaseAdapter.getInstance();

        // get the user so we know who they follow
        dba.pullUser(new DatabaseAdapter.ProfileCallback() {
            @Override
            public void onProfileCallback(User user) {

                // build up the habit list of public habits by
                // getting the habits of the people this user follows
                for(int i =0; i< user.getFriendList().size(); i++){

                    // UID of the user for which to get their public habits
                    String followingUID = user.getFriendList().get(i);

                    // pull username of user we are getting public habits for
                    dba.pullUsernameFromUID(followingUID, new DatabaseAdapter.UsernameCallback() {
                        @Override
                        public void onUsernameCallback(String username) {

                            // pull the habits of the user we follow
                            dba.pullHabits(followingUID, new DatabaseAdapter.HabitCallback() {
                                @Override
                                public void onHabitCallback(HabitList hList) {
                                    for (Habit habit: hList.getHabitList()) {
                                        // add the habit to the feed if it is public
                                        if(!habit.getIsPrivate()){
                                            feedHabits.add(habit);
                                            usernames.add(username);
                                            adapter.notifyItemInserted(adapter.getItemCount()+1);
                                        }
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }

}

