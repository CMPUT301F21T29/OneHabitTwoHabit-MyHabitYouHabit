package com.example.ohthmhyh;
<<<<<<< Updated upstream
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.view.View;
import android.widget.EditText;
=======

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
>>>>>>> Stashed changes

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

<<<<<<< Updated upstream
import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

public class HabitFragmentTest {

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
        // TODO: Use constants.
        mAuth.signInWithEmailAndPassword("cjjans@ualberta.ca", "password");
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
        solo.clickOnView(solo.getView(R.id.habits_nav_item));
    }

    /**
     * Ensure the button to add a habit exists.
     * @throws Exception
     */
    @Test
    public void testAddButtonExists() throws Exception {
        solo.sleep(1000);
        assertTrue(solo.searchText("Add a Habit"));
    }

    /**
     * Ensure the dialog to add/edit/view a habit shows up.
     * @throws Exception
     */
    @Test
    public void testAddHabitDialogShowsUp() throws Exception {
        solo.clickOnButton("Add a Habit");
        assertTrue(solo.searchText("Enter a Habit"));
        assertTrue(solo.searchText("Enter descriptions"));
        assertTrue(solo.searchText("Enter a date"));
        assertTrue(solo.searchText("This habit is"));
        assertTrue(solo.searchText("Done"));
        assertTrue(solo.searchText("Cancel"));
    }

    /**
     * Ensure that user input that is too long does not get added.
     * @throws Exception
     */
    @Test
    public void testFormValidation() throws Exception {
        solo.clickOnButton("Add a Habit");
        solo.enterText((EditText) solo.getView(R.id.enter_habit_name), "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        assertTrue(solo.searchText("Too long!"));
        solo.enterText((EditText) solo.getView(R.id.enter_habit_des), "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        assertTrue(solo.searchText("Too long!"));
        solo.clickOnView(solo.getView(android.R.id.button1));  // Click on the "Done" button in the AlertDialog.
        assertTrue(solo.searchText("ENTER A DATE"));
        assertTrue(solo.searchText("(Error: Choose a schedule)"));
    }

    /**
     * Ensure that a user can add and delete a habit. Add and delete so our test user will keep a
     * small amount of actual habits.
     * @throws Exception
     */
    @Test
    public void testAddAndDeleteHabit() throws Exception {
        int fromX, toX, fromY, toY;
        int[] location = new int[2];

        Thread.sleep(3000);  // Wait for everything to load.

        // Define a (relatively) unique name for this habit. It doesn't have to be unique.
        final String HABIT_NAME = String.valueOf(System.currentTimeMillis() % 10000000);

        solo.clickOnButton("Add a Habit");
        solo.enterText((EditText) solo.getView(R.id.enter_habit_name), HABIT_NAME);
        solo.enterText((EditText) solo.getView(R.id.enter_habit_des), "HabitDescription");
        solo.clickOnView(solo.getView(R.id.enter_date));
        solo.clickOnButton("OK");
        solo.clickOnButton("Sun");
        solo.clickOnButton("Tue");
        solo.clickOnButton("PUBLIC");
        solo.clickOnView(solo.getView(android.R.id.button1));  // Click on the "Done" button in the AlertDialog.
        assertTrue(solo.searchText(HABIT_NAME));
        assertTrue(solo.searchText("HabitDescription"));
        assertTrue(solo.searchText("Done"));

        View row = solo.getText(HABIT_NAME);
        row.getLocationInWindow(location);

        // fail if the view with text cannot be located in the window

        fromX = location[0] + 100;
        fromY = location[1];

        toX = location[0];
        toY = fromY;

        solo.drag(fromX, toX, fromY, toY, 2);

        assertFalse(solo.searchText(HABIT_NAME));
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

=======
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.Text;

public class HabitFragmentTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests to create a usable Solo instance for the test and create the existing
     * user if they do not yet exist.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Gets the MainActivity from the rule.
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Tests to see if adding habit works
     * @throws Exception
     */
    @Test
    public void testAddHabit() throws Exception {
        // Ensure we are in the MainActivity.
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        // Move to HabitEvent fragment
        switchToHabitFragment();

        // Click on one of the add event button
        solo.clickOnView((Button) solo.getView(R.id.add_habit));

        // Ensure we are in CreateHabitEvent activity.
        //solo.assertCurrentActivity("Wrong activity", CreateHabitEvent.class);

        // Set a random name
        solo.enterText((EditText) solo.getView(R.id.enter_habit_name), "RANDOM NAME");

        // Set a random description
        solo.enterText((EditText) solo.getView(R.id.enter_habit_des), "RANDOM DESCRIPTION");

        // Set a random start date
        //solo.clickOnView(solo.getView(R.id.enter_date));
        //solo.clickOnText("27");
        //solo.clickOnButton("Ok");

        // Put weekly dates.
        solo.clickOnView((Button) solo.getView(R.id.mon));

        // Create event
        solo.clickOnButton("Add");

        // Check to see if  is name/description is proper
        assertTrue(solo.waitForText("RANDOM NAME"));
        assertTrue(solo.waitForText("RANDOM DESCRIPTION"));
    }


    /**
     * Test to see if it can delete a habit
     * @throws Exception
     */
    @Test
    public void testDeleteEvent() throws Exception {
        // Ensure we are in the MainActivity.
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        // Move to HabitEvent fragment
        switchToHabitFragment();

        // Click on one of the add event button
        solo.clickOnView((Button) solo.getView(R.id.add_habit));

        // Ensure we are in CreateHabitEvent activity.
        //solo.assertCurrentActivity("Wrong activity", CreateHabitEvent.class);

        // Set a random name
        solo.enterText((EditText) solo.getView(R.id.enter_habit_name), "RANDOM NAME");

        // Set a random description
        solo.enterText((EditText) solo.getView(R.id.enter_habit_des), "RANDOM DESCRIPTION");

        // Set a random start date
        //solo.clickOnView(solo.getView(R.id.enter_date));
        //solo.clickOnText("27");
        //solo.clickOnButton("Ok");

        // Put weekly dates.
        solo.clickOnView((Button) solo.getView(R.id.mon));

        // Create event
        solo.clickOnButton("Add");

        // Check to see if  is name/description is proper
        assertTrue(solo.waitForText("RANDOM NAME"));
        assertTrue(solo.waitForText("RANDOM DESCRIPTION"));

        // Delete the view by swiping
        solo.clickOnView(solo.getView(R.id.recycler_view_hf));
        solo.clickOnView(solo.getView(TextView.class, 0));
        TextView view = solo.getView(TextView.class, 0);
        int location[] = new int[2];
        view.getLocationOnScreen(location);
        float f1 = location[0];
        float f2 = location[1];
        PointF pt1 = new PointF(f1, f2);
        PointF pt2 = new PointF(f1+100, f2);
        solo.swipe(pt1, pt1, pt2, pt2);

        // Check to see if  is name/description is proper
        assertFalse(solo.waitForText("RANDOM NAME"));
        assertFalse(solo.waitForText("RANDOM DESCRIPTION"));
    }

    /**
     * Test to see if it can add two events
     * @throws Exception
     */
    @Test
    public void testAddTwoEvent() throws Exception {
        // Ensure we are in the MainActivity.
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        // Move to HabitEvent fragment
        switchToHabitFragment();

        // Click on one of the add event button
        solo.clickOnView((Button) solo.getView(R.id.add_habit));

        // Ensure we are in CreateHabitEvent activity.
        //solo.assertCurrentActivity("Wrong activity", CreateHabitEvent.class);

        // Set a random name
        solo.enterText((EditText) solo.getView(R.id.enter_habit_name), "RANDOM NAME");

        // Set a random description
        solo.enterText((EditText) solo.getView(R.id.enter_habit_des), "RANDOM DESCRIPTION");

        // Set a random start date
        //solo.clickOnView(solo.getView(R.id.enter_date));
        //solo.clickOnText("27");
        //solo.clickOnButton("Ok");

        // Put weekly dates.
        solo.clickOnView((Button) solo.getView(R.id.mon));

        // Create event
        solo.clickOnButton("Add");

        // Check to see if  is name/description is proper
        assertTrue(solo.waitForText("RANDOM NAME"));
        assertTrue(solo.waitForText("RANDOM DESCRIPTION"));

        // Ensure we are in the MainActivity.
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        // Move to HabitEvent fragment
        switchToHabitFragment();

        // Click on one of the add event button
        solo.clickOnView((Button) solo.getView(R.id.add_habit));

        // Ensure we are in CreateHabitEvent activity.
        //solo.assertCurrentActivity("Wrong activity", CreateHabitEvent.class);

        // Set a random name
        solo.enterText((EditText) solo.getView(R.id.enter_habit_name), "EEEE");

        // Set a random description
        solo.enterText((EditText) solo.getView(R.id.enter_habit_des), "YEET");

        // Set a random start date
        //solo.clickOnView(solo.getView(R.id.enter_date));
        //solo.clickOnText("27");
        //solo.clickOnButton("Ok");

        // Put weekly dates.
        solo.clickOnView((Button) solo.getView(R.id.mon));

        // Create event
        solo.clickOnButton("Add");

        // Check to see if  is name/description is proper
        assertTrue(solo.waitForText("EEEE"));
        assertTrue(solo.waitForText("YEET"));
    }

    /**
     * Test case for editing the habit
     * @throws Exception
     */
    @Test
    public void testEditHabit() throws Exception {
        // Ensure we are in the MainActivity.
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        // Move to HabitEvent fragment
        switchToHabitFragment();

        // Click on one of the add event button
        solo.clickOnView((Button) solo.getView(R.id.add_habit));

        // Ensure we are in CreateHabitEvent activity.
        //solo.assertCurrentActivity("Wrong activity", CreateHabitEvent.class);

        // Set a random name
        solo.enterText((EditText) solo.getView(R.id.enter_habit_name), "RANDOM NAME");

        // Set a random description
        solo.enterText((EditText) solo.getView(R.id.enter_habit_des), "RANDOM DESCRIPTION");

        // Set a random start date
        //solo.clickOnView(solo.getView(R.id.enter_date));
        //solo.clickOnText("27");
        //solo.clickOnButton("Ok");

        // Put weekly dates.
        solo.clickOnView((Button) solo.getView(R.id.mon));

        // Create event
        solo.clickOnButton("Add");

        // Check to see if  is name/description is proper
        assertTrue(solo.waitForText("RANDOM NAME"));
        assertTrue(solo.waitForText("RANDOM DESCRIPTION"));

        // Click on the list item
        solo.clickOnText("RANDOM NAME");

        // Set a random name
        solo.enterText((EditText) solo.getView(R.id.enter_habit_name), "RANDOM EHH");

        // Set a random description
        solo.enterText((EditText) solo.getView(R.id.enter_habit_des), "RANDOM AHH");

    }

    /**
     *  Switch to the proper fragment
     */
    private void switchToHabitFragment() {
        solo.clickOnView(solo.getView(R.id.habits_nav_item));
        solo.waitForText("Events", 1, 1000);
    }
>>>>>>> Stashed changes
}
