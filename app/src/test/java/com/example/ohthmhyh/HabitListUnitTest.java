package com.example.ohthmhyh;

import com.example.ohthmhyh.database.HabitList;
import com.example.ohthmhyh.entities.Habit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * A unit test to test the functionality of the HabitList object.
 */
public class HabitListUnitTest {

    private HabitList habitList;
    private int originalSize;

    /**
     * Returns a distinct Habit.
     * @return A distinct Habit.
     */
    private static Habit testHabit() {
        return new Habit(
                String.valueOf(Math.random() % Integer.MAX_VALUE),
                String.valueOf(Math.random() % Integer.MAX_VALUE),
                LocalDate.now(),
                new ArrayList<>(),
                true);
    }

    /**
     * Called before each test to set up a HabitList and its size.
     */
    @BeforeEach
    public void setUp() {
        habitList = new HabitList();
        originalSize = habitList.size();
    }

    /**
     * Test adding a Habit to the HabitList.
     */
    @Test
    public void testAddHabit() {
        Habit habit = testHabit();
        habitList.addHabit(habit);
        assertEquals(habitList.size(), originalSize + 1);
    }

    /**
     * Test getting a Habit from the HabitList.
     */
    @Test
    public void testGetHabit() {
        Habit habit = testHabit();
        habitList.addHabit(habit);
        assertEquals(habitList.size(), originalSize + 1);
        Habit habit1 = habitList.getHabit(0);
        assertEquals(habit, habit1);
        assertEquals(habitList.size(), originalSize + 1);
    }

    /**
     * Test setting a Habit in the HabitList.
     */
    @Test
    public void setHabit() {
        Habit habit = testHabit();
        Habit habit1 = testHabit();
        habitList.addHabit(habit);
        habitList.setHabit(0, habit1);
        assertEquals(habitList.getHabit(0), habit1);
    }

    /**
     * Test removing a Habit from the HabitList.
     */
    @Test
    public void removeHabit() {
        Habit habit = testHabit();
        habitList.addHabit(habit);
        assertEquals(habitList.size(), originalSize + 1);
        habitList.removeHabit(0);
        assertEquals(habitList.size(), originalSize);
    }

    /**
     * Test moving a Habit in the HabitList.
     */
    @Test
    public void moveHabit() {
        Habit habit = testHabit();
        Habit habit1 = testHabit();

        habitList.addHabit(habit);
        habitList.addHabit(habit1);

        habitList.moveHabit(0, 1);

        assertEquals(habitList.getHabit(0), habit1);
        assertEquals(habitList.getHabit(1), habit);
    }

    /**
     * Test replacing a Habit in the HabitList.
     */
    @Test
    public void replaceHabit() {
        Habit habit = testHabit();
        Habit habit1 = testHabit();
        habitList.addHabit(habit);
        assertEquals(habitList.size(), originalSize + 1);
        habitList.replaceHabit(0, habit1);
        assertEquals(habitList.getHabit(0), habit1);
        assertEquals(habitList.size(), originalSize + 1);
    }

    /**
     * Test getting the size of the HabitList.
     */
    @Test
    public void testSize() {
        assertEquals(habitList.size(), originalSize);
        habitList.addHabit(testHabit());
        assertEquals(habitList.size(), originalSize + 1);
    }

    /**
     * Test getting the next UHID from the HabitList.
     */
    @Test
    public void nextUHID() {
        int originalUHID = habitList.nextUHID();
        assertEquals(habitList.nextUHID(), originalUHID + 1);
    }

}
