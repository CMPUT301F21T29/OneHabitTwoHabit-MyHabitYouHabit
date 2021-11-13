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
import android.widget.Toast;

import com.example.ohthmhyh.CERecycleviewAdapter;
import com.example.ohthmhyh.CETouchHelp;
import com.example.ohthmhyh.activities.UpdateHabitEventActivity;
import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.database.HabitEventList;
import com.example.ohthmhyh.R;
import com.example.ohthmhyh.activities.CreateHabitEvent;
import com.example.ohthmhyh.database.HabitList;
import com.example.ohthmhyh.entities.Habit;
import com.example.ohthmhyh.entities.HabitEvent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HabitEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HabitEventsFragment extends Fragment implements CERecycleviewAdapter.OntouchListener {

    public static final String ARG_RETURNED_HABIT_EVENT = "returned_habit_event_arg";

    private int chosenHabitEventIndex = -1;  // The index of the chosen HabitEvent from the list.
    private ActivityResultLauncher<Intent> resultLauncher;
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

        fab = view.findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUpdateHabitEventActivity(-1);
            }
        });

        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // Called when the UpdateHabitEventActivity returns. If it returns with a
                        // RESULT_OK, then we can add/edit a HabitEvent in the HabitEventList.
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            HabitEvent habitEvent =
                                    (HabitEvent) result.getData().getSerializableExtra(
                                            ARG_RETURNED_HABIT_EVENT);
                            if (chosenHabitEventIndex < 0) {
                                habitEventList.addHabitEvent(habitEvent);
                            } else {
                                habitEventList.replaceHabitEvent(chosenHabitEventIndex, habitEvent);
                            }
                            mAdapter.notifyDataSetChanged();
                        }
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
     * Go to the UpdateHabitEventActivity, passing in the HabitEvent's index in the HabitEventList,
     * or -1 if you are referencing a Habit that does not currently exist in the HabitEventList
     * (e.g. when adding a HabitEvent).
     * @param habitEventIndex The index of the HabitEvent in the HabitEventList, -1 for no
     *                        HabitEvent.
     */
    private void goToUpdateHabitEventActivity(int habitEventIndex) {
        chosenHabitEventIndex = habitEventIndex;

        databaseAdapter.pullHabits(new DatabaseAdapter.HabitCallback() {
            @Override
            public void onHabitCallback(HabitList hList) {
                // If there are no Habits, don't try and add a HabitEvent.
                if (hList.size() == 0) {
                    Toast noHabitsToast = Toast.makeText(
                            getContext(), "You have no habits!", Toast.LENGTH_SHORT);
                    noHabitsToast.show();
                    return;
                }

                // Otherwise, go the UpdateHabitEventActivity passing the HabitEvent (if
                // applicable), the HabitEvent's UPID (or the next available UPID if we are creating
                // a HabitEvent), and the ArrayList of Habits.
                Intent intent = new Intent(getActivity(), UpdateHabitEventActivity.class);
                int UPID;

                if (habitEventIndex >= 0) {
                    // We are editing/viewing a HabitEvent.
                    HabitEvent habitEvent = habitEventList.getHabitEvent(habitEventIndex);
                    UPID = habitEvent.getUPID();
                    intent.putExtra(UpdateHabitEventActivity.ARG_HABIT_EVENT, habitEvent);
                } else {
                    // We are adding a HabitEvent.
                    UPID = habitEventList.nextUPID();
                }

                intent.putExtra(UpdateHabitEventActivity.ARG_HABIT_EVENT_UPID, UPID);
                intent.putExtra(
                        UpdateHabitEventActivity.ARG_HABIT_ARRAYLIST, hList.getHabitList());

                resultLauncher.launch(intent);
            }
        });
    }
}

