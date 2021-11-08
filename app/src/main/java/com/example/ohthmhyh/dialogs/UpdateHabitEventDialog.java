package com.example.ohthmhyh.dialogs;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.example.ohthmhyh.HabitEvent;
import com.example.ohthmhyh.HabitEventList;
import com.example.ohthmhyh.R;
import com.example.ohthmhyh.watchers.LengthTextWatcher;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class UpdateHabitEventDialog extends DialogFragment {

    private static final String HABIT_EVENT_LIST_ARG = "habit_event_list";
    private static final String CHOSEN_HABIT_EVENT_INDEX_ARG = "chosen_habit_event_index";

    private HabitEventList habitEventList;
    private HabitEvent habitEvent;
    private int chosenHabitEventIndex;

    private TextView locationTextView;
    private Button locationButton;
    private EditText commentEditText;
    private ImageView eventImageView;

    public UpdateHabitEventDialog() {
        // Empty constructor for the fragment.
    }

    public static UpdateHabitEventDialog newInstance(
            HabitEventList habitEventList, int chosenHabitEventIndex) {
        // Create the instance.
        UpdateHabitEventDialog updateHabitEventDialog = new UpdateHabitEventDialog();

        // Set the arguments.
        Bundle arguments = new Bundle();
        arguments.putSerializable(HABIT_EVENT_LIST_ARG, habitEventList);
        arguments.putInt(CHOSEN_HABIT_EVENT_INDEX_ARG, chosenHabitEventIndex);
        updateHabitEventDialog.setArguments(arguments);

        return updateHabitEventDialog;
    }

//    public UpdateHabitEventDialog(
//            @NonNull Context context, HabitEventList habitEventList, int chosenHabitEventIndex) {
//        super(context);
//        this.context = context;
//        this.habitEventList = habitEventList;
//        this.chosenHabitEventIndex = chosenHabitEventIndex;
//
//        if (this.chosenHabitEventIndex >= 0) {
//            this.habitEvent = this.habitEventList.getHabitEvent(this.chosenHabitEventIndex);
//        } else {
//            this.habitEvent = new HabitEvent();
//        }
//    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get the HabitEventList and the index of the chosen HabitEvent.
        habitEventList = (HabitEventList) getArguments().getSerializable(HABIT_EVENT_LIST_ARG);
        chosenHabitEventIndex = getArguments().getInt(CHOSEN_HABIT_EVENT_INDEX_ARG);

        // Set the HabitEvent to be an initial HabitEvent if we are adding it, otherwise, get the
        // chosen HabitEvent.
        if (this.chosenHabitEventIndex >= 0) {
            this.habitEvent = this.habitEventList.getHabitEvent(this.chosenHabitEventIndex);
        } else {
            this.habitEvent = new HabitEvent();
        }

        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View dialogView = layoutInflater.inflate(R.layout.create_habit_event, null);

        dialog.setView(dialogView);
        dialog.setTitle("Add a Habit Event");
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (dialogInterface, i) -> {});
        dialog.setButton(
                AlertDialog.BUTTON_POSITIVE, "Done", (dialogInterface, i) -> dialog.dismiss());

        // Set up the drop-down habit list element.
        String[] habitList = {"Habit one", "Habit two", "Habit three"};
        ArrayAdapter habitListArrayAdapter = new ArrayAdapter(
                getContext(), R.layout.create_habit_habit_drop_down_menu, habitList);
        AutoCompleteTextView habitListAutoCompleteTextView = dialogView.findViewById(
                R.id.AutoCompleteTextviewCE);
        habitListAutoCompleteTextView.setText(
                habitListArrayAdapter.getItem(0).toString(), false);
        habitListAutoCompleteTextView.setAdapter(habitListArrayAdapter);

        // Get references to views on the screen.
        locationTextView = dialogView.findViewById(R.id.Add_Location_text);
        locationButton = dialogView.findViewById(R.id.Add_location_button);
        commentEditText = dialogView.findViewById(R.id.Get_a_comment_CE);
        eventImageView = dialogView.findViewById(R.id.pickImage);

        // Add extra functionality to these views.
        locationButton.setOnClickListener(view -> setHabitEventLocation());
        commentEditText.addTextChangedListener(new LengthTextWatcher(commentEditText, 0, 20));
        eventImageView.setOnClickListener(
                view -> ImagePicker.with(getActivity()).crop().compress(1024).maxResultSize(1080, 1080).start());

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

        return dialog;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.create_habit_event);
//        setTitle("Add a Habit Event");
//
//        // Set up the drop-down habit list element.
//        String[] habitList = {"Habit one", "Habit two", "Habit three"};
//        ArrayAdapter habitListArrayAdapter = new ArrayAdapter(
//                context, R.layout.create_habit_habit_drop_down_menu, habitList);
//        AutoCompleteTextView habitListAutoCompleteTextView = findViewById(
//                R.id.AutoCompleteTextviewCE);
//        habitListAutoCompleteTextView.setText(
//                habitListArrayAdapter.getItem(0).toString(), false);
//        habitListAutoCompleteTextView.setAdapter(habitListArrayAdapter);
//
////        // Obtain permissions for camera and storage.
////        String[] cameraPermission = new String[]{
////                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
////        String[] storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
////
////        // Obtain the location provider client.
////        FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(context);
//
//        // Get references to views on the screen.
//        TextView locationTextView = findViewById(R.id.Add_Location_text);
//        Button locationButton = findViewById(R.id.Add_location_button);
//        EditText commentEditText = findViewById(R.id.Get_a_comment_CE);
//        ImageView eventImageView = findViewById(R.id.pickImage);
//
//        // Add extra functionality to these views.
//        locationButton.setOnClickListener(
//                view -> setHabitEventLocation(habitEvent, locationTextView));
//        commentEditText.addTextChangedListener(new LengthTextWatcher(commentEditText, 0, 20));
//        eventImageView.setOnClickListener(
//                view -> ImagePicker.with(getOwnerActivity()).crop().compress(1024).maxResultSize(1080, 1080).start());
//
//        if (chosenHabitEventIndex >= 0) {
//            // View/edit an existing habit... Fill in the fields of the view.
//            HabitEvent habitEvent = habitEventList.getHabitEvent(chosenHabitEventIndex);
//
//            // Display the image of the habit event.
//            habitEvent.getBitmapPic(new HabitEvent.BMPcallback() {
//                @Override
//                public void onBMPcallback(Bitmap bitmap) {
//                    eventImageView.setImageBitmap(bitmap);
//                }
//            });
//
//            // Display the comment of the habit event.
//            commentEditText.setText(habitEvent.getComment());
//
//            // Display the location (if applicable).
//            if (habitEvent.getLocation() == null) {
//                locationTextView.setText("");
//            } else {
//                locationTextView.setText(habitEvent.getLocation());
//            }
//
//            // TODO: Set the habit that this habit event is associated with.
//        }
//    }

    // TODO: Probably doesn't have to take in the HabitEvent since it's a private member.
    private void setHabitEventLocation() {
        int permission = ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // We have permission to access the user's location.
            FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
            locationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        try {
                            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
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

    private void setHabitEventImage(HabitEvent habitEvent, ImageView habitEventImageView) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("asdf", "qwer");
        if (resultCode == Activity.RESULT_OK) {
            Toast.makeText(getContext(), "asdf", Toast.LENGTH_SHORT).show();
            if (data == null){
                Toast.makeText(getContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri uri=data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                eventImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }



}
