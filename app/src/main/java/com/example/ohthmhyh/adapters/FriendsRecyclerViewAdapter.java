package com.example.ohthmhyh.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ohthmhyh.R;
import com.example.ohthmhyh.database.DatabaseAdapter;

import java.util.ArrayList;

public class FriendsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * Used to provide a reference to the views (items) in the friend request view
     */
    public class FriendRequestViewHolder extends RecyclerView.ViewHolder{
        // views of the friend request layout
        private TextView usernameTV;
        private Button acceptBtn;
        private Button declineBtn;

        /**
         * Make a new ViewHolder for providing references to a friend request in the RecyclerView.
         * @param view the parent view to this view
         */
        public FriendRequestViewHolder(View view){
            super(view);
            // get all the views in the item_friend_request view
            usernameTV = view.findViewById(R.id.request_username_tv);
            acceptBtn  = view.findViewById(R.id.accept_btn);
            declineBtn = view.findViewById(R.id.decline_btn);
        }
    }


    /**
     * Used to provide a reference to the views (items) in the friend request view
     */
    public class FriendViewHolder extends RecyclerView.ViewHolder{
        // views of the friend layout
        private TextView usernameTV;

        /**
         * Make a new ViewHolder for providing references to a friend in the RecyclerView.
         * @param view the parent view to this view
         */
        public FriendViewHolder(View view){
            super(view);
            // get all the views in the item_friend view
            usernameTV = view.findViewById(R.id.item_friend);
        }
    }


    /**
     * Used to provide a reference to the views (items) in the plain text view
     */
    public class PlainTextViewHolder extends RecyclerView.ViewHolder{
        // views of the plain text view layout
        private TextView headerTV;

        /**
         * Make a new ViewHolder for providing references to a header in the RecyclerView.
         * @param view the parent view to this view
         */
        public PlainTextViewHolder(View view){
            super(view);
            // get all the views in the header view
            headerTV = view.findViewById(R.id.headerTV);
        }
    }


    /**
     * Used to setting the functions that are called when buttons in the RecyclerView elements
     * are pressed.
     */
    public interface buttonListener {
        public void onAcceptClickListener(int position);
        public void onDeclineClickListener(int position);
        public void onFriendClickListener(int position);
    }


    private ArrayList<String> friends;
    private ArrayList<String> friendRequests;
    private enum viewTypes {FRIENDS, REQUESTS, HEADER};
    private FriendsRecyclerViewAdapter.buttonListener btnListener;


    /**
     * Creates an instance of the custom RecyclerView adapter used for showing a users friends
     * and their friend requests
     * @param friends The friends of the user
     * @param friendRequests The friend requests of the user
     */
    public FriendsRecyclerViewAdapter(ArrayList<String> friends, ArrayList<String> friendRequests){
        this.friends = friends;
        this.friendRequests = friendRequests;
    }


    /**
     * Sets the listener that defines what happens when buttons in the list are pressed
     * @param listener
     */
    public void setCustomButtonListener(FriendsRecyclerViewAdapter.buttonListener listener) {
        this.btnListener = listener;
    }


    /**
     * Used to determine which view should be inserted at this position in the RecyclerView
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position){
        // a header is first in the RecyclerView
        if(position == 0){
            return viewTypes.HEADER.ordinal();
        }
        // friend requests show up after the first header in the RecyclerView
        if(position > 0 && position < friendRequests.size()+1){
            return viewTypes.REQUESTS.ordinal();
        }
        // another header should follow the friend requests
        if(position == friendRequests.size()+1){
            return viewTypes.HEADER.ordinal();
        }
        // remaining positions are populated by friends
        return viewTypes.FRIENDS.ordinal();
    }


    /**
     * Creates new views (elements) in the RecyclerView
     * @param parent The parent viewGroup which will hold the new view
     * @param viewType The type of ViewHolder being used
     * @return The completed ViewHolder
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // make a header view holder
        if(viewType == viewTypes.HEADER.ordinal()){
            return new PlainTextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plain_text, parent,false));
        }
        // make a friend request view holder
        if(viewType == viewTypes.REQUESTS.ordinal()){
            return new FriendRequestViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_request, parent,false));
        }
        // make a friend view holder
        return new FriendViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent,false));
    }


    /**
     * Replaces the content of the views in the RecyclerView with the proper view for a friend
     * request, friend, or a header.
     * @param holder the view holder of the RecyclerView element
     * @param position the position of the habit view in the RecyclerView
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // if we are putting information into a header view
        if(holder.getItemViewType() == viewTypes.HEADER.ordinal()){
            PlainTextViewHolder holder1 = (PlainTextViewHolder)holder;
            // if we are filling the first header
            if (position == 0){
                if(friendRequests.size() == 0){
                    holder1.headerTV.setText("Friend Requests (none)");
                }
                else {
                    holder1.headerTV.setText("Friend Requests");
                }
            }
            // if we are filling the second header
            if (position == friendRequests.size()+1){
                if(friends.size()==0){
                    holder1.headerTV.setText("Friends (none)");
                }
                else{
                    holder1.headerTV.setText("Friends");
                }
            }
        }

        // if we are putting information into a friend request view
        if(holder.getItemViewType() == viewTypes.REQUESTS.ordinal()){
            FriendRequestViewHolder holder1 = (FriendRequestViewHolder) holder;
            // when the accept button is pressed
            holder1.acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnListener.onAcceptClickListener(holder1.getAdapterPosition()-1);
                }
            });
            // when the decline button is pressed
            holder1.declineBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnListener.onDeclineClickListener(holder1.getAdapterPosition()-1);
                }
            });
            // insert the username into the view
            DatabaseAdapter dba = DatabaseAdapter.getInstance();
            dba.pullUsernameFromUID(friendRequests.get(position-1), new DatabaseAdapter.UsernameCallback() {
                @Override
                public void onUsernameCallback(String username) {
                    holder1.usernameTV.setText(username);
                }
            });
        }

        // if we are putting information into a friend view
        if(holder.getItemViewType() == viewTypes.FRIENDS.ordinal()){
            FriendViewHolder holder1 = (FriendViewHolder) holder;
            DatabaseAdapter dba = DatabaseAdapter.getInstance();
            // insert the username into the view
            dba.pullUsernameFromUID(friends.get(position-2-friendRequests.size()), new DatabaseAdapter.UsernameCallback() {
                @Override
                public void onUsernameCallback(String username) {
                    holder1.usernameTV.setText(username);
                }
            });
            // set what happens when the username is clicked
            holder1.usernameTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnListener.onFriendClickListener(
                            holder1.getAdapterPosition()-2-friendRequests.size());
                }
            });
        }
    }


    /**
     * Returns the amount of items in the RecyclerView
     * @return content.size()
     */
    @Override
    public int getItemCount() {
        return friends.size()+friendRequests.size()+2; // +2 for the two headers
    }
}

