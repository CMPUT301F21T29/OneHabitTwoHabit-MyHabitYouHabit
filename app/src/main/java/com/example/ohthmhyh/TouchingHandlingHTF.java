package com.example.ohthmhyh;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class TouchingHandlingHTF extends ItemTouchHelper.Callback {
    private final TouchingHandlingAdaptorHTF madapter;
    /**
     *This is the creator for TouchingHandlingHTF
     */
    public TouchingHandlingHTF(TouchingHandlingAdaptorHTF adapter) {
        this.madapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    /**
     *Overrides for the motion we allow in the views
     * what movements the user can actually do
     */
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0,0);
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
