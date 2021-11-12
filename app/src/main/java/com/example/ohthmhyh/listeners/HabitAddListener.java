package com.example.ohthmhyh.listeners;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.ohthmhyh.CustomAdapterHF;
import com.example.ohthmhyh.entities.Habit;
import com.example.ohthmhyh.database.HabitList;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The listener that is called to verify a new habit
 */
public class HabitAddListener extends HabitUpdateListener {

    public HabitAddListener(AlertDialog alertDialog, EditText habitDescriptionET, TextView habitDateET, EditText habitNameET, ToggleButton monFrequency, ToggleButton tueFrequency, ToggleButton wedFrequency, ToggleButton thuFrequency, ToggleButton friFrequency, ToggleButton satFrequency, ToggleButton sunFrequency, ToggleButton private_button, HabitList habitList, TextView errorSchedule, CustomAdapterHF adapter) {
        super(alertDialog, habitDescriptionET, habitDateET, habitNameET, monFrequency, tueFrequency, wedFrequency, thuFrequency, friFrequency, satFrequency, sunFrequency, private_button, habitList, errorSchedule, adapter);
    }

    @Override
    protected void action(String habitName, String habitDescription, LocalDate startDate, ArrayList<Habit.Days> schedule) {
        habitList.addHabit(
                new Habit(habitName, habitDescription, startDate, schedule, private_button.isChecked()));
    }

}
