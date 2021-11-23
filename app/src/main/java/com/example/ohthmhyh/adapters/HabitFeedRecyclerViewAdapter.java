package com.example.ohthmhyh.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.ohthmhyh.database.HabitList;
import com.example.ohthmhyh.entities.Habit;

import java.util.ArrayList;

public class HabitFeedRecyclerViewAdapter extends HabitRecyclerViewAdapter{

    ArrayList<String> usernames;

    /**
     * Creates an instance of the custom RecyclerView adapter used for showing habits in the feed
     * @param context   Context from the activity
     * @param habitList The HabitList containing the habits
     * @param content The Habits to display in the RecyclerView
     * @param usernames The usernames associated with each Habit
     */
    public HabitFeedRecyclerViewAdapter(Context context, HabitList habitList,
                                        ArrayList<Habit> content,
                                        ArrayList<String> usernames) {
        super(context, habitList, content);
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
