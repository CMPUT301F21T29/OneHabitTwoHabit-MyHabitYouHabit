package com.example.ohthmhyh.adapters;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.ohthmhyh.database.HabitList;

import java.util.ArrayList;

public class HabitFeedRecyclerViewAdapter extends HabitRecyclerViewAdapter{

    ArrayList<String> usernames;

    /**
     * Creates an instance of the custom RecyclerView adapter used for showing habits in the feed
     * @param context   Context from the activity
     * @param habitList The HabitList containing the habits
     */
    public HabitFeedRecyclerViewAdapter(Context context, HabitList habitList,
                                        ArrayList<String> usernames) {
        super(context, habitList);
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
        holder.username.setText(usernames.get(position));
    }

}
