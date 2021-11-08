package com.example.ohthmhyh;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ohthmhyh.activities.UpdateHabitEventActivity;
import com.example.ohthmhyh.dialogs.UpdateHabitEventDialog;
import com.example.ohthmhyh.watchers.LengthTextWatcher;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

        recyclerView = view.findViewById(R.id.Displayed_HabitEvent_list_CE);

        // pull the habit events from the database
        databaseAdapter = new DatabaseAdapter();
        // TODO: This can probably be combined with the callback in HabitsFragment.java.
        databaseAdapter.pullHabitEvents(new DatabaseAdapter.HabitEventCallback() {
            @Override
            public void onHabitEventCallback(HabitEventList habitEvents) {
                habitEventList = habitEvents;

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setHasFixedSize(true);
                mAdapter = new CERecycleviewAdapter(habitEventList, getContext(), HabitEventsFragment.this);
                ItemTouchHelper.Callback callback = new CETouchHelp(mAdapter);
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
                mAdapter.setTouchhelper(itemTouchHelper);
                itemTouchHelper.attachToRecyclerView(recyclerView);
                recyclerView.setAdapter(mAdapter);

//                // put the habit events into the recycler view
//                recyclerView=view.findViewById(R.id.Displayed_HabitEvent_list_CE);
//                LinearLayoutManager Mmanager=new LinearLayoutManager(getActivity());
//                recyclerView.setLayoutManager(Mmanager);
//                recyclerView.setHasFixedSize(true);
//                mAdapter=new CERecycleviewAdapter(habitEventList,getActivity(),HabitEventsFragment.this);//Might error getActivity works?
//                ItemTouchHelper.Callback callback=new CETouchHelp(mAdapter);
//                ItemTouchHelper itemTouchHelper=new ItemTouchHelper(callback);
//                mAdapter.setTouchhelper(itemTouchHelper);
//                itemTouchHelper.attachToRecyclerView(recyclerView);
//                recyclerView.setAdapter(mAdapter);

            }
        });


//        fab = (FloatingActionButton) view.findViewById(R.id.floatingActionButton2);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //todo
//                // add a check to make sure habit list is not empty (simple if)
//                Intent intent =new Intent(getContext(),CreateHabitEvent.class);
//                startActivity(intent);
//            }
//        });
        fab = view.findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener((v) -> {
//            updateHabitEventAlertDialog(v, -1);
//            UpdateHabitEventDialog updateHabitEventDialog = new UpdateHabitEventDialog(
//                    getContext(), habitEventList, -1);
//            updateHabitEventDialog.show();
//            UpdateHabitEventDialog updateHabitEventDialog = UpdateHabitEventDialog.newInstance(
//                    habitEventList, -1);
//            updateHabitEventDialog.show(getChildFragmentManager(), "UpdateHabitEventDialog");
            goToUpdateHabitEvent(-1);
        });

        return view;
    }



    // TODO: Put in the AlertDialog class.
    public void updateHabitEventAlertDialog(View v, int chosenHabitEventIndex) {
        AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();

        v = LayoutInflater.from(getContext()).inflate(R.layout.create_habit_event, null);
        alertDialog.setView(v);
        alertDialog.setTitle("Add a Habit Event");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Empty because the cancel button doesn't do anything.
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Done",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                });


        // Set up the drop-down habit list element.
        String[] habitList = {"Habit one", "Habit two", "Habit three"};
        ArrayAdapter habitListArrayAdapter = new ArrayAdapter(v.getContext(), R.layout.create_habit_habit_drop_down_menu, habitList);
        AutoCompleteTextView habitListAutoCompleteTextView = v.findViewById(R.id.AutoCompleteTextviewCE);
        habitListAutoCompleteTextView.setText(habitListArrayAdapter.getItem(0).toString(), false);
        habitListAutoCompleteTextView.setAdapter(habitListArrayAdapter);

        // Obtain permissions for camera and storage.
        String[] cameraPermission = new String[]{
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        String[] storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // Obtain the location provider client.
        FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(v.getContext());

        // Get references to views on the screen.
        TextView locationTextView = v.findViewById(R.id.Add_Location_text);
        EditText commentEditText = v.findViewById(R.id.Get_a_comment_CE);
        commentEditText.addTextChangedListener(new LengthTextWatcher(commentEditText, 0, 20));
        ImageView eventImageView = v.findViewById(R.id.pickImage);
        eventImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(getActivity()).crop().compress(1024).maxResultSize(1080, 1080).start();
            }
        });

        if (chosenHabitEventIndex >= 0) {
            // View/edit an existing habit... Fill in the fields of the view.
            HabitEvent habitEvent = habitEventList.getHabitEvent(chosenHabitEventIndex);

            // Display the image of the habit event.
            habitEvent.getBitmapPic(new HabitEvent.BMPcallback() {
                @Override
                public void onBMPcallback(Bitmap bitmap) {
                    eventImageView.setImageBitmap(bitmap);
                }
            });

            // Display the comment of the habit event.
            commentEditText.setText(habitEvent.getComment());

            // Display the location (if applicable).
            if (habitEvent.getLocation() == null) {
                locationTextView.setText("");
            } else {
                locationTextView.setText(habitEvent.getLocation());
            }

            // TODO: Set the habit that this habit event is associated with.
        }

        alertDialog.show();
    }

    // TODO: Put in the AlertDialog class.
    private void setUserLocation(
            View v, FusedLocationProviderClient locationProviderClient, HabitEvent habitEvent,
            TextView locationTextView) {
        int permission = ActivityCompat.checkSelfPermission(
                v.getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // We have permission to access the user's location.
            locationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        try {
                            Geocoder geocoder = new Geocoder(v.getContext(), Locale.getDefault());
                            List<Address> addressList = geocoder.getFromLocation(
                                    location.getLatitude(), location.getLongitude(), 1);

                            // Set the location TextView and HabitEvent's location to the location
                            // represented as a String.
                            String locationString = addressList.get(0).getAddressLine(0);
                            locationTextView.setText(locationString);
                            habitEvent.setLocation(locationString);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            // We need to request permission to access the user's location.
            ActivityCompat.requestPermissions(
                    getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    // TODO: Put in the AlertDialog class.
    public void validateHabitEvent(View view, HabitEvent habitEvent, EditText commentEditText) {
        String comment = commentEditText.getText().toString();

        if (comment.length() > 20) {
            // Invalid input. Simply return.
            return;
        }

        if (habitEvent == null) {
            // We are adding a new habit.
            // TODO: Create a new HabitEvent with attributes.
        } else {
            // We are editing an existing habit.
            // TODO: Set the new attributes of the HabitEvent.
        }
    }


    public void goToUpdateHabitEvent(int position) {
        Intent intent = new Intent(getActivity(), UpdateHabitEventActivity.class);
        intent.putExtra(UpdateHabitEventActivity.HABIT_EVENT_LIST_ARG, habitEventList);
        intent.putExtra(UpdateHabitEventActivity.CHOSEN_HABIT_EVENT_INDEX_ARG, position);
        getActivity().startActivity(intent);
    }

    /**
     * This is used for editing when called it adds an edit flag
     * @param position the position where it needs to edit
     */
    @Override
    public void onItemclicked(int position) {
        habitEventList.getHabitEvent(position).setFlag(1);
        Intent intent = new Intent(getActivity(),CreateHabitEvent.class);
        intent.putExtra("flag", habitEventList.getHabitEvent(position).getFlag());
        intent.putExtra("position",position);
        getActivity().startActivity(intent);
    }

}

