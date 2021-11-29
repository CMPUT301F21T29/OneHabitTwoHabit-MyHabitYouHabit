package com.example.ohthmhyh;

import android.graphics.Typeface;

/**
 * A class that holds constants used throughout the app. Rather than referencing literals everywhere
 * all the time, these constants provide easy and universal access to numbers that are critical to
 * the app's requirements.
 *
 * There are no outstanding issues that we are aware of.
 */
public class Constants {

    /**
     * The minimum length of a habit's name.
     */
    public static final int HABIT_NAME_MIN_LENGTH = 1;

    /**
     * The maximum length of a habit's name.
     */
    public static final int HABIT_NAME_MAX_LENGTH = 20;

    /**
     * The minimum length of a habit's description.
     */
    public static final int HABIT_DESCRIPTION_MIN_LENGTH = 1;

    /**
     * The maximum length of a habit's description.
     */
    public static final int HABIT_DESCRIPTION_MAX_LENGTH = 30;

    /**
     * The minimum length of a habit event's comment.
     */
    public static final int HABIT_EVENT_COMMENT_MIN_LENGTH = 0;

    /**
     * The maximum length of a habit event's comment.
     */
    public static final int HABIT_EVENT_COMMENT_MAX_LENGTH = 20;

    /**
     * The % adherence score for which the text (ex "100%") on a progress bar will become green
     */
    public static final int ADHERENCE_TEXT_GREEN_THRESHOLD = 100;

    /**
     * The % adherence score threshold for which the progress bar turns green
     */
    public static final int ADHERENCE_PROGRESS_BAR_GREEN_THRESHOLD = 85;

    /**
     * The % adherence score threshold for which the progress bar turns amber
     */
    public static final int ADHERENCE_PROGRESS_BAR_AMBER_THRESHOLD = 60;

    /**
     * The % adherence score threshold for which the progress bar turns red
     */
    public static final int ADHERENCE_PROGRESS_BAR_RED_THRESHOLD = 0;

    /**
     * The default zoom level on the map activity
     */
    public static final float DEFAULT_MAP_ZOOM_LEVEL = 15.0f;

    /**
     * The max file size for the habit event images in kB
     */
    public static final int MAX_IMAGE_FILE_SIZE = 1024;

    /**
     * The max vertical resolution of the habit event images in pixels
     */
    public static final int MAX_IMAGE_VERTICAL_RESOLUTION = 1080;

    /**
     * The max horizontal resolution of the habit event images in pixels
     */
    public static final int MAX_IMAGE_HORIZONTAL_RESOLUTION = 1080;

    /**
     * The font type for the days in which a habit is not to be completed. Used in habit recycler
     * views.
     */
    public static final int HABIT_VIEW_INACTIVE_DAY_TYPEFACE = Typeface.NORMAL;

    /**
     * The font type for the days in which a habit is to be completed. Used in habit recycler
     * views.
     */
    public static final int HABIT_VIEW_ACTIVE_DAY_TYPEFACE = Typeface.BOLD;

    /**
     * The view alpha for the days in which a habit is not to be completed. Used in habit recycler
     * views.
     */
    public static final float HABIT_VIEW_INACTIVE_DAY_ALPHA = 0.3f;

    /**
     * The view alpha for the days in which a habit is to be completed. Used in habit recycler
     * views.
     */
    public static final float HABIT_VIEW_ACTIVE_DAY_ALPHA = 1f;

    /**
     * The name of the database collection with habits
     */
    public static final String HABIT_COLLECTION_NAME = "Habits";

    /**
     * The name of the database collection with habit events
     */
    public static final String HABIT_EVENT_COLLECTION_NAME = "HabitEvents";

    /**
     * The name of the database collection with user profiles
     */
    public static final String PROFILE_COLLECTION_NAME = "Profiles";

    /**
     * The name of the database field name for usernames
     */
    public static final String USERNAME_FIELD_NAME = "username";

}
