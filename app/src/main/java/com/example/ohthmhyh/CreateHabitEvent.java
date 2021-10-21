package com.example.ohthmhyh;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

public class CreateHabitEvent extends AppCompatActivity {
    public ImageView pick;
    public static final int Camra_request=100;
    public static final int Storage_request=101;
String camraPermition[];
String storagePermition[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_habit_event);

    camraPermition=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    storagePermition=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

    pick=(ImageView) findViewById(R.id.pickImage);
    pick.setOnClickListener(new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            int picd=0;
            if (picd==0){
                if (!checkCamraPermission()) {
                    requestCameraPermission();
                }else{
                    pickFromGallery();
                }
            }
                else if(picd==1){
                    if (!checkStoragePermition()){
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
        requestPermissions(storagePermition,Storage_request);
    }

    private boolean checkStoragePermition() {
        boolean result= ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void pickFromGallery() {
        CropImage.activity().start(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(camraPermition,Camra_request);
    }

    private boolean checkCamraPermission() {
        boolean result= ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        boolean result1=ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
    return result && result1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);

        if (resultCode==RESULT_OK) {
            Uri resultUri = result.getUri();
            Picasso.with(this).load(resultUri).into(pick);
        }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case Camra_request:{
                if (grantResults.length>0){
                    boolean camra_accepted=grantResults[0]==(PackageManager.PERMISSION_GRANTED);
                    boolean storage_accepted=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if (camra_accepted && storage_accepted){
                        pickFromGallery();
                    }else{
                        Toast.makeText(this, "Please Enable camra and storage Permition", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
            case Storage_request:{
                if (grantResults.length>0){
                    boolean storage_accepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if (storage_accepted){
                        pickFromGallery();
                    }else{
                        Toast.makeText(this, "Please Enable storage Permition", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
        }
    }

    public void thing(View view){

    }

    public void final_create_habit(View view){



        Intent intent =new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}
