package com.example.ohthmhyh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ohthmhyh.database.HabitList;
import com.example.ohthmhyh.entities.Habit;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * A simple RecycleviewAdapter for the Habit list.
 */
public class CustomAdapterHTF extends RecyclerView.Adapter<CustomAdapterHTF.Myviewholder> implements TouchingHandlingAdaptorHTF {
    HabitList habitList;
    ArrayList<Habit> newHabitList;


    Context context;
    ItemTouchHelper mTouchhelper;
    OntouchListener mOntouchListener;

    /**
     * Creates the custom adapter instance
     * @param habitList The HabitList containing the habits
     * @param context Context from the activity
     * @param mOntouchListener A thing that does touch actions
     * The CERecycleviewAdapter creater Needs and array, context and a touch Listener
     */
    public CustomAdapterHTF(Context context, OntouchListener mOntouchListener, ArrayList<Habit> newHabitList, HabitList habitList) {
        this.newHabitList = newHabitList;
        this.context = context;
        this.mOntouchListener = mOntouchListener;
        this.habitList = habitList;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_htf,parent,false);
        Myviewholder holder = new Myviewholder(view, mOntouchListener);

        return holder;
    }

    //sets the things in the display
    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, @SuppressLint("RecyclerView") int position) {
        //Todo
        //Need to error check because somethings might be null

        holder.name.setText(newHabitList.get(position).getName());
        holder.description.setText(newHabitList.get(position).getDescription());
        setProgressBar(holder, position);
        setDays(holder, position);

        Habit h = newHabitList.get(position);

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.checkbox.isChecked()){
                    Log.d("tag", h.getName() + " checked");
                    int index = habitList.getHabitIndex(h);

                    h.logCompleted();
                    habitList.setHabit(index, h);
                } else {
                    Log.d("tag", h.getName() + " unchecked");
                    int index = habitList.getHabitIndex(h);

                    h.undoCompleted();
                    habitList.setHabit(index, h);
                }
            }
        });

    }

    /**
     *Returns the amount of items in the Recyclerview
     * @return habitList.size()
     */
    @Override
    public int getItemCount() {
        return newHabitList.size();
    }

    /**
     *This is used for moving items in the Recycleview
     * @param fromPosition When we move a item in the list this is the position
     * @param toPosition This is where me wmove the item to
     */
    @Override
    public void onItemMoved(int fromPosition, int toPosition) {
//        habitList.moveHabit(fromPosition, toPosition);
//        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     *This is used for deleting items in the Recycleview
     * @param  position the item to delete
     */
    @Override
    public void onItemSwiped(int position) {
//        //TODO: Add confirmation alert dialog
//        habitList.removeHabit(position);
//        notifyItemRemoved(position);
    }

    public void setTouchhelper(ItemTouchHelper touchhelper){
        this.mTouchhelper = touchhelper;
    }

    public class Myviewholder extends RecyclerView.ViewHolder implements
            View.OnTouchListener,
            GestureDetector.OnGestureListener
    {
        TextView name;
        TextView description;
        ProgressBar pb;
        TextView percent;
        CheckBox checkbox;
        //CheckBox checkbox;

        //days of week
        TextView sun;
        TextView mon;
        TextView tues;
        TextView wed;
        TextView thurs;
        TextView fri;
        TextView sat;

        GestureDetector mGestureDetector;
        ConstraintLayout parentLayout;
        OntouchListener ontouchListener;
        public Myviewholder(@NonNull View itemView,OntouchListener ontouchListener) {
            super(itemView);
            name = itemView.findViewById(R.id.name_rv_ht);
            description = itemView.findViewById(R.id.habit_description_rv_ht);
            pb = (ProgressBar) itemView.findViewById(R.id.pb_ht);
            percent = itemView.findViewById(R.id.percent_ht);
            checkbox = itemView.findViewById(R.id.checkBox_ht);

            //days of week
            sun = itemView.findViewById(R.id.sun_ht);
            mon = itemView.findViewById(R.id.mon_ht);
            tues = itemView.findViewById(R.id.tues_ht);
            wed = itemView.findViewById(R.id.wed_ht);
            thurs = itemView.findViewById(R.id.thurs_ht);
            fri = itemView.findViewById(R.id.fri_ht);
            sat = itemView.findViewById(R.id.sat_ht);


            //This is the name of the contrant layout in display HE list
            parentLayout = itemView.findViewById(R.id.rv_cl_ht);

            mGestureDetector = new GestureDetector(itemView.getContext(),this);

            this.ontouchListener= ontouchListener;

            itemView.setOnTouchListener(this);

        }

        /**
         *These are all possible motions a user can do
         * With the corresponding actions
         */
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            ontouchListener.onItemClicked(getAdapterPosition());
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            mTouchhelper.startDrag(this);
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return true;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mGestureDetector.onTouchEvent(motionEvent);
            return true;
        }


    }

    public interface OntouchListener{
        /**
         *This method is used to goto the edit screen
         * @param position the position of the list we want to edit
         */
        void onItemClicked(int position);
    }

    /**
     * Contains the logic to set the progress bar, both in magnitude and colour
     * Also set the % value
     * @author Matt
     * @param holder the viewholder holding objects
     * @param position the position of the habit in the list that we are using
     */
    public void setProgressBar(@NonNull Myviewholder holder, @SuppressLint("RecyclerView") int position) {
        double progress = newHabitList.get(position).getAdherence(LocalDate.now());


        //set colours of bar and text to grey
        holder.percent.setTextColor(context.getResources().getColor(R.color.progressBarGray));
        holder.pb.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.progressBarGray)));

        int progressPercent = (int) progress;
        holder.pb.setProgress(progressPercent);
        holder.percent.setText(String.valueOf(progressPercent) + "%");

        //set colour based on progress
        if (progress == Constants.ADHERENCE_TEXT_GREEN_THRESHOLD) {
            holder.percent.setTextColor(context.getResources().getColor(R.color.progressBarGreen));
        }

        if (progress >= Constants.ADHERENCE_PROGRESS_BAR_GREEN_THRESHOLD) {
            //make progress bar green
            holder.pb.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.progressBarGreen)));
        }
        else if (progress >= Constants.ADHERENCE_PROGRESS_BAR_AMBER_THRESHOLD) {
            //make progress bar amber
            holder.pb.setProgressTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.progressBarAmber))); //amber

        }
        else if (progress > Constants.ADHERENCE_PROGRESS_BAR_RED_THRESHOLD) {
            //make progress bar red
            holder.pb.setProgressTintList(ColorStateList.valueOf(Color.RED));
        }
    }

    /**
     * contains the logic to bold and set text colour of applicable days in the week
     * @author Matt
     * @param holder the viewholder holding objects
     * @param position the position of the habit in the list that we are using
     */
    public void setDays(@NonNull Myviewholder holder, @SuppressLint("RecyclerView") int position) {
        //Bold/change colour of the days for which a habit is applicable

        ArrayList<Habit.Days> days = newHabitList.get(position).getSchedule();

        float minOpacity = 0.3f;

        //set all to default params
        holder.sun.setTypeface(null, Typeface.NORMAL);
        holder.mon.setTypeface(null, Typeface.NORMAL);
        holder.tues.setTypeface(null, Typeface.NORMAL);
        holder.wed.setTypeface(null, Typeface.NORMAL);
        holder.thurs.setTypeface(null, Typeface.NORMAL);
        holder.fri.setTypeface(null, Typeface.NORMAL);
        holder.sat.setTypeface(null, Typeface.NORMAL);
        holder.sun.setAlpha(minOpacity);
        holder.mon.setAlpha(minOpacity);
        holder.tues.setAlpha(minOpacity);
        holder.wed.setAlpha(minOpacity);
        holder.thurs.setAlpha(minOpacity);
        holder.fri.setAlpha(minOpacity);
        holder.sat.setAlpha(minOpacity);

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

//    public void onCheckboxClicked(View view) {
//        // Is the view now checked?
//        boolean checked = ((CheckBox) view).isChecked();
//
//        // Check which checkbox was clicked
//        switch(view.getId()) {
//            case R.id.checkBox_ht:
//                if (checked) {
//                    Log.d("tag", "box checked");
//                }
//                // Put some meat on the sandwich
//
//                else {
//                    Log.d("tag", "box unchecked");
//                }
//        }
//    }

}