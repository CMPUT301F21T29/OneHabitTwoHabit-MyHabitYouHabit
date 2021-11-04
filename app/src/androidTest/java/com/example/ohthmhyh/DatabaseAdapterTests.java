package com.example.ohthmhyh;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    private static final String EXISTING_USER_EMAIL = "bobby_wasabi@gmail.com";
    private static final String EXISTING_USER_USERNAME = "bobby_wasabi";
    private static final String EXISTING_USER_PASSWORD = "bobby_wasabi_password";

    /**
     * Runs before all tests to create an existing Firebase user that can be used for the tests.
     * This user will be the user that Firebase references.
     * @throws Exception
     */
    @BeforeClass
    public static void setUp() throws Exception {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(
                EXISTING_USER_EMAIL, EXISTING_USER_PASSWORD).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("DatabaseAdapterTests", "Login succeeded");
            }
        });
        // TODO: Find a better way to wait until the user is authenticated.
        while (mAuth.getCurrentUser() == null);  // Wait until the user is authenticated.
    }

    /**
     * Tests pushing a User object into the database. Check the firebase console to
     * make sure that it worked.
     * @throws Exception
     */
    @Test
    public void pushUserTest_1() throws Exception{
        // make a database adapter and force a UID because we're not logged in while testing
        dba = new DatabaseAdapter();
        // test pushing a user to the DB
        User user = new User(EXISTING_USER_USERNAME);
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
        dba = new DatabaseAdapter();
        // push a user to the DB
        User user = new User(EXISTING_USER_USERNAME);
        user.setUPIDCounter(1);
        dba.pushUser(user);
        // get the user back from the DB
        dba.pullUser(new DatabaseAdapter.ProfileCallback() {
            @Override
            public void onProfileCallback(User user) {
                // make sure the stuff matches
                assertTrue(EXISTING_USER_USERNAME.equals(user.getUsername()));
                assertTrue(user.getUPIDCounter() == 1);
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
        dba = new DatabaseAdapter();
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
        dba = new DatabaseAdapter();

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
     * Tests the method to check if a username is already taken. This test checks a username
     * which is already taken.
     * @throws Exception
     */
    @Test
    public void checkUsernameExists_1() throws Exception{
        // verify that we can tell if their username is in the database
        dba.checkUsernameExists(EXISTING_USER_USERNAME, new DatabaseAdapter.UsernameCheckCallback() {
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
        dba.checkUsernameExists("asdf", new DatabaseAdapter.UsernameCheckCallback() {
            @Override
            public void onUsernameCheckCallback(boolean usernameExists) {
                assertFalse(usernameExists);
            }
        });
    }

    /**
     * Sign Firebase out of the created test user and remove the created user from the database.
     * @throws Exception
     */
    @AfterClass
    public static void tearDown() throws Exception {
        // Delete the existing user.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d("asdf", "asdfa");
            user.delete();
        }

        // Sign out of the user.
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
    }

}

