package com.example.ohthmhyh;

import java.util.ArrayList;

public class HabitList{

    ArrayList<Habit> habitList;


    public HabitList(){
        this.habitList = new ArrayList<>();
    }


    public void setHabitList(ArrayList<Habit> habitList) {
        this.habitList = habitList;
    }


    public ArrayList<Habit> getHabitList() {
        return habitList;
    }


    /**
     * Add a habit to the user
     * @param habit The habit to add to the user
     */
    public void addHabit(User user, Habit habit){
        if(habit.getUHID() == -1) {
            habit.setUHID(user.nextUHID());
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

}
