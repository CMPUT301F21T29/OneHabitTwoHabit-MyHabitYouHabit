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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

//import com.github.dhaval2404.imagepicker.ImagePicker; Imported later

import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView pick;
    private Bitmap bitmap;

    private Button pickProfile;
    private Button submitButton;
    private Button cancelButton;
    private EditText name_edit;
    EditText username_edit;
    EditText bio_edit;

    String camraPermition[];
    String storagePermition[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String name = (String) bundle.get("NAME");
        String user_name = bundle.getString("USERNAME");
        String biography = bundle.getString("BIO");

        pickProfile = findViewById(R.id.profile_button_image);
        submitButton = findViewById(R.id.profile_button_submit);
        cancelButton = findViewById(R.id.profile_button_cancel);

        name_edit = findViewById(R.id.profile_enter_name);
        name_edit.setText(name);

        username_edit = findViewById(R.id.profile_enter_username);
        name_edit.setText(user_name);

        bio_edit = findViewById(R.id.profile_enter_bio);
        bio_edit.setText(biography);

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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("NAME", name_edit.getText().toString());
                intent.putExtra("USERNAME", username_edit.getText().toString());
                intent.putExtra("BIO", bio_edit.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
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