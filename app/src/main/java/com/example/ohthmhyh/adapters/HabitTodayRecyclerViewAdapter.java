package com.example.ohthmhyh.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.entities.Habit;

import java.util.ArrayList;

/**
 * A simple RecycleviewAdapter for the Habit list.
 */
public class HabitTodayRecyclerViewAdapter extends HabitRecyclerViewAdapter {

    private DatabaseAdapter databaseAdapter = DatabaseAdapter.getInstance();
    
    /**
     * Creates the custom adapter instance
     * @param context Context from the activity
     * @param content The Habits to display in the RecyclerView
     */
    public HabitTodayRecyclerViewAdapter(Context context, ArrayList<Habit> content) {
        super(context, content);
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
                    int index = databaseAdapter.indexForHabit(habit);

                    habit.logCompleted();
                    databaseAdapter.setHabit(index, habit);
                } else {
                    Log.d("tag", habit.getName() + " unchecked");
                    int index = databaseAdapter.indexForHabit(habit);

                    habit.undoCompleted();
                    databaseAdapter.setHabit(index, habit);
                }
            }
        });

    }

}
