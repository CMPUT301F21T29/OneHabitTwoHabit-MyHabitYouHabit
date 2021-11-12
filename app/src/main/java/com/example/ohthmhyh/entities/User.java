package com.example.ohthmhyh.entities;

import com.example.ohthmhyh.database.DatabaseAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * The entity class for a user. This class represents a user, and the parameters related
 * to a user (username, friend list, etc.). Make an instance of this class to
 * represent a user of the app.
 */
public class User {
    // instance variables
    private String username;
    private String name;
    private String bio;
    private ArrayList<String> friendList, friendRequests;
    private int UPIDCounter;
    private DatabaseAdapter dba;

    /**
     * Construct a new, empty user. You MUST manually declare all of the instance
     * variables when using this constructor.
     */
    public User(){
        dba = new DatabaseAdapter();
    }


    /**
     * Construct a new user, specifying their username now. Check to make sure
     * that the username is not in use first!
     * @param username The username to give this user.
     */
    public User(String username)  {
        this.UPIDCounter = 0;
        this.username = username;
        this.friendList = new ArrayList<String>();
        this.friendRequests = new ArrayList<String>();
        dba = new DatabaseAdapter();
    }

    /**
     * Get the users username
     * @return String.
     */
    public String getUsername() {
        return username;
    }


    /**
     * Set the users username
     * @return String.
     */
    public void setUsername(String username) {
        this.username = username;
    }


    /**
     * update the users username. Check if the username has been taken first!
     */
    public void updateUsername(String username) {
        this.username = username;
        dba.pushUser(this);
    }


    /**
     * Set the users friend list as a list of UID's
     * @param friends  The users friend list as UID's
     */
    public void setFriendList(ArrayList<String> friends){
        this.friendList = friends;
    }


    /**
     * Get the users friend list as a list of UID's
     * @return  The users friend list as UID's
     */
    public ArrayList<String> getFriendList(){
        return friendList;
    }


    /**
     * Set the users incoming friend request list as a list of UID's
     * @param friendRequests  The users friend request list as UID's
     */
    public void setFriendRequests(ArrayList<String> friendRequests){
        this.friendRequests = friendRequests;
    }


    /**
     * Get the users incoming friend request list as a list of UID's
     * @return  The users friend request list as UID's
     */
    public ArrayList<String> getFriendRequests(){
        return friendRequests;
    }


    /**
     * Sends a friend request to the person who currently has the username. Make sure the
     * username is valid first!
     */
    public void sendFriendRequest(String username){
        // get the UID of the username to send the request from
        dba.pullUIDFromUsername(username, new DatabaseAdapter.UIDCallback() {
            @Override
            public void onUIDCallback(String UID) {
                if(UID != null){

                    // now pull the user we want to send the friend request to
                    dba.pullUser(UID, new DatabaseAdapter.ProfileCallback() {
                        @Override
                        public void onProfileCallback(User otherUser) {
                            // add this users UID to the other users friend requests
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            // prevent sending multiple requests to the same person
                            if(!otherUser.getFriendRequests().contains(currentUser.getUid())) {
                                otherUser.getFriendRequests().add(currentUser.getUid());
                            }
                            // push the other user back to the database
                            dba.pushUser(UID, otherUser);
                        }
                    });
                }
            }
        });
    }


    /**
     * Accept an incoming friend request by moving it from the friend request list to
     * the friends list. Also update the other users friend list.
     * @param index the index of the friend request to accept
     */
    public void acceptFriendRequest(int index){
        // get the user who sent the friend request
        String UID = friendRequests.get(index);
        dba.pullUser(UID, new DatabaseAdapter.ProfileCallback() {
            @Override
            public void onProfileCallback(User user) {
                // add this user to the requesters friends list
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                user.getFriendList().add(currentUser.getUid());
                dba.pushUser(UID, user);

                // remove the request from the current user
                friendRequests.remove(index);
            }
        });
    }


    /**
     * Deny a friend request by removing it from the friend request list.
     * @param index the index of the friend request to accept
     */
    public void denyFriendRequest(int index){
        friendRequests.remove((index));
        dba.pushUser(this);
    }


    /**
     * Removes a friend
     * @param index the index of the friend to remove
     */
    public void removeFriend(int index){
        friendList.remove((index));
        dba.pushUser(this);
    }


    /**
     * Get the biography of the user
     * @return The user bio as string
     */
    public String getBio() {
        return this.bio;
    }


    /**
     * Set the biography of the user
     * @param value Biography set by the user.
     */
    public void setBio(String value) {
        this.bio = value;
    }


    /**
     * update the users bio.
     */
    public void updateBio(String bio) {
        this.bio = bio;
        dba.pushUser(this);
    }


    /**
     * Get the name of the user
     * @return name of the user
     */
    public String getName() {
        return this.name;
    }


    /**
     * Set the name of the user
     * @param name the name of the user
     */
    public void setName(String name) {
        this.name = name;
    }

}

