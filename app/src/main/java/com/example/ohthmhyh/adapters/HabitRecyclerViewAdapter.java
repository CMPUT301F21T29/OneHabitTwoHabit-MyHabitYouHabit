package com.example.ohthmhyh.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ohthmhyh.Constants;
import com.example.ohthmhyh.R;
import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.database.HabitEventList;
import com.example.ohthmhyh.entities.Habit;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * An adapter used for putting habit objects into elements of a RecyclerView.
 */
public abstract class HabitRecyclerViewAdapter extends RecyclerView.Adapter<HabitRecyclerViewAdapter.ViewHolder> {

    /**
     * Used to provide a reference to the views (items) in the RecyclerView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        // views of the RecyclerView items/elements
        protected TextView name, description, percent, username;
        protected CheckBox checkbox;
        private ProgressBar pb;
        private TextView sun, mon, tues, wed, thurs, fri, sat;
        private DatabaseAdapter databaseAdapter;
        /**
         * Make a new ViewHolder for providing references to a view (item) in the RecyclerView.
         * @param view the parent view to this view
         */
        public ViewHolder(@NonNull View view) {
            super(view);

            // get all of the views in the item_habit view
            name = view.findViewById(R.id.name_rv);
            description = view.findViewById(R.id.habit_description_rv);
            pb = (ProgressBar) view.findViewById(R.id.pb);
            percent = view.findViewById(R.id.percent);
            username = view.findViewById(R.id.creator_usernameTV);
            checkbox = view.findViewById(R.id.checkBox_ht);
            sun = view.findViewById(R.id.sun);
            mon = view.findViewById(R.id.mon);
            tues = view.findViewById(R.id.tues);
            wed = view.findViewById(R.id.wed);
            thurs = view.findViewById(R.id.thurs);
            fri = view.findViewById(R.id.fri);
            sat = view.findViewById(R.id.sat);
        }
    }


    protected ArrayList<Habit> content;
    protected Context context;


    /**
     * Creates an instance of the custom RecyclerView adapter used for showing habits
     * @param context Context from the activity
     * @param content The list of Habits to display in the RecyclerView
     */
    public HabitRecyclerViewAdapter(Context context, ArrayList<Habit> content) {
        this.context = context;
        this.content = content;
    }


    /**
     * Creates new views (elements) in the RecyclerView
     * @param parent The parent viewGroup which will hold the new view
     * @param viewType The type of ViewHolder being used
     * @return The completed ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_habit, parent,false);

        return new ViewHolder(view);
    }


    /**
     * Replaces the content of the views in the RecyclerView with the proper view for a habit.
     * @param holder the view holder of the habit
     * @param position the position of the habit view in the RecyclerView
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // set the content of the views in the RecyclerView element
        holder.name.setText(content.get(position).getName());
        holder.description.setText(content.get(position).getDescription());
        holder.username.setVisibility(View.GONE);  // The username is invisible by default.
        holder.checkbox.setVisibility(View.GONE);  // Checkbox is invisible by default.
        setProgressBar(holder, position);
        setDays(holder, position);
    }


    /**
     * Returns the amount of items in the RecyclerView
     * @return content.size()
     */
    @Override
    public int getItemCount() {
        return content.size();
    }


    /**
     * Contains the logic to set the progress bar, both in magnitude and colour.
     * Also sets the adherence percentage value.
     * @param holder the ViewHolder holding objects
     * @param position the position of the habit in the list that we are using
     */
    public void setProgressBar(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        double progress = content.get(position).getAdherence(LocalDate.now());

        //set colours of bar and text to grey
        holder.percent.setTextColor(context.getResources().getColor(R.color.progressBarGray));
        holder.pb.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.progressBarGray)));

        int progressPercent = (int) progress;
        holder.pb.setProgress(progressPercent);
        holder.percent.setText(String.valueOf(progressPercent) + "%");

        // set the progress bar and percentage color based on how well we adhere to the schedule
        if (progress >= Constants.ADHERENCE_PROGRESS_BAR_GREEN_THRESHOLD) {
            //make progress bar and percentage green
            holder.pb.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.progressBarGreen)));
            holder.percent.setTextColor(context.getResources().getColor(R.color.progressBarGreen));
        }
        else if (progress >= Constants.ADHERENCE_PROGRESS_BAR_AMBER_THRESHOLD) {
            //make progress bar and percentage amber
            holder.pb.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.progressBarAmber)));
            holder.percent.setTextColor(context.getResources().getColor(R.color.progressBarAmber));
        }
        else if (progress > Constants.ADHERENCE_PROGRESS_BAR_RED_THRESHOLD) {
            //make progress bar and percentage red
            holder.pb.setProgressTintList(ColorStateList.valueOf(Color.RED));
            holder.percent.setTextColor(ColorStateList.valueOf(Color.RED));
        }
    }


    /**
     * Contains the logic to bold and set text colour of applicable days in the week for which a
     * a habit should be completed.
     * @param holder the ViewHolder of the RecyclerView element
     * @param position the position of the ViewHolder (element) in the RecyclerView
     */
    public void setDays(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ArrayList<Habit.Days> days = content.get(position).getSchedule();
        final float minOpacity = 0.3f;

        // set all "day" views to default font
        holder.sun.setTypeface(null, Typeface.NORMAL);
        holder.mon.setTypeface(null, Typeface.NORMAL);
        holder.tues.setTypeface(null, Typeface.NORMAL);
        holder.wed.setTypeface(null, Typeface.NORMAL);
        holder.thurs.setTypeface(null, Typeface.NORMAL);
        holder.fri.setTypeface(null, Typeface.NORMAL);
        holder.sat.setTypeface(null, Typeface.NORMAL);

        // set all "day" views to default opacity
        holder.sun.setAlpha(minOpacity);
        holder.mon.setAlpha(minOpacity);
        holder.tues.setAlpha(minOpacity);
        holder.wed.setAlpha(minOpacity);
        holder.thurs.setAlpha(minOpacity);
        holder.fri.setAlpha(minOpacity);
        holder.sat.setAlpha(minOpacity);

        // Bold the days of the week for which this habit should occur
        if(days.contains(Habit.Days.Sun)){
            // bold / change colour
            holder.sun.setTypeface(null, Typeface.BOLD);
            holder.sun.setAlpha(1f);
        }

        if(days.contains(Habit.Days.Mon)){
            // bold / change colour
            holder.mon.setTypeface(null, Typeface.BOLD);
            holder.mon.setAlpha(1f);
        }

        if(days.contains(Habit.Days.Tue)){
            // bold / change colour
            holder.tues.setTypeface(null, Typeface.BOLD);
            holder.tues.setAlpha(1f);
        }

        if(days.contains(Habit.Days.Wed)){
            // bold / change colour
            holder.wed.setTypeface(null, Typeface.BOLD);
            holder.wed.setAlpha(1f);
        }

        if(days.contains(Habit.Days.Thu)){
            // bold / change colour
            holder.thurs.setTypeface(null, Typeface.BOLD);
            holder.thurs.setAlpha(1f);
        }

        if(days.contains(Habit.Days.Fri)){
            // bold / change colour
            holder.fri.setTypeface(null, Typeface.BOLD);
            holder.fri.setAlpha(1f);
        }

        if(days.contains(Habit.Days.Sat)){
            // bold / change colour
            holder.sat.setTypeface(null, Typeface.BOLD);
            holder.sat.setAlpha(1f);
        }
    }

}
