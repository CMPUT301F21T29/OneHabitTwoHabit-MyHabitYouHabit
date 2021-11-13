package com.example.ohthmhyh.listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.ohthmhyh.entities.HabitEvent;
import com.example.ohthmhyh.fragments.HabitEventsFragment;

public class HabitEventUpdateListener implements View.OnClickListener {

    private Activity activity;
    private HabitEvent habitEvent;

    public HabitEventUpdateListener(Activity activity, HabitEvent habitEvent) {
        this.activity = activity;
        this.habitEvent = habitEvent;
    }

    @Override
    public void onClick(View view) {
        if (valid(habitEvent)) {
            Intent intent = new Intent();
            intent.putExtra(HabitEventsFragment.ARG_RETURNED_HABIT_EVENT, habitEvent);
            activity.setResult(Activity.RESULT_OK, intent);
            activity.finish();
        }
    }

    private boolean valid(HabitEvent habitEvent) {
        // TODO: Add these to the Constants.java file as HABIT_EVENT_COMMENT_MIN_LENGTH and
        //       HABIT_EVENT_COMMENT_MAX_LENGTH, respectively.
        if (habitEvent.getComment().length() < 0 || habitEvent.getComment().length() > 20) {
            return false;
        }
        return true;
    }
}
