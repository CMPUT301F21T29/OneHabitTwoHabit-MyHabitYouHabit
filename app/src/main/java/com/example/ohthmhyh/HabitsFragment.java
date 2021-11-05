package com.example.ohthmhyh;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.ohthmhyh.listeners.DatePickerListener;
import com.example.ohthmhyh.listeners.HabitAddListener;
import com.example.ohthmhyh.listeners.HabitEditListener;
import com.example.ohthmhyh.watchers.LengthTextWatcher;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HabitsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HabitsFragment extends Fragment implements DatePickerDialog.OnDateSetListener, CustomAdapterHF.OntouchListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView habitDateET;
    private HabitList habitList;
    private CustomAdapterHF adapter;
    private DatabaseAdapter databaseAdapter;

    public HabitsFragment(){
        // Required empty public constructor
    }

    // TODO: Remove all the created todos.
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HabitsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HabitsFragment newInstance(String param1, String param2) {
        HabitsFragment fragment = new HabitsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_habits, null);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * this creates the fragment
     * this also sets the recyclerView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Create a new alert dialog when Add a Habit button is clicked

        View view = inflater.inflate(R.layout.fragment_habits, container, false);

        HabitsFragment thisHabitsFragment = this;

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_hf);

        // Get the HabitList from the database.
        databaseAdapter = new DatabaseAdapter();
        databaseAdapter.pullHabits(new DatabaseAdapter.HabitCallback() {
            @Override
            public void onHabitCallback(HabitList hList) {
                habitList = hList;

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setHasFixedSize(true);
                adapter = new CustomAdapterHF(getContext(), thisHabitsFragment, habitList);
                ItemTouchHelper.Callback callback = new TouchingHandlingHF(adapter);
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
                adapter.setTouchhelper(itemTouchHelper);
                itemTouchHelper.attachToRecyclerView(recyclerView);
                recyclerView.setAdapter(adapter);
            }
        });

        Button addButton = view.findViewById(R.id.add_habit);
        addButton.setOnClickListener((v) -> {
            updateHabitAlertDialog(v, -1);
        });

        return view;
    }

    // TODO: Move this somewhere else (like the stringToDate method).
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

    // TODO: Should be moved to the alert dialog responsible for spawning the date picker.
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
        habitDateET.setText(day + "/" + month + "/" + year);
    }

    /**
     * Calls the edit dialog
     * @param position the position we need to edit
     */
    @Override
    public void onItemClicked(int position) {
        updateHabitAlertDialog(getView(), position);
    }

    // TODO: This should eventually be moved to its own alert dialog class that implements the
    //       DatePickerDialog.OnDateSetListener.
    /**
     * Create an alert dialog to edit/view an existing habit, or create a new habit.
     * @param v The view to create the alert dialog from
     * @param chosenHabitIndex The index of the chosen habit
     */
    public void updateHabitAlertDialog(View v, int chosenHabitIndex) {
        AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
        v = LayoutInflater.from(getContext()).inflate(R.layout.alert_addhabit, null);
        alertDialog.setView(v);

        alertDialog.setTitle("Add a Habit");

        EditText habitNameET = v.findViewById(R.id.enter_habit_name);
        habitNameET.addTextChangedListener(new LengthTextWatcher(habitNameET, 1, 20));

        EditText habitDescriptionET = v.findViewById(R.id.enter_habit_des);
        habitDescriptionET.addTextChangedListener(new LengthTextWatcher(habitDescriptionET, 1, 30));

        habitDateET = v.findViewById(R.id.enter_date);
        habitDateET.setHint("Enter a date");
        habitDateET.setOnClickListener(new DatePickerListener(getContext(), HabitsFragment.this));

        TextView errorSchedule = v.findViewById(R.id.choose_frequency_txtview);
        ToggleButton monFrequency = v.findViewById(R.id.mon);
        ToggleButton tueFrequency = v.findViewById(R.id.tue);
        ToggleButton wedFrequency = v.findViewById(R.id.wed);
        ToggleButton thuFrequency = v.findViewById(R.id.thu);
        ToggleButton friFrequency = v.findViewById(R.id.fri);
        ToggleButton satFrequency = v.findViewById(R.id.sat);
        ToggleButton sunFrequency = v.findViewById(R.id.sun);

        ToggleButton private_button = v.findViewById(R.id.private_button);

        View.OnClickListener habitUpdateListener;

        if (chosenHabitIndex < 0) {
            // Add a habit
            habitUpdateListener = new HabitAddListener(
                    alertDialog,
                    habitDescriptionET,
                    habitDateET,
                    habitNameET,
                    monFrequency,
                    tueFrequency,
                    wedFrequency,
                    thuFrequency,
                    friFrequency,
                    satFrequency,
                    sunFrequency,
                    private_button,
                    habitList,
                    errorSchedule,
                    adapter
            );
        } else {
            // Edit/view a habit
            Habit chosenHabit = habitList.getHabit(chosenHabitIndex);

            habitUpdateListener = new HabitEditListener(
                    alertDialog,
                    habitDescriptionET,
                    habitDateET,
                    habitNameET,
                    monFrequency,
                    tueFrequency,
                    wedFrequency,
                    thuFrequency,
                    friFrequency,
                    satFrequency,
                    sunFrequency,
                    private_button,
                    habitList,
                    errorSchedule,
                    adapter,
                    chosenHabit,
                    chosenHabitIndex
            );

            // Show the chosen habit's attributes by filling out the views
            habitNameET.setText(chosenHabit.getName());
            habitDescriptionET.setText(chosenHabit.getDescription());
            habitDateET.setText(dateToString(chosenHabit.StartDateAsLocalDate()));
            private_button.setChecked(chosenHabit.getIsPrivate());

            ArrayList<Habit.Days> existedSchedule = chosenHabit.getSchedule();
            if (existedSchedule.contains(Habit.Days.Mon)) monFrequency.setChecked(true);
            if (existedSchedule.contains(Habit.Days.Tue)) tueFrequency.setChecked(true);
            if (existedSchedule.contains(Habit.Days.Wed)) wedFrequency.setChecked(true);
            if (existedSchedule.contains(Habit.Days.Thu)) thuFrequency.setChecked(true);
            if (existedSchedule.contains(Habit.Days.Fri)) friFrequency.setChecked(true);
            if (existedSchedule.contains(Habit.Days.Sat)) satFrequency.setChecked(true);
            if (existedSchedule.contains(Habit.Days.Sun)) sunFrequency.setChecked(true);
        }

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Empty Listener as the Cancel button doesn't do anything.
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Done",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });

        alertDialog.show();

        // NOTE: This must be set AFTER the show() method is called in order to keep the alert
        // dialog from closing when invalid input is given. See:
        // https://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(habitUpdateListener);
    }

}
