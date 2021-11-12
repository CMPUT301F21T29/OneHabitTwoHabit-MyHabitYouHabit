package com.example.ohthmhyh.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.ohthmhyh.Constants;
import com.example.ohthmhyh.R;
import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.database.HabitList;
import com.example.ohthmhyh.entities.Habit;
import com.example.ohthmhyh.listeners.DatePickerListener;
import com.example.ohthmhyh.listeners.HabitAddListener;
import com.example.ohthmhyh.listeners.HabitEditListener;
import com.example.ohthmhyh.watchers.LengthTextWatcher;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class UpdateHabitActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String ARG_HABIT_INDEX = "habit_index_arg";

    private EditText nameEditText;
    private EditText descriptionEditText;
    private TextView dateTextView;
    private TextView scheduleErrorTextView;
    private HabitList habitList;
    private Button doneButton;
    private ToggleButton mondayToggleButton;
    private ToggleButton tuesdayToggleButton;
    private ToggleButton wednesdayToggleButton;
    private ToggleButton thursdayToggleButton;
    private ToggleButton fridayToggleButton;
    private ToggleButton saturdayToggleButton;
    private ToggleButton sundayToggleButton;
    private ToggleButton privateButton;

    private int habitIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_addhabit);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Bundle extras = getIntent().getExtras();
        habitIndex = extras.getInt(ARG_HABIT_INDEX, -1);

        // Get the HabitList from the database.
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        databaseAdapter.pullHabits(new DatabaseAdapter.HabitCallback() {
            @Override
            public void onHabitCallback(HabitList hList) {
                habitList = hList;
                setup();  // TODO: Once habitList doesn't have to be loaded, move setUp to this method.
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Sets the date of the edit text
     * @param i this is the year
     * @param i1 this is the month
     * @param i2 this is the day
     */
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        int year = i;
        int month = i1 + 1;
        int day = i2;
        dateTextView.setText(day + "/" + month + "/" + year);
    }

    /**
     * Converts a local date object to a string
     * @param localDate this takes a date time object
     * @return string object in the form of a date
     */
    private String dateToString(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        String formattedString = localDate.format(formatter);
        return formattedString;
    }

    /**
     * Show the Habit by changing the attributes of the views that describe things about the Habit.
     * @param habit The Habit to show through the views.
     */
    private void showHabit(Habit habit) {
        nameEditText.setText(habit.getName());
        descriptionEditText.setText(habit.getDescription());
        dateTextView.setText(dateToString(habit.StartDateAsLocalDate()));
        privateButton.setChecked(habit.getIsPrivate());

        ArrayList<Habit.Days> schedule = habit.getSchedule();
        if (schedule.contains(Habit.Days.Mon)) mondayToggleButton.setChecked(true);
        if (schedule.contains(Habit.Days.Tue)) tuesdayToggleButton.setChecked(true);
        if (schedule.contains(Habit.Days.Wed)) wednesdayToggleButton.setChecked(true);
        if (schedule.contains(Habit.Days.Thu)) thursdayToggleButton.setChecked(true);
        if (schedule.contains(Habit.Days.Fri)) fridayToggleButton.setChecked(true);
        if (schedule.contains(Habit.Days.Sat)) saturdayToggleButton.setChecked(true);
        if (schedule.contains(Habit.Days.Sun)) sundayToggleButton.setChecked(true);
    }

    private void setup() {
        // Get references to views in this activity.
        nameEditText = findViewById(R.id.enter_habit_name);
        descriptionEditText = findViewById(R.id.enter_habit_des);
        dateTextView = findViewById(R.id.enter_date);
        scheduleErrorTextView = findViewById(R.id.choose_frequency_txtview);
        doneButton = findViewById(R.id.done_button);
        mondayToggleButton = findViewById(R.id.mon);
        tuesdayToggleButton = findViewById(R.id.tue);
        wednesdayToggleButton = findViewById(R.id.wed);
        thursdayToggleButton = findViewById(R.id.thu);
        fridayToggleButton = findViewById(R.id.fri);
        saturdayToggleButton = findViewById(R.id.sat);
        sundayToggleButton = findViewById(R.id.sun);
        privateButton = findViewById(R.id.private_button);

        // Set some properties about these views.
        nameEditText.addTextChangedListener(
                new LengthTextWatcher(
                        nameEditText,
                        Constants.HABIT_NAME_MIN_LENGTH,
                        Constants.HABIT_NAME_MAX_LENGTH
                ));
        descriptionEditText.addTextChangedListener(
                new LengthTextWatcher(
                        descriptionEditText,
                        Constants.HABIT_DESCRIPTION_MIN_LENGTH,
                        Constants.HABIT_DESCRIPTION_MAX_LENGTH
                ));
        dateTextView.setText("Enter a date");
        dateTextView.setOnClickListener(
                new DatePickerListener(this, UpdateHabitActivity.this));

        View.OnClickListener habitUpdateListener;
        if (habitIndex < 0) {
            // We are adding a habit.
            habitUpdateListener = new HabitAddListener(
                    descriptionEditText,
                    dateTextView,
                    nameEditText,
                    mondayToggleButton,
                    tuesdayToggleButton,
                    wednesdayToggleButton,
                    thursdayToggleButton,
                    fridayToggleButton,
                    saturdayToggleButton,
                    sundayToggleButton,
                    privateButton,
                    habitList,
                    scheduleErrorTextView
            );
        } else {
            // We are editing/viewing a habit.
            Habit habit = habitList.getHabit(habitIndex);

            habitUpdateListener = new HabitEditListener(
                    descriptionEditText,
                    dateTextView,
                    nameEditText,
                    mondayToggleButton,
                    tuesdayToggleButton,
                    wednesdayToggleButton,
                    thursdayToggleButton,
                    fridayToggleButton,
                    saturdayToggleButton,
                    sundayToggleButton,
                    privateButton,
                    habitList,
                    scheduleErrorTextView,
                    habit,
                    habitIndex
            );

            showHabit(habit);
        }

        // Set the behaviour of the done button when it's clicked.
        doneButton.setOnClickListener(habitUpdateListener);
    }
}
