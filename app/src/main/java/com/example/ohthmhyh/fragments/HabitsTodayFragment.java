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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.ohthmhyh.CustomAdapterHTF;
import com.example.ohthmhyh.activities.UpdateHabitActivity;
import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.entities.Habit;
import com.example.ohthmhyh.database.HabitList;
import com.example.ohthmhyh.R;
import com.example.ohthmhyh.TouchingHandlingHTF;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HabitsTodayFragment extends Fragment implements CustomAdapterHTF.OntouchListener {

    
    public static final String ARG_RETURNED_HABIT = "returned_habit_arg";

    private int chosenHabitIndex = -1;
    private ActivityResultLauncher<Intent> resultLauncher;
    private HabitList habitList;
    private CustomAdapterHTF adapter;
    private DatabaseAdapter databaseAdapter;

    public HabitsTodayFragment(){/* Required empty public constructor*/}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * this creates the fragment
     * this also sets the recyclerView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Create a new alert dialog when Add a Habit button is clicked

        View view = inflater.inflate(R.layout.fragment_habits_today, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_htf);

        // Get the HabitList from the database.
        databaseAdapter = new DatabaseAdapter();
        databaseAdapter.pullHabits(new DatabaseAdapter.HabitCallback() {
            @Override
            public void onHabitCallback(HabitList hList) {
                habitList = hList;

                //make smaller list of habits to put into the recycler view
                ArrayList<Habit> newList = new ArrayList<>();

                LocalDate today = LocalDate.now();

                int DOWjav = LocalDate.now().getDayOfWeek().getValue(); //note that mon is 1 in this convention, sun is 7
                int DOW = DOWjav % 7; //this is the convention our code uses, where sun is 0, and sat is 6

                for (int i=0; i<hList.size(); i++){
                    Habit h = hList.getHabit(i);


                    if ((today.isAfter(h.StartDateAsLocalDate().minusDays(1))) && (h.getSchedule().contains(Habit.Days.values()[DOW]))) {
                        newList.add(h);
                    }
                }



                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setHasFixedSize(true);




                adapter = new CustomAdapterHTF(getContext(), HabitsTodayFragment.this, newList, habitList);
                ItemTouchHelper.Callback callback = new TouchingHandlingHTF(adapter);
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
                adapter.setTouchhelper(itemTouchHelper);
                itemTouchHelper.attachToRecyclerView(recyclerView);
                recyclerView.setAdapter(adapter);
            }
        });



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
                                habitList.addHabit(habit);
                            } else {
                                habitList.replaceHabit(chosenHabitIndex, habit);
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
        // goToUpdateHabitActivity(position); //we don't actually want to do anything when clicking a habit
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
            intent.putExtra(UpdateHabitActivity.ARG_HABIT, habitList.getHabit(habitIndex));
        }
        resultLauncher.launch(intent);
    }

}
