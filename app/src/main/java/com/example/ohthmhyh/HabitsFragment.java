package com.example.ohthmhyh;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

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
    int year, month, day;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView habitDateET;
    private ArrayList<Habit> habitArrayList = new ArrayList<>();
    private CustomAdapterHF adapter;

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

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_hf);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new CustomAdapterHF(getContext(), this, habitArrayList);
        ItemTouchHelper.Callback callback = new TouchingHandlingHF(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        adapter.setTouchhelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);


        Button addButton = view.findViewById(R.id.add_habit);
        addButton.setOnClickListener((v) -> {
            addDialog(v);
        });

        return view;
    }


    private LocalDate stringToDate(String dateAsString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

        //convert String to LocalDate
        LocalDate localDate = LocalDate.parse(dateAsString, formatter);
        return localDate;
    }
    private String dateToString(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        String formattedString = localDate.format(formatter);
        return formattedString;
    }

    public ArrayList<Habit> getHabitArrayList() {
        return habitArrayList;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        this.year=i;
        this.month=i1+1;
        this.day=i2;
        habitDateET.setText(day + "/" + month + "/" + year);
    }

    @Override
    public void onItemClicked(int position) {
        editDialog(getView(), position);
    }

    public void addDialog(View v) {

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
        habitDateET = v.findViewById(R.id.enter_date);
        habitDateET.setHint("Enter a date");
        habitDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        HabitsFragment.this,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();
                System.out.println(year);

            }
        });



        TextView errorSchedule = v.findViewById(R.id.choose_frequency_txtview);
        ToggleButton monFrequency = v.findViewById(R.id.mon);
        ToggleButton tueFrequency = v.findViewById(R.id.tue);
        ToggleButton wedFrequency = v.findViewById(R.id.wed);
        ToggleButton thuFrequency = v.findViewById(R.id.thu);
        ToggleButton friFrequency = v.findViewById(R.id.fri);
        ToggleButton satFrequency = v.findViewById(R.id.sat);
        ToggleButton sunFrequency = v.findViewById(R.id.sun);

        ToggleButton private_button = v.findViewById(R.id.private_button);
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
                    habitArrayList.add(
                            new Habit(habitName, habitDescription, startDate, schedule, private_button.isChecked()));
                    //System.out.println(habitArrayList.size() + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
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
        });
    }

    public void editDialog(View v, int position) {
        Habit chosenHabit = habitArrayList.get(position);
        AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
        v = LayoutInflater.from(getContext()).inflate(R.layout.alert_addhabit, null);
        alertDialog.setView(v);

        alertDialog.setTitle("Add a Habit");

        EditText habitNameET = v.findViewById(R.id.enter_habit_name);
        habitNameET.setText(chosenHabit.getName());
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
        habitDescriptionET.setText(chosenHabit.getDescription());
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
        habitDateET = v.findViewById(R.id.enter_date);
        habitDateET.setText(dateToString(chosenHabit.StartDateAsLocalDate()));
        habitDateET.setHint("Enter a date");
        habitDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        HabitsFragment.this,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();
            }
        });



        TextView errorSchedule = v.findViewById(R.id.choose_frequency_txtview);
        ToggleButton monFrequency = v.findViewById(R.id.mon);
        ToggleButton tueFrequency = v.findViewById(R.id.tue);
        ToggleButton wedFrequency = v.findViewById(R.id.wed);
        ToggleButton thuFrequency = v.findViewById(R.id.thu);
        ToggleButton friFrequency = v.findViewById(R.id.fri);
        ToggleButton satFrequency = v.findViewById(R.id.sat);
        ToggleButton sunFrequency = v.findViewById(R.id.sun);

        ArrayList<Habit.Days> existedSchedule = chosenHabit.getSchedule();
        if (existedSchedule.contains(Habit.Days.Mon)) monFrequency.setChecked(true);
        if (existedSchedule.contains(Habit.Days.Tue)) tueFrequency.setChecked(true);
        if (existedSchedule.contains(Habit.Days.Wed)) wedFrequency.setChecked(true);
        if (existedSchedule.contains(Habit.Days.Thu)) thuFrequency.setChecked(true);
        if (existedSchedule.contains(Habit.Days.Fri)) friFrequency.setChecked(true);
        if (existedSchedule.contains(Habit.Days.Sat)) satFrequency.setChecked(true);
        if (existedSchedule.contains(Habit.Days.Sun)) sunFrequency.setChecked(true);


        ToggleButton private_button = v.findViewById(R.id.private_button);
        private_button.setChecked(chosenHabit.getIsPrivate());
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

                    chosenHabit.setName(habitName);
                    chosenHabit.setDescription(habitDescription);
                    chosenHabit.setStartDate(startDate.toEpochDay());
                    chosenHabit.setSchedule(schedule);
                    chosenHabit.setIsPrivate(private_button.isChecked());
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
        });
    }
}