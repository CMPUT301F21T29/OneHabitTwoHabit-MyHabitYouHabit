package com.example.ohthmhyh.database;

import com.example.ohthmhyh.entities.Habit;

import java.util.ArrayList;

/**
 * The entity class for a the users habits. This class represents all of the habits created by the
 * user. Make an instance of this class to represent a collection of habits.
 *
 * There are no outstanding issues that we are aware of.
 */
public class HabitList {

    // instance variables
    private ArrayList<Habit> habitList;
    private int UHIDCounter;

    /**
     * Construct a new HabitList for storing all of the users habits.
     */
    public HabitList(){
        UHIDCounter = 0;
        habitList = new ArrayList<>();
    }

    /**
     * Replaces the entire habit list held by this class
     * @param habitList The entire habit array list
     */
    public void setHabitList(ArrayList<Habit> habitList) {
        this.habitList = habitList;
    }

    /**
     * Gets the entire habit list held by this class
     * @return The entire habit array list
     */
    public ArrayList<Habit> getHabitList() {
        return habitList;
    }

    /**
     * Add a habit to the user and update the database with the changes
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
     * @param index The index of the habit to retrieve
     * @return The habit at the specified index
     */
    public Habit getHabit(int index) {
        return habitList.get(index);
    }


    /**
     * Get the index of a habit in the habitlist
     *
     * This is useful for modifying parameters in a habitList (synced with server) using a non-synced habit
     * @param h the habit which we are trying to match
     * @return index of habit if successful, -1 if not
     */
    public int getHabitIndex(Habit h) {
        for (int i=0; i<habitList.size(); i++) {
            if (habitList.get(i).equals(h)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Set a habit in the list without removing it
     * @param index the index of the habit you want to set
     * @param h the habit you'd like to set
     */
    public void setHabit(int index, Habit h) {
        habitList.set(index, h);
    }

    /**
     * Removes a habit from the list and updates the database with the changes
     * @param index The index of the habit to remove
     */
    public void removeHabit(int index) {
        habitList.remove(index);
    }

    /**
     * Moves a habit from one position to another position in the list and updates the database with
     * the changes
     * @param fromIndex The index of the habit
     * @param toIndex The index to put the habit
     */
    public void moveHabit(int fromIndex, int toIndex) {
        Habit habitToMove = habitList.remove(fromIndex);
        habitList.add(toIndex, habitToMove);
    }

    /**
     * Replace a habit at an index with a new habit instance
     * @param index The index of the habit to replace
     * @param newHabit The habit to replace the old habit with
     */
    public void replaceHabit(int index, Habit newHabit) {
        habitList.remove(index);
        habitList.add(index, newHabit);
    }

    /**
     * Returns the number of habits in the list
     * @return The number of habits in the list
     */
    public int size() {
        return habitList.size();
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
     * @return The value of the UHID counter
     */
    public int getUHIDCounter() {
        return UHIDCounter;
    }

    /**
     * Get the next available habit id for this user (auto-increments)
     * @return The next available habit id for the user
     */
    public int nextUHID() {
        return UHIDCounter++;
    }

}
