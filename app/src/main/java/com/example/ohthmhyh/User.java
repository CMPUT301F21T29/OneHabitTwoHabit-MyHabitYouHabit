package com.example.ohthmhyh;

import java.util.ArrayList;

public class User {
    private String username, password;
    private ArrayList<Habit> habitList;
    private ArrayList<User> friendList;
    // TODO: Some encryption features for the password?
    public User(String username, String password)  {
        this.username = username;
        this.password = password;
        this.habitList = new ArrayList<Habit>();
        this.friendList = new ArrayList<User>();
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // TODO: Implement setHabitList
    public void setHabitList(ArrayList<Habit> habitList) {

    }

    public ArrayList<Habit> getHabitList() {
        return habitList;
    }


}