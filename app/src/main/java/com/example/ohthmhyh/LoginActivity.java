package com.example.ohthmhyh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    private TextView usernameText;
    private EditText usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Used to verify login credentials and/or create a new user with an email and password.
        mAuth = FirebaseAuth.getInstance();

        greetingText = findViewById(R.id.greeting_text);
        usernameText = findViewById(R.id.text_view_username);
        usernameEditText = findViewById(R.id.edit_text_username);

        continueButton = findViewById(R.id.button_continue);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validInputs = false;
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if ((!email.equals("")) && (!password.equals(""))) {
                    validInputs = true;
                }

                if (loggingIn && validInputs) {
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
                                        // TODO: May want to display more informative errors.
                                        Log.w("LoginActivity", "loginUserEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else if (validInputs) {
                    // We are signing up a new user.
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(
                                    LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign uo success! Go to the main activity.
                                        Log.d("LoginActivity", "createUserEmail:success");
                                        goToMainActivity();
                                    } else {
                                        // TODO: May want to display more informative errors.
                                        Log.w("LoginActivity", "createUserEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Sign up failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {
                    //display error: Inputs are whitespace
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

        passwordEditText = findViewById(R.id.edit_text_password);

        // Display to the user whether we are trying to log them in or sign them up.
        updateResponsibility();
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
            usernameText.setVisibility(View.GONE);
            usernameEditText.setVisibility(View.GONE);
            continueButton.setText("Login");
            switchButton.setText("Don't have an account? Sign up!");
        } else {
            // We are currently using this activity to sign up a non-existing user.
            greetingText.setText("To sign up, please fill out the following fields: ");
            usernameText.setVisibility(View.VISIBLE);
            usernameEditText.setVisibility(View.VISIBLE);
            continueButton.setText("Sign up");
            switchButton.setText("Already have an account? Login!");
        }
    }
}