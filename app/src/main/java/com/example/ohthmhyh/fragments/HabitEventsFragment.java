package com.example.ohthmhyh.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ohthmhyh.adapters.HabitEventRecyclerViewAdapter;
import com.example.ohthmhyh.activities.UpdateHabitEventActivity;
import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.R;
import com.example.ohthmhyh.entities.HabitEvent;
import com.example.ohthmhyh.helpers.TransportableTouchHelper;

import java.util.ArrayList;

/**
 * The Fragment that shows the user's HabitEvents.. This Fragment's parent Activity is MainActivity
 * and is created when the user clicks on the bottom navigation bar's habit event button.
 *
 * There are no outstanding issues that we are aware of.
 */
public class HabitEventsFragment extends Fragment implements HabitEventRecyclerViewAdapter.OnTouchListener {

    private RecyclerView recyclerView;
    private HabitEventRecyclerViewAdapter mAdapter;
    private DatabaseAdapter databaseAdapter = DatabaseAdapter.getInstance();

    public HabitEventsFragment() {
        // Required empty public constructor
    }

    /**
     * This is used for setting up the view and creating the fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_habit_events, container, false);

        ArrayList<HabitEvent> habitEvents = new ArrayList<>();
        for (int i = 0; i < databaseAdapter.numberOfHabitEvents(); i++) {
            habitEvents.add(databaseAdapter.habitEventAtIndex(i));
        }

        recyclerView = view.findViewById(R.id.Displayed_HabitEvent_list_CE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new HabitEventRecyclerViewAdapter(
                habitEvents, getActivity(), HabitEventsFragment.this);
        ItemTouchHelper.Callback callback = new TransportableTouchHelper(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        mAdapter.setTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    /**
     * This is used for editing when called it adds an edit flag
     * @param position the position where it needs to edit
     */
    @Override
    public void onItemClicked(int position) {
        goToUpdateHabitEventActivity(position);
    }

    /**
     * Go to the UpdateHabitEventActivity, providing a HabitEvent index (>= 0) if you want edit/view
     * an existing HabitEvent, or an index < 0 if you want to try and add a HabitEvent.
     * @param position The position of the HabitEvent.
     */
    private void goToUpdateHabitEventActivity(int position) {
        Intent intent = new Intent(getActivity(), UpdateHabitEventActivity.class);
        if (position >= 0) {
            intent.putExtra(UpdateHabitEventActivity.ARG_HABIT_EVENT_INDEX, position);
        }
        getActivity().startActivity(intent);
    }

}

