package com.example.ohthmhyh.listeners;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.ohthmhyh.activities.UpdateHabitEventActivity;
import com.example.ohthmhyh.entities.Habit;
import com.example.ohthmhyh.entities.HabitEvent;
import com.example.ohthmhyh.fragments.HabitEventsFragment;

import java.util.ArrayList;

public class HabitEventUpdateListener implements View.OnClickListener {

    private Activity activity;
    private ArrayList<Habit> habitArrayList;
    private EditText commentEditText;
    private AutoCompleteTextView habitListAutoCompleteTextView;
    private ImageView pictureImageView;
    private Location location;
    private int UPID;
    private int selectedHabitPosition = 0;

    public HabitEventUpdateListener(
            Activity activity,
            ArrayList<Habit> habitArrayList,
            EditText commentEditText,
            AutoCompleteTextView habitListAutoCompleteTextView,
            ImageView pictureImageView,
            Location location,
            int UPID
    ) {
        this.activity = activity;
        this.habitArrayList = habitArrayList;
        this.commentEditText = commentEditText;
        this.habitListAutoCompleteTextView = habitListAutoCompleteTextView;
        this.pictureImageView = pictureImageView;
        this.location = location;
        this.UPID = UPID;

        this.habitListAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedHabitPosition = i;
            }
        });
    }

    @Override
    public void onClick(View view) {
        String comment = commentEditText.getText().toString();
        String habitName = habitListAutoCompleteTextView.getText().toString();
        Bitmap picture = ((BitmapDrawable) pictureImageView.getDrawable()).getBitmap();

        if (valid(comment)) {
            Double latitude = null;
            Double longitude = null;

            if (location.getProvider().equals(UpdateHabitEventActivity.PROVIDER_LOCATION_SET)) {
                // The location is set, ensure the latitude and longitude are added to the
                // HabitEvent.
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            // TODO: Obtain correct Habit UHID.
            Habit selectedHabit = habitArrayList.get(selectedHabitPosition);
            Log.d("Listener", "selectedHabit name: " + selectedHabit.getName());
            Log.d("Listener", "selectedHabit UHID: " + selectedHabit.getUHID());
            HabitEvent habitEvent = new HabitEvent(
                    selectedHabit.getUHID(), comment, latitude, longitude, picture, UPID);
            Intent intent = new Intent();
            intent.putExtra(HabitEventsFragment.ARG_RETURNED_HABIT_EVENT, habitEvent);
            activity.setResult(Activity.RESULT_OK, intent);
            activity.finish();
        }
    }

    private boolean valid(String comment) {
        // TODO: Add these to the Constants.java file as HABIT_EVENT_COMMENT_MIN_LENGTH and
        //       HABIT_EVENT_COMMENT_MAX_LENGTH, respectively.
        if (comment.length() < 0 || comment.length() > 20) {
            return false;
        }
        return true;
    }
}
