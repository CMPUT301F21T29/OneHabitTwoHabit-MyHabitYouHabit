package com.example.ohthmhyh;

import java.util.ArrayList;

/**
 * The entity class for a user. This class represents a user, and the parameters related
 * to a user (username, friend list, etc.). Make an instance of this class to
 * represent a user of the app.
 */
public class User {
    // instance variables
    private String username;
    private String name;
    private String bio;
    private ArrayList<String> friendList;
    private int UPIDCounter;

    /**
     * Construct a new, empty user. You MUST manually declare all of the instance
     * variables when using this constructor.
     */
    public User(){}


    /**
     * Construct a new user, specifying their username now. Check to make sure
     * that the username is not in use first!
     * @param username The username to give this user.
     */
    public User(String username)  {
        this.UPIDCounter = 0;
        this.username = username;
        this.friendList = new ArrayList<String>();
    }

    /**
     * Get the users username
     * @return String.
     */
    public String getUsername() {
        return username;
    }


    /**
     * Set the users username
     * @return String.
     */
    public void setUsername(String username) {
        this.username = username;
    }


    /**
     * Set the picture ID counter for this user
     * @param UPIDCounter the number to set the counter to
     */
    public void setUPIDCounter(int UPIDCounter) {
        this.UPIDCounter = UPIDCounter;
    }


    /**
     * Get the picture ID counter for this user
     * @return  the value of the UPID counter
     */
    public int getUPIDCounter() {
        return UPIDCounter;
    }


    /**
     * Get the next available picture id for this user (auto-increments)
     * @return  the next available picture id for the user
     */
    public int nextUPID() {
        return UPIDCounter++;
    }


    /**
     * Set the users friend list as a list of UID's
     * @param friends  The users friend list as UID's
     */
    public void setFriendList(ArrayList<String> friends){
        this.friendList = friends;
    }


    /**
     * Get the users friend list as a list of UID's
     * @return  The users friend list as UID's
     */
    public ArrayList<String> getFriendList(){
        return friendList;
    }


    /**
     * Get the biography of the user
     * @return The user bio as string
     */
    public String getBio() {
        return this.bio;
    }


    /**
     * Set the biography of the user
     * @param value Biography set by the user.
     */
    public void setBio(String value) {
        this.bio = value;
    }


    /**
     * Get the name of the user
     * @return name of the user
     */
    public String getName() {
        return this.name;
    }


    /**
     * Set the name of the user
     * @param name the name of the user
     */
    public void setName(String name) {
        this.name = name;
    }

}

