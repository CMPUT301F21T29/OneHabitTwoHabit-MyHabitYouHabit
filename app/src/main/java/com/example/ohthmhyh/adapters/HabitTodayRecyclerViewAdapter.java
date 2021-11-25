package com.example.ohthmhyh.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.ohthmhyh.activities.MainActivity;
import com.example.ohthmhyh.activities.UpdateHabitEventActivity;
import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.database.HabitEventList;
import com.example.ohthmhyh.database.HabitList;
import com.example.ohthmhyh.entities.Habit;

import java.util.ArrayList;

/**
 * A simple RecycleviewAdapter for the Habit list.
 */
public class HabitTodayRecyclerViewAdapter extends HabitRecyclerViewAdapter {

    private HabitList habitList;
    private int habitEventpos;
    private DatabaseAdapter databaseAdapter;
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
        }
        // Hide the username TextView but show the Checkbox
        holder.checkbox.setVisibility(View.VISIBLE);

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.checkbox.isChecked()){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    Log.d("tag", habit.getName() + " checked");
                    int index = habitList.getHabitIndex(habit);  // Get the index of the Habit.

                    if (habit.isDueToday()&&habit.wasCompletedToday()){
                        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                        alertDialogBuilder.setMessage("You have already Made a habit Event to day");
                        alertDialogBuilder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    //If user wants to delete and has confirmed run this code with deletes the habit
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                    }
                                });
                        alertDialogBuilder.setNegativeButton("Edit",
                                new DialogInterface.OnClickListener() {
                                    //If user wants to delete and has confirmed run this code with deletes the habit
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(context, UpdateHabitEventActivity.class);
                                        databaseAdapter = DatabaseAdapter.getInstance();
                                        databaseAdapter.pullHabitEvents(new DatabaseAdapter.HabitEventCallback() {
                                            @Override
                                            public void onHabitEventCallback(HabitEventList habitEvents) {
                                               for (int i=0;i<habitEvents.size();i++){
                                                   if (habitEvents.getHabitEvent(i).getHabitUHID()==habit.getUHID()){
                                                       habitEventpos=i;
                                                   }
                                               }
                                            }
                                        });
                                        intent.putExtra(UpdateHabitEventActivity.ARG_HABIT_EVENT_INDEX, habitEventpos);
                                        context.startActivity(intent);
                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                    else{//Goto make a habit Event.
                        Intent intent = new Intent(context, UpdateHabitEventActivity.class);
                        intent.putExtra(UpdateHabitEventActivity.ARG_HABIT_INDEX, index);
                        context.startActivity(intent);}


                }
                else {
//                    Log.d("tag", habit.getName() + " unchecked");
//                    int index = habitList.getHabitIndex(habit);  // Get the index of the Habit.
//
//                    habit.undoCompleted();
//                    habitList.setHabit(index, habit);
                }
            }
        });

    }

}
