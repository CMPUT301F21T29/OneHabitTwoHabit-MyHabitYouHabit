package com.example.ohthmhyh;

import static org.junit.Assert.assertTrue;

import android.util.Log;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.ohthmhyh.activities.MainActivity;
import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.entities.User;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

public class UserDatabaseTests {

    private DatabaseAdapter dba;

    @Rule
    public ActivityScenarioRule<MainActivity> rule
            = new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Sign in to the existing user before any tests run.
     * @throws Exception
     */
    @BeforeClass
    public static void setUp() throws Exception {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(Constants.EXISTING_USER_EMAIL, Constants.EXISTING_USER_PASSWORD);
        Thread.sleep(10000);  // Wait for sign in to occur.
    }


    /**
     * Tests sending a friend request to another user
     * @throws Exception
     */
    @Test
    public void TestSendFriendRequest_1() throws Exception{
        // push users to the DB
        dba = new DatabaseAdapter();
        User user1 = new User(Constants.EXISTING_USER_USERNAME);
        User user2 = new User(Constants.EXISTING_USER_USERNAME2);
        dba.pushUser(user1);
        dba.pushUser("UID2", user2);

        // send a friend request from user 1 to user 2
        user1.sendFriendRequest(Constants.EXISTING_USER_USERNAME2);
        Thread.sleep(2000);  // Wait for push to finish.

        // make sure that user2 got the friend request.
        // get the UID of user2
        dba.pullUIDFromUsername(Constants.EXISTING_USER_USERNAME2,
            new DatabaseAdapter.UIDCallback() {
                @Override
                public void onUIDCallback(String UID) {
                    // now pull user 2 and make sure they got the username
                    dba.pullUser(UID, new DatabaseAdapter.ProfileCallback() {
                        @Override
                        public void onProfileCallback(User user2) {
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            assertTrue(user2.getFriendRequests().contains(mAuth.getUid()));
                        }
                    });
                }
            });
    }


//    /**
//     * Tests accepting a friend request from another user
//     * @throws Exception
//     */
//    @Test
//    public void TestAcceptFriendRequest_1() throws Exception{
//        // push users to the DB
//        dba = new DatabaseAdapter();
//        User user1 = new User(Constants.EXISTING_USER_USERNAME);
//        User user2 = new User(Constants.EXISTING_USER_USERNAME2);
//        dba.pushUser(user1);
//        dba.pushUser("UID2", user2);
//
//        // send a friend request from user 1 to user 2
//        user1.sendFriendRequest(Constants.EXISTING_USER_USERNAME2);
//        Thread.sleep(2000);  // Wait for push to finish.
//
//        // now user 2 accepts the friend request. To do this, sign in as user 2
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        mAuth.signOut();
//
//        // get the UID of user2
//        dba.pullUIDFromUsername(Constants.EXISTING_USER_USERNAME2,
//                new DatabaseAdapter.UIDCallback() {
//                    @Override
//                    public void onUIDCallback(String UID) {
//                        // now pull user 2 and make sure they got the username
//                        dba.pullUser(UID, new DatabaseAdapter.ProfileCallback() {
//                            @Override
//                            public void onProfileCallback(User user2) {
//                                FirebaseAuth mAuth = FirebaseAuth.getInstance();
//                                assertTrue(user2.getFriendRequests().contains(mAuth.getUid()));
//                            }
//                        });
//                    }
//                });
//    }


    /**
     * Sign out of the existing user after all tests have run.
     * @throws Exception
     */
    @AfterClass
    public static void tearDown() throws Exception {
        // Sign out of the user.
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
    }


}

