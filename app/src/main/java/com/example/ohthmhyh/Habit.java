package com.example.ohthmhyh;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;


public class Habit {

    // define days with an enum to avoid mapping days to numbers
    public enum Days {Sun, Mon, Tue, Wed, Thu, Fri, Sat};
    // instance variables of the habit
    private String name;
    private String description;
    private long startDate;
    private ArrayList<Days> schedule = new ArrayList<Days>();
    private int UHID = -1;  // Set as -1 to indicate this Habit does not have a unique habit ID


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
    public Habit(String name, String description, LocalDate startDate, ArrayList<Days> schedule, int UHID){
        this.UHID = UHID;
        this.name = name;
        this.description = description;
        this.startDate = startDate.toEpochDay();
        this.schedule = schedule;
    }


    /**
     * Builds and returns a randomized habit. Useful for testing.
     * @return A randomly created Habit object.
     */
    public static Habit makeDummyHabit(){
        Habit h = new Habit();

        // used for generating random stuff
        String SALTCHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ -_";
        Random myRNG = new Random();

        // build a random name up to 20 characters long
        StringBuilder name = new StringBuilder();
        int nameLength = myRNG.nextInt(20 )+1;
        while(name.length() < nameLength){
            name.append( SALTCHARS.charAt( (int)(myRNG.nextFloat() * SALTCHARS.length())));
        }
        h.setName(name.toString());

        // build a random description up to 30 characters long
        StringBuilder desc = new StringBuilder();
        int descLength = myRNG.nextInt(30 )+1;
        while(desc.length() < descLength){
            desc.append( SALTCHARS.charAt( (int)(myRNG.nextFloat() * SALTCHARS.length())));
        }
        h.setDescription(desc.toString());

        // build a random start date
        LocalDate startDate = LocalDate.ofEpochDay((long)(myRNG.nextFloat() * LocalDate.now().toEpochDay()));
        h.setStartDate(startDate.toEpochDay());

        // build a random schedule
        int seed = myRNG.nextInt(128);
        if((seed & 1<<0) > 0){
            h.scheduleAddDay(Days.Sun);
        }
        if((seed & 1<<1) > 0){
            h.scheduleAddDay(Days.Mon);
        }
        if((seed & 1<<2) > 0){
            h.scheduleAddDay(Days.Tue);
        }
        if((seed & 1<<3) > 0){
            h.scheduleAddDay(Days.Wed);
        }
        if((seed & 1<<4) > 0){
            h.scheduleAddDay(Days.Thu);
        }
        if((seed & 1<<5) > 0){
            h.scheduleAddDay(Days.Fri);
        }
        if((seed & 1<<6) > 0){
            h.scheduleAddDay(Days.Sat);
        }

        int UHID = myRNG.nextInt(2000000000);
        h.setUHID(UHID);

        // return the habit object
        return h;
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
    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }


    /**
     * Gets the start date of the habit as a localDate. You probably want to be using this method.
     * @return The start date of the habit as a LocalDate.
     */
    public LocalDate StartDateAsLocalDate() {
        return LocalDate.ofEpochDay(startDate);
    }


    /**
     * Gets the start date of the habit as the number of days since the unix epoch. Needed for
     * database reasons. You probably don't want to use this elsewhere.
     * @return The start date of the habit as days since the unix epoch.
     */
    public long getStartDate() {
        return startDate;
    }


    /**
     * Sets the intended completion schedule of the habit.
     * @param schedule The intended completion schedule of the habit.
     */
    public void setSchedule(ArrayList<Days> schedule) {
        this.schedule = schedule;
    }


    /**
     * Gets the intended completion schedule of the habit.
     * @return  The intended completion schedule of the habit.
     */
    public ArrayList<Days> getSchedule() {
        return schedule;
    }


    /**
     * Adds a day into the schedule. No need to check if that day is already in the schedule, you
     * can just try adding the day. Dupes are automatically removed.
     * @param day The day to add to the schedule.
     * @return True if the day was added to the schedule, false if the day was already in the schedule.
     */
    public boolean scheduleAddDay(Days day){
        if(!schedule.contains(day)){
            schedule.add(day);
            return true;
        }
        return false;
    }


    /**
     * Removes a day from the schedule. No need to check if that day exists in the schedule, you
     * can just try removing the day.
     * @param day The day to remove from the schedule.
     * @return True if the day was removed, false if the day was not in the schedule.
     */
    public boolean scheduleRemoveDay(Days day){
        return schedule.remove(day);
    }


    /**
     * Sets the habit ID for this user
     * @param UHID the habit ID
     */
    public void setUHID(int UHID){
        this.UHID = UHID;
    }


    /**
     * Gets the habit ID for this user
     * @return the ID fo this habit
     */
    public int getUHID(){
        return this.UHID;
    }

}

