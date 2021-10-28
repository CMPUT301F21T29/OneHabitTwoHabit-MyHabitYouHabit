package com.example.ohthmhyh;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HabitEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HabitEventsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayList<HabitEvent> habitEventArrayList;
        TestClassStuart testClassStuart = (TestClassStuart) getActivity().getApplication();
        habitEventArrayList= TestClassStuart.getHabiteventlist();
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_habit_events, container, false);
        recyclerView=view.findViewById(R.id.Displayed_HabitEvent_list_CE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter=new CERecycleviewAdapter(habitEventArrayList,getActivity());//Might error getActivity works?
        recyclerView.setAdapter(mAdapter);
        return view;
    }
}