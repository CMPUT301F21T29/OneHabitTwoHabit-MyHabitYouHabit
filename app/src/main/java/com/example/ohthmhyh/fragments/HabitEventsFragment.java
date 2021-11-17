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

import com.example.ohthmhyh.CERecycleviewAdapter;
import com.example.ohthmhyh.CETouchHelp;
import com.example.ohthmhyh.activities.UpdateHabitEventActivity;
import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.database.HabitEventList;
import com.example.ohthmhyh.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HabitEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HabitEventsFragment extends Fragment implements CERecycleviewAdapter.OntouchListener {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private CERecycleviewAdapter mAdapter;
    private HabitEventList habitEventList;
    private DatabaseAdapter databaseAdapter;

    public HabitEventsFragment() {
        // Required empty public constructor
    }

    /**
     * This is used for setting up the view and creating the fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_habit_events, container, false);

        // pull the habit events from the database
        databaseAdapter = new DatabaseAdapter();
        databaseAdapter.pullHabitEvents(new DatabaseAdapter.HabitEventCallback() {
            @Override
            public void onHabitEventCallback(HabitEventList habitEvents) {
                habitEventList = habitEvents;

                // put the habit events into the recycler view
                recyclerView=view.findViewById(R.id.Displayed_HabitEvent_list_CE);
                LinearLayoutManager Mmanager=new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(Mmanager);
                recyclerView.setHasFixedSize(true);
                mAdapter=new CERecycleviewAdapter(habitEventList,getActivity(),HabitEventsFragment.this);//Might error getActivity works?
                ItemTouchHelper.Callback callback=new CETouchHelp(mAdapter);
                ItemTouchHelper itemTouchHelper=new ItemTouchHelper(callback);
                mAdapter.setTouchhelper(itemTouchHelper);
                itemTouchHelper.attachToRecyclerView(recyclerView);
                recyclerView.setAdapter(mAdapter);

            }
        });

        fab = (FloatingActionButton) view.findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUpdateHabitEventActivity(-1);
            }
        });

        return view;
    }

    /**
     * This is used for editing when called it adds an edit flag
     * @param position the position where it needs to edit
     */
    @Override
    public void onItemclicked(int position) {
        goToUpdateHabitEventActivity(position);
    }

    /**
     * Go to the UpdateHabitEventActivity, providing a HabitEvent index (>= 0) if you want edit/view
     * an existing HabitEvent, or an index < 0 if you want to try and add a HabitEvent.
     * @param position The position of the HabitEvent.
     */
    private void goToUpdateHabitEventActivity(int position) {
        // TODO: Add a check to make sure the HabitList is not empty.
        Intent intent = new Intent(getActivity(), UpdateHabitEventActivity.class);
        if (position >= 0) {
            intent.putExtra(UpdateHabitEventActivity.ARG_HABIT_EVENT_INDEX, position);
        }
        getActivity().startActivity(intent);
    }

}

