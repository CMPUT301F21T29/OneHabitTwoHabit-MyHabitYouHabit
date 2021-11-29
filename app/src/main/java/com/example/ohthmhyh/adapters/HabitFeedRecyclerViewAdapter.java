package com.example.ohthmhyh.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.ohthmhyh.entities.Habit;

import java.util.ArrayList;

/**
 * An adapter used for putting Habit objects into elements of a RecyclerView for the user's feed.
 * Each row is populated with views to display the Habit's attributes. There are no gestures or
 * clicking allowed for these feed Habits.
 *
 * There are no outstanding issues that we are aware of.
 */
public class HabitFeedRecyclerViewAdapter extends HabitRecyclerViewAdapter{

    ArrayList<String> usernames;

    /**
     * Creates an instance of the custom RecyclerView adapter used for showing habits in the feed
     * @param context   Context from the activity
     * @param content The Habits to display in the RecyclerView
     * @param usernames The usernames associated with each Habit
     */
    public HabitFeedRecyclerViewAdapter(Context context,
                                        ArrayList<Habit> content,
                                        ArrayList<String> usernames) {
        super(context, content);
        this.usernames = usernames;
    }


    /**
     * Replaces the content of the views in the RecyclerView with the proper view for a habit.
     * @param holder the view holder of the habit
     * @param position the position of the habit view in the RecyclerView
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // set the content of the views in the RecyclerView element
        super.onBindViewHolder(holder, position);
        holder.username.setVisibility(View.VISIBLE);
        holder.username.setText(usernames.get(position));
    }

}
