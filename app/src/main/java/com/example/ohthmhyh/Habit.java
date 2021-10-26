package com.example.ohthmhyh;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Random;
import java.util.Set;


public class Habit {

    // define days with an enum to avoid mapping days to numbers
    public enum Days {Sun, Mon, Tue, Wed, Thu, Fri, Sat};
    // instance variables of the habit
    private String name;
    private String description;
    private LocalDate startDate;
    private EnumSet<Days> schedule = EnumSet.noneOf(Days.class);


    /**
     * Construct an empty habit. You MUST manually declare all of the instance variables (name,
     * description, start date, and schedule when using this constructor.
     */
    public Habit(){}


    /**
     * Construct an empty habit, supplying all instance variables at instantiation time (now).
     * @param name The name of this habit.
     * @param description The description of this habit.
     * @param startDate The date this habit was started/created.
     * @param schedule The weekdays this habit should be completed on.
     */
    public Habit(String name, String description, LocalDate startDate, EnumSet<Days> schedule){
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.schedule = schedule;
    }


    /**
     * Builds and returns a randomized habit. Useful for testing.
     * @return A randomly created Habit object.
     */
    public static Habit makeDummyHabit(){
        // used for generating random stuff
        String SALTCHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ -_";
        Random myRNG = new Random();

        // build a random name up to 20 characters long
        StringBuilder name = new StringBuilder();
        int nameLength = myRNG.nextInt(20 )+1;
        while(name.length() < nameLength){
            name.append( SALTCHARS.charAt( (int)(myRNG.nextFloat() * SALTCHARS.length())));
        }

        // build a random description up to 30 characters long
        StringBuilder desc = new StringBuilder();
        int descLength = myRNG.nextInt(30 )+1;
        while(desc.length() < descLength){
            desc.append( SALTCHARS.charAt( (int)(myRNG.nextFloat() * SALTCHARS.length())));
        }

        // build a random start date
        LocalDate startDate = LocalDate.ofEpochDay((long)(myRNG.nextFloat() * LocalDate.now().toEpochDay()));

        // build a random schedule
        EnumSet schedule = EnumSet.noneOf(Days.class);
        int seed = myRNG.nextInt(128);
        if((seed & 1<<0) > 0){
            schedule.add(Days.Sun);
        }
        if((seed & 1<<1) > 0){
            schedule.add(Days.Mon);
        }
        if((seed & 1<<2) > 0){
            schedule.add(Days.Tue);
        }
        if((seed & 1<<3) > 0){
            schedule.add(Days.Wed);
        }
        if((seed & 1<<4) > 0){
            schedule.add(Days.Thu);
        }
        if((seed & 1<<5) > 0){
            schedule.add(Days.Fri);
        }
        if((seed & 1<<6) > 0){
            schedule.add(Days.Sat);
        }

        // build and return the habit object
        return new Habit(name.toString() ,desc.toString(), startDate, schedule);
    }


    /**
     * Sets the name of the habit.
     * @param name The name of the habit.
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Gets the name of the habit.
     * @return The name of this habit
     */
    public String getName() {
        return name;
    }


    /**
     * Sets the description of the habit.
     * @param description The description of the habit.
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * Gets the description of the habit.
     * @return  The description of the habit.
     */
    public String getDescription() {
        return description;
    }


    /**
     * Sets the start date of the habit.
     * @param startDate The start date of the habit.
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }


    /**
     * Gets the start date of the habit.
     * @return The start date of the habit.
     */
    public LocalDate getStartDate() {
        return startDate;
    }


    /**
     * Sets the intended completion schedule of the habit.
     * @param schedule The intended completion schedule of the habit.
     */
    public void setSchedule(EnumSet<Days> schedule) {
        this.schedule = schedule;
    }


    /**
     * Gets the intended completion schedule of the habit.
     * @return  The intended completion schedule of the habit.
     */
    public Set<Days> getSchedule() {
        return schedule;
    }


    /**
     * Adds a day into the schedule. No need to check if that day is already in the schedule, you
     * can just try adding the day. Dupes are automatically removed.
     * @param day The day to add to the schedule.
     * @return True if the day was added to the schedule, false if the day was already in the schedule.
     */
    public boolean scheduleAddDay(Days day){
        return schedule.add(day);
    }


    /**
     * Removes a day from the schedule. No need to check if that day exists in the schedule, you
     * can just try removing the day.
     * @param day The day to remove from the schedule.
     * @return True if the day was removed, false if the day was not in the schedule.
     */
    public void scheduleRemoveDay(Days day){
        schedule.remove(day);
    }

    //Added a thing to make a habit a string/ might change
    @Override
    public String toString() {
        return "Habit{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", schedule=" + schedule +
                '}';
    }
}

