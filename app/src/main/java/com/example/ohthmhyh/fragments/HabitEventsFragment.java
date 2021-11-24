package com.example.ohthmhyh.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.ohthmhyh.activities.MainActivity;
import com.example.ohthmhyh.adapters.HabitEventRecyclerViewAdapter;
import com.example.ohthmhyh.activities.UpdateHabitEventActivity;
import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.database.HabitEventList;
import com.example.ohthmhyh.R;
import com.example.ohthmhyh.database.HabitList;
import com.example.ohthmhyh.helpers.TransportableTouchHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HabitEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HabitEventsFragment extends Fragment implements HabitEventRecyclerViewAdapter.OntouchListener {

    private Button addHabitEventButton;
    private RecyclerView recyclerView;
    private HabitEventRecyclerViewAdapter mAdapter;
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
        databaseAdapter = DatabaseAdapter.getInstance();
        databaseAdapter.pullHabitEvents(new DatabaseAdapter.HabitEventCallback() {
            @Override
            public void onHabitEventCallback(HabitEventList habitEvents) {
                habitEventList = habitEvents;

                // put the habit events into the recycler view
                recyclerView=view.findViewById(R.id.Displayed_HabitEvent_list_CE);
                LinearLayoutManager Mmanager=new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(Mmanager);
                recyclerView.setHasFixedSize(true);
                mAdapter=new HabitEventRecyclerViewAdapter(habitEventList,getActivity(),HabitEventsFragment.this);//Might error getActivity works?
                ItemTouchHelper.Callback callback=new TransportableTouchHelper(mAdapter);
                ItemTouchHelper itemTouchHelper=new ItemTouchHelper(callback);
                mAdapter.setTouchhelper(itemTouchHelper);
                itemTouchHelper.attachToRecyclerView(recyclerView);
                recyclerView.setAdapter(mAdapter);

            }
        });

        addHabitEventButton = view.findViewById(R.id.add_habit_event);
        addHabitEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseAdapter.pullHabits(new DatabaseAdapter.HabitCallback() {
                    @Override
                    public void onHabitCallback(HabitList hList) {

                        HabitList validHabits = hList.ValidHabitForDay();
                        if (validHabits.size()==0){
                            gotoMainActivity();
                        }
                        else { goToUpdateHabitEventActivity(-1);}
                    }
                });

            }
        });//end of on click

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
        Intent intent = new Intent(getActivity(), UpdateHabitEventActivity.class);
        if (position >= 0) {
            intent.putExtra(UpdateHabitEventActivity.ARG_HABIT_EVENT_INDEX, position);
        }
        getActivity().startActivity(intent);
    }
    /**
     * Call this method to goto main Activity while displaying a dialog pop up
     */
    private void gotoMainActivity(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder.setMessage("You have No valid Habits to turn into Habit Events!");
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    //If user wants to delete and has confirmed run this code with deletes the habit
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        getActivity().startActivity(intent);
                    }
                });
        //if the user clciks outside the box run this code (same as saying no)
        alertDialogBuilder.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        getActivity().startActivity(intent);
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}

