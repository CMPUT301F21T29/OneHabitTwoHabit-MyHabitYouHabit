package com.example.ohthmhyh;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HabitEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HabitEventsFragment extends Fragment implements CERecycleviewAdapter.OntouchListener {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    CERecycleviewAdapter mAdapter;
    private HabitEventList habitEvents;

    public HabitEventsFragment() {
        // Required empty public constructor
    }

    /**
     * This is used for setting up the view and creating the fragment
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //TODO: pull the habit event list from the DB
        habitEvents = new HabitEventList();


        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_habit_events, container, false);
        fab = (FloatingActionButton) view.findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                //todo
//                // add a check to make sure habit list is not empty (simple if)
//                Intent intent =new Intent(getContext(),CreateHabitEvent.class);
//                startActivity(intent);
            }
        });

        recyclerView=view.findViewById(R.id.Displayed_HabitEvent_list_CE);
        LinearLayoutManager Mmanager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(Mmanager);
        recyclerView.setHasFixedSize(true);
        mAdapter=new CERecycleviewAdapter(habitEvents.getHabitEventList(),getActivity(),this);//Might error getActivity works?
        ItemTouchHelper.Callback callback=new CETouchHelp(mAdapter);
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(callback);
        mAdapter.setTouchhelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(mAdapter);
        return view;
    }



    /**
     * This is used for editing when called it adds an edit flag
     * @param position the position hwere it needs to edit
     */
    @Override
    public void onItemclicked(int position) {
        habitEvents.getHabitEvent(position).setFlag(1);
        Intent intent = new Intent(getActivity(),CreateHabitEvent.class);
//        intent.putExtra("flag", habitEvents.getHabitEvent(position).getFlag());
//        intent.putExtra("position",position);
//        getActivity().startActivity(intent);
    }
}