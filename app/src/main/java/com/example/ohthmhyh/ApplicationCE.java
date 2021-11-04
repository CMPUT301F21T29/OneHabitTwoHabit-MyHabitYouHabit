package com.example.ohthmhyh;

import android.app.Application;

import java.util.ArrayList;

public class ApplicationCE extends Application {


private static ArrayList<HabitEvent> Habiteventlist=new ArrayList<HabitEvent>();
private static ArrayList<Habit> habitArrayList=new ArrayList<Habit>();


private static int nextId=0;
private static int sumfrequ=0;

public ApplicationCE() {

        }

public static ArrayList<HabitEvent> getHabiteventlist() {
        return Habiteventlist;
        }


    public static ArrayList<Habit> getHablitArrayList() {
        return habitArrayList;
    }

    public static void setHabitList(ArrayList<Habit> medbookList) {
        ApplicationCE.habitArrayList = habitArrayList;
    }


}