package com.example.ohthmhyh;

import com.example.ohthmhyh.database.HabitEventList;
import com.example.ohthmhyh.entities.HabitEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A unit test to test the functionality of the HabitEventList object.
 */
public class HabitEventListUnitTest {

    private HabitEventList habitEventList;
    private int originalSize;

    /**
     * Returns a distinct HabitEvent.
     * @return A distinct HabitEvent.
     */
    private static HabitEvent testHabitEvent() {
        return new HabitEvent(
                (int) (Math.random() * Integer.MAX_VALUE),
                "Comment",
                null,
                null,
                null,
                (int) (Math.random() * Integer.MAX_VALUE));
    }

    /**
     * Called before each test to set up a HabitEventList and its size.
     */
    @BeforeEach
    public void setUp() {
        habitEventList = new HabitEventList();
        originalSize = habitEventList.size();
    }

    /**
     * Test adding a HabitEvent to the HabitEventList.
     */
    @Test
    public void testAddHabitEvent() {
        HabitEvent habitEvent = testHabitEvent();
        habitEventList.addHabitEvent(habitEvent);
        assertEquals(habitEventList.size(), originalSize + 1);
    }

    /**
     * Test getting a HabitEvent from the HabitEventList.
     */
    @Test
    public void testGetHabitEvent() {
        HabitEvent habitEvent = testHabitEvent();
        habitEventList.addHabitEvent(habitEvent);
        assertEquals(habitEventList.size(), originalSize + 1);
        HabitEvent habitEvent1 = habitEventList.getHabitEvent(0);
        assertEquals(habitEvent, habitEvent1);
        assertEquals(habitEventList.size(), originalSize + 1);
    }

    /**
     * Test replacing a HabitEvent in the HabitEventList.
     */
    @Test
    public void testReplaceHabitEvent() {
        HabitEvent habitEvent = testHabitEvent();
        HabitEvent habitEvent1 = testHabitEvent();
        habitEventList.addHabitEvent(habitEvent);
        assertEquals(habitEventList.size(), originalSize + 1);
        habitEventList.replaceHabitEvent(0, habitEvent1);
        assertEquals(habitEventList.getHabitEvent(0), habitEvent1);
        assertEquals(habitEventList.size(), originalSize + 1);
    }

    /**
     * Test removing a HabitEvent in the HabitEventList.
     */
    @Test
    public void testRemoveHabitEvent() {
        HabitEvent habitEvent = testHabitEvent();
        habitEventList.addHabitEvent(habitEvent);
        assertEquals(habitEventList.size(), originalSize + 1);
        habitEventList.removeHabitEvent(0);
        assertEquals(habitEventList.size(), originalSize);
    }

    /**
     * Test moving a HabitEvent in the HabitEventList.
     */
    @Test
    public void testMoveHabitEvent() {
        HabitEvent habitEvent = testHabitEvent();
        HabitEvent habitEvent1 = testHabitEvent();

        habitEventList.addHabitEvent(habitEvent);
        habitEventList.addHabitEvent(habitEvent1);

        habitEventList.moveHabitEvent(0, 1);

        assertEquals(habitEventList.getHabitEvent(0), habitEvent1);
        assertEquals(habitEventList.getHabitEvent(1), habitEvent);
    }

    /**
     * Test getting the size of the HabitEventList.
     */
    @Test
    public void testSize() {
        assertEquals(habitEventList.size(), originalSize);
        habitEventList.addHabitEvent(testHabitEvent());
        assertEquals(habitEventList.size(), originalSize + 1);
    }

    /**
     * Test getting the next UPID from the HabitEventList.
     */
    @Test
    public void testNextUPID() {
        int originalUPID = habitEventList.nextUPID();
        assertEquals(habitEventList.nextUPID(), originalUPID + 1);
    }

}
