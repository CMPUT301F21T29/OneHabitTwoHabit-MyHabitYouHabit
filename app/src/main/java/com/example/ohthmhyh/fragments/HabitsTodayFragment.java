package com.example.ohthmhyh.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ohthmhyh.adapters.HabitTodayRecyclerViewAdapter;
import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.entities.Habit;
import com.example.ohthmhyh.R;

import java.util.ArrayList;

/**
 * The Fragment that shows the user's Habits that are supposed to be done today. This Fragment's
 * parent Activity is MainActivity and is created when the user clicks on the bottom navigation
 * bar's habits today button.
 *
 * There are no outstanding issues that we are aware of.
 */
public class HabitsTodayFragment extends Fragment {

    private HabitTodayRecyclerViewAdapter adapter;
    private DatabaseAdapter databaseAdapter = DatabaseAdapter.getInstance();

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

        // Inflate the layout and get views for this fragment
        View view = inflater.inflate(R.layout.fragment_habits_today, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_htf);

        ArrayList<Habit> habitsToday = new ArrayList<>();
        for (int i = 0; i < databaseAdapter.numberOfHabits(); i++) {
            Habit habit = databaseAdapter.habitAtIndex(i);
            if (habit.isDueToday()) {
                habitsToday.add(habit);
            }

            adapter = new HabitTodayRecyclerViewAdapter(getContext(), habitsToday);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setHasFixedSize(true);
        }

        return view;
    }

}
