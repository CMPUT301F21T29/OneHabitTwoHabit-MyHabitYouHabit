package com.example.ohthmhyh;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.ohthmhyh.database.DatabaseAdapter;

import java.util.ArrayList;

public class FriendRequestListAdapter extends ArrayAdapter<String> {

    /**
     * Used when setting the functions that are called when buttons in the listView are pressed
     */
    public interface buttonListener {
        public void onAcceptClickListener(int position);
        public void onDeclineClickListener(int position);
    }

    // resource ID of the layout to use as the listView elements
    private int resource;
    // the context of the activity using this adapter instance
    private Context context;
    private buttonListener btnListener;


    /**
     * Sets the listener that defines what happens when buttons in the list are pressed
     * @param listener
     */
    public void setCustomButtonListener(buttonListener listener) {
        this.btnListener = listener;
    }


    /**
     * A constructor for making an adapter which puts friend requests into a listView
     * @param context the context of the listview
     * @param resource the resource ID of the view to put data into
     * @param friendRequests the arrayList of friend requests
     */
    public FriendRequestListAdapter(Context context, int resource, ArrayList<String> friendRequests) {
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

        // get the views in the list element
        TextView usernameTV = view.findViewById(R.id.request_username_tv);
        Button acceptBtn  = view.findViewById(R.id.accept_btn);
        Button declineBtn = view.findViewById(R.id.decline_btn);

        // get the username from the database and put it into the text view
        DatabaseAdapter dba = new DatabaseAdapter();
        dba.pullUsernameFromUID(getItem(position), new DatabaseAdapter.UsernameCallback() {
            @Override
            public void onUsernameCallback(String username) {
                usernameTV.setText(username);
            }
        });
        usernameTV.setText(getItem(position));

        // set up the callbacks for the accept and decline buttons
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnListener.onAcceptClickListener(position);
            }
        });
        declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnListener.onDeclineClickListener(position);
            }
        });

        // send back the completed view
        return view;
    }

}

