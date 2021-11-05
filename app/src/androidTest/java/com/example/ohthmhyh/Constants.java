package com.example.ohthmhyh;

public class Constants {
    // Email, username, and password of a user that should always exist in the database before each
    // test. The credentials of this user should not be used by any actual user using this app.
    public final static String EXISTING_USER_EMAIL = "cjjans@ualberta.ca";
    public final static String EXISTING_USER_USERNAME = "christian";
    public final static String EXISTING_USER_PASSWORD = "password";

    // Email, username, and password of a user that will not exist in the database before any tests.
    // The credentials of this user should not be used by any actual user using this app.
    public final static String NONEXISTENT_USER_EMAIL = "non_existent@gmail.com";
    public final static String NONEXISTENT_USER_USERNAME = "non_existent_username";
    public final static String NONEXISTENT_USER_PASSWORD = "password";
}
