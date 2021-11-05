package com.example.ohthmhyh.listeners;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.ohthmhyh.CustomAdapterHF;
import com.example.ohthmhyh.Habit;
import com.example.ohthmhyh.HabitList;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The listener that is called to verify an edited habit
 */
public class HabitEditListener extends HabitUpdateListener {

    private Habit chosenHabit;
    private int chosenHabitPosition;

    public HabitEditListener(AlertDialog alertDialog, EditText habitDescriptionET, TextView habitDateET, EditText habitNameET, ToggleButton monFrequency, ToggleButton tueFrequency, ToggleButton wedFrequency, ToggleButton thuFrequency, ToggleButton friFrequency, ToggleButton satFrequency, ToggleButton sunFrequency, ToggleButton private_button, HabitList habitList, TextView errorSchedule, CustomAdapterHF adapter, Habit chosenHabit, int chosenHabitPosition) {
        super(alertDialog, habitDescriptionET, habitDateET, habitNameET, monFrequency, tueFrequency, wedFrequency, thuFrequency, friFrequency, satFrequency, sunFrequency, private_button, habitList, errorSchedule, adapter);
        this.chosenHabit = chosenHabit;
        this.chosenHabitPosition = chosenHabitPosition;
    }

    @Override
    protected void action(String habitName, String habitDescription, LocalDate startDate, ArrayList<Habit.Days> schedule) {
        // Change the habit attributes and replace it in the habit list
        chosenHabit.setName(habitName);
        chosenHabit.setDescription(habitDescription);
        chosenHabit.setStartDate(startDate.toEpochDay());
        chosenHabit.setSchedule(schedule);
        chosenHabit.setIsPrivate(private_button.isChecked());
        habitList.replaceHabit(chosenHabitPosition, chosenHabit);
    }

}
