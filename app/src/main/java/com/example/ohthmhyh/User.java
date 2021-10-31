package com.example.ohthmhyh;

import java.util.ArrayList;

public class User {
    private String username;
    private ArrayList<String> friendList;
    private int UHIDCounter, UPIDCounter;

    // We need this for database reasons.
    public User(){}

    public User(String username)  {
        this.UPIDCounter = 0;
        this.UHIDCounter = 0;
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
     * Set the habit ID counter for this user
     * @param UHIDCounter the number to set the counter to
     */
    public void setUHIDCounter(int UHIDCounter) {
        this.UHIDCounter = UHIDCounter;
    }


    /**
     * Get the habit ID counter for this user
     * @return  the value of the UHID counter
     */
    public int getUHIDCounter() {
        return UHIDCounter;
    }


    /**
     * Get the next available habit id for this user (auto-increments)
     * @return  the next available habit id for the user
     */
    public int nextUHID() {
        return UHIDCounter++;
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

}

