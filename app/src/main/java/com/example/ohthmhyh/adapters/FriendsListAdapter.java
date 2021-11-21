package com.example.ohthmhyh.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ohthmhyh.R;
import com.example.ohthmhyh.database.DatabaseAdapter;

import java.util.ArrayList;

public class FriendsListAdapter extends ArrayAdapter<String> {

    // resource ID of the layout to use as the listView elements
    private int resource;
    // the context of the activity using this adapter instance
    private Context context;

    /**
     * A constructor for making an adapter which puts friends into a listView
     * @param context the context of the listview
     * @param resource the resource ID of the view to put data into
     * @param friendRequests the arrayList of friends
     */
    public FriendsListAdapter(Context context, int resource, ArrayList<String> friendRequests) {
        super(context, resource, friendRequests);
        this.resource = resource;
        this.context = context;
    }


    /**
     * Called when mapping data into the elements of the list view
     * @param position
     * @param view
     * @param parent
     * @return
     */
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // if the view is null (empty) we need to inflate it from its XML definition
        if (view == null) {
            view = LayoutInflater.from(context).inflate(resource, null);
        }

        // get the username text view
        TextView usernameTV = view.findViewById(R.id.item_friend);

        // set the content of the username text view
        DatabaseAdapter dba = DatabaseAdapter.getInstance();
        dba.pullUsernameFromUID(getItem(position), new DatabaseAdapter.UsernameCallback() {
            @Override
            public void onUsernameCallback(String username) {
                usernameTV.setText(username);
            }
        });

        // send back the completed view
        return view;
    }

}

