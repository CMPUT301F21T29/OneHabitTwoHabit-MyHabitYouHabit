package com.example.ohthmhyh;

//@author Matt

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.example.ohthmhyh.entities.User;

import java.util.ArrayList;


/**
 * This class is used to run tests on the user class
 */
public class UserTest {


    /**
     * Test creating a user with an empty constructor. Also tests some getters and setters
     * @throws Exception
     */
    @Test
    public void testEmptyConstr() {
        User u = new User();
        String username = "hello";

        //set values
        u.setUsername(username);

        //check that values were set correctly
        assertEquals(u.getUsername(), username);
    }

    /**
     * Test creating a user with a non empty constructor. Also tests some getters
     * @throws Exception
     */
    @Test
    public void testConstr() {
        String username = "hello";
        User u = new User(username);

        //check that values were set correctly
        assertEquals(u.getUsername(), username);
    }


    /**
     * Test all getters and setters for the user class
     * @throws Exception
     */
    @Test
    public void testGettersSetters(){
        //initialize values
        String username = "suh dood";
        String name = "jim halpert";
        String bio = "I like cheese";
        ArrayList<String> friendList = new ArrayList<>();
        friendList.add("Joe");
        friendList.add("Michael");
        friendList.add("Fred");
        friendList.add("Jimmy");
        int UPIDCounter = 242332;

        User u = new User(); //create user

        //set values
        u.setUsername(username);
        u.setName(name);
        u.setBio(bio);
        u.setFriendList(friendList);

        //make sure the newly set values are what they're supposed to be using the getters
        assertEquals(username, u.getUsername());
        assertEquals(name, u.getName());
        assertEquals(bio, u.getBio());
        assertEquals(friendList, u.getFriendList());
    }

}

