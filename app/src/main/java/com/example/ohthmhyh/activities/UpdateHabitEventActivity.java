package com.example.ohthmhyh.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.example.ohthmhyh.R;
import com.example.ohthmhyh.entities.Habit;
import com.example.ohthmhyh.entities.HabitEvent;
import com.example.ohthmhyh.listeners.HabitEventUpdateListener;
import com.example.ohthmhyh.watchers.HabitEventCommentTextWatcher;
import com.example.ohthmhyh.watchers.LengthTextWatcher;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UpdateHabitEventActivity extends AppCompatActivity {

    public static final String ARG_HABIT_EVENT = "habit_event_arg";
    public static final String ARG_HABIT_EVENT_UPID = "habit_event_upid_arg";
    public static final String ARG_HABIT_ARRAYLIST = "habit_arraylist_arg";

    private int UPID;
    private ArrayList<Habit> habitArrayList;
    private AutoCompleteTextView habitListAutoCompleteTextView;
    private TextView locationTextView;
    private Button locationButton;
    private Button doneButton;
    private EditText commentEditText;
    private ImageView eventImageView;
    private HabitEvent habitEvent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_habit_event);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        UPID = intent.getIntExtra(ARG_HABIT_EVENT_UPID, 1);
        habitArrayList = (ArrayList<Habit>) intent.getSerializableExtra(ARG_HABIT_ARRAYLIST);

        // Create the array of Habit names.
        String[] habitNameArray = new String[habitArrayList.size()];
        for (int i = 0; i < habitNameArray.length; i++) {
            habitNameArray[i] = habitArrayList.get(i).getName();
        }

        // Get references to views on the screen.
        habitListAutoCompleteTextView = findViewById(R.id.AutoCompleteTextviewCE);
        locationTextView = findViewById(R.id.Add_Location_text);
        locationButton = findViewById(R.id.Add_location_button);
        doneButton = findViewById(R.id.button2);
        commentEditText = findViewById(R.id.Get_a_comment_CE);
        eventImageView = findViewById(R.id.pickImage);

        if (intent.hasExtra(ARG_HABIT_EVENT)) {
            // A HabitEvent was passed. Set the views to display this HabitEvent.
            habitEvent = (HabitEvent) intent.getSerializableExtra(ARG_HABIT_EVENT);
            showHabitEvent(habitEvent);
        } else {
            // No HabitEvent was passed, create an empty HabitEvent to use and set its UPID.
            habitEvent = new HabitEvent();
            habitEvent.setUPID(UPID);
        }

        // TODO: Replace with actual Habits.
        // Set up the drop-down habit list element.
//        String[] habitList = {"Habit one", "Habit two", "Habit three"};
//        ArrayAdapter habitListArrayAdapter = new ArrayAdapter(
//                this, R.layout.create_habit_habit_drop_down_menu, habitList);
        ArrayAdapter habitListArrayAdapter = new ArrayAdapter(
                this, R.layout.create_habit_habit_drop_down_menu, habitNameArray);
        habitListAutoCompleteTextView.setText(
                habitListArrayAdapter.getItem(0).toString(), false);
        habitListAutoCompleteTextView.setListSelection(0);
        habitListAutoCompleteTextView.setAdapter(habitListArrayAdapter);
        habitListAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                habitEvent.setHabitUHID(habitArrayList.get(i).getUHID());
            }
        });

        // TODO: Make some (or all) of these literals into constants.
        // Add extra functionality to these views.
        locationButton.setOnClickListener(view -> setHabitEventLocation());
        doneButton.setOnClickListener(new HabitEventUpdateListener(this, habitEvent));
        commentEditText.addTextChangedListener(
                new HabitEventCommentTextWatcher(commentEditText, 0, 20, habitEvent));
        eventImageView.setOnClickListener(
                view -> ImagePicker.with(this)
                        .crop()
                        .compress(1024).maxResultSize(1080, 1080)
                        .start()
        );
    }

    /**
     * Handle button presses in the top bar.
     * @param item The MenuItem that was selected.
     * @return Whether the action was successful or not.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:  // The back arrow is pressed.
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Ensures that when the back arrow is pressed, that we return to the calling Fragment and not
     * just the default Fragment.
     */
    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Receives an image from the camera or gallery.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "asdf", Toast.LENGTH_SHORT).show();
            if (data == null){
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri uri=data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                habitEvent.setBitmapPic(bitmap);
                eventImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Gets the current location of the user and sets the HabitEvent's latitude and longitude.
     */
    private void setHabitEventLocation() {
        int permission = ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // We have permission to access the user's location.
            FusedLocationProviderClient locationProviderClient =
                    LocationServices.getFusedLocationProviderClient(this);
            locationProviderClient.getLastLocation().addOnCompleteListener(
                    new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location selectedLocation = task.getResult();
                    if (selectedLocation != null) {
                        try {
                            Geocoder geocoder = new Geocoder(
                                    UpdateHabitEventActivity.this, Locale.getDefault());
                            List<Address> addressList = geocoder.getFromLocation(
                                    selectedLocation.getLatitude(), selectedLocation.getLongitude(), 1);

                            habitEvent.setLatitude(selectedLocation.getLatitude());
                            habitEvent.setLongitude(selectedLocation.getLongitude());

                            locationTextView.setText(
                                    "lat: " + selectedLocation.getLatitude()
                                            + "Lon: " + selectedLocation.getLongitude());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            // We need to request permission to access the user's location.
            ActivityCompat.requestPermissions(
                    this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    /**
     * Show the HabitEvent by changing the attributes of the views that descrive things about the
     * HabitEvent.
     * @param habitEvent The HabitEvent to show through the views.
     */
    private void showHabitEvent(HabitEvent habitEvent) {
        habitEvent.getBitmapPic(new HabitEvent.BMPcallback() {
            @Override
            public void onBMPcallback(Bitmap bitmap) {
                eventImageView.setImageBitmap(bitmap);
            }
        });
        commentEditText.setText(habitEvent.getComment());
        locationTextView.setText(
                "lat: " + habitEvent.getLatitude() + "Lon: " + habitEvent.getLongitude());
        // TODO: Set the habit that this habit event is associated with.
    }
}
