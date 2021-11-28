package com.example.ohthmhyh.database;

import com.example.ohthmhyh.entities.HabitEvent;

import java.util.ArrayList;

/**
 * The entity class for a the users habit events. This class represents all of the habit events
 * created by the user. Make an instance of this class to represent a collection of habit events.
 *
 * There are no outstanding issues that we are of.
 */
public class HabitEventList {

    private ArrayList<HabitEvent> habitEventList;
    private int UPIDCounter;

    public HabitEventList() {
        habitEventList = new ArrayList<>();
        UPIDCounter = 1; // start at 1. ID 0 is the PFP
    }

    /**
     * Replaces the entire habit event list held by this class
     * @param habitEventList The habit event list
     */
    public void setHabitEventList(ArrayList<HabitEvent> habitEventList) {
        this.habitEventList = habitEventList;
    }

    /**
     * Gets the entire habit event list held by this class
     * @return the entire habit event list
     */
    public ArrayList<HabitEvent> getHabitEventList() {
        return habitEventList;
    }

    /**
     * Add a habit event to the user and updates the database with the changes
     * @param habitEvent The habit event to add to the user
     */
    public void addHabitEvent(HabitEvent habitEvent) {
        habitEventList.add(habitEvent);
    }

    /**
     * Get a specific habit event from the list
     * @param index The index of the habit event to retrieve
     * @return The habit at the specified index
     */
    public HabitEvent getHabitEvent(int index) {
        return habitEventList.get(index);
    }


    /**
     * Replace a habit event at a given position in the list
     * @param index The index of the habit event to replace
     * @param habitEvent The habit to replace the exitsting one
     */
    public void replaceHabitEvent(int index, HabitEvent habitEvent) {
        habitEventList.set(index, habitEvent);
    }


    /**
     * Removes a habit event from the list and updates the database with the changes
     * @param index The index of the habit event to remove
     */
    public void removeHabitEvent(int index) {
        habitEventList.remove(index);
    }


    /**
     * Moves a habit event from one position to another position in the list and updates the
     * database with the changes
     * @param fromIndex The index of the habit
     * @param toIndex The index to put the habit
     */
    public void moveHabitEvent(int fromIndex, int toIndex) {
        HabitEvent habitEventToMove = habitEventList.remove(fromIndex);
        habitEventList.add(toIndex, habitEventToMove);
    }


    /**
     * Returns the number of habit events in the list
     * @return The number of habit events in the list
     */
    public int size() {
        return habitEventList.size();
    }


    /**
     * Set the habit event picture ID counter
     * @param UPIDCounter the number to set the counter to
     */
    public void setUPIDCounter(int UPIDCounter) {
        this.UPIDCounter = UPIDCounter;
    }

    /**
     * Get the habit event picture ID counter for this user
     * @return The value of the UPID counter
     */
    public int getUPIDCounter() {
        return UPIDCounter;
    }

    /**
     * Get the next available habit picture id for this user (auto-increments)
     * @return The next available habit event picture id for the user
     */
    public int nextUPID() {
        return UPIDCounter++;
    }
}
