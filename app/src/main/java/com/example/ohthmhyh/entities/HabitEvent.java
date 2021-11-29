package com.example.ohthmhyh.entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.text.Html;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ohthmhyh.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * The habitEvent class is used to define a habit event.
 *
 * A habit event is created whenever a user does a habit (for example, if you had a habit stating
 * you want to brush teeth every day, and at 9pm you actually brushed your teeth, you would create a
 * habit event.
 *
 * There are no outstanding issues that we are aware of.
 */
public class HabitEvent {
    private int habitUHID;
    private String comment;
    private Double latitude, longitude;
    private int UPID;

    /**
     * A callback used when retrieving images from the database for this habit event
     */
    public interface BMPcallback{
        void onBMPcallback(Bitmap bitmap);
    }


    /**
     * Constructor to create a new habit event.
     */
    public HabitEvent(){
        // need the empty constructor for database reasons
    }

    /**
     * Constructor to create a new habit event
     * @param habitUHID The habit UHID relating to this event
     * @param comment A comment regarding this event
     * @param latitude The latitude where the event took place
     * @param longitude The longitude where the event took place
     * @param BitmapPic A photo attached to the habit event post
     * @param UPID The id of the picture for this habit event
     */
    public HabitEvent(
            int habitUHID,
            String comment,
            Double latitude,
            Double longitude,
            Bitmap BitmapPic,
            int UPID
    ) {
        this.habitUHID = habitUHID;
        this.comment = comment;
        this.latitude = latitude;
        this.longitude = longitude;
        this.UPID = UPID;
        setBitmapPic(BitmapPic);
    }


    /**
     * Get the habit UHID relating to this event
     * @return The habit UHID relating to this event
     */
    public int getHabitUHID() {
        return habitUHID;
    }

    /**
     * Set the habit UHID for this event
     * @param habitUHID The habit UHID for the event
     */
    public void setHabitUHID(int habitUHID) {
        this.habitUHID = habitUHID;
    }

    /**
     * Get the comment on this event
     * @return The comment on this event
     */
    public String getComment() {
        return comment;
    }

    /**
     * Set the comment for this event
     * @param comment The comment for this event
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Get the latitude where this event occurred
     * @return The latitude of where the event occurred
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Set the latitude where this event occurred
     * @param latitude The latitude of where the event occurred
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * Get the latitude where this event occurred
     * @return The latitude of where the event occurred
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Set the latitude where this event occurred
     * @param longitude The latitude of where the event occurred
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * Get the photo attached to this habit event from online storage
     * @param callback
     * @return The photo (in bitmap form) attached to this event
     */
    public Bitmap getBitmapPic(HabitEvent.BMPcallback callback) {
        // set up the remote end
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String UID = user.getUid();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imgref = storage.getReference().child(
                "images/"+ UID + "/"+Integer.toString(UPID)+".jpeg");

        // pull the image
        imgref.getBytes(Constants.MAX_IMAGE_FILE_SIZE*1024) //kB to B
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // when we get the image, send it to the caller
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                callback.onBMPcallback(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.e("Failure", "No picture found. Sadge.");
            }
        });
        return null;
    }

    /**
     * Set the photo for this habit event, and puts it into online storage
     * @param pic The photo for this event
     */
    public void setBitmapPic(Bitmap pic) {
        // handle the event where the picture is null
        if(pic == null){
            Log.e("NULL PIC", "NULL");
            return;
        }
        // compress and format the image for uploading
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        pic.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] picBytes = baos.toByteArray();

        // set up the remote end
        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String UID = user.getUid();

        // Create a storage reference for the image
        StorageReference imgref = storage.getReference()
                .child("images/"+ UID + "/"+Integer.toString(UPID)+".jpeg");

        UploadTask uploadTask = imgref.putBytes(picBytes);
    }
    
    /**
     * Get the picture ID of this habit event
     * @return The UPID of this habit event
     */
    public int getUPID() {
        return UPID;
    }

    /**
     * Set the UPID for this habit event
     * @param UPID the UPID
     */
    public void setUPID(int UPID) {
        this.UPID = UPID;
    }

    /**
     * Returns the location of this HabitEvent in a more human-friendly format.
     * @param context The context used to build the Geocoder object.
     * @return The location of this HabitEvent in a more human-friendly format.
     */
    public String locationString(Context context) {
        if (getLatitude() == null || getLongitude() == null) {
            return "Not provided";
        }

        String locationString = null;

        try {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses = geocoder.getFromLocation(getLatitude(), getLongitude(), 1);
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
