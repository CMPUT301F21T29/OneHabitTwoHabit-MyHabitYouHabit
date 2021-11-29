package com.example.ohthmhyh.adapters;

import android.provider.ContactsContract;
import android.util.Log;
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

    public class FriendRequestViewHolder extends RecyclerView.ViewHolder{

        private TextView usernameTV;
        private Button acceptBtn;
        private Button declineBtn;

        public FriendRequestViewHolder(View view, FriendsRecyclerViewAdapter.buttonListener btnListener){
            super(view);
            // get all the views in the item_friend_request view
            usernameTV = view.findViewById(R.id.request_username_tv);
            acceptBtn  = view.findViewById(R.id.accept_btn);
            declineBtn = view.findViewById(R.id.decline_btn);
            
        }
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder{
        private TextView usernameTV;

        public FriendViewHolder(View view){
            super(view);
            // get all the views in the item_friend_request view
            usernameTV = view.findViewById(R.id.item_friend);
        }
    }

    public class PlainTextViewHolder extends RecyclerView.ViewHolder{
        private TextView headerTV;

        public PlainTextViewHolder(View view){
            super(view);
            headerTV = view.findViewById(R.id.headerTV);
        }
    }

    /**
     * Used when setting the functions that are called when buttons in the listView are pressed
     */
    public interface buttonListener {
        public void onAcceptClickListener(int position);
        public void onDeclineClickListener(int position);
    }


    private ArrayList<String> friends;
    private ArrayList<String> friendRequests;
    private enum viewTypes {FRIENDS, REQUESTS, HEADER};
    private FriendsRecyclerViewAdapter.buttonListener btnListener;


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

    @Override
    public int getItemViewType(int position){
        if(position == 0){
            return viewTypes.HEADER.ordinal();
        }

        if(position > 0 && position < friendRequests.size()+1){
            return viewTypes.REQUESTS.ordinal();
        }

        if(position == friendRequests.size()+1){
            return viewTypes.HEADER.ordinal();
        }

        return viewTypes.FRIENDS.ordinal();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == viewTypes.HEADER.ordinal()){
            return new PlainTextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plain_text, parent,false));
        }

        if(viewType == viewTypes.REQUESTS.ordinal()){
            return new FriendRequestViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_request, parent,false),
                    btnListener);
        }

        return new FriendViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType() == viewTypes.HEADER.ordinal()){
            PlainTextViewHolder holder1 = (PlainTextViewHolder)holder;

            if (position == 0) holder1.headerTV.setText("Friend Requests");
            if (position == friendRequests.size()) holder1.headerTV.setText("Friends");
        }

        if(holder.getItemViewType() == viewTypes.REQUESTS.ordinal()){
            Log.e("HERE IN THE ", "REQUESTS");
            FriendRequestViewHolder holder1 = (FriendRequestViewHolder) holder;
            DatabaseAdapter dba = DatabaseAdapter.getInstance();
            dba.pullUsernameFromUID(friendRequests.get(position-1), new DatabaseAdapter.UsernameCallback() {
                @Override
                public void onUsernameCallback(String username) {
                    holder1.usernameTV.setText(username);
                }
            });
        }

        if(holder.getItemViewType() == viewTypes.FRIENDS.ordinal()){
            Log.e("HERE IN THE ", "FRIENDS");
            FriendViewHolder holder1 = (FriendViewHolder) holder;
            DatabaseAdapter dba = DatabaseAdapter.getInstance();
            dba.pullUsernameFromUID(friends.get(position-2-friendRequests.size()), new DatabaseAdapter.UsernameCallback() {
                @Override
                public void onUsernameCallback(String username) {
                    holder1.usernameTV.setText(username);
                }
            });
        }

        Log.e("HERE IN THE ", "NOT HEADER");
    }


    @Override
    public int getItemCount() {
        return friends.size()+friendRequests.size()+2;
    }
}

