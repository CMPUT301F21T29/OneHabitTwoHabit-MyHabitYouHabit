package com.example.ohthmhyh;

import static junit.framework.TestCase.assertTrue;

import static org.junit.Assert.assertFalse;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.ohthmhyh.activities.UpdateHabitEventActivity;
import com.example.ohthmhyh.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

public class HabitEventsFragmentTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
        new ActivityTestRule<>(MainActivity.class, true, true);

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
        solo.clickOnView(solo.getView(R.id.habits_today_nav_item));
    }

    /**
     * Ensure that clicking on a habit today will make a new habit event
     * @throws Exception
     */
    @Test
    public void testAddButtonExists() throws Exception {
        solo.sleep(1000);
        // Ensure we are in the MainActivity.
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        // Click on the checkmark button
        solo.clickOnView((Button) solo.getView(R.id.checkBox_ht));

        // Ensure we are in UpdateHabitEventActivity activity.
        solo.assertCurrentActivity("Wrong activity", UpdateHabitEventActivity.class);
    }

    /**
     * Ensure the dialog to add/edit/view a habit shows up.
     * @throws Exception
     */
    @Test
    public void testAddHabitEventShowsUp() throws Exception {
        solo.clickOnView((Button) solo.getView(R.id.checkBox_ht));
        assertTrue(solo.searchText("Enter a comment"));
        assertTrue(solo.searchText("Click to add Image"));
        assertTrue(solo.searchText("Add location button"));
        assertTrue(solo.searchText("Create Event"));
    }

    /**
     * Test to see if the app creates and deletes an habit event
     * @throws Exception
     */
    @Test
    public void testAddAndDeleteEvent() throws Exception {
        int fromX, toX, fromY, toY;
        int[] location = new int[2];
        String comment = String.valueOf(System.currentTimeMillis());

        Thread.sleep(3000);  // Wait for everything to load.

        // Click on one of the add event button
        solo.clickOnView((Button) solo.getView(R.id.checkBox_ht));

        // Ensure we are in UpdateHabitEventActivity activity.
        solo.assertCurrentActivity("Wrong activity", UpdateHabitEventActivity.class);


        // Set a random comment
        solo.enterText((EditText) solo.getView(R.id.Get_a_comment_CE), comment);

        // Create event
        solo.clickOnView(solo.getView(R.id.button2));

        // Ensure we are in UpdateHabitEventActivity activity.
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.habit_events_nav_item));

        // Check to see if comments is proper
        assertTrue(solo.searchText(comment));
        //assertTrue(solo.searchText("Mountain View, United States"));

        // Delete
        View row = solo.getText("Comment: " + comment);
        row.getLocationInWindow(location);

        // fail if the view with text cannot be located in the window

        fromX = location[0] + 500;
        fromY = location[1];

        toX = location[0]-50;
        toY = fromY;

        solo.drag(fromX, toX, fromY, toY, 2);

        solo.sleep(1000);
        solo.clickOnButton("Yes");
        solo.sleep(1000);

        assertFalse(solo.searchText("Comment: " + comment));
    }

    /**
     * Check to see when different location of the map is selected
     * @throws Exception
     */
    @Ignore  // Currently unable to click on the map to select a new location in Robotium.
    @Test
    public void testMapUpdate() throws Exception {
        int fromX, toX, fromY, toY;
        int[] location = new int[2];

        Thread.sleep(3000);  // Wait for everything to load.

        // Click on one of the add event button
        solo.clickOnView((Button) solo.getView(R.id.checkBox_ht));

        // Ensure we are in UpdateHabitEventActivity activity.
        solo.assertCurrentActivity("Wrong activity", UpdateHabitEventActivity.class);


        // Set a random comment
        solo.enterText((EditText) solo.getView(R.id.Get_a_comment_CE), "TESTING COMMENT");

        // Fetch a location
        solo.clickOnView(solo.getView(R.id.Add_location_button));

        // Wait for map to popup
        solo.sleep(6000);

        fromX = location[0] + 200;
        fromY = location[1] + 700;

        toX = location[0] + 400;
        toY = fromY + 800;

        solo.drag(fromX, toX, fromY, toY, 2);

        solo.sleep(3000);

        solo.clickLongOnScreen(500, 500, 2);

        solo.sleep(3000);

        solo.clickOnView((Button)solo.getView(R.id.set_location_button));

        solo.sleep(1000);

        assertTrue(solo.searchText("Mountain View, United States"));

        // Create event
        solo.clickOnView(solo.getView(R.id.button2));

        // Ensure we are in UpdateHabitEventActivity activity.
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.habit_events_nav_item));

        // Check to see if comments is proper
        assertTrue(solo.searchText("TESTING COMMENT"));
        assertTrue(solo.searchText("Mountain View, United States"));
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
