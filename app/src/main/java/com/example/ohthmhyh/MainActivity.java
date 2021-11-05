package com.example.ohthmhyh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FeedFragment feedFragment = new FeedFragment();
    private HabitsFragment habitsFragment = new HabitsFragment();
    private HabitsTodayFragment habitsTodayFragment = new HabitsTodayFragment();
    private HabitEventsFragment habitEventsFragment = new HabitEventsFragment();
    private UserFragment userFragment = new UserFragment();

    /**
     * Called to create the main activity.
     * @param savedInstanceState
     *      Information about the previous state of the main activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get the navigation bar in the MainActivity and specify which fragments it should go to
        // when the buttons in the navigation bar are tapped. Also, set the default fragment to be
        // shown as the "Habit Today" fragment.
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.feed_nav_item:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, feedFragment).commit();
                        return true;
                    case R.id.habits_nav_item:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, habitsFragment).commit();
                        return true;
                    case R.id.habits_today_nav_item:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, habitsTodayFragment).commit();
                        return true;
                    case R.id.habit_events_nav_item:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, habitEventsFragment).commit();
                        return true;
                    case R.id.user_nav_item:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, userFragment).commit();
                        return true;
                }

                return false;
            }
        });  // Determine which fragment to navigate to when the navigation bar is tapped.

        //this is used in createhabitevent so that when the activity ends it goes back to the right screen
        Intent intent= getIntent();
        int flag=intent.getIntExtra("gotofragmentedit",-1);
        if (flag==1){bottomNavigationView.setSelectedItemId(R.id.habit_events_nav_item);}
        else {
            bottomNavigationView.setSelectedItemId(R.id.habits_today_nav_item);  // Set habit today as the initial screen.
             }

        }
}