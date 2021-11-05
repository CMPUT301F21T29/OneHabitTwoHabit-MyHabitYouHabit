package com.example.ohthmhyh;

import java.util.ArrayList;

/**
 * The entity class for a the users habits. This class represents all of the habits created
 * by the user. Make an instance of this class to represent a collection of habits.
 */
public class HabitList{

    // instance variables
    private ArrayList<Habit> habitList;
    private int UHIDCounter;

    /**
     * Construct a new HabitList for storing all of the users habits.
     */
    public HabitList(){
        UHIDCounter = 0;
        this.habitList = new ArrayList<>();
    }


    /**
     * Replaces the entire habit list held by this class
     * @param habitList The habit habit list
     */
    public void setHabitList(ArrayList<Habit> habitList) {
        this.habitList = habitList;
    }


    /**
     * Gets the entire habit list held by this class
     * @return the entire habit array list
     */
    public ArrayList<Habit> getHabitList() {
        return habitList;
    }


    /**
     * Add a habit to the user
     * @param habit The habit to add to the user
     */
    public void addHabit(Habit habit){
        if(habit.getUHID() == -1) {
            habit.setUHID(nextUHID());
        }
        habitList.add(habit);
    }


    /**
     * Get a specific habit from the list
     * @param index the habit index to retrieve
     */
    public Habit getHabit(int index) {
        return habitList.get(index);
    }


    /**
     * Sorts the list of habits
     */
    public void sort() {
        //TODO: In the future, we need to be able to reorder our habits... I think.
    }


    /**
     * Set the habit ID counter
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

}
