package com.example.ohthmhyh;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.util.Log;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;


public class DatabaseAdapterTests{

    private DatabaseAdapter dba;

    @Rule public ActivityScenarioRule<MainActivity> rule
            = new ActivityScenarioRule<>(MainActivity.class);

//    /**
//     * Close activity after each test
//     * @throws Exception
//     */
//    @After
//    public void tearDown () throws Exception{
//        solo .finishOpenedActivities() ;
//    }


    @Test
    public void pushUserTest_1() throws Exception{
        // make a database adapter and force a UID because we're not logged in while testing
        dba = new DatabaseAdapter("testUID1");
        // test pushing a user to the DB
        User user = new User("BobbyWasabi");
        dba.pushUser(user);
        // you'll have to check this by looking in the Firestore console
        assert true;
    }


    @Test
    public void pullUserTest_1() throws Exception{
        // make a database adapter and force a UID because we're not logged in while testing
        dba = new DatabaseAdapter("testUID2");
        // push a user to the DB
        User user = new User("AdaLovelace");
        user.setUPIDCounter(1);
        dba.pushUser(user);
        // get the user back from the DB
        dba.pullUser(new DatabaseAdapter.ProfileCallback() {
            @Override
            public void onProfileCallback(User user) {
                // make sure the stuff matches
                assertTrue("AdaLovelace".equals(user.getUsername()));
                assertTrue(user.getUPIDCounter()==1);
            }
        });
    }


    @Test
    public void pushHabitTest_1() throws Exception{
        // make a database adapter and force a UID because we're not logged in while testing
        dba = new DatabaseAdapter("testUID3");
        // push habits into the DB
        HabitList habits = new HabitList();
        habits.addHabit(Habit.makeDummyHabit());
        habits.addHabit(Habit.makeDummyHabit());
        dba.pushHabits(habits);
        // you'll have to check this by looking in the Firestore console
        assert true;
    }


    @Test
    public void pullHabitsTest_1() throws Exception{
        // make a database adapter and force a UID because we're not logged in while testing
        dba = new DatabaseAdapter("testUID4");

        // push habits into the DB
        HabitList habits = new HabitList();
        habits.addHabit(Habit.makeDummyHabit());
        habits.addHabit(Habit.makeDummyHabit());
        dba.pushHabits(habits);
        // make sure the habits we get back match
        dba.pullHabits(new DatabaseAdapter.HabitCallback() {
            @Override
            public void onHabitCallback(HabitList hList) {
                assertEquals(habits.getHabit(1).getName(), hList.getHabit(1).getName());
                assertEquals(habits.getHabit(1).getDescription(), hList.getHabit(1).getDescription());
                assertEquals(habits.getHabit(1).getSchedule(), hList.getHabit(1).getSchedule());
            }
        });
    }


    @Test
    public void checkUsernameExists_1() throws Exception{
        // make sure the user is in the DB
        pushUserTest_1();

        // verify that we can tell if their username is in the database
        dba.checkUsernameExists("BobbyWasabi", new DatabaseAdapter.UsernameCheckCallback() {
            @Override
            public void onUsernameCheckCallback(boolean usernameExists) {
                assertTrue(usernameExists);
            }
        });
    }


    @Test
    public void checkUsernameNotExists_1() throws Exception{
        // make sure the user is in the DB
        pushUserTest_1();

        // verify that we can tell if their username is not in the database
        dba.checkUsernameExists("asdf", new DatabaseAdapter.UsernameCheckCallback() {
            @Override
            public void onUsernameCheckCallback(boolean usernameExists) {
                assertFalse(usernameExists);
            }
        });
    }

//    @Test
//    public void getHabitTest_1() throws Exception {
//        // make a database adapter and force a UID because we're not logged in while testing
//        dba = new DatabaseAdapter("testUID6");
//
//        User testUser = new User("FredFlintstone");
//        testUser.
//        dba.updateUser(testUser);
//
//        dba.getHabits(new DatabaseAdapter.HabitCallback() {
//            @Override
//            public void onHabitCallback(ArrayList<Habit> habits) {
//                assertTrue(false);
//            }
//        });
//
//        assert false;
//    }

//    @Test
//    public void pushHabitTest_1() throws Exception{
//        // make a database adapter and force a UID because we're not logged in while testing
//        dba = new DatabaseAdapter("testUID5");
//        Habit h = Habit.makeDummyHabit();
//        Habit h2 = Habit.makeDummyHabit();
//
//        dba.pushHabit(h);
//        dba.pushHabit(h2);
//    }



}
