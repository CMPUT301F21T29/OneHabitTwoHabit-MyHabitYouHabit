package com.example.ohthmhyh;

import android.app.Activity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LoginActivityTest {

    private Solo solo;

    // Email, username, and password of a user that will always exist in the database before each
    // test. The credentials of this user should not be used by any actual user using this app.
    // Hopefully these won't be used.
    // TODO: Any way to enforce that this user will not be created by any actual users?
    private static final String EXISTING_USER_EMAIL = "come_to_del_taco_they_have_fre_shavaca_do@gmail.com";
    private static final String EXISTING_USER_USERNAME = "come_to_del_taco_they_have_fre_shavaca_do";
    private static final String EXISTING_USER_PASSWORD = "fre_sha_vaca_do";

    // Email, username, and password of a user that will not exist in the database before any tests.
    // The credentials of this user should not be used by any actual user using this app. Hopefully
    // these won't be used.
    // TODO: Any way to enforce that this user will not be created by any actual users?
    private static final String TEMP_USER_EMAIL = "back_at_it_again_at_krispy_kreme@gmail.com";
    private static final String TEMP_USER_USERNAME = "back_at_it_again_at_krispy_kreme";
    private static final String TEMP_USER_PASSWORD = "krispy_kreme";

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(
            LoginActivity.class, true, true);

    /**
     * Runs before all tests to create a usable Solo instance for the test and create the existing
     * user if they do not yet exist.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        // Create the user that should be existing before each test begins. If the user already
        // exists, that's fine. Firebase will take care of that.
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(EXISTING_USER_EMAIL, EXISTING_USER_PASSWORD);

        // Sign out of the existing user before the test starts.
        FirebaseAuth.getInstance().signOut();
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
        solo.clickOnView(solo.getView(R.id.button_continue));

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
        solo.enterText((EditText) solo.getView(R.id.edit_text_email), TEMP_USER_EMAIL);
        solo.clickOnView(solo.getView(R.id.button_continue));

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
        solo.enterText((EditText) solo.getView(R.id.edit_text_password), TEMP_USER_PASSWORD);
        solo.clickOnView(solo.getView(R.id.button_continue));

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
        solo.enterText((EditText) solo.getView(R.id.edit_text_username), TEMP_USER_USERNAME);
        solo.clickOnView(solo.getView(R.id.button_continue));

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
        solo.enterText((EditText) solo.getView(R.id.edit_text_email), TEMP_USER_EMAIL);
        solo.enterText((EditText) solo.getView(R.id.edit_text_password), TEMP_USER_PASSWORD);
        solo.clickOnView(solo.getView(R.id.button_continue));

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
        solo.enterText((EditText) solo.getView(R.id.edit_text_email), EXISTING_USER_EMAIL);
        solo.enterText((EditText) solo.getView(R.id.edit_text_password), EXISTING_USER_PASSWORD);
        solo.clickOnView(solo.getView(R.id.button_continue));

        // Ensure we have moved to the MainActivity.
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
    }

    /**
     * Test valid user sign up.
     * @throws Exception
     */
    @Test
    public void testValidUserSignUp() throws Exception {
        // Ensure we are in the LoginActivity.
        solo.assertCurrentActivity("Wrong activity", LoginActivity.class);

        // Ensure we are using the sign up functionality.
        switchToSignupFunctionality();

        // Enter the email, username, and password of the valid user to sign them up.
        solo.enterText((EditText) solo.getView(R.id.edit_text_email), TEMP_USER_EMAIL);
        solo.enterText((EditText) solo.getView(R.id.edit_text_username), TEMP_USER_USERNAME);
        solo.enterText((EditText) solo.getView(R.id.edit_text_password), TEMP_USER_PASSWORD);
        solo.clickOnView(solo.getView(R.id.button_continue));

        // Ensure we have moved to the MainActivity with this valid user sign up.
        solo.assertCurrentActivity("Wrong activity", MainActivity.class);
    }

    /**
     * Test that a signed-in user goes automatically to the main activity.
     * @throws Exception
     */
    @Test
    public void testSignedInUser() throws Exception {
        // TODO: Can't think of a good way to do this right now. Thankfully, this is fairly easy to
        //       test and will be caught if it is not working since this is the usual use of the app
        //       (that is, the user usually does not login/signup when using the app, usually just
        //       once).
    }

    /**
     * Close the LoginActivity after each test.
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        // Delete the account of the valid user that may have been used.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("LoginActivityTest", "User account deleted.");
                            }
                        }
                    });
        }
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        solo.finishOpenedActivities();
    }

    private void switchToLoginFunctionality() {
        // If the continue button is not letting us login, we must be using the sign up
        // functionality. Click the switch button to switch to the login functionality.
        String continue_button_text = ((Button) solo.getView(R.id.button_continue)).getText().toString();
        String login_text = solo.getCurrentActivity().getResources().getString(R.string.login_continue_button_login);
        if (!continue_button_text.equals(login_text)) {
            solo.clickOnView(solo.getView(R.id.button_switch));
        }
        solo.waitForText(login_text, 1, 1000);
    }

    private void switchToSignupFunctionality() {
        // If the continue button is not letting us signup, we must be using the login
        // functionality. Click the switch button to switch to the signup functionality.
        String continue_button_text = ((Button) solo.getView(R.id.button_continue)).getText().toString();
        String signup_text = solo.getCurrentActivity().getResources().getString(R.string.login_continue_button_signup);
        if (!continue_button_text.equals(signup_text)) {
            solo.clickOnView(solo.getView(R.id.button_switch));
        }
        solo.waitForText(signup_text, 1, 1000);
    }

}
