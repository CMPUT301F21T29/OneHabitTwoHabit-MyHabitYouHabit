package com.example.ohthmhyh.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.ohthmhyh.R;
import com.example.ohthmhyh.adapters.HabitFeedRecyclerViewAdapter;
import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.entities.Habit;

import java.util.ArrayList;

/**
 * The Fragment that shows the user's feed. This Fragment's parent Activity is MainActivity and is
 * created when the user clicks on the bottom navigation bar's feed button.
 *
 * There are no outstanding issues that we are aware of.
 */
public class FeedFragment extends Fragment{

    private ArrayList<Habit> feedHabits;
    private ArrayList<String> usernames;
    private HabitFeedRecyclerViewAdapter adapter;
    private DatabaseAdapter databaseAdapter = DatabaseAdapter.getInstance();

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
        setHasOptionsMenu(true);  // For the refresh button at the top menu bar.

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
                    buildFeed();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Builds up the habit feed from the database.
     */
    public void buildFeed() {
        // Clear the feed and usernames before loading the feed (in case of a refresh).
        while (feedHabits.size() > 0) {
            feedHabits.remove(0);
            usernames.remove(0);
            adapter.notifyItemRemoved(0);
        }

        for (int i = 0; i < databaseAdapter.userFriendList().size(); i++) {
            // UID of the user for which to get their public habits
            String followingUID = databaseAdapter.userFriendList().get(i);

            // pull username of user we are getting public habits for
            databaseAdapter.pullUsernameFromUID(followingUID, new DatabaseAdapter.UsernameCallback() {
                @Override
                public void onUsernameCallback(String username) {
                    // Pull the Habits of the Users we follow.
                    databaseAdapter.pullHabitsForUser(followingUID, new DatabaseAdapter.OnLoadedHabitsListener() {
                        @Override
                        public void onLoadedHabits(ArrayList<Habit> habits) {
                            for (Habit habit : habits) {
                                if (!habit.getIsPrivate()) {
                                    feedHabits.add(habit);
                                    usernames.add(username);
                                    adapter.notifyItemInserted(adapter.getItemCount() + 1);
                                }
                            }
                        }
                    });
                }
            });
        }
    }

}

