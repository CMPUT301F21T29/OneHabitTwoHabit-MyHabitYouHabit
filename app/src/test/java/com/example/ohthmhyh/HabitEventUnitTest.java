package com.example.ohthmhyh;

//@author Matt

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Location;

import java.time.LocalDate;
import java.util.Locale;


/**
 * This class is used to run tests on the HabitEvent class
 */
public class HabitEventUnitTest {

    /**
     * A simple function to create a sample habit, to use for testing
     * @return sample habit
     */
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

    /**
     * simple function to create a sample address, to use for testing
     * @return sample location
     */
    Location createAddress() {
        Location l = new Location("");
        l.setLongitude(69.420);
        l.setLatitude(42.069);
        return l;
    }

    /**
     * Test building a habitevent with predefined values. Also tests getters.
     * @throws Exception
     */
    @Test
    public void testConstrAndGetters() {
        Habit h = createHabit();
        String comment = "Testing the comment";
        Location a = createAddress();

        Bitmap b = Bitmap.createBitmap(2, 3, Bitmap.Config.ARGB_8888); //create dummy bitmap

        int flag = -1;

        HabitEvent event = new  HabitEvent(h, comment, a.getLatitude(), a.getLongitude(), b, flag, 1);

        assertEquals(h, event.getHabit());
        assertEquals(comment, event.getComment());
        assertTrue(a.getLatitude() == event.getLatitude());
        assertTrue(a.getLongitude() == event.getLongitude());
        // can't use here because of dependency on firebase. Need to be logged in first.
        // assertEquals(b, event.getBitmapPic());
        assertEquals(flag, event.getFlag());
    }

    /**
     * Test building a habitevent with predefined values. Then it changes those values with
     * the setter methods, and verifies if they were set correctly using a getter method
     * @throws Exception
     */
    @Test
    public void testSetters() {
        //initialize the habit event
        Habit h = createHabit();
        String comment = "Testing the comment";
        Location a = createAddress();

        Bitmap b = Bitmap.createBitmap(2, 3, Bitmap.Config.ARGB_8888); //create dummy bitmap

        int flag = -1;
        HabitEvent event = new  HabitEvent(h, comment, a.getLatitude(), a.getLongitude(), b, flag, 1);

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

        //create dummy bitmap
        Bitmap b2 = Bitmap.createBitmap(3, 4, Bitmap.Config.ARGB_8888);

        int flag2 = 0;

        //set everything to it's new values
        event.setHabit(h2);
        event.setComment(comment2);
        event.setLatitude(a.getLatitude());
        event.setLongitude(a.getLongitude());
        event.setBitmapPic(b2);
        event.setFlag(flag2);

        assertEquals(h2, event.getHabit());
        assertEquals(comment2, event.getComment());
        assertTrue(a.getLatitude() == event.getLatitude());
        assertTrue(a.getLongitude() == event.getLongitude());
        // can't use here because of dependency on firebase. Need to be logged in first.
        //assertEquals(b2, event.getBitmapPic());
        assertEquals(flag2, event.getFlag());
    }
}
