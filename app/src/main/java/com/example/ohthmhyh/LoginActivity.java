package com.example.ohthmhyh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
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
                    if (username.length() == 0) {
                        return;
                    }

                    // TODO: Check if username already exists.

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(
                                    LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign uo success! Go to the main activity.
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
            greetingText.setText("To begin, please log in: ");

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

            continueButton.setText("Login");
            switchButton.setText("Don't have an account? Sign up!");
        } else {
            // We are currently using this activity to sign up a non-existing user.
            greetingText.setText("To sign up, please fill out the following fields: ");

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

            continueButton.setText("Sign up");
            switchButton.setText("Already have an account? Login!");
        }
    }
}