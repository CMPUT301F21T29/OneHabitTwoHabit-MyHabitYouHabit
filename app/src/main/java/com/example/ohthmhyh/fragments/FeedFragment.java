package com.example.ohthmhyh.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ohthmhyh.CustomAdapterHF;
import com.example.ohthmhyh.R;
import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.database.HabitList;
import com.example.ohthmhyh.entities.Habit;
import com.example.ohthmhyh.entities.User;
import com.example.ohthmhyh.helpers.TransportableTouchHelper;

import java.util.ArrayList;


public class FeedFragment extends Fragment implements CustomAdapterHF.OntouchListener{


    ArrayList<Habit> feedHabits;
    CustomAdapterHF adapter;

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
        feedHabits = new ArrayList<Habit>();

        // Inflate the layout and get views for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        RecyclerView feedRV = view.findViewById(R.id.feed_RV);

        // make the feedHabits into a HabitList for compatibility with the recyclerView adapter
        HabitList habitList = new HabitList();
        habitList.setHabitList(feedHabits);

        // set up the recyclerView adapter
        adapter = new CustomAdapterHF(view.getContext(), FeedFragment.this, habitList);
        ItemTouchHelper.Callback callback = new TransportableTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(feedRV);
        adapter.setTouchhelper(itemTouchHelper);

        // set up the feed recyclerView
        feedRV.setLayoutManager(new LinearLayoutManager(view.getContext()));
        feedRV.setHasFixedSize(true);
        feedRV.setAdapter(adapter);

        // populate the feed from the database
        buildFeed();

//        for(int i = 0; i<30; i++){
//            feedHabits.add(Habit.makeDummyHabit());
//            adapter.notifyItemInserted(adapter.getItemCount()+1);
//        }

        return view;
    }


    /**
     * Builds up the habit feed from the database.
     */
    public void buildFeed(){
        DatabaseAdapter dba = new DatabaseAdapter();

        // get the user so we know who they follow
        dba.pullUser(new DatabaseAdapter.ProfileCallback() {
            @Override
            public void onProfileCallback(User user) {

                // build up the habit list of public habits by
                // getting the habits of the people this user follows
                for(int i =0; i< user.getFriendList().size(); i++){

                    // UID of the user for which to get their public habits
                    String followingUID = user.getFriendList().get(i);

                    // pull the habits of the user we follow
                    dba.pullHabits(followingUID, new DatabaseAdapter.HabitCallback() {
                        @Override
                        public void onHabitCallback(HabitList hList) {
                            for (Habit habit: hList.getHabitList()) {
                                // add the habit to the feed if it is public
                                if(!habit.getIsPrivate()){
                                    feedHabits.add(habit);
                                    adapter.notifyItemInserted(adapter.getItemCount()+1);
                                }
                            }
                        }
                    });
                }
            }
        });
    }


    /**
     * Called when we tap an item in the feed recyclerView
     * @param position the position of the item that was tapped
     */
    @Override
    public void onItemClicked(int position) {
        // TODO: remove this dependency if we don't need on-click functionality.
        // doing so will require re-working the recyclerView adapter
    }

}

