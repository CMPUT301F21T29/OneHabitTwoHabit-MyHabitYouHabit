package com.example.ohthmhyh.activities;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ohthmhyh.CustomAdapterHF;
import com.example.ohthmhyh.TouchingHandlingHF;
import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.database.HabitList;
import com.example.ohthmhyh.entities.Habit;
import com.example.ohthmhyh.entities.HabitEvent;
import com.example.ohthmhyh.database.HabitEventList;
import com.example.ohthmhyh.R;
import com.example.ohthmhyh.watchers.LengthTextWatcher;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UpdateHabitEventActivity extends AppCompatActivity {

    public static final String ARG_HABIT_EVENT_INDEX = "habit_event_index_arg";

    private Bitmap bitmap = null;
    private Habit habit = Habit.makeDummyHabit();  // This is temp
    private HabitEventList habitEventList;
    private HabitList habitList;
    private DatabaseAdapter databaseAdapter;
    ArrayList<String> NameArray=new ArrayList<>();
    private Location location = null;
    private int habitEventIndex;

    private AutoCompleteTextView habitListAutoCompleteTextView;
    private EditText commentEditText;
    private ImageView pictureImageView;
    private TextView locationTextView;
    private Button editandCreateEventButton;
    private Button locationButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_habit_event);

        databaseAdapter = new DatabaseAdapter();
        databaseAdapter.pullHabitEvents(new DatabaseAdapter.HabitEventCallback() {
            @Override
            public void onHabitEventCallback(HabitEventList habitEvents) {
                habitEventList = habitEvents;
                makeAndEdit();
            }
        });
        databaseAdapter = new DatabaseAdapter();
        databaseAdapter.pullHabits(new DatabaseAdapter.HabitCallback() {
            @Override
            public void onHabitCallback(HabitList hList) {
                habitList = hList;
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Down here is for dropdown menu. Need to get the habit.
        NameArray = habitList.getStringNameArray();
        NameArray.add("tet");
        NameArray.add("bcb");
        habitListAutoCompleteTextView = findViewById(R.id.AutoCompleteTextviewCE);
        ArrayAdapter arrayAdapter = new ArrayAdapter(
                this, R.layout.create_habit_habit_drop_down_menu, NameArray);
        habitListAutoCompleteTextView.setText(arrayAdapter.getItem(0).toString(),false);
        habitListAutoCompleteTextView.setAdapter(arrayAdapter);
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
        editandCreateEventButton=(Button) findViewById(R.id.Create_edit_event_button);
        locationButton=(Button) findViewById(R.id.Add_location_button);
        // Set attributes for the views in the Activity.
        commentEditText.addTextChangedListener(new LengthTextWatcher(commentEditText, 0, 20));
        editandCreateEventButton.setText("Create New Habit");
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
        if (habitEventIndex >= 0) {//THIS IS THE EDIT AREA
            HabitEvent habitEvent = habitEventList.getHabitEvent(habitEventIndex);

            // Set the image picture.
            habitEvent.getBitmapPic(new HabitEvent.BMPcallback() {
                @Override
                public void onBMPcallback(Bitmap bitmap) {
                    pictureImageView.setImageBitmap(bitmap);
                }
            });
            editandCreateEventButton.setText("commit edit");
            locationButton.setText("Update Location?");
            // Set the comment.
            commentEditText.setText(habitEvent.getComment());

            // Set the location.
            if (habitEvent.getLatitude() == null || habitEvent.getLongitude() == null) {
                locationTextView.setText("");
                location = null;
            } else {
                Geocoder geocoder=new Geocoder(this, Locale.getDefault());
                location = new Location("");
                try {
                    List<Address> addresses=geocoder.getFromLocation(habitEvent.getLatitude(),habitEvent.getLongitude(),1);
                    locationTextView.setText(Html.fromHtml("<font color='#6200EEE'><b>Address: " +
                            addresses.get(0).getLocality())+" " +addresses.get(0).getCountryName());
                } catch (IOException e) {//Failed to get location from user
                    e.printStackTrace();
                    locationTextView.setText("lat: "+habitEvent.getLatitude()+ "Lon: "+ habitEvent.getLongitude());

                }
            }
            //Changes the name of the habit to be in pos zero when editing
            //for(int i=0;i<habitList.size();i++){
              //  if (habitList.getHabit(i).getUHID()==(habitEvent.getHabitUHID())){
                //    NameArray.remove(i);
                  //  NameArray.add(0,habitList.getHabit(i).getName());
                    //break;}
            NameArray.remove("tet");
            NameArray.remove("bcb");
            NameArray.add("bcb");
            NameArray.add("tet");
            ArrayAdapter arrayAdapter = new ArrayAdapter(
                        this, R.layout.create_habit_habit_drop_down_menu, NameArray);
            habitListAutoCompleteTextView.setText(arrayAdapter.getItem(0).toString(),false);
          //}
        }
    }//THIS IS THE END OF EDIT AREA

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
            getLocation();
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

                        locationTextView.setText(Html.fromHtml("<font color='#6200EEE'><b>Address: " +
                                addressList.get(0).getLocality())+" " +addressList.get(0).getCountryName());
                    } catch (IOException e) {
                        e.printStackTrace();
                        locationTextView.setText("Failed to find your Location!");
                    }
                }
            }
        });
    }

    /**
     * Verify the string is less than 20 characters long.
     * @param string Takes a string and make sure to get a string less then 20
     */
    private static Boolean validComment(String string){
        return string.length() <= 20;
    }

    /**
     * Call this method to get read all the data from screen and make
     * habit event
     */
    public void final_create_habit(View view) {
        String comment = commentEditText.getText().toString();

        // Return if the comment is not the right length.
        if (!validComment(comment)) {
            return;
        }

        // No image was given.
        if (bitmap == null){
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lol_pic);
        }

        // TODO: Associate the HabitEvent with an actual Habit
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
}
