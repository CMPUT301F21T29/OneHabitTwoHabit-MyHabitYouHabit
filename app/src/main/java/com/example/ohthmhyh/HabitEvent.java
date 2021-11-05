package com.example.ohthmhyh;

import android.graphics.Bitmap;
import android.location.Address;
import android.net.Uri;

import java.util.Date;

/**
 * The habitEvent class is used to define a habit event.
 *
 * A habit event is created whenever a user does a habit (for example, if you had a habit stating you
 * want to brush teeth every day, and at 9pm you actually brushed your teeth, you would create a habit event.
 * It is analogous to a post on a social media platform.
 */
public class HabitEvent {
    private Habit habit;
    private String comment;
    private Address Locatoion;
    private Bitmap BitmapPic;
    private int flag;

    /**
     * Constructor to create a new habit event
     * @param habit The habit relating to this event
     * @param comment A comment regarding this event
     * @param Locatoion The location where the event took place
     * @param BitmapPic A photo attached to the habit event post
     * @param flag A status flag regarding editing
     */
    public HabitEvent(Habit habit, String comment,
                      Address Locatoion, Bitmap BitmapPic,int flag) {
        this.habit = habit;
        this.comment = comment;

        this.Locatoion = Locatoion;
        this.BitmapPic=BitmapPic;
        this.flag=flag;
    }


    /**
     * Get the habit relating to this event
     * @return The habit relating to this event
     */
    public Habit getHabit() {
        return habit;
    }

    /**
     * Set the habit for this event
     * @param habit The habit for the event
     */
    public void setHabit(Habit habit) {
        this.habit = habit;
    }

    /**
     * Get the comment on this event
     * @return The comment on this event
     */
    public String getComment() {
        return comment;
    }

    /**
     * Set the comment for this event
     * @param comment The comment for this event
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Get the location where this event occured
     * @return The location of the event
     */
    public Address getLocatoion() {
        return Locatoion;
    }

    /**
     * Set the location where for the event
     * @param locatoion The location for the event
     */
    public void setLocatoion(Address locatoion) {
        Locatoion = locatoion;
    }

    /**
     * Get the photo attached to this event
     * @return The photo (in bitmap form) attached to this event
     */
    public Bitmap getBitmapPic() {
        return BitmapPic;
    }

    /**
     * Set the photo for this event
     * @param bitmapPic The photo for this event
     */
    public void setBitmapPic(Bitmap bitmapPic) {
        BitmapPic = bitmapPic;
    }

    /**
     * Get the flag for this habitEvent
     * @return The flag for this habitEvent
     */
    public int getFlag() {
        return flag;
    }

    /**
     * Set the flag for this habitevent
     * @param flag the flag for this habitEvent
     */
    public void setFlag(int flag) {
        this.flag = flag;
    }
}
