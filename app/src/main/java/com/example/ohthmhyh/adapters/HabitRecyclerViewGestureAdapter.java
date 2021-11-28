package com.example.ohthmhyh.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.example.ohthmhyh.R;
import com.example.ohthmhyh.database.DatabaseAdapter;
import com.example.ohthmhyh.entities.Habit;
import com.example.ohthmhyh.interfaces.ItemTransportable;

import java.util.ArrayList;

/**
 * An adapter used for putting Habit objects into elements of a RecyclerView for the user's list of
 * all their Habits. Each row is populated with views to display the Habit's attributes. Gestures
 * like swiping and moving Habits are supported.
 *
 * There are no outstanding issues that we are aware of.
 */
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
        openDialog(position);
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
    /**
     *This method is used to open a conformation screen with the user before a delete
     * @param position the position of the item being swiped
     */
    private void openDialog(int position) {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(
                "Deleting this habit will remove associated events. Continue?");
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    // If user wants to delete and has confirmed, run this code which deletes the
                    // Habit and HabitEvent.
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // Remove all associated HabitEvents.
                        ArrayList<Integer> positionsToDelete = new ArrayList<>();
                        for (int index = 0; index < databaseAdapter.numberOfHabitEvents(); index++) {
                            if (content.get(position).getUHID()
                                    == databaseAdapter.habitEventAtIndex(index).getHabitUHID()) {
                                positionsToDelete.add(index);
                            }
                        }
                        if (positionsToDelete.size() > 0){
                            for (int i = positionsToDelete.size() - 1; i >= 0; i--) {
                                databaseAdapter.removeHabitEvent(positionsToDelete.get(i));
                            }
                        }

                        // Remove the Habit from the content.
                        content.remove(position);

                        // Remove the Habit from the database.
                        databaseAdapter.removeHabit(position);

                        notifyItemRemoved(position);
                    }
                });
        // If the user hits no they don't want to delete run this code
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        notifyItemChanged(position);

                    }
                });
        // If the user clicks outside the box run this code (same as saying no)
        alertDialogBuilder.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        notifyItemChanged(position);
                    }
                });

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
