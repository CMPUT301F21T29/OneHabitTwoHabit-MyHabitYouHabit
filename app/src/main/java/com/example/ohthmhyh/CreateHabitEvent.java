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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CreateHabitEvent extends AppCompatActivity {
    Bitmap bitmap = null;

    Habit habit=Habit.makeDummyHabit();
    ArrayList<HabitEvent> habiteventlist;
    HabitEvent habitEvent;

    //used for image
    public ImageView pick;
    int flag5=-1;
    String camraPermition[];
    String storagePermition[];
    //used for location
    FusedLocationProviderClient fusedLocationProviderClient;
    Address address;
    //Used for drop down menu
    private AutoCompleteTextView autoCompleteTextView;


    //Used for comment
    EditText getComment;

    private String comment;
    //Flags, Used to error check and see what optinal data the user used.
    //flag=pic
    //flag2=comment
    //flag3=location
     Boolean flag=false, flag2=false, flag3=false;
     int flag4=-1;
    int position;
    TextView localText;
//Only Needed if fragments happen
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
        habiteventlist= ApplicationCE.getHabiteventlist();
        camraPermition = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermition = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        localText= (TextView) findViewById(R.id.Add_Location_text);
        getComment=(EditText) findViewById(R.id.Get_a_comment_CE);
        pick = (ImageView) findViewById(R.id.pickImage);



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
        flag4=intent.getIntExtra("flag",-1);
        position=intent.getIntExtra("position",69);

        //edit Habitevent when clicked on in list
        if (flag4>=0){
            habitEvent=habiteventlist.get(position);
            //todo
            //edit medicine when clicked on in list
            //habitEvent=magichabitlist.get(position);

            pick.setImageBitmap(habitEvent.getBitmapPic());
            getComment.setText(habitEvent.getComment());
            if (habitEvent.getLocatoion()==null){
                localText.setText("");
            }else{
                localText.setText(Html.fromHtml(habitEvent.getLocatoion().getAddressLine(0)));
            }
            String temp= habitEvent.getHabit().getName();
            //pop item from string habit list take note of position
            //append it to the front
            String [] habitList={"Habit one", "Habit two", "Habit three"};
            autoCompleteTextView=findViewById(R.id.AutoCompleteTextviewCE);
            ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.create_habit_habit_drop_down_menu,habitList);
            autoCompleteTextView.setText(arrayAdapter.getItem(0).toString(),false);
            autoCompleteTextView.setAdapter(arrayAdapter);

            autoCompleteTextView.setText(arrayAdapter.getItem(0).toString(),false);
            //reverse the changes above.
            }
    }//End of on create

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
                flag=true;
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

    //Does give an error but..........works, Does not see that I ask for Permission elsewhere I believe
    @SuppressLint("MissingPermission")
    private void getLocation() {
        //Simple tests
        TextView testLocationthing;
        testLocationthing = findViewById(R.id.testLocationthing);

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
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
                        flag3=true;
                        address=addressList.get(0);
                        testLocationthing.setText(Html.fromHtml(address.getAddressLine(0)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    Boolean commentvalidater(String string){
        return string.length() <= 20;
    }

    //Should work in maybe...
    //todo
    //get everything running with data
    public void final_create_habit(View view) {

        if (flag4 >=0){//need to edit medicine
            comment=getComment.getText().toString();

            if (commentvalidater(comment)){
                getComment.setBackgroundColor(Color.rgb(0,255,0));
                flag2=true;
            }
            else{
                getComment.setBackgroundColor(Color.rgb(255,0,0));
                flag2=false;
                return;
            }
            //No image was givin
            if (!flag){
                bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.lol_pic);

            }
            if (!flag3){
                address=null;
            }
            String test;
            test=autoCompleteTextView.getText().toString();
            Toast.makeText(this, test, Toast.LENGTH_LONG).show();
            HabitEvent updatehabitEvent=new HabitEvent(habit,comment,address,bitmap,flag5);
            //Magichabitlist.set.(position,updatehabitEvent);
            habiteventlist.set(position,updatehabitEvent);
            Intent intent =new Intent(CreateHabitEvent.this,MainActivity.class);
            startActivity(intent);

        }else{

        comment=getComment.getText().toString();

        if (commentvalidater(comment)){
            getComment.setBackgroundColor(Color.rgb(0,255,0));
            flag2=true;
        }
        else{
            getComment.setBackgroundColor(Color.rgb(255,0,0));
            flag2=false;
            return;
        }
        //No image was givin
        if (!flag){
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.lol_pic);
        }
        if (!flag3){
            address=null;
        }
        String test;
        test=autoCompleteTextView.getText().toString();
        //Do a for loop in habit list and find the name of test
        Toast.makeText(this, test, Toast.LENGTH_LONG).show();
        //TODO
        //Get user info
        //Get a habit from user
        HabitEvent habitEvent=new HabitEvent(habit,comment,address,bitmap,flag5);
        //push habitEvent into data base
            habiteventlist.add(habitEvent);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    }
    /**
     * https://stackoverflow.com/questions/16954109/reduce-the-size-of-a-bitmap-to-a-specified-size-in-android
     * reduces the size of the image
     * @param image
     * @param maxSize
     * @return
     */
    //public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
      //  int width = image.getWidth();
        //int height = image.getHeight();

        //float bitmapRatio = (float)width / (float) height;
        //if (bitmapRatio > 1) {
          //  width = maxSize;
           // height = (int) (width / bitmapRatio);
        //} else {
         //   height = maxSize;
        //    width = (int) (height * bitmapRatio);
        //}
       // return Bitmap.createScaledBitmap(image, width, height, true);
     //}
}
