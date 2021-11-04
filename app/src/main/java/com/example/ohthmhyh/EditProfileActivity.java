package com.example.ohthmhyh;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

//import com.github.dhaval2404.imagepicker.ImagePicker; Imported later


public class EditProfileActivity extends AppCompatActivity {

    private ImageView pick;
    private Bitmap bitmap;

    String camraPermition[];
    String storagePermition[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_user);

        //TODO: pull the user from the database here
        User user = new User();

        // get the views in the activity
        EditText nameET = findViewById(R.id.profile_enter_name);
        EditText usernameET = findViewById(R.id.profile_enter_username);
        EditText bioET = findViewById(R.id.profile_enter_bio);
        Button pickProfile = findViewById(R.id.profile_button_image);
        Button saveBtn = findViewById(R.id.profile_button_submit);

        // prefill the edit texts with their current values
        nameET.setText(user.getName());
        usernameET.setText(user.getUsername());
        bioET.setText(user.getBio());

        // when the "update profile image" button is pressed
        pickProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*ImagePicker.with(EditProfileActivity.this)
                        .crop()	//Crop image with 16:9 aspect ratio
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();*/
            }
        });

        // when the save/submit button is pressed
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // make sure that the new username isn't already taken
                DatabaseAdapter.checkUsernameExists(usernameET.getText().toString()
                        , new DatabaseAdapter.UsernameCheckCallback() {
                    @Override
                    public void onUsernameCheckCallback(boolean usernameExists) {
                        // tell user if username taken
                        if(usernameExists){
                            Toast.makeText(EditProfileActivity.this,
                                    "Error: username already taken!", Toast.LENGTH_SHORT).show();
                        }
                        // otherwise update the user profile
                        else{
                            user.setUsername(usernameET.getText().toString());
                            user.setName(nameET.getText().toString());
                            user.setBio(bioET.getText().toString());

                            //TODO: push the updated user to the database here

                            finish();
                        }
                    }
                });
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