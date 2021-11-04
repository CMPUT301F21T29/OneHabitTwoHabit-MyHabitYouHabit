package com.example.ohthmhyh;


import org.junit.Test;

import static org.junit.Assert.*;

import com.google.gson.internal.Streams;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class is used to run tests on the Habit class
 */
public class HabitUnitTest {

    /**
     * Test building a habit with the empty constructor. Also tests the getters and setters.
     * @throws Exception
     */
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


    /**
     * Test building a habit with the parametrized constructor. Also tests the getters and setters.
     * @throws Exception
     */
    @Test
    public void Init_Habit_2() {

        // compare against this later
        ArrayList<Habit.Days> sched = new ArrayList<Habit.Days>();
        sched.add(Habit.Days.Mon);
        sched.add(Habit.Days.Wed);
        sched.add(Habit.Days.Fri);

        Habit h = new Habit("test name", "test description", LocalDate.now(), sched, true);

        // test the parameters
        assertEquals("test name", h.getName());
        assertEquals("test description", h.getDescription());
        assertEquals(sched, h.getSchedule());
        assertEquals(LocalDate.now().toEpochDay(), h.getStartDate());
        assertEquals(true, h.getIsPrivate());
        assertTrue(h.getSchedule().size() == 3);
        assertTrue(h.getSchedule().containsAll(sched) && sched.containsAll(h.getSchedule()));
    }


    /**
     * Test building a random habit.
     * @throws Exception
     */
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


    /**
     * Test getters and setters not covered by other tests
     * @throws Exception
     */
    @Test
    public void Test_getters_setters() {
        Habit h = new Habit();

        // compare against this later
        ArrayList<Habit.Days> sched = new ArrayList<Habit.Days>();
        sched.add(Habit.Days.Mon);
        sched.add(Habit.Days.Wed);
        sched.add(Habit.Days.Fri);

        // set the parameters
        h.setStartDate(LocalDate.now().toEpochDay());
        h.setSchedule(sched);
        h.scheduleRemoveDay(Habit.Days.Mon);
        h.setIsPrivate(true);

        sched.remove(Habit.Days.Mon);

        // make sure that the getters/setters worked
        assertEquals(LocalDate.now(), h.StartDateAsLocalDate());
        assertEquals(sched, h.getSchedule());
        assertTrue(h.getIsPrivate());
    }

}



// Stream demo immortalized here for posterity. Thanks Alex :)
//    @Test
//    public void test(){
//        ArrayList<Habit> demo = (ArrayList<Habit>) Stream.generate(Habit::makeDummyHabit).limit(10)
//                                                    .peek(habit -> {
//                                                        System.out.println(habit.getName());
//                                                    }).collect(Collectors.toList());
//        System.out.println(demo.toString());
//    }

