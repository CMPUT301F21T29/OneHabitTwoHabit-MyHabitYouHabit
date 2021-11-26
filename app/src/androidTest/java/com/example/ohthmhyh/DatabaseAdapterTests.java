package com.example.ohthmhyh;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.ohthmhyh.activities.MainActivity;
import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.database.HabitEventList;
import com.example.ohthmhyh.database.HabitList;
import com.example.ohthmhyh.entities.Habit;
import com.example.ohthmhyh.entities.HabitEvent;
import com.example.ohthmhyh.entities.User;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;


/**
 * This class is used to run tests on the DatabaseAdapter class
 */
public class DatabaseAdapterTests{

    private DatabaseAdapter dba;

    @Rule public ActivityScenarioRule<MainActivity> rule
            = new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Sign in to the existing user before any tests run.
     * @throws Exception
     */
    @BeforeClass
    public static void setUp() throws Exception {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(TestConstants.EXISTING_USER_EMAIL, TestConstants.EXISTING_USER_PASSWORD);
        Thread.sleep(10000);  // Wait for sign in to occur.
    }

    /**
     * Tests pushing a User object into the database. Check the firebase console to
     * make sure that it worked.
     * @throws Exception
     */
    @Test
    public void pushUserTest_1() throws Exception{
        // make a database adapter and force a UID because we're not logged in while testing
        dba = DatabaseAdapter.getInstance();
        // test pushing a user to the DB
        User user = new User(TestConstants.EXISTING_USER_USERNAME);
        dba.pushUser(user);
        // you'll have to check this by looking in the Firestore console
        assert true;
    }


    /**
     * Tests pulling a user object from the Database.
     * @throws Exception
     */
    @Test
    public void pullUserTest_1() throws Exception{
        // make a database adapter and force a UID because we're not logged in while testing
        dba = DatabaseAdapter.getInstance();
        // push a user to the DB
        User user = new User(TestConstants.EXISTING_USER_USERNAME);
        dba.pushUser(user);
        // get the user back from the DB
        dba.pullUser(new DatabaseAdapter.ProfileCallback() {
            @Override
            public void onProfileCallback(User user) {
                // make sure the stuff matches
                assertTrue(TestConstants.EXISTING_USER_USERNAME.equals(user.getUsername()));
            }
        });
    }


    /**
     * Tests pushing a HabitList object to the database. Check the firebase console to
     * make sure that it worked.
     * @throws Exception
     */
    @Test
    public void pushHabitListTest_1() throws Exception{
        // make a database adapter and force a UID because we're not logged in while testing
        dba = DatabaseAdapter.getInstance();
        // push habits into the DB
        HabitList habits = new HabitList();
        habits.addHabit(Habit.makeDummyHabit());
        habits.addHabit(Habit.makeDummyHabit());
        dba.pushHabits(habits);
        // you'll have to check this by looking in the Firestore console
        assert true;
    }


    /**
     * Test pulling a HabitList object from the database.
     * @throws Exception
     */
    @Test
    public void pullHabitListTest_1() throws Exception{
        // make a database adapter and force a UID because we're not logged in while testing
        dba = DatabaseAdapter.getInstance();

        // push habits into the DB
        HabitList habits = new HabitList();
        habits.addHabit(Habit.makeDummyHabit());
        habits.addHabit(Habit.makeDummyHabit());
        dba.pushHabits(habits);

        // make sure the habits we get back from the db match what we pushed
        dba.pullHabits(new DatabaseAdapter.HabitCallback() {
            @Override
            public void onHabitCallback(HabitList hList) {
                assertEquals(habits.getHabit(1).getName(), hList.getHabit(1).getName());
                assertEquals(habits.getHabit(1).getDescription(), hList.getHabit(1).getDescription());
                assertEquals(habits.getHabit(1).getSchedule(), hList.getHabit(1).getSchedule());
            }
        });
    }


    /**
     * Tests pushing a HabitEventList object to the database. Check the firebase console to
     * make sure that it worked.
     * @throws Exception
     */
    @Test
    public void pushHabitEventListTest_1() throws Exception{
        // make a database adapter and force a UID because we're not logged in while testing
        dba = DatabaseAdapter.getInstance();
        // push habits into the DB
        HabitEventList eventList = new HabitEventList();
        eventList.addHabitEvent(new HabitEvent(
            1,
            "event1",
            1.11,
            11.1,
            null,
            1
        ));
        eventList.addHabitEvent(new HabitEvent(
            2,
            "event2",
            2.22,
            22.2,
            null,
            2
        ));
        dba.pushHabitEvents(eventList);
        assert true;
    }


    /**
     * Tests pulling a HabitEventList object from the database.
     * @throws Exception
     */
    @Test
    public void pullHabitEventListTest_1() throws Exception{
        // make a database adapter and force a UID because we're not logged in while testing
        dba = DatabaseAdapter.getInstance();
        // push habits into the DB
        HabitEventList eventList = new HabitEventList();
        eventList.addHabitEvent(new HabitEvent(
                1,
                "event1",
                1.11,
                11.1,
                null,
                1
        ));
        eventList.addHabitEvent(new HabitEvent(
                2,
                "event2",
                2.22,
                22.2,
                null,
                2
        ));
        dba.pushHabitEvents(eventList);
        // make sure the habit events we get back from the db match what we pushed
        dba.pullHabitEvents(new DatabaseAdapter.HabitEventCallback() {
            @Override
            public void onHabitEventCallback(HabitEventList habitEvents) {
                assertEquals(habitEvents.getHabitEvent(0).getComment(),
                        eventList.getHabitEvent(0).getComment());
                assertEquals(habitEvents.getHabitEvent(0).getHabitUHID(),
                        eventList.getHabitEvent(0).getHabitUHID());
                assertEquals(habitEvents.getHabitEvent(0).getLatitude(),
                        eventList.getHabitEvent(0).getLatitude());

                assertEquals(habitEvents.getHabitEvent(1).getComment(),
                        eventList.getHabitEvent(1).getComment());
                assertEquals(habitEvents.getHabitEvent(1).getHabitUHID(),
                        eventList.getHabitEvent(1).getHabitUHID());
                assertEquals(habitEvents.getHabitEvent(1).getLatitude(),
                        eventList.getHabitEvent(1).getLatitude());
            }
        });
    }


    /**
     * Tests getting a UID from a username.
     * @throws Exception
     */
    @Test
    public void testPullUIDFromUsername_1() throws Exception{
        dba = DatabaseAdapter.getInstance();
        dba.pullUIDFromUsername(TestConstants.EXISTING_USER_USERNAME,
            new DatabaseAdapter.UIDCallback() {
                @Override
                public void onUIDCallback(String UID) {
                    Log.e("THE UID IS", UID);
                    assertEquals(UID, "2uxnFdVOeAbPdVHjUASc9FjShpm1");
                }
        });
    }


    /**
     * Tests getting a username from a UID.
     * @throws Exception
     */
    @Test
    public void testGetUsernameFromUID_1() throws Exception{
        dba = DatabaseAdapter.getInstance();
        dba.pullUsernameFromUID("2uxnFdVOeAbPdVHjUASc9FjShpm1",
            new DatabaseAdapter.UsernameCallback() {
                @Override
                public void onUsernameCallback(String username) {
                    Log.e("THE USERNAME IS", username);
                }
            });
    }


    /**
     * Tests sending a friend request
     * @throws Exception
     */
    @Test
    public void testSendFriendRequest_1() throws Exception{
        dba = DatabaseAdapter.getInstance();
        // push a user to the DB
        User user = new User(TestConstants.EXISTING_USER_USERNAME);
        dba.pushUser(user);


    }


    /**
     * Tests the method to check if a username is already taken. This test checks a username
     * which is already taken.
     * @throws Exception
     */
    @Test
    public void checkUsernameExists_1() throws Exception{
        // verify that we can tell if their username is in the database
        dba.checkUsernameExists(TestConstants.EXISTING_USER_USERNAME, new DatabaseAdapter.UsernameCheckCallback() {
            @Override
            public void onUsernameCheckCallback(boolean usernameExists) {
                assertTrue(usernameExists);
            }
        });
    }


    /**
     * Tests the method to check if a username is already taken. This test checks a username
     * which is not already taken.
     * @throws Exception
     */
    @Test
    public void checkUsernameNotExists_1() throws Exception{
        // make sure the user is in the DB
        pushUserTest_1();

        // verify that we can tell if their username is not in the database
        dba.checkUsernameExists(TestConstants.NONEXISTENT_USER_USERNAME, new DatabaseAdapter.UsernameCheckCallback() {
            @Override
            public void onUsernameCheckCallback(boolean usernameExists) {
                assertFalse(usernameExists);
            }
        });
    }

    /**
     * Sign out of the existing user after all tests have run.
     * @throws Exception
     */
    @AfterClass
    public static void tearDown() throws Exception {
        // Sign out of the user.
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
    }

}

