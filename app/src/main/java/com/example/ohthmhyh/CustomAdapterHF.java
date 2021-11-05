package com.example.ohthmhyh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.text.Html;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

/**
 * A simple RecycleviewAdapter for the Habit list.
 */
public class CustomAdapterHF extends RecyclerView.Adapter<CustomAdapterHF.Myviewholder>
        implements TouchingHandlingAdaptorHF{
    HabitList habitList;
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
    public CustomAdapterHF(Context context, OntouchListener mOntouchListener, HabitList habitList) {
        this.habitList = habitList;
        this.context = context;
        this.mOntouchListener = mOntouchListener;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_hf,parent,false);
        Myviewholder holder = new Myviewholder(view, mOntouchListener);

        return holder;
    }

    //sets the things in the display
    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, @SuppressLint("RecyclerView") int position) {
        //Todo
        //Need to error check because somethings might be null
        holder.name.setText(habitList.getHabit(position).getName());
        holder.description.setText(habitList.getHabit(position).getDescription());
    }

    /**
     *Returns the amount of items in the Recycleview
     * @return habitList.size()
     */
    @Override
    public int getItemCount() {
        return habitList.size();
    }

    /**
     *This is used for moving items in the Recycleview
     * @param fromPosition When we move a item in the list this is the position
     * @param toPosition This is where me wmove the item to
     */
    @Override
    public void onItemMoved(int fromPosition, int toPosition) {
        habitList.moveHabit(fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     *This is used for deleting items in the Recycleview
     * @param  position the item to delete
     */
    @Override
    public void onItemSwiped(int position) {
        //TODO: Add confirmation alert dialog
        habitList.removeHabit(position);
        notifyItemRemoved(position);
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

        GestureDetector mGestureDetector;
        ConstraintLayout parentLayout;
        OntouchListener ontouchListener;
        public Myviewholder(@NonNull View itemView,OntouchListener ontouchListener) {
            super(itemView);
            name = itemView.findViewById(R.id.name_rv);
            description = itemView.findViewById(R.id.habit_description_rv);
            //This is the name of the contrant layout in display HE list
            parentLayout = itemView.findViewById(R.id.rv_cl);

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
}
