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
    private ArrayList<HabitEvent> habitEventArrayList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HabitEventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HabitEventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HabitEventsFragment newInstance(String param1, String param2) {
        HabitEventsFragment fragment = new HabitEventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    /**
     * This is used for setting up the view and creating the fragment
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_habit_events, container, false);
        fab = (FloatingActionButton) view.findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo
                // add a check to make sure habit list is not empty (simple if)
                Intent intent =new Intent(getContext(),CreateHabitEvent.class);
                startActivity(intent);
            }
        });

        habitEventArrayList= ApplicationCE.getHabiteventlist();
        recyclerView=view.findViewById(R.id.Displayed_HabitEvent_list_CE);
        LinearLayoutManager Mmanager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(Mmanager);
        recyclerView.setHasFixedSize(true);
        mAdapter=new CERecycleviewAdapter(habitEventArrayList,getActivity(),this);//Might error getActivity works?
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
        habitEventArrayList= ApplicationCE.getHabiteventlist();
        habitEventArrayList.get(position).setFlag(1);
        Intent intent = new Intent(getActivity(),CreateHabitEvent.class);
        intent.putExtra("flag",habitEventArrayList.get(position).getFlag());
        intent.putExtra("position",position);
        getActivity().startActivity(intent);
    }
}