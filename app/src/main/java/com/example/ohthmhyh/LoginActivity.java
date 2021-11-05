package com.example.ohthmhyh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * The login activity of this app. This activity is responsible for logging in an existing user, or
 * signing up a non-existent user.
 *
 * The user should not get past this activity without either have signed into their existing
 * account, or have created a new account. That way, the fragments of the app can freely use the
 * signed-in user and all of their data.
 */
public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private boolean loggingIn = true;  // Keeps track of whether we are logging in a user or signing up a user.

    private Button continueButton;  // Confirms either creation of a new user or authentication of a user.
    private Button switchButton;  // Switches the responsibility of this activity from either signing up or logging in.
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView greetingText;
    private EditText usernameEditText;

    /**
     * The method used to create the LoginActivity.
     * @param savedInstanceState The previous instance state of this activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Used to verify login credentials and/or create a new user with an email and password.
        mAuth = FirebaseAuth.getInstance();

        greetingText = findViewById(R.id.greeting_text);

        continueButton = findViewById(R.id.button_continue);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Don't do anything if the email and password fields are blank.
                if (email.length() == 0 || password.length() == 0) {
                    return;
                }

                if (loggingIn) {
                    // We are logging in an existing user.
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(
                                    LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Login success! Go to the main activity.
                                        Log.d("LoginActivity", "loginUserEmail:success");
                                        goToMainActivity();
                                    } else {
                                        Log.w("LoginActivity", "loginUserEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    // We are signing up a new user.
                    String username = usernameEditText.getText().toString();
                    // tell the user to pick a username if they didn't
                    if (username.length() == 0) {
                        Toast.makeText(getApplicationContext(),"Please pick a username!",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // TODO: Check if username already exists.
                    // only allow signing up if the username is not already in use
                    DatabaseAdapter.checkUsernameExists(username, new DatabaseAdapter.UsernameCheckCallback() {
                        @Override
                        public void onUsernameCheckCallback(boolean usernameExists) {
                            // if the username exists, tell the user to pick another one
                            if(usernameExists){
                                Toast.makeText(getApplicationContext(),"Username already in use!",Toast.LENGTH_SHORT).show();
                            }
                            // else username is good; sign the user up
                            else{
                                mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(
                                        LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // Sign up success! Go to the main activity.
                                                    Log.d("LoginActivity", "createUserEmail:success");
                                                    // TODO: Get current user and set their username.
                                                    goToMainActivity();
                                                } else {
                                                    Log.w("LoginActivity", "createUserEmail:failure", task.getException());
                                                    Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    });

                }
            }
        });

        switchButton = findViewById(R.id.button_switch);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loggingIn = !loggingIn;  // Toggle whether we are logging in or signing up.
                updateResponsibility();
            }
        });

        emailEditText = findViewById(R.id.edit_text_email);

        usernameEditText = findViewById(R.id.edit_text_username);

        passwordEditText = findViewById(R.id.edit_text_password);
    }

    /**
     * The method that is called when the window changes focus. If this LoginActivity has the focus,
     * then it needs to display the functionality it's trying to present (whether login or sign up).
     * @param hasFocus Whether the current window has the focus.
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // Need to put the update responsibility in here so that animation is performed even on the
        // app's startup. If the responsibility update is just done in onCreate, it won't perform
        // the animation won't play.
        if (hasFocus) {
            // Display to the user whether we are trying to log them in or sign them up.
            updateResponsibility();
        }
    }

    /**
     * Called when starting this activity. If the user is signed in, skip the LoginActivity and go
     * straight to the MainActivity.
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Check if the suer is signed in (non-null). If so, go to the main activity.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            goToMainActivity();
        }
    }

    /**
     * Creates and executes an intent to go to the main activity.
     */
    private void goToMainActivity() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
    }

    /**
     * Switch the activity's function from either signing in an existing user or creating a new
     * user.
     */
    private void updateResponsibility() {
        if (loggingIn) {
            // We are currently using this activity to log in an existing.
            greetingText.setText(R.string.login_greeting_edittext_login_greeting);

            // Perform animations in a separate thread. This is for performance enhancement and also
            // to allow the animation to occur after the initial onCreate call.
            new Thread(new Runnable() {
                @Override
                public void run() {
                    float distance = passwordEditText.getY() - usernameEditText.getY();
                    emailEditText.animate().translationY(distance / 2);
                    passwordEditText.animate().translationY(-distance / 2);
                    usernameEditText.animate().alpha(0.0f).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            usernameEditText.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }).start();

            continueButton.setText(R.string.login_continue_button_login);
            switchButton.setText(R.string.login_switch_button_option_to_signup);
        } else {
            // We are currently using this activity to sign up a non-existing user.
            greetingText.setText(R.string.login_greeting_edittext_signup_greeting);

            // Perform animations in a separate thread. This is for performance enhancement and also
            // to allow the animation to occur after the initial onCreate call.
            new Thread(new Runnable() {
                @Override
                public void run() {
                    emailEditText.animate().translationY(0);
                    passwordEditText.animate().translationY(0);
                    usernameEditText.animate().alpha(1.0f).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            usernameEditText.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }).start();

            continueButton.setText(R.string.login_continue_button_signup);
            switchButton.setText(R.string.login_switch_button_option_to_login);
        }
    }
}