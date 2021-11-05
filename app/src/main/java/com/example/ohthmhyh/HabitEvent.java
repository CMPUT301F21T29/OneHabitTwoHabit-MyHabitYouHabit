package com.example.ohthmhyh;

import android.graphics.Bitmap;
import android.location.Address;

public class HabitEvent {
    private Habit habit;
    private String comment;
    private Address location;
    private Bitmap BitmapPic;
    private int flag;

    public HabitEvent(){

    }

    public HabitEvent(Habit habit, String comment,
                      Address location, Bitmap BitmapPic, int flag) {
        this.habit = habit;
        this.comment = comment;

        this.location = location;
        //this.BitmapPic=BitmapPic;
        this.flag=flag;
    }

    public Habit getHabit() {
        return habit;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Address getLocation() {
        return location;
    }

    public void setLocation(Address location) {
        this.location = location;
    }

    public Bitmap getBitmapPic() {
        return BitmapPic;
    }

//    public void setBitmapPic(Bitmap bitmapPic) {
//        BitmapPic = bitmapPic;
//    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
