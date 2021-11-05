package com.example.ohthmhyh;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.app.Instrumentation;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class HabitFragmentTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(
            MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.clickOnView(solo.getView(R.id.habits_nav_item));
    }

    @Test
    public void addButtonExists() {
        solo.sleep(1000);
        assertTrue(solo.searchText("Add a Habit"));
    }

    @Test
    public void addHabitDialogShowsUp() {
        solo.clickOnButton("Add a Habit");
        assertTrue(solo.searchText("Enter a Habit"));
        assertTrue(solo.searchText("Enter descriptions"));
        assertTrue(solo.searchText("Enter a date"));
        assertTrue(solo.searchText("This habit is"));
        assertTrue(solo.searchText("Add"));
        assertTrue(solo.searchText("Cancel"));
    }

    @Test
    public void testAddingHabit() {
        solo.clickOnButton("Add a Habit");
        solo.enterText((EditText) solo.getView(R.id.enter_habit_name), "HabitTitle");
        solo.enterText((EditText) solo.getView(R.id.enter_habit_des), "HabitDescription");
        solo.clickOnView(solo.getView(R.id.enter_date));
        solo.clickOnButton("OK");
        solo.clickOnButton("Sun");
        solo.clickOnButton("Tue");
        solo.clickOnButton("PUBLIC");
        solo.clickOnButton("Add");
        assertTrue(solo.searchText("HabitTitle"));
        assertTrue(solo.searchText("HabitDescription"));
        assertTrue(solo.searchText("Done"));
    }

    @Test
    public void testFormValidation() {
        solo.clickOnButton("Add a Habit");
        solo.enterText((EditText) solo.getView(R.id.enter_habit_name), "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        assertTrue(solo.searchText("Title is too long"));
        solo.enterText((EditText) solo.getView(R.id.enter_habit_des), "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        assertTrue(solo.searchText("Description is too long"));
        solo.clickOnButton("Add");
        assertTrue(solo.searchText("ENTER A DATE"));
        assertTrue(solo.searchText("(Error: Choose a schedule)"));
    }

    @Test
    public void testDeleteHabit() {
        int fromX, toX, fromY, toY;
        int[] location = new int[2];

        solo.clickOnButton("Add a Habit");
        solo.enterText((EditText) solo.getView(R.id.enter_habit_name), "ToBeDeleted");
        solo.enterText((EditText) solo.getView(R.id.enter_habit_des), "HabitDescription");
        solo.clickOnView(solo.getView(R.id.enter_date));
        solo.clickOnButton("OK");
        solo.clickOnButton("Sun");
        solo.clickOnButton("Tue");
        solo.clickOnButton("PUBLIC");
        solo.clickOnButton("Add");

        assertTrue(solo.searchText("ToBeDeleted"));
        View row = solo.getText("ToBeDeleted");
        row.getLocationInWindow(location);

        // fail if the view with text cannot be located in the window

        fromX = location[0] + 100;
        fromY = location[1];

        toX = location[0];
        toY = fromY;

        solo.drag(fromX, toX, fromY, toY, 10);

        assertFalse(solo.searchText("ToBeDeleted"));
    }
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
