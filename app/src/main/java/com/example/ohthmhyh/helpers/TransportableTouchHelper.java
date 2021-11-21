package com.example.ohthmhyh.helpers;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ohthmhyh.R;
import com.example.ohthmhyh.interfaces.ItemTransportable;

public class TransportableTouchHelper extends ItemTouchHelper.Callback {

    private static final int DRAG_FLAGS = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
    private static final int SWIPE_FLAGS = ItemTouchHelper.START | ItemTouchHelper.END;
    private final ItemTransportable itemTransportable;

    /**
     * Creates a new object given an interface that supports this type of touching.
     * @param adapter The interface to support.
     */
    public TransportableTouchHelper(ItemTransportable adapter) {
        this.itemTransportable = adapter;
    }

    /**
     * Whether an item can be dragged on a long press.
     * @return Whether an item can be dragged on a long press.
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    /**
     * Whether an item can be swiped.
     * @return Whether an item can be swiped.
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    /**
     * Sets the view's background when it is to be cleared.
     * @param recyclerView The RecyclerView containing the view.
     * @param viewHolder The ViewHolder holding the view that is to be cleared.
     */
    @Override
    public void clearView(
            @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackgroundColor(
                ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.white));
    }

    /**
     * Sets the view's background colour when it is selected.
     * @param viewHolder The ViewHolder holding the view that is selected.
     * @param actionState The type of action that is being performed.
     */
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG){
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        }
    }

    /**
     * Determines which movements are allowed.
     * @param recyclerView The RecyclerView containing the view.
     * @param viewHolder The ViewHolder holding the view.
     * @return Which movements are allowed.
     */
    @Override
    public int getMovementFlags(
            @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(DRAG_FLAGS, SWIPE_FLAGS);
    }

    /**
     * Calls the method that is to invoke move behaviour.
     * @param recyclerView The RecyclerView containing the view.
     * @param viewHolder The ViewHolder holding the view.
     * @param target The ViewHolder holding the target view.
     * @return Whether the operation was successful.
     */
    @Override
    public boolean onMove(
            @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
            @NonNull RecyclerView.ViewHolder target) {
        itemTransportable.onItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    /**
     * Calls the method that is to invoke swipe behaviour.
     * @param viewHolder The ViewHolder holding the view.
     * @param direction The direction of the swipe.
     */
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        itemTransportable.onItemSwiped(viewHolder.getAdapterPosition());
    }

}
