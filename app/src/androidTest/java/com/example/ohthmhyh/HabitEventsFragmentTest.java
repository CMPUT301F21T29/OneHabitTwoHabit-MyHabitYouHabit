package com.example.ohthmhyh;

import static junit.framework.TestCase.assertTrue;

import static org.junit.Assert.assertFalse;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

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
        mAuth.signInWithEmailAndPassword(Constants.EXISTING_USER_EMAIL, Constants.EXISTING_USER_PASSWORD);
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
        solo.clickOnView(solo.getView(R.id.habit_events_nav_item));
    }

    /**
     * Ensure the button to add a habit event exists.
     * @throws Exception
     */
    @Test
    public void testAddButtonExists() throws Exception {
        solo.sleep(1000);
        // Ensure we are in the MainActivity.
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        // Click on one of the add event button
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.floatingActionButton2));

        // Ensure we are in CreateHabitEvent activity.
        solo.assertCurrentActivity("Wrong activity", CreateHabitEvent.class);
    }

    /**
     * Ensure the dialog to add/edit/view a habit shows up.
     * @throws Exception
     */
    @Test
    public void testAddHabitEventShowsUp() throws Exception {
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.floatingActionButton2));
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

        Thread.sleep(3000);  // Wait for everything to load.

        // Click on one of the add event button
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.floatingActionButton2));

        // Ensure we are in CreateHabitEvent activity.
        solo.assertCurrentActivity("Wrong activity", CreateHabitEvent.class);

        // Set an event using the spinner.
        solo.clickOnView(solo.getView(R.id.AutoCompleteTextviewCE));
        solo.clickOnView(solo.getView(TextView.class, 1));

        // Set a random comment
        solo.enterText((EditText) solo.getView(R.id.Get_a_comment_CE), "TESTING COMMENT");

        // Fetch a location
        solo.clickOnView(solo.getView(R.id.Add_location_button));

        solo.sleep(1000);

        assertTrue(solo.searchText("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA"));

        // Create event
        solo.clickOnView(solo.getView(R.id.button2));

        // Ensure we are in CreateHabitEvent activity.
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.habit_events_nav_item));

        // Check to see if comments is proper
        assertTrue(solo.searchText("TESTING COMMENT"));
        assertTrue(solo.searchText("1600 Amphitheatre Pkwy, Mountain View, CA 94043, USA"));

        // Delete
        View row = solo.getText("Comment: TESTING COMMENT");
        row.getLocationInWindow(location);

        // fail if the view with text cannot be located in the window

        fromX = location[0] + 100;
        fromY = location[1];

        toX = location[0];
        toY = fromY;

        solo.drag(fromX, toX, fromY, toY, 2);

        solo.sleep(1000);

        assertFalse(solo.searchText("Comment: TESTING COMMENT"));
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