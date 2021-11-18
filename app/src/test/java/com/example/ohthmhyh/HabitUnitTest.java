package com.example.ohthmhyh;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.example.ohthmhyh.entities.Habit;

import java.time.LocalDate;
import java.util.ArrayList;

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
    public void Test_Getters_Setters() {
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


    /**
     * Performs a basic test on the calculateOpportunit() method
     * This is simply a 1 week test
     * @author Matt
     * @throws Exception
     */
    @Test
    public void Test_Opportunity_Basic() {
        Habit h = new Habit();
        ArrayList<Habit.Days> sched = new ArrayList<Habit.Days>();
        sched.add(Habit.Days.Mon);
        sched.add(Habit.Days.Fri);
        h.setSchedule(sched);
        LocalDate startDay = LocalDate.of(2021, 11, 10);
        LocalDate endDay = LocalDate.of(2021, 11, 17);

        int totalOpportunity = h.calculateOpportunity(startDay, endDay);
        assertEquals(2, totalOpportunity);

    }

    /**
     * Performs a more advanced test on the calculateOpportunit() method where the valid range
     * spans multiple weeks, and months
     * @author Matt
     * @throws Exception
     */
    @Test
    public void Test_Opportunity_Advanced() {
        Habit h = new Habit();
        ArrayList<Habit.Days> sched = new ArrayList<Habit.Days>();
        sched.add(Habit.Days.Wed);
        sched.add(Habit.Days.Thu);
        sched.add(Habit.Days.Sat);
        h.setSchedule(sched);
        LocalDate startDay = LocalDate.of(2021, 11, 9);
        LocalDate endDay = LocalDate.of(2021, 12, 10);

        int totalOpportunity = h.calculateOpportunity(startDay, endDay);
        assertEquals(14, totalOpportunity);

    }

    /**
     * Performs a test where the habit's valid range spans the transition between years
     *
     * This test also tests the usage of sunday. This is notable because of date
     * format conversion inside Habit's code revolving around sundays.
     * @author Matt
     * @throws Exception
     */
    @Test
    public void Test_Opportunity_Year_Crossover() {
        Habit h = new Habit();
        ArrayList<Habit.Days> sched = new ArrayList<Habit.Days>();
        sched.add(Habit.Days.Sun);
        sched.add(Habit.Days.Tue);
        sched.add(Habit.Days.Sat);
        h.setSchedule(sched);
        LocalDate startDay = LocalDate.of(2021, 12, 22);
        LocalDate endDay = LocalDate.of(2022, 1, 7);

        int totalOpportunity = h.calculateOpportunity(startDay, endDay);
        assertEquals(6, totalOpportunity);
    }

    /**
     * Tests the edge cases of this function, where the valid testing range begins and ends on a valid
     * habit day.
     *
     * We want to count the starting day, as that was a valid day for which the user could have completed
     *
     * We also want to count the end day. This is a design decision. This means that if you had a 100% score one
     * day, but now it's the next day, your score will be lower than 100 until you complete this item for the day.
     * Then it will be back at 100% after completion.
     *
     * @author Matt
     * @throws Exception
     */
    @Test
    public void Test_Opportunity_Start_End_Edgecase() {
        Habit h = new Habit();
        ArrayList<Habit.Days> sched = new ArrayList<Habit.Days>();
        sched.add(Habit.Days.Thu);
        h.setSchedule(sched);
        LocalDate startDay = LocalDate.of(2021, 12, 16);
        LocalDate endDay = LocalDate.of(2021, 12, 30);

        int totalOpportunity = h.calculateOpportunity(startDay, endDay);
        assertEquals(3, totalOpportunity);

    }

    /**
     * Test the calling and calculation of the getAdherance function
     *
     * While I am testing using h.getAdherence(specificDate), the actual function will always be called
     * in practice using h.getAdherence(LocalDate.now()). I am not using the now function in tests because
     * in the future, the assertions would be incorrect.
     *
     * @author Matt
     * @throws Exception
     */
    @Test
    public void Test_getAdherence() {
        Habit h = new Habit();
        ArrayList<Habit.Days> sched = new ArrayList<Habit.Days>();
        sched.add(Habit.Days.Thu);
        sched.add(Habit.Days.Wed);
        h.setSchedule(sched);

        LocalDate startDate = LocalDate.of(2021, 11, 11);
        h.setStartDate(startDate.toEpochDay());

        assertEquals(0, h.getAdherence(LocalDate.of(2021, 11, 18))); //is zero because we have completed 0/3

        h.logCompleted();
        h.logCompleted();

        double t1 = 2;
        double t2 = 3;

        assertEquals((t1/t2)*100, h.getAdherence(LocalDate.of(2021, 11, 18))); //66.6% because we have completed 2/3

        h.logCompleted();

        assertEquals(100, h.getAdherence(LocalDate.of(2021, 11, 18))); //100% because we have completed 3/3
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

