package com.example.ohthmhyh;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class TouchingHandlingHF extends ItemTouchHelper.Callback {
    private final TouchingHandlingAdaptorHF madapter;
    /**
     *This is the creater for TouchingHandlingHF
     */
    public TouchingHandlingHF(TouchingHandlingAdaptorHF adapter) {
        this.madapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }
    /**
     *Overrides for the motion we allow in the views
     *This corresponds the dragging and moving
     */
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackgroundColor(
                ContextCompat.getColor(viewHolder.itemView.getContext(),R.color.white)
        );
    }
    /**
     *Overrides for the motion we allow in the views
     *This changes the color when an item is selected to move
     */
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (actionState==ItemTouchHelper.ACTION_STATE_DRAG){
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        }
    }

    /**
     *Overrides for the motion we allow in the views
     * what movements the user can actually do
     */
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        final int dragFlags= ItemTouchHelper.UP |ItemTouchHelper.DOWN;
        final int swipeFlags= ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags,swipeFlags);
    }
    /**
     *Overrides for the motion we allow in the views
     *Gives to to and from position
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        madapter.onItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        return true;
    }
    /**
     *Overrides for the motion we allow in the views
     * Gives the position to delete
     */
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        madapter.onItemSwiped(viewHolder.getAdapterPosition());
    }


}
