package com.example.ohthmhyh;

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
     * The % adherance score threshold for which the progress bar turns green
     */
    public static final int ADHERENCE_PROGRESS_BAR_GREEN_THRESHOLD = 85;

    /**
     * The % adherance score threshold for which the progress bar turns amber
     */
    public static final int ADHERENCE_PROGRESS_BAR_AMBER_THRESHOLD = 60;

    /**
     * The % adherance score threshold for which the progress bar turns red
     */
    public static final int ADHERENCE_PROGRESS_BAR_RED_THRESHOLD = 0;

}
