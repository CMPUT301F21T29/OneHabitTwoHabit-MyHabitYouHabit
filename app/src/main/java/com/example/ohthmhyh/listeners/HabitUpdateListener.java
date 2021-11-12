package com.example.ohthmhyh.listeners;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.ohthmhyh.entities.Habit;
import com.example.ohthmhyh.database.HabitList;
import com.example.ohthmhyh.fragments.HabitsFragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Verifies that a new or edited habit is valid
 */
public class HabitUpdateListener implements View.OnClickListener {
    protected Activity activity;
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
    protected TextView errorSchedule;
    private ColorStateList originalErrorScheduleColor;

    public HabitUpdateListener(
            Activity activity,
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
            TextView errorSchedule
    ) {
        this.activity = activity;
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
        this.errorSchedule = errorSchedule;

        this.originalErrorScheduleColor = errorSchedule.getTextColors();
    }

    /**
     * Verify that the new or edited habit is valid
     * @param v The view that was clicked on to initiate verification
     */
    @Override
    public void onClick(View v) {
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
            LocalDate startDate = stringToDate(habitDateET.getText().toString());
            action(habitName, habitDescription, startDate, schedule);
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
            } else {
                errorSchedule.setText("Weekly Frequency");
                errorSchedule.setTextColor(originalErrorScheduleColor);
            }

            if (habitDateET.getText().toString().equals("")) {
                habitDateET.setHint("ENTER A DATE");
                habitDateET.setHintTextColor(Color.RED);
            }
        }
    }

    /**
     * Performs an action based on whether this update listener is responding to verify a new habit
     * or an existing habit
     * @param habitName The edited or new name for the habit
     * @param habitDescription The edited or new description for the habit
     * @param startDate The edited or new start date for the habit
     * @param schedule The edited or new schedule for the habit
     */
    protected void action(
            String habitName,
            String habitDescription,
            LocalDate startDate,
            ArrayList<Habit.Days> schedule
    ) {
        Habit habit = new Habit(
                habitName, habitDescription, startDate, schedule, private_button.isChecked());
        Intent intent = new Intent();
        intent.putExtra(HabitsFragment.ARG_RETURNED_HABIT, habit);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    /**
     * Coverts a String to a LocalDate object
     * @param dateAsString The date string to convert in "d/MM/yyyy" format
     * @return The LocalDate object created from the string
     */
    private LocalDate stringToDate(String dateAsString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

        //convert String to LocalDate
        LocalDate localDate = LocalDate.parse(dateAsString, formatter);
        return localDate;
    }
}
