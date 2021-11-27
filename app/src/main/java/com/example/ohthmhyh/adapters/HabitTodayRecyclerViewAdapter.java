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
        //Todo
        //Need to error check because somethings might be null

        super.onBindViewHolder(holder, position);

        Habit habit = content.get(position);

        // Hide the username TextView but show the Checkbox
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
