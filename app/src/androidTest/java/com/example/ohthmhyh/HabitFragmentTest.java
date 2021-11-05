package com.example.ohthmhyh;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.view.View;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

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
        String habitName = String.valueOf(System.currentTimeMillis() % 10000000);

        solo.clickOnButton("Add a Habit");
        solo.enterText((EditText) solo.getView(R.id.enter_habit_name), habitName);
        solo.enterText((EditText) solo.getView(R.id.enter_habit_des), "HabitDescription");
        solo.clickOnView(solo.getView(R.id.enter_date));
        solo.clickOnButton("OK");
        solo.clickOnButton("Sun");
        solo.clickOnButton("Tue");
        solo.clickOnButton("PUBLIC");
        solo.clickOnView(solo.getView(android.R.id.button1));  // Click on the "Done" button in the AlertDialog.
        assertTrue(solo.searchText("ToBeDeleted"));
        assertTrue(solo.searchText("HabitDescription"));
        assertTrue(solo.searchText("Done"));

        View row = solo.getText(habitName);
        row.getLocationInWindow(location);

        // fail if the view with text cannot be located in the window

        fromX = location[0] + 100;
        fromY = location[1];

        toX = location[0];
        toY = fromY;

        solo.drag(fromX, toX, fromY, toY, 2);

        assertFalse(solo.searchText(habitName));
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
