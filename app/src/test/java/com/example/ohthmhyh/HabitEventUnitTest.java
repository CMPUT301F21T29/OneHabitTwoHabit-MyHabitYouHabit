package com.example.ohthmhyh;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.*;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

@RunWith(AndroidJUnit4.class)
public class HabitEventUnitTest {
    private Context context = ApplicationProvider.getApplicationContext();

    @Test
    public void Init_Habit_1() {
        int h = 1;
        Bitmap b = BitmapFactory.decodeResource(context.getResources(),R.drawable.lol_pic);
    }
}
