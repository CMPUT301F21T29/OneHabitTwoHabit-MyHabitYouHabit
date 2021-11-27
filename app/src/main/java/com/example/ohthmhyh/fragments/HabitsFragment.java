package com.example.ohthmhyh.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ohthmhyh.activities.UpdateHabitActivity;
import com.example.ohthmhyh.adapters.HabitRecyclerViewGestureAdapter;
import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.entities.Habit;
import com.example.ohthmhyh.R;
import com.example.ohthmhyh.helpers.TransportableTouchHelper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HabitsFragment extends Fragment implements HabitRecyclerViewGestureAdapter.OnTouchListener {

    public static final String ARG_RETURNED_HABIT = "returned_habit_arg";

    private int chosenHabitIndex = -1;
    private ActivityResultLauncher<Intent> resultLauncher;
    private ArrayList<Habit> listOfHabits;
    private HabitRecyclerViewGestureAdapter adapter;
    private DatabaseAdapter databaseAdapter;

    public HabitsFragment(){
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_habits, null);
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

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_hf);

        listOfHabits = new ArrayList<>();
        databaseAdapter = DatabaseAdapter.getInstance();

        for (int i = 0; i < databaseAdapter.numberOfHabits(); i++) {
            listOfHabits.add(databaseAdapter.habitAtIndex(i));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        adapter = new HabitRecyclerViewGestureAdapter(
                getContext(), listOfHabits, HabitsFragment.this);
        ItemTouchHelper.Callback callback = new TransportableTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        adapter.setTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);

        Button addButton = view.findViewById(R.id.add_habit);
        addButton.setOnClickListener(v -> goToUpdateHabitActivity(-1));

        // Gets the result of the UpdateHabitActivity (the modified/new Habit).
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // Called when the UpdateHabitActivity returns. If it returns with a
                        // RESULT_OK, then we can add/edit a habit in the habitList.
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Habit habit = (Habit) result.getData().getSerializableExtra(
                                    ARG_RETURNED_HABIT);
                            if (chosenHabitIndex < 0) {
                                // Add the Habit to the ArrayList of Habits.
                                listOfHabits.add(habit);

                                // Also add this Habit to the database.
                                databaseAdapter.addHabit(habit);
                            } else {
                                // Replace this Habit in the ArrayList of Habits.
                                listOfHabits.set(chosenHabitIndex, habit);

                                // Also replace this Habit in the database.
                                databaseAdapter.replaceHabit(chosenHabitIndex, habit);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

        return view;
    }

    /**
     * Called when an item in the RecyclerView is clicked.
     * @param position the position we need to edit
     */
    @Override
    public void onItemClicked(int position) {
        goToUpdateHabitActivity(position);
    }

    /**
     * Go to the UpdateHabitActivity, passing in the Habit's index in the HabitList, or -1 if you
     * are referencing a Habit that does not currently exist in the HabitList (e.g. when adding a
     * Habit).
     * @param habitIndex The index of the Habit in the HabitList, -1 for no Habit.
     */
    private void goToUpdateHabitActivity(int habitIndex) {
        chosenHabitIndex = habitIndex;
        Intent intent = new Intent(getActivity(), UpdateHabitActivity.class);
        if (habitIndex >= 0) {
            // Pass the Habit to the UpdateHabitActivity.
            intent.putExtra(UpdateHabitActivity.ARG_HABIT, listOfHabits.get(habitIndex));
        }
        resultLauncher.launch(intent);
    }

}
