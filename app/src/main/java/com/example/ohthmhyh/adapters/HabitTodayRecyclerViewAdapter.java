package com.example.ohthmhyh.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.ohthmhyh.database.HabitList;
import com.example.ohthmhyh.entities.Habit;

/**
 * A simple RecycleviewAdapter for the Habit list.
 */
public class HabitTodayRecyclerViewAdapter extends HabitRecyclerViewAdapter {

    /**
     * Creates the custom adapter instance
     * @param habitList The HabitList containing the habits
     * @param context Context from the activity
     * The CERecycleviewAdapter creater Needs and array, context and a touch Listener
     */
    public HabitTodayRecyclerViewAdapter(Context context, HabitList habitList) {
        super(context, habitList);
    }

    //sets the things in the display
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //Todo
        //Need to error check because somethings might be null

        super.onBindViewHolder(holder, position);

        Habit h = habitList.getHabit(position);

        // Hide the username TextView but show the Checkbox
        holder.checkbox.setVisibility(View.VISIBLE);

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.checkbox.isChecked()){
                    Log.d("tag", h.getName() + " checked");
                    int index = habitList.getHabitIndex(h);

                    h.logCompleted();
                    habitList.setHabit(index, h);
                } else {
                    Log.d("tag", h.getName() + " unchecked");
                    int index = habitList.getHabitIndex(h);

                    h.undoCompleted();
                    habitList.setHabit(index, h);
                }
            }
        });

    }

}
