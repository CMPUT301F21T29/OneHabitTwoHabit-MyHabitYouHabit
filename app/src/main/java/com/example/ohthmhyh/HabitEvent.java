package com.example.ohthmhyh;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * The habitEvent class is used to define a habit event.
 *
 * A habit event is created whenever a user does a habit (for example, if you had a habit stating you
 * want to brush teeth every day, and at 9pm you actually brushed your teeth, you would create a habit event.
 * It is analogous to a post on a social media platform.
 */
public class HabitEvent {
    private Habit habit;
    private String comment;
    private String location;
    private int UPID;
    private int flag;

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
     * @param habit The habit relating to this event
     * @param comment A comment regarding this event
     * @param location The location where the event took place
     * @param BitmapPic A photo attached to the habit event post
     * @param flag A status flag regarding editing
     * @param UPID The id of the picture for this habit event
     */
    public HabitEvent(Habit habit, String comment, String location, Bitmap BitmapPic, int flag, int UPID) {
        this.habit = habit;
        this.comment = comment;
        this.location = location;
        this.flag=flag;
        this.UPID = UPID;
        setBitmapPic(BitmapPic);
    }


    /**
     * Get the habit relating to this event
     * @return The habit relating to this event
     */
    public Habit getHabit() {
        return habit;
    }

    /**
     * Set the habit for this event
     * @param habit The habit for the event
     */
    public void setHabit(Habit habit) {
        this.habit = habit;
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
     * Get the location where this event occured
     * @return The location of the event
     */
    public String getLocation() {
        return location;
    }

    /**
     * Set the location where for the event
     * @param location The location for the event
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Get the photo attached to this habit event from online storage
     * @return The photo (in bitmap form) attached to this event
     */
    public Bitmap getBitmapPic(HabitEvent.BMPcallback callback) {
        // set up the remote end
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String UID = user.getUid();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imgref = storage.getReference().child("images/"+ UID + "/"+Integer.toString(UPID)+".jpeg");

        // pull the image
        final long ONE_MEGABYTE = 1024 * 1024;
        imgref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
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
     * Get the flag for this habitEvent
     * @return The flag for this habitEvent
     */
    public int getFlag() {
        return flag;
    }

    /**
     * Set the flag for this habitevent
     * @param flag the flag for this habitEvent
     */
    public void setFlag(int flag) {
        this.flag = flag;
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

}
