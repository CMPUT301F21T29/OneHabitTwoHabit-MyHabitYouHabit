package com.example.ohthmhyh;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
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

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CreateHabitEvent extends AppCompatActivity {
    //LOCAL VARIABLES
    Bitmap bitmap = null;
    Habit habit=Habit.makeDummyHabit();//This is temp
    HabitEventList habiteventlist;
    DatabaseAdapter databaseAdapter;
    HabitEvent habitEvent;

    //used for image
    public ImageView pick;
    String camraPermition[];
    String storagePermition[];
    //used for location
    FusedLocationProviderClient fusedLocationProviderClient;
    String address=null;
    //Used for drop down menu
    private AutoCompleteTextView autoCompleteTextView;
    //Used for comment
    EditText getComment;
    String comment;
    //Used for editing
    int Edit=-1;
    int position;
    TextView localText;
//Used for the drop down menu
    @Override
    protected void onResume() {
        super.onResume();
        //Down here is for dropdown menu
        //Needs to get the habit
        //Need to habit
        String [] habitList={"Habit one", "Habit two", "Habit three"};
        autoCompleteTextView=findViewById(R.id.AutoCompleteTextviewCE);
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.create_habit_habit_drop_down_menu,habitList);
        autoCompleteTextView.setText(arrayAdapter.getItem(0).toString(),false);
        autoCompleteTextView.setAdapter(arrayAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_habit_event);

        databaseAdapter = new DatabaseAdapter();
        databaseAdapter.pullHabitEvents(new DatabaseAdapter.HabitEventCallback() {
            @Override
            public void onHabitEventCallback(HabitEventList habitEvents) {
                habiteventlist = habitEvents;

                makeAndEdit();
            }
        });
    }//End of on create


    private void makeAndEdit(){

        camraPermition = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermition = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        localText= (TextView) findViewById(R.id.Add_Location_text);
        getComment=(EditText) findViewById(R.id.Get_a_comment_CE);
        pick = (ImageView) findViewById(R.id.pickImage);


        //Used to get a picture from the user
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(CreateHabitEvent.this)
                        .crop()	//Crop image with 16:9 aspect ratio
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();

            }
        });
        Intent intent= getIntent();
        Edit=intent.getIntExtra("flag",-1);
        position=intent.getIntExtra("position",69);

        //edit Habitevent when clicked on in list
        if (Edit>=0){
            habitEvent=habiteventlist.getHabitEvent(position);
            //todo
            //edit medicine when clicked on in list
            //habitEvent=magichabitlist.get(position);

            //This reads the clicked habit event and sets it for editing
            pick.setImageBitmap(habitEvent.getBitmapPic());
            getComment.setText(habitEvent.getComment());
            if (habitEvent.getLocation()==null){
                localText.setText("");
            }else{
                localText.setText(address);
            }
            bitmap=habitEvent.getBitmapPic();
            address=habitEvent.getLocation();
            String temp= habitEvent.getHabit().getName();
            //pop item from string habit list take note of position
            //append it to the front
            String [] habitList={"Habit one", "Habit two", "Habit three"};//This is temport untill I can get the user habit list
            autoCompleteTextView=findViewById(R.id.AutoCompleteTextviewCE);
            ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.create_habit_habit_drop_down_menu,habitList);
            autoCompleteTextView.setText(arrayAdapter.getItem(0).toString(),false);
            autoCompleteTextView.setAdapter(arrayAdapter);

            autoCompleteTextView.setText(arrayAdapter.getItem(0).toString(),false);
            //reverse the changes above.
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
            Uri uri=data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                pick.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
//Image Uri will not be null for RESULT_OK
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Call this method to get the user location permissions
     *
     */

    public void get_user_location(View view) {
        //Check Permitions
        if (ActivityCompat.checkSelfPermission(CreateHabitEvent.this
                , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //whe permition is granted
            getLocation();
        } else {
            //When denided
            ActivityCompat.requestPermissions(CreateHabitEvent.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            Toast.makeText(this, "Please Enable Location Permition", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Call this method to get the user location
     * Note there is a  @SuppressLint("MissingPermission") I assume this happens because
     * it does not see me asking for permitions else where
     */
    @SuppressLint("MissingPermission")
    private void getLocation() {
        //Simple tests
        TextView testLocationthing;
        testLocationthing = findViewById(R.id.testLocationthing);

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            /**
             *Simple method that when called gets user location
             *
             */
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                //Initalize location
                Location location = task.getResult();
                if (location != null) {
                    try {
                        //get GeoCoder
                        Geocoder geocoder = new Geocoder(CreateHabitEvent.this,
                                Locale.getDefault());
                        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        //To get everything do addressList.get(0).getLatitude
                        //getLongatude, get(0).getCountry  getAddressLine

                        address= addressList.get(0).getAddressLine(0);

                        testLocationthing.setText(address);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    /**
     * Verify the string is less than 20 characters long.
     * @param string Takes a string and make sure to get a string less then 20
     */
    Boolean commentvalidater(String string){
        return string.length() <= 20;
    }

    /**
     * Call this method to get read all the data from screen and make
     * habit event
     */
    public void final_create_habit(View view) {
        if (Edit >=0){//need to edit medicine
            comment=getComment.getText().toString();

            if (commentvalidater(comment)){
                getComment.setBackgroundColor(Color.rgb(0,255,0));

            }
            else{
                getComment.setBackgroundColor(Color.rgb(255,0,0));
                return;
            }
            String test;
            test=autoCompleteTextView.getText().toString();
            Toast.makeText(this, test, Toast.LENGTH_LONG).show();
            HabitEvent updatehabitEvent=new HabitEvent(habit,comment,address,bitmap,-1, habiteventlist.nextUPID());
            //Magichabitlist.set.(position,updatehabitEvent);
            habiteventlist.replaceHabitEvent(position,updatehabitEvent);
            Intent intent = new Intent(CreateHabitEvent.this,MainActivity.class);
            startActivity(intent);

        }else{

        comment=getComment.getText().toString();

        if (commentvalidater(comment)){
            getComment.setBackgroundColor(Color.rgb(0,255,0));
        }
        else{
            getComment.setBackgroundColor(Color.rgb(255,0,0));
            return;
        }
        //No image was givin
        if (bitmap==null){
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.lol_pic);
        }
        String test;
        test=autoCompleteTextView.getText().toString();
        //Do a for loop in habit list and find the name of test
        Toast.makeText(this, test, Toast.LENGTH_LONG).show();
        //TODO
        //Get user info
        //Get a habit from user
        HabitEvent habitEvent=new HabitEvent(habit,comment,address,bitmap,-1, habiteventlist.nextUPID());
        //push habitEvent into data base
            habiteventlist.addHabitEvent(habitEvent);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    }
}
