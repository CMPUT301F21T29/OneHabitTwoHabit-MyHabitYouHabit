package com.example.ohthmhyh.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.example.ohthmhyh.Constants;
import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.entities.Habit;
import com.example.ohthmhyh.entities.HabitEvent;
import com.example.ohthmhyh.database.HabitEventList;
import com.example.ohthmhyh.R;
import com.example.ohthmhyh.watchers.LengthTextWatcher;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class UpdateHabitEventActivity extends AppCompatActivity {

    public static final String ARG_HABIT_EVENT_INDEX = "habit_event_index_arg";

    private Bitmap bitmap = null;
    private Habit habit = Habit.makeDummyHabit();  // This is temp
    private HabitEventList habitEventList;
    private DatabaseAdapter databaseAdapter;
    private Location location = null;
    private int habitEventIndex;

    private AutoCompleteTextView habitListAutoCompleteTextView;
    private EditText commentEditText;
    private ImageView pictureImageView;
    private TextView locationTextView;
    //private ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onResume() {
        super.onResume();

        // Down here is for dropdown menu. Need to get the habit.
        String[] habitList = {"Habit one", "Habit two", "Habit three"};
        habitListAutoCompleteTextView = findViewById(R.id.AutoCompleteTextviewCE);
        ArrayAdapter arrayAdapter = new ArrayAdapter(
                this, R.layout.create_habit_habit_drop_down_menu, habitList);
        habitListAutoCompleteTextView.setText(arrayAdapter.getItem(0).toString(),false);
        habitListAutoCompleteTextView.setAdapter(arrayAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_habit_event);

        databaseAdapter = DatabaseAdapter.getInstance();
        databaseAdapter.pullHabitEvents(new DatabaseAdapter.HabitEventCallback() {
            @Override
            public void onHabitEventCallback(HabitEventList habitEvents) {
                habitEventList = habitEvents;
                makeAndEdit();
            }
        });

    }

    /**
     * When map is closed and not cancalled, this method gets called
     * @param intent contains the location data selected from the map
     */
    private void onMapClosed(Intent intent) {
        this.location = (Location) intent.getExtras().get(MapActivity.ARG_LOCATION);
        locationTextView.setText(locationString(location));
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

    private void makeAndEdit() {
        String[] cameraPermission = new String[]{
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        String[] storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // Get references to the views in the Activity.
        locationTextView = (TextView) findViewById(R.id.Add_Location_text);
        commentEditText = (EditText) findViewById(R.id.Get_a_comment_CE);
        pictureImageView = (ImageView) findViewById(R.id.pickImage);

        // Set attributes for the views in the Activity.
        commentEditText.addTextChangedListener(
                new LengthTextWatcher(
                        commentEditText, Constants.HABIT_EVENT_COMMENT_MIN_LENGTH,
                        Constants.HABIT_EVENT_COMMENT_MAX_LENGTH));
        pictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(UpdateHabitEventActivity.this)
                        .crop()	 // Crop image with 16:9 aspect ratio
                        .compress(1024)  //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)  // Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        Intent intent = getIntent();
        habitEventIndex = intent.getIntExtra(ARG_HABIT_EVENT_INDEX, -1);

        // Show the selected HabitEvent.
        if (habitEventIndex >= 0) {
            HabitEvent habitEvent = habitEventList.getHabitEvent(habitEventIndex);

            // Set the image picture.
            habitEvent.getBitmapPic(new HabitEvent.BMPcallback() {
                @Override
                public void onBMPcallback(Bitmap bitmap) {
                    UpdateHabitEventActivity.this.bitmap = bitmap;  // Set this instance's bitmap.
                    pictureImageView.setImageBitmap(bitmap);
                }
            });

            // Set the comment.
            commentEditText.setText(habitEvent.getComment());

            // Set the location.
            if (habitEvent.getLatitude() == null || habitEvent.getLongitude() == null) {
                locationTextView.setText("");
                location = null;
            } else {
                location = new Location("");
                location.setLatitude(habitEvent.getLatitude());
                location.setLongitude(habitEvent.getLongitude());
                locationTextView.setText(locationString(location));
            }

            // TODO: Set the habit associated with this HabitEvent.
        }
    }

    /**
     * Call this method to get the camera from the user
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data == null){
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                pictureImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        // Image Uri will not be null for RESULT_OK
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else if (resultCode == MapActivity.LOCATION_OK) {
            if (data == null) {
                return;
            }
            onMapClosed(data);
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Call this method to get the user location permissions
     */
    public void get_user_location(View view) {
        // Check permissions.
        if (ActivityCompat.checkSelfPermission(UpdateHabitEventActivity.this
                , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted.
            if (this.location == null) {
                getLocation();
            } else {
                openMaps();
            }
        } else {
            // Permission is denied.
            ActivityCompat.requestPermissions(UpdateHabitEventActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            Toast.makeText(this, "Please enable location permission", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Call this method to get the user location
     * Note there is a  @SuppressLint("MissingPermission") I assume this happens because
     * it does not see me asking for permissions else where
     */
    @SuppressLint("MissingPermission")
    private void getLocation() {
        FusedLocationProviderClient fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(
                new OnCompleteListener<Location>() {
            /**
             * Obtains the user's phone's location.
             */
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                // Initialize location
                Location currentLocation = task.getResult();
                if (currentLocation != null) {
                    try {
                        // Get GeoCoder
                        Geocoder geocoder = new Geocoder(UpdateHabitEventActivity.this,
                                Locale.getDefault());
                        List<Address> addressList = geocoder.getFromLocation(
                                currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                        // To get everything do addressList.get(0).getLatitude
                        // getLongitude, get(0).getCountry  getAddressLine

                        location = new Location("");
                        location.setLatitude(addressList.get(0).getLatitude());
                        location.setLongitude(addressList.get(0).getLongitude());

                        openMaps();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * Call this method to open the google map to select a location
     */
    private void openMaps() {
        Intent mapIntent = new Intent(this, MapActivity.class);
        mapIntent.putExtra(MapActivity.ARG_LOCATION, location);
        this.startActivityForResult(mapIntent, 0);
    }

    /**
     * Call this method to get read all the data from screen and make
     * habit event
     */
    public void final_create_habit(View view) {
        String comment = commentEditText.getText().toString();

        // Return if the comment is not the right length.
        if (comment.length() < Constants.HABIT_EVENT_COMMENT_MIN_LENGTH ||
                comment.length() > Constants.HABIT_EVENT_COMMENT_MAX_LENGTH) {
            return;
        }

        // No image was given.
        if (bitmap == null){
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.habit_event_default_img);
        }

        // TODO: Associate the HabitEvent with an actual Habit.
        HabitEvent habitEvent;
        if (location == null) {
            habitEvent = new HabitEvent(
                    habit.getUHID(),
                    comment,
                    null,
                    null,
                    bitmap,
                    habitEventList.nextUPID()
            );
        } else {
            habitEvent = new HabitEvent(
                    habit.getUHID(),
                    comment,
                    location.getLatitude(),
                    location.getLongitude(),
                    bitmap,
                    habitEventList.nextUPID()
            );
        }

        // Show which Habit was chosen
        String habitTitle = habitListAutoCompleteTextView.getText().toString();
        Toast.makeText(this, habitTitle, Toast.LENGTH_LONG).show();

        if (habitEventIndex >= 0){  // Editing the HabitEvent.
            // Replace the HabitEvent at the given position.
            habitEventList.replaceHabitEvent(habitEventIndex, habitEvent);
        } else {  // Adding the HabitEvent.
            // Add HabitEvent into the HabitEventList.
            habitEventList.addHabitEvent(habitEvent);
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // TODO: This is very similar to the HabitEvent's locationString method. Would be good to
    //       eventually aggregate this functionality somehow.
    /**
     * Returns the location cordinates in a more human-friendly format.
     * @param location that needs to be converted
     * @return The location of this HabitEvent in a more human-friendly format.
     */
    public String locationString(Location location) {
        String locationString = null;

        try {
            Geocoder geocoder = new Geocoder(UpdateHabitEventActivity.this,
                    Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String locality = addresses.get(0).getLocality();
            String country = addresses.get(0).getCountryName();

            // If we have both the locality and country, then use them both. If we only have the
            // country, then just use that.
            if (locality != null && country != null) {
                locationString = locality + ", " + country;
            } else if (country != null) {
                locationString = country;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (locationString == null) {
            locationString = "Unable to find the specific location";
        }

        return locationString;
    }

}
