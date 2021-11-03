package com.example.ohthmhyh;

import androidx.fragment.app.Fragment;

/**
 * A simple Interface .
 * Used for moving items
 */
public interface CeitemHelpToucherAdapter {

    void onItemMove(int frompositon, int toposition);

    void onItemSwiped(int position);
}
