package com.example.ohthmhyh.listeners;

import android.app.AlertDialog;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.ohthmhyh.CustomAdapterHF;
import com.example.ohthmhyh.Habit;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public abstract class HabitUpdateListener implements View.OnClickListener {
    protected AlertDialog alertDialog;
    protected EditText habitDescriptionET;
    protected TextView habitDateET;
    protected EditText habitNameET;
    protected ToggleButton monFrequency;
    protected ToggleButton tueFrequency;
    protected ToggleButton wedFrequency;
    protected ToggleButton thuFrequency;
    protected ToggleButton friFrequency;
    protected ToggleButton satFrequency;
    protected ToggleButton sunFrequency;
    protected ToggleButton private_button;
    protected ArrayList<Habit> habitArrayList;
    protected TextView errorSchedule;
    protected CustomAdapterHF adapter;

    public HabitUpdateListener(AlertDialog alertDialog,
            EditText habitDescriptionET,
            TextView habitDateET,
            EditText habitNameET,
            ToggleButton monFrequency,
            ToggleButton tueFrequency,
            ToggleButton wedFrequency,
            ToggleButton thuFrequency,
            ToggleButton friFrequency,
            ToggleButton satFrequency,
            ToggleButton sunFrequency,
            ToggleButton private_button,
            ArrayList<Habit> habitArrayList,
            TextView errorSchedule,
            CustomAdapterHF adapter) {
        this.alertDialog = alertDialog;
        this.habitDescriptionET = habitDescriptionET;
        this.habitDateET = habitDateET;
        this.habitNameET = habitNameET;
        this.monFrequency = monFrequency;
        this.tueFrequency = tueFrequency;
        this.wedFrequency = wedFrequency;
        this.thuFrequency = thuFrequency;
        this.friFrequency = friFrequency;
        this.satFrequency = satFrequency;
        this.sunFrequency = sunFrequency;
        this.private_button = private_button;
        this.habitArrayList = habitArrayList;
        this.errorSchedule = errorSchedule;
        this.adapter = adapter;
    }

    @Override
    public void onClick(View view) {
        boolean validated = false;
        String habitName = habitNameET.getText().toString();

        String habitDescription = habitDescriptionET.getText().toString();


        ArrayList<Habit.Days> schedule = new ArrayList<>();

        if (monFrequency.isChecked()) schedule.add(Habit.Days.Mon);
        if (tueFrequency.isChecked()) schedule.add(Habit.Days.Tue);
        if (wedFrequency.isChecked()) schedule.add(Habit.Days.Wed);
        if (thuFrequency.isChecked()) schedule.add(Habit.Days.Thu);
        if (friFrequency.isChecked()) schedule.add(Habit.Days.Fri);
        if (satFrequency.isChecked()) schedule.add(Habit.Days.Sat);
        if (sunFrequency.isChecked()) schedule.add(Habit.Days.Sun);

        if (habitName.length() > 0 && habitName.length() <= 20
                && habitDescription.length() > 0 && habitDescription.length() <= 30
                && schedule.size() > 0
                && habitDateET.getText().length() > 0) {
            validated = true;
        }
        if (validated) {
            alertDialog.dismiss();
            LocalDate startDate = stringToDate(habitDateET.getText().toString());
            action(habitName, habitDescription, startDate, schedule);
            adapter.notifyDataSetChanged();
        }

        else {
            if (habitNameET.getText().toString().length() <= 0) {
                habitNameET.setError("Title is empty");
                habitNameET.requestFocus();
            }
            else if (habitNameET.getText().toString().length() > 20) {
                habitNameET.setError("Title is too long");
                habitNameET.requestFocus();
            }

            if (habitDescriptionET.getText().toString().length() > 30) {
                habitDescriptionET.setError("Description is too long");
                habitDescriptionET.requestFocus();
            }
            else if (habitDescriptionET.getText().toString().length() <= 0) {
                habitDescriptionET.setError("Description is empty");
                habitDescriptionET.requestFocus();
            }

            if (schedule.size() == 0) {
                errorSchedule.setText("Weekly Frequency  (Error: Choose a schedule)");
                errorSchedule.setTextColor(Color.RED);
            }

            if (habitDateET.getText().toString().equals("")) {
                habitDateET.setHint("ENTER A DATE");
                habitDateET.setHintTextColor(Color.RED);
            }
        }
    }

    protected abstract void action(String habitName, String habitDescription, LocalDate startDate, ArrayList<Habit.Days> schedule);

    private LocalDate stringToDate(String dateAsString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

        //convert String to LocalDate
        LocalDate localDate = LocalDate.parse(dateAsString, formatter);
        return localDate;
    }
}
