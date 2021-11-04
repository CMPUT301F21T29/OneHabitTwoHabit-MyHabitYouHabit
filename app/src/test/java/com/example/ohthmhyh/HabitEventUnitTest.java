package com.example.ohthmhyh;

//@author Matt

import org.junit.Test;
import org.junit.runner.RunWith;
import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;
import static org.junit.Assert.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import java.time.LocalDate;
import java.util.Locale;

@RunWith(AndroidJUnit4.class)
public class HabitEventUnitTest {
    private Context context = ApplicationProvider.getApplicationContext();

    Habit createHabit() {
        //Initialize habit for testing purposes
        Habit h = new Habit();
        h.setName("test name");
        h.setDescription("test description");
        h.setStartDate(LocalDate.now().toEpochDay());
        h.setUHID(2021);
        h.scheduleAddDay(Habit.Days.Mon);
        h.scheduleAddDay(Habit.Days.Wed);
        h.scheduleAddDay(Habit.Days.Wed);
        h.scheduleAddDay(Habit.Days.Fri);

        return h;
    }

    Address createAddress() {
        Locale l = new Locale("en", "US");
        Address a = new Address(l);
        return a;

    }

    @Test
    public void testConstrAndGetters() {
        Habit h = createHabit();
        String comment = "Testing the comment";
        Address a = createAddress();
        Bitmap b = BitmapFactory.decodeResource(context.getResources(),R.drawable.lol_pic);
        int flag = -1;

        HabitEvent event = new  HabitEvent(h, comment, a, b, flag);

        assertEquals(h, event.getHabit());
        assertEquals(comment, event.getComment());
        assertEquals(a, event.getLocatoion());
        assertEquals(b, event.getBitmapPic());
        assertEquals(flag, event.getFlag());
    }

    @Test
    public void testSetters() {
        //initialize the habbit event
        Habit h = createHabit();
        String comment = "Testing the comment";
        Address a = createAddress();
        Bitmap b = BitmapFactory.decodeResource(context.getResources(),R.drawable.lol_pic);
        int flag = -1;
        HabitEvent event = new  HabitEvent(h, comment, a, b, flag);

        //create alternate parameters
        Habit h2 = new Habit();
            h2.setName("test name");
            h2.setDescription("test description");
            h2.setStartDate(LocalDate.now().toEpochDay());
            h2.setUHID(2021);
            h2.scheduleAddDay(Habit.Days.Mon);
            h2.scheduleAddDay(Habit.Days.Wed);
            h2.scheduleAddDay(Habit.Days.Wed);
            h2.scheduleAddDay(Habit.Days.Fri);
        String comment2 = "New comment?";
        Locale l = new Locale("fr", "US");
        Address a2 = new Address(l);
        Bitmap b2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.doge);
        int flag2 = 0;

        //set everything to it's new values
        event.setHabit(h2);
        event.setComment(comment2);
        event.setLocatoion(a2);
        event.setBitmapPic(b2);
        event.setFlag(flag2);

        assertEquals(h2, event.getHabit());
        assertEquals(comment2, event.getComment());
        assertEquals(a2, event.getLocatoion());
        assertEquals(b2, event.getBitmapPic());
        assertEquals(flag2, event.getFlag());
    }
}
