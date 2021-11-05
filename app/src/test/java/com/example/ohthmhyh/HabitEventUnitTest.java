package com.example.ohthmhyh;

//@author Matt

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import android.graphics.Bitmap;
import android.location.Address;
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
     * @return sample address
     */
    Address createAddress() {
        Locale l = new Locale("en", "US");
        Address a = new Address(l);
        return a;

    }

    /**
     * Test building a habitevent with predefined values. Also tests getters.
     * @throws Exception
     */
    @Test
    public void testConstrAndGetters() {
        Habit h = createHabit();
        String comment = "Testing the comment";
        Address a = createAddress();

        Bitmap b = Bitmap.createBitmap(2, 3, Bitmap.Config.ARGB_8888); //create dummy bitmap

        int flag = -1;

        HabitEvent event = new  HabitEvent(h, comment, a, b, flag);

        assertEquals(h, event.getHabit());
        assertEquals(comment, event.getComment());
        assertEquals(a, event.getLocatoion());
        assertEquals(b, event.getBitmapPic());
        assertEquals(flag, event.getFlag());
    }

    /**
     * Test building a habitevent with predefined values. Then it changes those values with
     * the setter methods, and verifies if they were set correctly using a getter method
     * @throws Exception
     */
    @Test
    public void testSetters() {
        //initialize the habbit event
        Habit h = createHabit();
        String comment = "Testing the comment";
        Address a = createAddress();

        Bitmap b = Bitmap.createBitmap(2, 3, Bitmap.Config.ARGB_8888); //create dummy bitmap

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

        //create dummy bitmap
        Bitmap b2 = Bitmap.createBitmap(3, 4, Bitmap.Config.ARGB_8888);

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
