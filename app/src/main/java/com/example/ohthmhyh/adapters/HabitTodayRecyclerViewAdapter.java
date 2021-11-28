package com.example.ohthmhyh.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.ohthmhyh.activities.UpdateHabitEventActivity;
import com.example.ohthmhyh.database.HabitList;
import com.example.ohthmhyh.entities.Habit;

import java.util.ArrayList;

/**
 * An adapter used for putting Habit objects into elements of a RecyclerView for the user's Habits
 * that are due today. Each row is populated with views to display the Habit's attributes. There are
 * no gestures or clicking allowed for these feed Habits.
 *
 * There are no outstanding issues that we are aware of.
 */
public class HabitTodayRecyclerViewAdapter extends HabitRecyclerViewAdapter {

    private HabitList habitList;

    /**
     * Creates the custom adapter instance
     * @param context Context from the activity
     * @param habitList The HabitList containing the habits
     * @param content The Habits to display in the RecyclerView
     */
    public HabitTodayRecyclerViewAdapter(Context context, HabitList habitList,
                                         ArrayList<Habit> content) {
        super(context, content);
        this.habitList = habitList;
    }

    //sets the things in the display
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        super.onBindViewHolder(holder, position);

        Habit habit = content.get(position);
        if (habit.wasCompletedToday()){
            holder.checkbox.setChecked(true);
            holder.checkbox.setEnabled(false);  // Disable the checkbox if it's already completed.
        }
        // Hide the username TextView but show the Checkbox
        holder.checkbox.setVisibility(View.VISIBLE);

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.checkbox.isChecked()){
                    Log.d("tag", habit.getName() + " checked");
                    int index = habitList.getHabitIndex(habit);  // Get the index of the Habit.

                    // Go make an event for this Habit if one was not already made today.
                    if (!habit.wasCompletedToday()) {
                        // Uncheck the checkbox in case they decide to back-out of the event.
                        holder.checkbox.setChecked(false);

                        Intent intent = new Intent(context, UpdateHabitEventActivity.class);
                        intent.putExtra(UpdateHabitEventActivity.ARG_HABIT_INDEX, index);
                        context.startActivity(intent);
                    }
                }
            }
        });

    }

}
