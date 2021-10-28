package com.example.ohthmhyh;

import android.app.Application;

import java.util.ArrayList;

public class TestClassStuart extends Application {


private static ArrayList<HabitEvent> Habiteventlist=new ArrayList<HabitEvent>();
private static ArrayList<Habit> Hablitlist=new ArrayList<Habit>();


private static int nextId=0;
private static int sumfrequ=0;

public TestClassStuart() {

        }

public static ArrayList<HabitEvent> getHabiteventlist() {
        return Habiteventlist;
        }


    public static ArrayList<Habit> getHablitList() {
        return Hablitlist;
    }

    public static void setHabitList(ArrayList<Habit> medbookList) {
        TestClassStuart.Hablitlist = Hablitlist;
    }


}