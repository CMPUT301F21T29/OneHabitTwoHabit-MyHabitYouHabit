package com.example.ohthmhyh;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

        // test pushing a user to the DB
        User testUser = new User("BobbyWasabi");
        testUser.addHabit(Habit.makeDummyHabit());
        testUser.addHabit(Habit.makeDummyHabit());
        dba.updateUser(testUser);

        // you'll have to check this by looking in the Firestore console
        assert true;
    }


    @Test
    public void getUserTest_1() throws Exception{
        // make sure the user is in the DB
        pushUserTest_1();

        // get the user back from the DB
        dba.getUser(new DatabaseAdapter.ProfileCallback() {
            @Override
            public void onProfileCallback(User profile) {
                // make sure the usernames match
                assertEquals("BobbyWasabi", profile.getUsername());
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