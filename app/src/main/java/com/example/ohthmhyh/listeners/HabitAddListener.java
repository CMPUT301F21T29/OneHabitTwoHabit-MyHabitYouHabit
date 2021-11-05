package com.example.ohthmhyh.listeners;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.ohthmhyh.CustomAdapterHF;
import com.example.ohthmhyh.Habit;

import java.time.LocalDate;
import java.util.ArrayList;

public class HabitAddListener extends HabitUpdateListener {



    public HabitAddListener(AlertDialog alertDialog, EditText habitDescriptionET, TextView habitDateET, EditText habitNameET, ToggleButton monFrequency, ToggleButton tueFrequency, ToggleButton wedFrequency, ToggleButton thuFrequency, ToggleButton friFrequency, ToggleButton satFrequency, ToggleButton sunFrequency, ToggleButton private_button, ArrayList<Habit> habitArrayList, TextView errorSchedule, CustomAdapterHF adapter) {
        super(alertDialog, habitDescriptionET, habitDateET, habitNameET, monFrequency, tueFrequency, wedFrequency, thuFrequency, friFrequency, satFrequency, sunFrequency, private_button, habitArrayList, errorSchedule, adapter);
    }

    @Override
    protected void action(String habitName, String habitDescription, LocalDate startDate, ArrayList<Habit.Days> schedule) {
        habitArrayList.add(
                new Habit(habitName, habitDescription, startDate, schedule, private_button.isChecked()));
    }
}
