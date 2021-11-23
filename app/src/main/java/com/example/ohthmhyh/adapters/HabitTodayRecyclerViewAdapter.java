package com.example.ohthmhyh.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.ohthmhyh.database.HabitList;
import com.example.ohthmhyh.entities.Habit;

import java.util.ArrayList;

/**
 * A simple RecycleviewAdapter for the Habit list.
 */
public class HabitTodayRecyclerViewAdapter extends HabitRecyclerViewAdapter {

    /**
     * Creates the custom adapter instance
     * @param context Context from the activity
     * @param habitList The HabitList containing the habits
     * @param content The Habits to display in the RecyclerView
     */
    public HabitTodayRecyclerViewAdapter(Context context, HabitList habitList,
                                         ArrayList<Habit> content) {
        super(context, habitList, content);
    }

    //sets the things in the display
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //Todo
        //Need to error check because somethings might be null

        super.onBindViewHolder(holder, position);

        Habit habit = content.get(position);

        // Hide the username TextView but show the Checkbox.
        holder.username.setVisibility(View.INVISIBLE);
        holder.checkbox.setVisibility(View.VISIBLE);

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.checkbox.isChecked()){
                    Log.d("tag", habit.getName() + " checked");
                    int index = habitList.getHabitIndex(habit);  // Get the index of the Habit.

                    habit.logCompleted();
                    habitList.setHabit(index, habit);
                } else {
                    Log.d("tag", habit.getName() + " unchecked");
                    int index = habitList.getHabitIndex(habit);  // Get the index of the Habit.

                    habit.undoCompleted();
                    habitList.setHabit(index, habit);
                }
            }
        });

    }

}
