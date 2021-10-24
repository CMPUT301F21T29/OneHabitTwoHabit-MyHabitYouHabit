package com.example.ohthmhyh;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.EnumSet;


public class HabitUnitTest {
    @Test
    public void Init_Habit_1() {
        Habit h = new Habit();

        // build the parameters
        EnumSet<Habit.Days> sched = EnumSet.noneOf(Habit.Days.class);
        sched.add(Habit.Days.Mon);
        sched.add(Habit.Days.Wed);
        sched.add(Habit.Days.Fri);

        // set the parameters
        h.setSchedule(sched);
        h.setName("test name");
        h.setDescription("test description");
        h.setStartDate(LocalDate.now());

        // test the parameters
        assertEquals("test name1", h.getName());
        assertEquals("test description", h.getDescription());
        assertEquals(sched, h.getSchedule());
        assertEquals(LocalDate.now(), h.getStartDate());
    }

    @Test
    public void Rand_Habit() {
        Habit h = Habit.makeDummyHabit();

        // look into the output log to make sure stuff was generated
        System.out.println("NAME: " + h.getName());
        System.out.println("DESC: " + h.getDescription());
        System.out.println("SCHEDULE: " + h.getSchedule());
        System.out.println("START DATE: " + h.getStartDate());
        assert(true);
    }

}
