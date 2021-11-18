package com.example.ohthmhyh;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.ohthmhyh.activities.LoginActivity;
import com.example.ohthmhyh.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

/**
 * The LoginActivity UI test. Tests valid and invalid user logins and sign ups.
 */
public class LoginActivityTest {

    private Solo solo;

    private static String LOGIN_BUTTON_TEXT;
    private static String SIGNUP_BUTTON_TEXT;

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(
            LoginActivity.class, true, true);

    /**
     * Runs before all tests to create a usable Solo instance for the test and sign out of the
     * existing user.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        // Sign out of the existing user before the test starts so that we don't immediately go to
        // the MainActivity.
        FirebaseAuth.getInstance().signOut();

        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        LOGIN_BUTTON_TEXT = solo.getCurrentActivity().getResources().getString(R.string.login_continue_button_login);
        SIGNUP_BUTTON_TEXT = solo.getCurrentActivity().getResources().getString(R.string.login_continue_button_signup);
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
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);

        // Ensure we are using the login functionality.
        switchToLoginFunctionality();

        // Click on the continue button without entering any input.
        solo.clickOnButton(LOGIN_BUTTON_TEXT);

        // Ensure we are still in the LoginActivity.
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
    }

    /**
     * Test the case where there is only an email inputted on the login screen.
     * @throws Exception
     */
    @Test
    public void testOnlyEmailInput() throws Exception {
        // Ensure we are in the LoginActivity.
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);

        // Ensure we are using the login functionality.
        switchToLoginFunctionality();

        // Enter the email, then press continue.
        solo.enterText((EditText) solo.getView(R.id.edit_text_email), TestConstants.NONEXISTENT_USER_EMAIL);
        solo.clickOnButton(LOGIN_BUTTON_TEXT);

        // Ensure we are still in the LoginActivity.
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
    }

    /**
     * Test the case where there is only a password inputted on the login screen.
     * @throws Exception
     */
    @Test
    public void testOnlyPasswordInput() throws Exception {
        // Ensure we are in the LoginActivity.
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);

        // Ensure we are using the login functionality.
        switchToLoginFunctionality();

        // Enter the password, then press continue.
        solo.enterText((EditText) solo.getView(R.id.edit_text_password), TestConstants.NONEXISTENT_USER_PASSWORD);
        solo.clickOnButton(LOGIN_BUTTON_TEXT);

        // Ensure we are still in the LoginActivity.
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
    }

    /**
     * Test the case where there is only a username inputted on the sign up screen.
     * @throws Exception
     */
    @Test
    public void testOnlyUsernameInput() throws Exception {
        // Ensure we are in the LoginActivity.
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);

        // Ensure we are using the sign up functionality.
        switchToSignupFunctionality();

        // Enter the password, then press continue.
        solo.enterText((EditText) solo.getView(R.id.edit_text_username), TestConstants.NONEXISTENT_USER_USERNAME);
        solo.clickOnButton(SIGNUP_BUTTON_TEXT);

        // Ensure we are still in the LoginActivity.
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
    }

    /**
     * Test invalid user login.
     * @throws Exception
     */
    @Test
    public void testInvalidUserLogin() throws Exception {
        // Ensure we are in the LoginActivity.
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);

        // Ensure we are using the sign up functionality.
        switchToLoginFunctionality();

        // Enter the password, then press continue.
        solo.enterText((EditText) solo.getView(R.id.edit_text_email), TestConstants.NONEXISTENT_USER_EMAIL);
        solo.enterText((EditText) solo.getView(R.id.edit_text_password), TestConstants.NONEXISTENT_USER_PASSWORD);
        solo.clickOnButton(LOGIN_BUTTON_TEXT);

        // Ensure we are still in the LoginActivity.
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);
    }

    /**
     * Test existing user login.
     * @throws Exception
     */
    @Test
    public void testExistingUserLogin() throws Exception {
        // Ensure we are in the LoginActivity.
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);

        // Ensure we are using the login functionality.
        switchToLoginFunctionality();

        // Enter the email and password of the existing user.
        solo.enterText((EditText) solo.getView(R.id.edit_text_email), TestConstants.EXISTING_USER_EMAIL);
        solo.enterText((EditText) solo.getView(R.id.edit_text_password), TestConstants.EXISTING_USER_PASSWORD);
        solo.clickOnButton(LOGIN_BUTTON_TEXT);

        // Ensure we have moved to the MainActivity.
        solo.waitForActivity(MainActivity.class, 20000);
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        FirebaseAuth.getInstance().signOut();
    }

    /**
     * Test valid user sign up.
     * @throws Exception
     */
    @Test
    @Ignore  // NOTE: We have to be able to delete all user data on deletion of a user first.
    public void testValidUserSignUp() throws Exception {
        // Ensure we are in the LoginActivity.
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);

        // Ensure we are using the sign up functionality.
        switchToSignupFunctionality();

        // Enter the email, username, and password of the valid user to sign them up.
        solo.enterText((EditText) solo.getView(R.id.edit_text_email), TestConstants.NONEXISTENT_USER_EMAIL);
        solo.enterText((EditText) solo.getView(R.id.edit_text_username), TestConstants.NONEXISTENT_USER_USERNAME);
        solo.enterText((EditText) solo.getView(R.id.edit_text_password), TestConstants.NONEXISTENT_USER_PASSWORD);
        solo.clickOnButton(SIGNUP_BUTTON_TEXT);

        // Ensure we have moved to the MainActivity with this valid user sign up.
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.delete();
        }
    }

    /**
     * Test that a signed-in user goes automatically to the main activity.
     * @throws Exception
     */
    @Test
    @Ignore
    public void testSignedInUser() throws Exception {
        // TODO: Can't think of a good way to do this right now. Thankfully, this is fairly easy to
        //       test and will be caught if it is not working since this is the usual use of the app
        //       (that is, the user usually does not login/signup when using the app, usually just
        //       once).
    }

    /**
     * Close the LoginActivity after each test and sign out of the user.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        FirebaseAuth.getInstance().signOut();

        solo.finishOpenedActivities();
    }

    /**
     * Switch the LoginActivity to the login functionality by clicking on the button that switches
     * the LoginActivity's functionality if it is not already in the login functionality.
     */
    private void switchToLoginFunctionality() {
        // If the continue button is not letting us login, we must be using the sign up
        // functionality. Click the switch button to switch to the login functionality.
        String continue_button_text = ((Button) solo.getView(R.id.button_continue)).getText().toString();
        solo.waitForText(LOGIN_BUTTON_TEXT, 1, 5000);
        if (!continue_button_text.equals(LOGIN_BUTTON_TEXT)) {
            solo.waitForText(SIGNUP_BUTTON_TEXT, 1, 5000);
            solo.clickOnView(solo.getView(R.id.button_continue));
        }
        solo.waitForText(LOGIN_BUTTON_TEXT, 1, 5000);
    }

    /**
     * Switch the LoginActivity to the signup functionality by clicking on the button that switches
     * the LoginActivity's functionality if it is not already in the signup functionality.
     */
    private void switchToSignupFunctionality() {
        // If the continue button is not letting us signup, we must be using the login
        // functionality. Click the switch button to switch to the signup functionality.
        String continue_button_text = ((Button) solo.getView(R.id.button_continue)).getText().toString();
        solo.waitForText(SIGNUP_BUTTON_TEXT, 1, 5000);
        if (!continue_button_text.equals(SIGNUP_BUTTON_TEXT)) {
            solo.waitForText(LOGIN_BUTTON_TEXT, 1, 5000);
            solo.clickOnView(solo.getView(R.id.button_continue));
        }
        solo.waitForText(SIGNUP_BUTTON_TEXT, 1, 5000);
    }

}
