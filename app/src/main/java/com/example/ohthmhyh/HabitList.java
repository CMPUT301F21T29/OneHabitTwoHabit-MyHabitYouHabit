package com.example.ohthmhyh;

import java.util.ArrayList;

public class HabitList{

    private ArrayList<Habit> habitList;
    private int UHIDCounter;
    private DatabaseAdapter databaseAdapter = new DatabaseAdapter();

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
        databaseAdapter.pushHabits(this);
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
     * Removes a habit from the list and updates the database with the changes
     * @param index The index of the habit to remove
     */
    public void removeHabit(int index) {
        habitList.remove(index);
        databaseAdapter.pushHabits(this);
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
        databaseAdapter.pushHabits(this);
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
