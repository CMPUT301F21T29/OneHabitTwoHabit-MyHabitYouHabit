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
import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.database.HabitEventList;
import com.example.ohthmhyh.R;
import com.example.ohthmhyh.activities.CreateHabitEvent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HabitEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HabitEventsFragment extends Fragment implements CERecycleviewAdapter.OntouchListener {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    CERecycleviewAdapter mAdapter;
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
                //todo
                // add a check to make sure habit list is not empty (simple if)
                Intent intent =new Intent(getContext(), CreateHabitEvent.class);
                startActivity(intent);
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
        Intent intent = new Intent(getActivity(),CreateHabitEvent.class);
        intent.putExtra("flag", 1);
        intent.putExtra("position",position);
        getActivity().startActivity(intent);
    }

}

