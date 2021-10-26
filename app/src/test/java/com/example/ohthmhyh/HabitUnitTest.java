package com.example.ohthmhyh;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;


public class HabitUnitTest {
    @Test
    public void Init_Habit_1() {
        Habit h = new Habit();

        // compare against this later
        ArrayList<Habit.Days> sched = new ArrayList<Habit.Days>();
        sched.add(Habit.Days.Mon);
        sched.add(Habit.Days.Wed);
        sched.add(Habit.Days.Fri);

        // set the parameters
        h.setName("test name");
        h.setDescription("test description");
        h.setStartDate(LocalDate.now().toEpochDay());
        h.setUHID(2021);
        h.scheduleAddDay(Habit.Days.Mon);
        h.scheduleAddDay(Habit.Days.Wed);
        h.scheduleAddDay(Habit.Days.Wed);
        h.scheduleAddDay(Habit.Days.Fri);

        // test the parameters
        assertEquals("test name", h.getName());
        assertEquals("test description", h.getDescription());
        assertEquals(sched, h.getSchedule());
        assertEquals(LocalDate.now().toEpochDay(), h.getStartDate());
        assertEquals(2021, h.getUHID());
        assertTrue(h.getSchedule().size() == 3);
        assertTrue(h.getSchedule().containsAll(sched) && sched.containsAll(h.getSchedule()));
    }

    @Test
    public void Rand_Habit() {
        Habit h = Habit.makeDummyHabit();

        // look into the output log to make sure stuff was generated
        System.out.println("NAME: " + h.getName());
        System.out.println("DESC: " + h.getDescription());
        System.out.println("SCHEDULE: " + h.getSchedule());
        System.out.println("START DATE: " + h.getStartDate());
        System.out.println("UHID: " + h.getUHID());
        assert(true);
    }

}