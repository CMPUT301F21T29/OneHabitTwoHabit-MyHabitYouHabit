package com.example.ohthmhyh;

public class TestConstants {
    // Email, username, and password of a user that should always exist in the database before each
    // test. The credentials of this user should not be used by any actual user using this app.
    public final static String EXISTING_USER_EMAIL = "cjjans@ualberta.ca";
    public final static String EXISTING_USER_USERNAME = "christian";
    public final static String EXISTING_USER_PASSWORD = "password";
    public final static String EXISTING_USER_EMAIL2 = "testusr@gmail.com";
    public final static String EXISTING_USER_USERNAME2 = "testusr";
    public final static String EXISTING_USER_PASSWORD2 = "password";

    // Email, username, and password of a user that will not exist in the database before any tests.
    // The credentials of this user should not be used by any actual user using this app.
    public final static String NONEXISTENT_USER_EMAIL = "non_existent@gmail.com";
    public final static String NONEXISTENT_USER_USERNAME = "non_existent_username";
    public final static String NONEXISTENT_USER_PASSWORD = "password";

    /**
     * The amount of time (in milliseconds) to wait for the user's data (HabitList, HabitEventList,
     * and User objects) upon signing in or loading the app.
     */
    public static final int LOADING_WAIT_TIME_MS = 15000;
}
