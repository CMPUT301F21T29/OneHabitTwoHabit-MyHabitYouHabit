package com.example.ohthmhyh;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CreateHabitEvent extends AppCompatActivity {
    Bitmap bitmap = null;


    //used for image
    public ImageView pick;
    public static final int Camra_request = 100;
    public static final int Storage_request = 101;
    private Uri resultUri;
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

        camraPermition = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermition = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getComment=(EditText) findViewById(R.id.Get_a_comment_CE);
        pick = (ImageView) findViewById(R.id.pickImage);
        pick.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                int picd = 0;
                if (picd == 0) {
                    if (!checkCamraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromGallery();
                    }
                } else if (picd == 1) {
                    if (!checkStoragePermition()) {
                        requestStoragePermition();
                    } else {
                        pickFromGallery();
                    }
                }

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermition() {
        requestPermissions(storagePermition, Storage_request);
    }

    private boolean checkStoragePermition() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void pickFromGallery() {
        CropImage.activity().start(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(camraPermition, Camra_request);
    }

    private boolean checkCamraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                Picasso.with(this).load(resultUri).into(pick);
                flag=true;
                //Should get the bitmap from the givin URL
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Camra_request: {
                if (grantResults.length > 0) {
                    boolean camra_accepted = grantResults[0] == (PackageManager.PERMISSION_GRANTED);
                    boolean storage_accepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camra_accepted && storage_accepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please Enable camra and storage Permition", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
            case Storage_request: {
                if (grantResults.length > 0) {
                    boolean storage_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storage_accepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please Enable storage Permition", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
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
        if (string.length()>20){
            return false;
        }
        return true;
    }

    public void final_create_habit(View view) {
        comment=getComment.getText().toString();

        if (commentvalidater(comment)){
            getComment.setBackgroundColor(Color.rgb(0,255,0));
            flag2=true;
        }
        else{
            getComment.setBackgroundColor(Color.rgb(255,0,0));
            flag2=false;
        }
        //No image was givin
        if (!flag){
            resultUri=null;
        }
        if (!flag3){
            address=null;
        }
        String test;
        test=autoCompleteTextView.getText().toString();
        Toast.makeText(this, test, Toast.LENGTH_LONG).show();
        //TODO
        //Get user info
        //Get a habit from user
        //HabitEvent habitEvent=new HabitEvent(habit,comment,UUID,UHID,location,resultUri);
        //push habitEvent into data base
        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
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
    }
}
