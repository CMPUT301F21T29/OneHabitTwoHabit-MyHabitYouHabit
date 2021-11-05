package com.example.ohthmhyh;

import java.util.ArrayList;

/**
 * The entity class for a the users habit events. This class represents all of the habit events
 * created by the user. Make an instance of this class to represent a collection of habit events.
 */
public class HabitEventList {

    private ArrayList<HabitEvent> habitEventList;
    private DatabaseAdapter databaseAdapter = new DatabaseAdapter();

    public HabitEventList() {
        habitEventList = new ArrayList<>();
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
        databaseAdapter.pushHabitEvents(this);
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
        databaseAdapter.pushHabitEvents(this);
    }


    /**
     * Removes a habit event from the list and updates the database with the changes
     * @param index The index of the habit event to remove
     */
    public void removeHabitEvent(int index) {
        habitEventList.remove(index);
        databaseAdapter.pushHabitEvents(this);
    }


    /**
     * Moves a habit event from one position to another position in the list and updates the
     * database with the changes
     * @param fromIndex The index of the habit
     * @param toIndex The index to put the habit
     */
    public void moveHabit(int fromIndex, int toIndex) {
        HabitEvent habitEventToMove = habitEventList.remove(fromIndex);
        habitEventList.add(toIndex, habitEventToMove);
        databaseAdapter.pushHabitEvents(this);
    }


    /**
     * Returns the number of habit events in the list
     * @return The number of habit events in the list
     */
    public int size() {
        return habitEventList.size();
    }
}
