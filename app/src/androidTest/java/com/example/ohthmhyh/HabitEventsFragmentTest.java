package com.example.ohthmhyh;

import static junit.framework.TestCase.assertTrue;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class HabitEventsFragmentTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
        new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void testAddEvent() throws Exception {
        // Ensure we are in the MainActivity.
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        // Move to HabitEvent fragment
        switchToHabitEventFragment();

        // Click on one of the add event button
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.floatingActionButton2));

        // Ensure we are in CreateHabitEvent activity.
        solo.assertCurrentActivity("Wrong activity", CreateHabitEvent.class);

        // Set an event using the spinner.
        solo.clickOnView(solo.getView(R.id.AutoCompleteTextviewCE));
        solo.clickOnView(solo.getView(TextView.class, 0));

        // Set a random comment
        solo.enterText((EditText) solo.getView(R.id.Get_a_comment_CE), "TESTING COMMENT");

        // Fetch a location
        solo.clickOnView(solo.getView(R.id.Add_location_button));

        // Create event
        solo.clickOnView(solo.getView(R.id.button2));

        // Ensure we are in CreateHabitEvent activity.
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        // Move back to HabitEvent fragment
        switchToHabitEventFragment();

        // Check to see if comments is proper
        assertTrue(solo.waitForText("Comment: TESTING COMMENT"));
    }

    @Test
    public void testDeleteEvent() {
        // Ensure we are in the MainActivity.
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        // Move to HabitEvent fragment
        switchToHabitEventFragment();

        // Click on one of the add event button
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.floatingActionButton2));

        // Ensure we are in CreateHabitEvent activity.
        solo.assertCurrentActivity("Wrong activity", CreateHabitEvent.class);

        // Set an event using the spinner.
        solo.clickOnView(solo.getView(R.id.AutoCompleteTextviewCE));
        solo.clickOnView(solo.getView(TextView.class, 0));

        // Set a random comment
        solo.enterText((EditText) solo.getView(R.id.Get_a_comment_CE), "TESTING COMMENT");

        // Fetch a location
        solo.clickOnView(solo.getView(R.id.Add_location_button));

        // Create event
        solo.clickOnView(solo.getView(R.id.button2));

        // Ensure we are in CreateHabitEvent activity.
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        // Move back to HabitEvent fragment
        switchToHabitEventFragment();

        // Check to see if comments is proper
        assertTrue(solo.waitForText("Comment: TESTING COMMENT"));



        //

        //solo.swipe();
    }

    @Test
    public void testAddTwoEvent() {
        // Ensure we are in the MainActivity.
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        // Move to HabitEvent fragment
        switchToHabitEventFragment();

        // Click on one of the add event button
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.floatingActionButton2));

        // Ensure we are in CreateHabitEvent activity.
        solo.assertCurrentActivity("Wrong activity", CreateHabitEvent.class);

        // Set an event using the spinner.
        solo.clickOnView(solo.getView(R.id.AutoCompleteTextviewCE));
        solo.clickOnView(solo.getView(TextView.class, 0));

        // Set a random comment
        solo.enterText((EditText) solo.getView(R.id.Get_a_comment_CE), "TESTING COMMENT");

        // Fetch a location
        solo.clickOnView(solo.getView(R.id.Add_location_button));

        // Create event
        solo.clickOnView(solo.getView(R.id.button2));

        // Ensure we are in CreateHabitEvent activity.
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        // Move back to HabitEvent fragment
        switchToHabitEventFragment();

        // Check to see if comments is proper
        assertTrue(solo.waitForText("Comment: TESTING COMMENT"));

        // Ensure we are in the MainActivity.
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        // Move to HabitEvent fragment
        switchToHabitEventFragment();

        // Click on one of the add event button
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.floatingActionButton2));

        // Ensure we are in CreateHabitEvent activity.
        solo.assertCurrentActivity("Wrong activity", CreateHabitEvent.class);

        // Set an event using the spinner.
        solo.clickOnView(solo.getView(R.id.AutoCompleteTextviewCE));
        solo.clickOnView(solo.getView(TextView.class, 0));

        // Set a random comment
        solo.enterText((EditText) solo.getView(R.id.Get_a_comment_CE), "TESTING COMMENT 2");

        // Fetch a location
        solo.clickOnView(solo.getView(R.id.Add_location_button));

        // Create event
        solo.clickOnView(solo.getView(R.id.button2));

        // Ensure we are in CreateHabitEvent activity.
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        // Move back to HabitEvent fragment
        switchToHabitEventFragment();

        // Check to see if comments is proper
        assertTrue(solo.waitForText("Comment: TESTING COMMENT 2"));
    }

    /**
     *
     */
    private void switchToHabitEventFragment() {
        solo.clickOnView(solo.getView(R.id.habit_events_nav_item));

        solo.waitForText("Events", 1, 1000);
    }

}
