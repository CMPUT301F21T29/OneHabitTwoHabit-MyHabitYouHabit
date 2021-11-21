package com.example.ohthmhyh.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ohthmhyh.CustomAdapterHTF;
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

                for (int i = 0; i < hList.size(); i++){
                    Habit h = hList.getHabit(i);
                    if (h.isDueToday()) {
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

        return view;
    }

    /**
     * Called when an item in the RecyclerView is clicked.
     * @param position The position of the item that was clicked.
     */
    @Override
    public void onItemClicked(int position) {
        // We don't actually want to do anything when clicking a habit
    }

}
