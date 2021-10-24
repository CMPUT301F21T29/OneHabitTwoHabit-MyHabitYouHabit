package com.example.ohthmhyh;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LoginActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(
            LoginActivity.class, true, true);

    /**
     * Runs before all tests to create a usable Solo instance for the test.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Gets the LoginActivity from the rule.
     * @throws Exception
     */
    @Test
    public void testRuleActivity() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Test the case where no input is given to the login screen.
     * @throws Exception
     */
    @Test
    public void testNoInput() throws Exception {
        // Ensure we are in the LoginActivity.
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        // Click on the continue button without entering any input.
        solo.clickOnView(solo.getView(R.id.button_continue));

        // Ensure we are still in the LoginActivity.
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
    }

    /**
     * Close the LoginActivity after each test.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}
