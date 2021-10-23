package com.example.ohthmhyh;

import android.net.Uri;

import java.util.Date;

public class HabitEvent {
    private Habit habit;
    private String comment;
    private String UUID;
    private String UHID;
    private String Locatoion;
    private Uri resultUri;


    public HabitEvent(Habit habit, String comment,
                       String UUID, String UHID,
                      String Locatoion, Uri resultUri) {
        this.habit = habit;
        this.comment = comment;
        this.UUID = UUID;
        this.UHID = UHID;
        this.Locatoion = Locatoion;
        this.resultUri=resultUri;
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

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getUHID() {
        return UHID;
    }

    public void setUHID(String UHID) {
        this.UHID = UHID;
    }

    public String getLocatoion() {
        return Locatoion;
    }

    public void setLocatoion(String locatoion) {
        Locatoion = locatoion;
    }

    public Uri getResultUri() {
        return resultUri;
    }

    public void setResultUri(Uri resultUri) {
        this.resultUri = resultUri;
    }
}
