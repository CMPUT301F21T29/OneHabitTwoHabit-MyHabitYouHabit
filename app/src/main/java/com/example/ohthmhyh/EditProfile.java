package com.example.ohthmhyh;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

//import com.github.dhaval2404.imagepicker.ImagePicker; Imported later

import java.io.IOException;

public class EditProfile extends AppCompatActivity {

    private ImageView pick;
    private Bitmap bitmap;

    String camraPermition[];
    String storagePermition[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Button pickProfile = findViewById(R.id.profile_button_image);

        pickProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*ImagePicker.with(EditProfile.this)
                        .crop()	//Crop image with 16:9 aspect ratio
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();*/
            }
        });
    }

    // Commented out for future usage.
    /*@Override
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
    }*/

}