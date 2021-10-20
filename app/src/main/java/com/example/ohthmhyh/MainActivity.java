package com.example.ohthmhyh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

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
        // shown as the "Feed" fragment.
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
        bottomNavigationView.setSelectedItemId(R.id.habits_today_nav_item);  // Set feed as the initial screen.
    }
}