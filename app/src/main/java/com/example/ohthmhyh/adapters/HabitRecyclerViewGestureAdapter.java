package com.example.ohthmhyh.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.example.ohthmhyh.R;
import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.entities.Habit;
import com.example.ohthmhyh.interfaces.ItemTransportable;

import java.util.ArrayList;

public class HabitRecyclerViewGestureAdapter extends HabitRecyclerViewAdapter
        implements ItemTransportable {

    /**
     * A listener/callback that is called when items in the RecyclerView are tapped
     */
    public interface OnTouchListener{
        void onItemClicked(int position);
    }


    /**
     * Used to provide a reference to the views (items) in the RecyclerView
     */
    public class ViewHolder extends HabitRecyclerViewAdapter.ViewHolder
            implements GestureDetector.OnGestureListener, View.OnTouchListener {
        OnTouchListener touchListener;
        GestureDetector gestureDetector;

        /**
         * Make a new ViewHolder for providing references to a view (item) in the RecyclerView.
         * @param view the parent view to this view
         */
        public ViewHolder(@NonNull View view, HabitRecyclerViewGestureAdapter.OnTouchListener touchListener) {
            super(view);
            this.touchListener = touchListener;
            gestureDetector = new GestureDetector(view.getContext(), this);
            itemView.setOnTouchListener(this);
            username.setVisibility(View.GONE);
        }

        /**
         * Called when the GestureDetector detects a finger put down
         * @param motionEvent the MotionEvent that triggered the call
         * @return false - ignore this gesture
         */
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        /**
         * Called when the GestureDetector detects a long press, but not a move or up yet
         * @param motionEvent the MotionEvent that triggered the call
         */
        @Override
        public void onShowPress(MotionEvent motionEvent) {}

        /**
         * Called when the GestureDetector detects a release of a single tap
         * @param motionEvent the MotionEvent that triggered the call
         * @return true - don't ignore this gesture
         */
        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            touchListener.onItemClicked(getAdapterPosition());
            return true;
        }

        /**
         * Called when the GestureDetector detects a scroll gesture
         * @param motionEvent the MotionEvent that triggered the call
         * @return false - ignore this gesture
         */
        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        /**
         * Called when the GestureDetector detects a long press
         * @param motionEvent the MotionEvent that triggered the call
         */
        @Override
        public void onLongPress(MotionEvent motionEvent) {
            touchHelper.startDrag(this);
        }

        /**
         * Called when the GestureDetector detects a fling gesture
         * @param motionEvent the MotionEvent at the start of the fling
         * @param motionEvent1 the MotionEvent at the end of the fling
         * @param v velocity in the X direction
         * @param v1 velocity in the Y direction
         * @return false - ignore this gesture
         */
        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        /**
         * Called when the GestureDetector detects a touch
         * @param view the view on which the gesture was detected
         * @param motionEvent the MotionEvent that triggered the call
         */
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            gestureDetector.onTouchEvent(motionEvent);
            return true;
        }
    }


    private OnTouchListener touchListener;
    private ItemTouchHelper touchHelper;
    private DatabaseAdapter databaseAdapter = DatabaseAdapter.getInstance();


    /**
     * Creates the custom adapter instance
     * @param context          Context from the activity
     * @param content          The Habits to display in the RecyclerView
     * @param touchListener    A thing that does touch actions
     */
    public HabitRecyclerViewGestureAdapter(Context context, ArrayList<Habit> content,
                                           OnTouchListener touchListener) {
        super(context, content);
        this.touchListener = touchListener;
    }


    /**
     * Sets the ItemTouchHelper used to manage what happens when items in the RecyclerView are
     * interacted with.
     * @param touchHelper
     */
    public void setTouchHelper(ItemTouchHelper touchHelper){
        this.touchHelper = touchHelper;
    }


    /**
     * Creates new views (elements) in the RecyclerView
     * @param parent The parent viewGroup which will hold the new view
     * @param viewType The type of ViewHolder being used
     * @return The completed ViewHolder
     */
    @NonNull
    @Override
    public HabitRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_habit, parent,false);
        return new HabitRecyclerViewGestureAdapter.ViewHolder(view, touchListener);
    }


    /**
     * This is used for deleting items in the RecyclerView when they are swiped
     * @param position index of the item to delete
     */
    @Override
    public void onItemSwiped(int position) {
        // show a confirmation dialog when deleting
        new AlertDialog.Builder(context)
                .setMessage("Do you really want to delete this habit?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    // when "yes" is pressed, delete the habit
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Remove the Habit from the content.
                        content.remove(position);

                        // Remove the Habit from the database.
                        databaseAdapter.removeHabit(position);
                        notifyItemRemoved(position);
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    // don't delete the habit when "no" is pressed, but undo the swipe animation
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        notifyDataSetChanged();
                    }
                }).show();
    }


    /**
     * This is used for moving items in the RecyclerView
     * @param fromPosition When we move a item in the list this is the position
     * @param toPosition This is where we move the item to
     */
    @Override
    public void onItemMoved(int fromPosition, int toPosition) {
        // Move the Habit in the content.
        Habit habitToMove = content.remove(fromPosition);
        content.add(toPosition, habitToMove);

        // Move the Habit in the database.
        databaseAdapter.moveHabit(fromPosition, toPosition);

        notifyItemMoved(fromPosition, toPosition);
    }

}
