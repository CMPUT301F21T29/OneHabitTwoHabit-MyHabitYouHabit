package com.example.ohthmhyh;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HabitsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HabitsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private ArrayList<Habit> habitArrayList = new ArrayList<>();


    public HabitsFragment(){
        // Required empty public constructor
    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Create a new alert dialog when Add a Habit button is clicked

        View view = inflater.inflate(R.layout.fragment_habits, container, false);

        Button addButton = view.findViewById(R.id.add_habit);
        addButton.setOnClickListener((v) -> {
            AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
            v = LayoutInflater.from(getContext()).inflate(R.layout.alert_addhabit, null);
            alertDialog.setView(v);

            alertDialog.setTitle("Add a Habit");

            EditText habitNameET = v.findViewById(R.id.enter_habit_name);
            habitNameET.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (habitNameET.getText().toString().length() > 20) {
                        habitNameET.setError("Title is too long");
                        habitNameET.requestFocus();
                    }
                    else if (habitNameET.getText().toString().length() <= 0) {
                        habitNameET.setError("Title is empty");
                        habitNameET.requestFocus();
                    }
                    else {
                        habitNameET.setError(null);
                    }
                }
            });


            EditText habitDescriptionET = v.findViewById(R.id.enter_habit_des);
            habitDescriptionET.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (habitDescriptionET.getText().toString().length() > 30) {
                        habitDescriptionET.setError("Description is too long");
                        habitDescriptionET.requestFocus();
                    }
                    else if (habitDescriptionET.getText().toString().length() <= 0) {
                        habitDescriptionET.setError("Description is empty");
                        habitDescriptionET.requestFocus();
                    }
                    else {
                        habitDescriptionET.setError(null);
                    }
                }
            });
            EditText habitDateET = v.findViewById(R.id.enter_date);
            TextView errorSchedule = v.findViewById(R.id.choose_frequency_txtview);
            ToggleButton monFrequency = v.findViewById(R.id.mon);
            ToggleButton tueFrequency = v.findViewById(R.id.tue);
            ToggleButton wedFrequency = v.findViewById(R.id.wed);
            ToggleButton thuFrequency = v.findViewById(R.id.thu);
            ToggleButton friFrequency = v.findViewById(R.id.fri);
            ToggleButton satFrequency = v.findViewById(R.id.sat);
            ToggleButton sunFrequency = v.findViewById(R.id.sun);

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Empty Listener as the Cancel button doesn't do anything.
                        }
                    });

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });


            alertDialog.show();
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean validated = false;
                    String habitName = habitNameET.getText().toString();

                    String habitDescription = habitDescriptionET.getText().toString();

                    LocalDate startDate = handleDate(habitDateET.getText().toString());
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
                    && schedule.size() > 0) {
                        validated = true;
                    }
                    if (validated) {
                        alertDialog.dismiss();
                        habitArrayList.add(
                                new Habit(habitName, habitDescription, startDate, schedule));
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
                    }
                }
            });
        });

        return view;
    }


    private LocalDate handleDate(String dateAsString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

        //convert String to LocalDate
        LocalDate localDate = LocalDate.parse(dateAsString, formatter);
        return localDate;
    }

    public ArrayList<Habit> getHabitArrayList() {
        return habitArrayList;
    }

    public void addHabit(Habit habit) {

    }
}