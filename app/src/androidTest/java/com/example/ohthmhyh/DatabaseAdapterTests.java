package com.example.ohthmhyh;

import static org.junit.Assert.assertEquals;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


public class DatabaseAdapterTests {

    private FirebaseFirestore db;
    private String UID;
    private DatabaseAdapter dba;
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);


    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

//    /**
//     * Close activity after each test
//     * @throws Exception
//     */
//    @After
//    public void tearDown () throws Exception{
//        solo .finishOpenedActivities() ;
//    }


    @Test
    public void pushUserTest_1() throws Exception{
        dba = new DatabaseAdapter("testUID");
        User testUser = new User("testerFace", "junkPW");
        dba.updateUser(testUser);

        assert true;
    }

    @Test
    public void pushUserTest_2() throws Exception{
        dba = new DatabaseAdapter("testUID");
        User user = dba.getUser();

        assertEquals("testerFace", user.getUsername());
    }

}