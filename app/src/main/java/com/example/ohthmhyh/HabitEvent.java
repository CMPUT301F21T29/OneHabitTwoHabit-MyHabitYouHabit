package com.example.ohthmhyh;

import android.graphics.Bitmap;
import android.location.Address;
import android.net.Uri;

import java.util.Date;

public class HabitEvent {
    private Habit habit;
    private String comment;
    private Address Locatoion;
    private Bitmap BitmapPic;
    private int flag;


    public HabitEvent(Habit habit, String comment,
                      Address Locatoion, Bitmap BitmapPic,int flag) {
        this.habit = habit;
        this.comment = comment;

        this.Locatoion = Locatoion;
        this.BitmapPic=BitmapPic;
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

    public Address getLocatoion() {
        return Locatoion;
    }

    public void setLocatoion(Address locatoion) {
        Locatoion = locatoion;
    }

    public Bitmap getBitmapPic() {
        return BitmapPic;
    }

    public void setBitmapPic(Bitmap bitmapPic) {
        BitmapPic = bitmapPic;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
