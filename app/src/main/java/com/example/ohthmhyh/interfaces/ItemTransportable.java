package com.example.ohthmhyh.interfaces;

/**
 * Allows for transporting of an item in a RecyclerView. Specifically, this interface supports
 * moving and swiping of items in a RecyclerView.
 *
 * This interface defines methods that a RecyclerView adapter can implement in order to achieve
 * specific gestures for the items in the RecyclerView.
 *
 * There are no outstanding issues that we are aware of.
 */
public interface ItemTransportable {

    /**
     * Called when an item is moved from one position in the RecyclerView to another.
     * @param fromPosition The starting position of the item in the RecyclerView.
     * @param toPosition The ending position of the item in the RecyclerView.
     */
    void onItemMoved(int fromPosition, int toPosition);

    /**
     * Called when an item is swiped from the RecyclerView.
     * @param position The position of the item in the RecyclerView.
     */
    void onItemSwiped(int position);

}
