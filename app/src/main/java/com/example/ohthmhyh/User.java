package com.example.ohthmhyh;

import java.util.ArrayList;

public class User {
    private static String username;
    private static ArrayList<Habit> habitList;
    private static ArrayList<String> friendList;
    private static int UHIDCounter, UPIDCounter;

    // We need this for database reasons.
    public User(){}

    public User(String username)  {
        this.UPIDCounter = 0;
        this.UHIDCounter = 0;
        this.username = username;
        this.habitList = new ArrayList<Habit>();
        this.friendList = new ArrayList<String>();
    }

    /**
     * Getter and Setter for username and password. In the future, users would
     * want to change their password, or reset their password if they
     * forget it. To do so we need access to the username as well.
     * @return String.
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }




    // TODO: Implement setHabitList
    public void setHabitList(ArrayList<Habit> habitList) {

    }

    public ArrayList<Habit> getHabitList() {
        return habitList;
    }


    /**
     * Add a habit to the user
     * @param habit The habit to add to the user
     */
    public void addHabit(Habit habit){
        habit.setUHID(nextUHID());
        habitList.add(habit);
    }


    /**
     * Set the habit ID counter for this user
     * @param UHIDCounter the number to set the counter to
     */
    public void setUHID(int UHIDCounter) {
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
    public void setUPID(int UPIDCounter) {
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

}

