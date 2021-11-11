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
    private static Habit createHabit() {
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
    private static Location createAddress() {
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
        Habit habit = createHabit();
        String comment = "Testing the comment";
        Location location = createAddress();
        Bitmap bitmap = Bitmap.createBitmap(2, 3, Bitmap.Config.ARGB_8888); //create dummy bitmap

        HabitEvent event = new  HabitEvent(
                habit.getUHID(),
                comment,
                location.getLatitude(),
                location.getLongitude(),
                bitmap,
                1
        );

        assertEquals(habit.getUHID(), event.getHabitUHID());
        assertEquals(comment, event.getComment());
        assertEquals(location.getLatitude(), event.getLatitude().doubleValue());
        assertEquals(location.getLongitude(), event.getLongitude().doubleValue());
        // can't use here because of dependency on firebase. Need to be logged in first.
        // assertEquals(b, event.getBitmapPic());
    }

    /**
     * Test building a habitevent with predefined values. Then it changes those values with
     * the setter methods, and verifies if they were set correctly using a getter method
     * @throws Exception
     */
    @Test
    public void testSetters() {
        Habit habit = createHabit();
        String comment = "Testing the comment";
        Location location = createAddress();
        Bitmap bitmap = Bitmap.createBitmap(2, 3, Bitmap.Config.ARGB_8888); //create dummy bitmap

        HabitEvent event = new HabitEvent();

        //set everything to it's new values
        event.setHabitUHID(habit.getUHID());
        event.setComment(comment);
        event.setLatitude(location.getLatitude());
        event.setLongitude(location.getLongitude());
        event.setBitmapPic(bitmap);

        assertEquals(habit.getUHID(), event.getHabitUHID());
        assertEquals(comment, event.getComment());
        assertEquals(location.getLatitude(), event.getLatitude().doubleValue());
        assertEquals(location.getLongitude(), event.getLongitude().doubleValue());
        // can't use here because of dependency on firebase. Need to be logged in first.
        //assertEquals(b2, event.getBitmapPic());
    }
}
