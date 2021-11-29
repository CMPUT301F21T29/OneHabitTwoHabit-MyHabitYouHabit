package com.example.ohthmhyh;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.ohthmhyh.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDate;

public class HabitTodayFragmentTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(
            MainActivity.class, true, true);

    /**
     * Sign in with the existing user so it can be used for all tests.
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(TestConstants.EXISTING_USER_EMAIL, TestConstants.EXISTING_USER_PASSWORD);
        Thread.sleep(10000);  // Wait for sign in to occur.
    }

    /**
     * Create the solo instance to be used to interact with the activity for all tests and set it to
     * navigate to the habits fragment.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.sleep(TestConstants.LOADING_WAIT_TIME_MS);
        solo.clickOnView(solo.getView(R.id.habits_today_nav_item));
    }

    /**
     * Ensure the button to add a habit does not exists.
     * @throws Exception
     */
    @Test
    public void testAddButtonNotExist() throws Exception {
        solo.sleep(1000);
        assertFalse(solo.searchText("Add a Habit"));
    }


    /**
     * Ensure that a user cannot edit, nor delete a habit from the habit today window
     * @throws Exception
     */
    @Test
    public void testEditAndDeleteHabitToday() throws Exception {
        int fromX, toX, fromY, toY;
        int[] location = new int[2];

        Thread.sleep(3000);  // Wait for everything to load.

        // Define a (relatively) unique name for this habit. It doesn't have to be unique.
        final String HABIT_NAME = String.valueOf(System.currentTimeMillis() % 10000000);

        //Create a habit valid every day
        solo.clickOnView(solo.getView(R.id.habits_nav_item));
        solo.clickOnButton("Add a Habit");
        solo.enterText((EditText) solo.getView(R.id.enter_habit_name), HABIT_NAME);
        solo.enterText((EditText) solo.getView(R.id.enter_habit_des), "HabitDescription");
        solo.clickOnView(solo.getView(R.id.enter_date));
        solo.clickOnButton("OK");
        solo.clickOnButton("Sun");
        solo.clickOnButton("Mon");
        solo.clickOnButton("Tue");
        solo.clickOnButton("Wed");
        solo.clickOnButton("Thu");
        solo.clickOnButton("Fri");
        solo.clickOnButton("Sat");
        solo.clickOnButton("Yes, publicly");
        solo.clickOnButton("Done");
        assertTrue(solo.searchText(HABIT_NAME));
        assertTrue(solo.searchText("HabitDescription"));


        //make sure we cannot drag to delete it in the Habits Today Window
        solo.clickOnView(solo.getView(R.id.habits_today_nav_item));
        assertTrue(solo.searchText(HABIT_NAME));
        View row = solo.getText(HABIT_NAME);
        row.getLocationInWindow(location);
        // fail if the view with text cannot be located in the window
        fromX = location[0] + 100;
        fromY = location[1];
        toX = location[0];
        toY = fromY;
        solo.drag(fromX, toX, fromY, toY, 2);
        assertTrue(solo.searchText(HABIT_NAME));

        //Make sure we cannot edit it
        solo.clickOnText(HABIT_NAME);
        assertFalse(solo.searchText("Enter the title here"));

        //delete the habit
        solo.clickOnView(solo.getView(R.id.habits_nav_item));
        assertTrue(solo.searchText(HABIT_NAME));
        row = solo.getText(HABIT_NAME);
        row.getLocationInWindow(location);
        // fail if the view with text cannot be located in the window
        fromX = location[0] + 100;
        fromY = location[1];
        toX = location[0];
        toY = fromY;
        solo.drag(fromX, toX, fromY, toY, 2);
        Thread.sleep(2000);  // Wait for everything to load.
        solo.clickOnView(solo.getView(android.R.id.button1));
        assertFalse(solo.searchText(HABIT_NAME));
    }

    /**
     * Make sure that habit today only shows habits due today
     * @throws Exception
     */
    @Test
    public void testHabitTodayDays() throws Exception {
        Thread.sleep(3000);  // Wait for everything to load.

        //Make 7 habits, one for every day of the week
        solo.clickOnView(solo.getView(R.id.habits_nav_item));
        String[] Titles = {"Sunday Test", "Monday Test", "Tuesday Test", "Wednesday Test", "Thursday Test", "Friday Test", "Saturday Test"};
        String[] Days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i=0; i<Titles.length; i++) {
            solo.clickOnButton("Add a Habit");
            solo.enterText((EditText) solo.getView(R.id.enter_habit_name), Titles[i]);
            solo.enterText((EditText) solo.getView(R.id.enter_habit_des), "Testing " + Days[i]);
            solo.clickOnView(solo.getView(R.id.enter_date));
            solo.clickOnButton("OK");
            solo.clickOnButton(Days[i]);
            solo.clickOnButton("Yes, publicly");
            solo.clickOnButton("Done");
            assertTrue(solo.searchText(Titles[i]));
        }


        //Make sure only the correct habits show up in habits Today
        solo.clickOnView(solo.getView(R.id.habits_today_nav_item));
        Thread.sleep(1000);  // Wait for everything to load.
        LocalDate date = LocalDate.now();
        int DOWjav = date.getDayOfWeek().getValue(); //note that mon is 1 in this convention, sun is 7
        int DOW = DOWjav % 7; //this is the convention our code uses, where sun is 0, and sat is 6
        for (int i=0; i<Titles.length; i++) {
            if (i == DOW) {assertTrue(solo.searchText(Titles[i])); }
            else {assertFalse(solo.searchText(Titles[i])); }
        }



        //Delete all 7 habits
        int fromX, toX, fromY, toY;
        int[] location = new int[2];
        View row;
        solo.clickOnView(solo.getView(R.id.habits_nav_item));
        for (int i=0; i<Titles.length; i++) {
            Thread.sleep(2000);  // Wait for everything to load.
            assertTrue(solo.searchText(Titles[i]));
            row = solo.getText(Titles[i]);
            row.getLocationInWindow(location);
            // fail if the view with text cannot be located in the window
            fromX = location[0] + 100;
            fromY = location[1];
            toX = location[0];
            toY = fromY;
            solo.drag(fromX, toX, fromY, toY, 3);
            Thread.sleep(2000);  // Wait for everything to load.
            solo.clickOnView(solo.getView(android.R.id.button1));
            assertFalse(solo.searchText(Titles[i]));
        }
    }


    /**
     * Make sure that habit today Doesn't show events starting in the future
     * @throws Exception
     */
    @Test
    public void testHabitTodayInvalidDays() throws Exception {
        Thread.sleep(3000);  // Wait for everything to load.
        LocalDate date;
        String title = "Invalid Day Test";

        //Make a habit due today, starting in the future.
        solo.clickOnView(solo.getView(R.id.habits_nav_item));
        String[] Days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        solo.clickOnButton("Add a Habit");
        solo.enterText((EditText) solo.getView(R.id.enter_habit_name), title);
        solo.enterText((EditText) solo.getView(R.id.enter_habit_des), "Testing " + title);
        solo.clickOnView(solo.getView(R.id.enter_date));

        //Select date 1 week from today
        LocalDate newDate = LocalDate.now().plusDays(7);
        solo.setDatePicker(0, newDate.getYear(), newDate.getMonthValue()-1, newDate.getDayOfMonth());

        solo.clickOnButton("OK");
        date = LocalDate.now();
        int DOWjav = date.getDayOfWeek().getValue(); //note that mon is 1 in this convention, sun is 7
        int DOW = DOWjav % 7; //this is the convention our code uses, where sun is 0, and sat is 6

        solo.clickOnButton(Days[DOW]);
        solo.clickOnButton("Yes, publicly");
        solo.clickOnButton("Done");
        assertTrue(solo.searchText(title));


        //Make sure no habits show up in habits Today (since they all start in the future)
        solo.clickOnView(solo.getView(R.id.habits_today_nav_item));
        Thread.sleep(1000);  // Wait for everything to load.
        assertFalse(solo.searchText(title));



        //Delete the habit
        int fromX, toX, fromY, toY;
        int[] location = new int[2];
        View row;
        solo.clickOnView(solo.getView(R.id.habits_nav_item));
        Thread.sleep(2000);  // Wait for everything to load.
        assertTrue(solo.searchText(title));
        row = solo.getText(title);
        row.getLocationInWindow(location);
        // fail if the view with text cannot be located in the window
        fromX = location[0] + 100;
        fromY = location[1];
        toX = location[0];
        toY = fromY;
        solo.drag(fromX, toX, fromY, toY, 3);
        Thread.sleep(2000);  // Wait for everything to load.
        solo.clickOnView(solo.getView(android.R.id.button1));
        assertFalse(solo.searchText(title));
    }


    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
    }

}
