package com.example.ohthmhyh.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.example.ohthmhyh.database.HabitList;
import com.example.ohthmhyh.interfaces.ItemTransportable;

public class HabitRecyclerViewGestureAdapter extends HabitRecyclerViewAdapter
        implements ItemTransportable {

    public interface OnTouchListener{
        /**
         * This method is used to goto the edit screen
         * @param position the position of the list we want to edit
         */
        void onItemClicked(int position);
    }

    OnTouchListener touchListener;
    private ItemTouchHelper touchHelper;

    public void setTouchHelper(ItemTouchHelper touchHelper){
        this.touchHelper = touchHelper;
    }

    /**
     * This is used for deleting items in the RecyclerView when they are swiped
     * @param position index of the item to delete
     */
    @Override
    public void onItemSwiped(int position) {
        //TODO: Add confirmation alert dialog
        habitList.removeHabit(position);
        notifyItemRemoved(position);
    }


    /**
     * This is used for moving items in the RecyclerView
     * @param fromPosition When we move a item in the list this is the position
     * @param toPosition This is where we move the item to
     */
    @Override
    public void onItemMoved(int fromPosition, int toPosition) {
        habitList.moveHabit(fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * Creates the custom adapter instance
     * @param context          Context from the activity
     * @param touchListener A thing that does touch actions
     * @param habitList        The HabitList containing the habits
     */
    public HabitRecyclerViewGestureAdapter(Context context, HabitList habitList, OnTouchListener touchListener) {
        super(context, habitList);
        this.touchListener = touchListener;
    }

}
