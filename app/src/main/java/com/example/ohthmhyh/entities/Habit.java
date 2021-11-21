package com.example.ohthmhyh.entities;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

/**
 * The entity class for a habit. This class represents a habit, and the parameters related
 * to a habit (name, description, start date, etc.). Make an instance of this class to
 * represent a habit.
 */
public class Habit implements Serializable {

    // define days with an enum to avoid mapping days to numbers
    public enum Days {Sun, Mon, Tue, Wed, Thu, Fri, Sat};
    // instance variables of the habit
    private String name;
    private boolean isPrivate;
    private String description;
    private long startDate;
    private ArrayList<Days> schedule = new ArrayList<Days>();
    private int UHID = -1;  // Set as -1 to indicate this Habit does not have a unique habit ID
    private int completedCounter = 0;
    private LocalDate lastTimeCompleted = StartDateAsLocalDate().minusDays(1);



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
    public Habit(String name, String description, LocalDate startDate, ArrayList<Days> schedule, boolean isPrivate){
        this.name = name;
        this.description = description;
        this.startDate = startDate.toEpochDay();
        this.schedule = schedule;
        this.isPrivate = isPrivate;
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

        // set a random habitID
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


    /**
     * Gets the private/public status of a habit.
     * @return Returns true if a habit is private, false otherwise.
     */
    public boolean getIsPrivate() {
        return isPrivate;
    }


    /**
     * Sets the private/public status of a habit.
     * @param isPrivate Set true for a private habit, false for a public habit.
     */
    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }


    /**
     * Gets the string representation of a habit. Useful for testing/debugging.
     * @return The habit as a string
     */
    @Override
    public String toString() {
        return "Habit{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", schedule=" + schedule +
                '}';
    }

    /**
     * Gets the number of times a habit has been successfully completed
     * @return number of times a habit has been successfully completed
     */
    public int getCompletedCounter() {
        return completedCounter;
    }

    /**
     * Sets the number of times a habit has been successfully completed
     * @param completedCounter number of times a habit has been successfully completed
     */
    public void setCompletedCounter(int completedCounter) {
        this.completedCounter = completedCounter;
    }

    /**
     * Increments the completed counter whenever a habit was successfully completed
     */
    public void logCompleted() {
        completedCounter++;
        lastTimeCompleted = LocalDate.now();
    }

    /**
     * Decrements the completed counter
     */
    public void undoCompleted() {completedCounter--;}

    /**
     * Calculate and return the % adherance for the given habit
     * This uses a basic completed / possible calculation.
     *
     * @return the % adherance to this habit
     */
    public double getAdherence(LocalDate today) {
        LocalDate currentDay = today;
        LocalDate startDay = LocalDate.ofEpochDay(startDate);
        Double totalOpportunity = Double.valueOf(calculateOpportunity(startDay, currentDay));

        //if opportunity is zero, return zero
        //Theoretically this will never be called, as the opportunity is the valid days from when the
        //habit started. And if you call it on the same day as creation it will =1.
        if (totalOpportunity == 0) {return 0;}

        return (completedCounter / (totalOpportunity)*100);

    }

    /**
     * Given a starting date, end date, and a given habit's scedule, find how many days for which this
     * habit was applicable. This is used to calculate an adherence score.
     * @author Matt
     * @param from The starting date
     * @param to the ending date
     * @return the number of days where the user had the opportunity to successfully complete the habit's task
     */
    public int calculateOpportunity(LocalDate from, LocalDate to) {
        int opportunity = 0;
        //iterate through the days, and see if it falls on an applicable day of the week
        for (LocalDate date = from; date.isBefore(to.plusDays(1)); date = date.plusDays(1)) {
            int DOWjav = date.getDayOfWeek().getValue(); //note that mon is 1 in this convention, sun is 7
            int DOW = DOWjav % 7; //this is the convention our code uses, where sun is 0, and sat is 6

            if (schedule.contains(Days.values()[DOW])) {
                opportunity++;
            }
        }
        return opportunity;
    }

    /**
     * Returns true if the Habit is supposed to be done today, false otherwise.
     * @return true if the Habit is supposed to be done today, false otherwise.
     */
    public boolean isDueToday() {
        LocalDate today = LocalDate.now();
        int dayOfWeekIndex = today.getDayOfWeek().getValue() % 7;
        return (today.isAfter(StartDateAsLocalDate().minusDays(1)) &&
                getSchedule().contains(Habit.Days.values()[dayOfWeekIndex]));
    }

    public boolean isCompletedToday() {
        System.out.println("dqwdwad"+lastTimeCompleted.equals(LocalDate.now()));
        return (lastTimeCompleted.equals(LocalDate.now()));
    }
}

