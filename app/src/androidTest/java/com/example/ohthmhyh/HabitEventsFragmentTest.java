package com.example.ohthmhyh;

import static junit.framework.TestCase.assertTrue;

import static org.junit.Assert.assertFalse;

import android.app.Instrumentation;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.ohthmhyh.activities.UpdateHabitEventActivity;
import com.example.ohthmhyh.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
        String HABIT_NAME=makeValidHabit();
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
        solo.clickOnView(solo.getView(R.id.checkBox_ht));
        assertTrue(solo.searchText("Enter a comment"));
        assertTrue(solo.searchText("Click image to change"));
        assertTrue(solo.searchText("Change Location"));
        assertTrue(solo.searchText("Done"));
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
        Thread.sleep(2000);  // Wait for everything to load.

        //Make sure we have a valid habit
        String HABIT_NAME=makeValidHabit();



        Thread.sleep(3000);  // Wait for everything to load.

        // Click on the checkbox button
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
        View row2 = solo.getText("Comment: " + comment);
        row2.getLocationInWindow(location);

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
        ResetTest(HABIT_NAME);

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
    /**
     * Realize dragging one view to the position of another view
     * @param fromX start X
     * @param toX end X
     * @param fromY start Y
     * @param toY end Y
     * @throws Exception
     */
    public void clickLongAndDrag(float fromX, float toX, float fromY, float toY) throws Exception {
        //Get the absolute x and y coordinates on the phone screen in the view

        Instrumentation inst=new Instrumentation();
        float xStart=fromX;
        float yStart=fromY;
        float xStop=toX;
        float yStop=toY;

        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        try{
            MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, xStart+10f, yStart+10f, 0);
            inst.sendPointerSync(event);
            //event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, xStart+10f+1.0f, yStart+10f+1.0f, 0);
            //inst.sendPointerSync(event);
            //Thread.sleep(1000);
            //Delay for one second, simulate long press operation
            eventTime = SystemClock.uptimeMillis() + 1000;
            //xStop adds 10 points of coordinates, the View coordinates obtained need to be adjusted slightly according to the actual situation of the application
            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, xStop+10f, yStop+50f, 0);
            inst.sendPointerSync(event);
            eventTime = SystemClock.uptimeMillis() + 1000;
            //Move again a little, if you don't do this, you can't activate the state of the application under test, causing the View to return to its original position after moving
            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, xStop+10f, yStop+10f, 0);
            inst.sendPointerSync(event);
            eventTime = SystemClock.uptimeMillis() + 1000;
            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, xStop+10f, yStop+10f, 0);
            inst.sendPointerSync(event);
        }catch (Exception ignored) {
            // Handle exceptions if necessary
        }
    }
/**
 * This deletes the habit we just made for testing
 * @param  HABIT_NAME the name we need to delete
    */
    protected void ResetTest(String HABIT_NAME){
        int fromX, fromY,toY,toX;
        int[] location = new int[2];

        solo.clickOnView(solo.getView(R.id.habits_nav_item));
        View row = solo.getText(HABIT_NAME);
        row.getLocationInWindow(location);

        // fail if the view with text cannot be located in the window

        fromX = location[0] + 500;
        fromY = location[1];

        toX = location[0]-50;
        toY = fromY;

        solo.drag(fromX, toX, fromY, toY, 2);
        solo.clickOnButton("Yes");
    }
    /**
     * This creates a habit that is always valid for testing
     * @return  HABIT_NAME the name of the habit we made
     */
    private String makeValidHabit(){
        int fromX, fromY,toY,toX;
        int[] location = new int[2];
        solo.clickOnView(solo.getView(R.id.habits_nav_item));
        final String HABIT_NAME = String.valueOf(System.currentTimeMillis() % 10000000);

        solo.clickOnButton("Add a Habit");
        solo.enterText((EditText) solo.getView(R.id.enter_habit_name), HABIT_NAME);
        solo.enterText((EditText) solo.getView(R.id.enter_habit_des), "TESTFORVALIDHABITEVENT");
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

        View row = solo.getText(HABIT_NAME);
        row.getLocationInWindow(location);

        fromX = location[0];
        fromY = location[1];
        toX = location[0];
        toY = 100;
        try {
            clickLongAndDrag(fromX,toX,fromY,toY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        solo.clickOnView(solo.getView(R.id.habits_today_nav_item));
        return HABIT_NAME;
        //end of getting a valid habit
    }
}
