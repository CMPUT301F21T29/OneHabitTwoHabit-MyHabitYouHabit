package com.example.ohthmhyh;

import static org.junit.Assert.assertTrue;

import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.entities.User;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserDatabaseTests {

    private DatabaseAdapter dba = DatabaseAdapter.getInstance();

    /**
     * Sign in to the existing user before any tests run.
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(TestConstants.EXISTING_USER_EMAIL, TestConstants.EXISTING_USER_PASSWORD);
        Thread.sleep(10000);  // Wait for sign in to occur.
    }

    /**
     * Sign in to the existing user before any tests run.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        if (dba.shouldUpdate()) {
            dba.pullAll(new DatabaseAdapter.OnLoadedListener() {
                @Override
                public void onLoaded() {}
            });
            Thread.sleep(TestConstants.LOADING_WAIT_TIME_MS);
        }
    }


    /**
     * Tests sending a friend request to another user
     * @throws Exception
     */
    @Test
    public void TestSendFriendRequest_1() throws Exception{
        // push users to the DB
        dba = DatabaseAdapter.getInstance();
        User user1 = new User(TestConstants.EXISTING_USER_USERNAME);
        User user2 = new User(TestConstants.EXISTING_USER_USERNAME2);
        dba.pushUser(user1);
        dba.pushUser("UID2", user2);

        // send a friend request from user 1 to user 2
        dba.sendUserFriendRequest(TestConstants.EXISTING_USER_USERNAME2);
        Thread.sleep(2000);  // Wait for push to finish.

        // make sure that user2 got the friend request.
        // get the UID of user2
        dba.pullUIDFromUsername(TestConstants.EXISTING_USER_USERNAME2,
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


    /**
     * Tests accepting a friend request from another user
     * @throws Exception
     */
      // TODO: Fix this test.
//    @Test
//    public void TestAcceptFriendRequest_1() throws Exception{
//        // push users to the DB
//        dba = DatabaseAdapter.getInstance();
//        User user1 = new User(TestConstants.EXISTING_USER_USERNAME);
//        User user2 = new User(TestConstants.EXISTING_USER_USERNAME2);
//        dba.pushUser(user1);
//        dba.pushUser("0daedC91Z0dGFwSvNIZoQLqmOlq2", user2); // nasty string is user2's UID
//
//        // send a friend request from user 1 to user 2
//        user1.sendFriendRequest(TestConstants.EXISTING_USER_USERNAME2);
//        Thread.sleep(2000);  // Wait for push to finish.
//
//        // now user 2 accepts the friend request. To do this, sign in as user 2
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        mAuth.signOut();
//        mAuth.signInWithEmailAndPassword(TestConstants.EXISTING_USER_EMAIL2, TestConstants.EXISTING_USER_PASSWORD2);
//        Thread.sleep(5000);  // Wait for sign in to occur.
//
//        // get the UID of user2
//        dba.pullUIDFromUsername(TestConstants.EXISTING_USER_USERNAME2,
//            new DatabaseAdapter.UIDCallback() {
//                @Override
//                public void onUIDCallback(String UID) {
//                    // now pull user 2 and accept the friend request
//                    dba.pullUser(UID, new DatabaseAdapter.ProfileCallback() {
//                        @Override
//                        public void onProfileCallback(User user2) {
//                            // the nasty string is the UID of user1
//                            int index = user2.getFriendRequests().indexOf("2uxnFdVOeAbPdVHjUASc9FjShpm1");
//                            user2.acceptFriendRequest(index);
//                        }
//                    });
//                }
//            });
//
//        Thread.sleep(3000);  // Wait above to finish
//
//        // now user1 should have user2 in their friends list. Nasty string is UID of user1
//        dba.pullUser("2uxnFdVOeAbPdVHjUASc9FjShpm1", new DatabaseAdapter.ProfileCallback() {
//            @Override
//            public void onProfileCallback(User user) {
//                // make sure the UID of user2 is in user1's friend list
//                assertTrue(user.getFriendList().contains("HZVcAii3GcMVgRWyi575m1z6sBK2"));
//            }
//        });
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

