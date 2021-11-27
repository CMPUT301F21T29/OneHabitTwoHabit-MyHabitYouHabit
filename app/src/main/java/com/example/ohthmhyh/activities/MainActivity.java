package com.example.ohthmhyh.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.fragments.FeedFragment;
import com.example.ohthmhyh.fragments.HabitEventsFragment;
import com.example.ohthmhyh.fragments.HabitsFragment;
import com.example.ohthmhyh.fragments.HabitsTodayFragment;
import com.example.ohthmhyh.R;
import com.example.ohthmhyh.fragments.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

/**
 * The main activity of the app responsible for moving between fragments in the
 * bottom navigation bar.
 */
public class MainActivity extends AppCompatActivity {

    private ProgressBar loadingProgressBar;
    private BottomNavigationView bottomNavigationView;
    private FeedFragment feedFragment = new FeedFragment();
    private HabitsFragment habitsFragment = new HabitsFragment();
    private HabitsTodayFragment habitsTodayFragment = new HabitsTodayFragment();
    private HabitEventsFragment habitEventsFragment = new HabitEventsFragment();
    private UserFragment userFragment = new UserFragment();
    private DatabaseAdapter databaseAdapter = DatabaseAdapter.getInstance();

    /**
     * Called to create the main activity.
     * @param savedInstanceState
     *      Information about the previous state of the main activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingProgressBar = findViewById(R.id.progress_bar_loading);
        loadingProgressBar.setVisibility(View.VISIBLE);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setSelectedItemId(R.id.habits_today_nav_item);  // Set habit today as the initial screen.

        if (databaseAdapter.shouldUpdate()) {
            databaseAdapter.pullHabitEventList(new DatabaseAdapter.OnLoadedListener() {
                @Override
                public void onLoaded() {
                    databaseAdapter.pullHabitList(new DatabaseAdapter.OnLoadedListener() {
                        @Override
                        public void onLoaded() {
                            databaseAdapter.pullUser(new DatabaseAdapter.OnLoadedListener() {
                                @Override
                                public void onLoaded() {
                                    loadingProgressBar.setVisibility(View.GONE);
                                    setupNavigationBar();
                                }
                            });
                        }
                    });
                }
            });
        } else {
            loadingProgressBar.setVisibility(View.GONE);
            setupNavigationBar();
        }
    }

    private void setupNavigationBar() {
        // Get the navigation bar in the MainActivity and specify which fragments it should go to
        // when the buttons in the navigation bar are tapped. Also, set the default fragment to be
        // shown as the "Habit Today" fragment.
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

        bottomNavigationView.setSelectedItemId(R.id.habits_today_nav_item);  // Set habit today as the initial screen.
    }
}