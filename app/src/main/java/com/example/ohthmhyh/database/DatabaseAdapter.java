package com.example.ohthmhyh.database;

import android.util.Log;

import com.example.ohthmhyh.entities.Habit;
import com.example.ohthmhyh.entities.HabitEvent;
import com.example.ohthmhyh.Constants;
import com.example.ohthmhyh.entities.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * The class manages how data is put into and pulled out of the database. Make an instance of this
 * class to be able to get or push the HabitList, HabitEventList, and User objects to/from firebase.
 * This class uses the Singleton design pattern.
 *
 * There are no outstanding issues that we are aware of.
 */
public class DatabaseAdapter{

    // region Interfaces

    /**
     * A callback used when retrieving the users profile from the database.
     */
    public interface ProfileCallback{
        void onProfileCallback(User user);
    }

    /**
     * A callback used when checking if a username is already in use.
     */
    public interface UsernameCheckCallback{
        void onUsernameCheckCallback(boolean usernameExists);
    }

    /**
     * A callback used when turning a UID into a username.
     */
    public interface UsernameCallback{
        void onUsernameCallback(String username);
    }

    /**
     * A callback used when turning a username into a UID
     */
    public interface UIDCallback{
        void onUIDCallback(String UID);
    }

    /**
     * A callback used when retrieving a users habits from the database.
     */
    public interface HabitCallback{
        void onHabitCallback(HabitList hList);
    }

    /**
     * A callback used when retrieving a users habit events from the database.
     */
    public interface HabitEventCallback{
        void onHabitEventCallback(HabitEventList habitEvents);
    }

    /**
     * A general callback used to indicate that a call to the database has returned with the data.
     */
    public interface OnLoadedListener {
        void onLoaded();
    }

    /**
     * A specific callback used to indicate that a call to the database has returned and a list of
     * Habits (from the database) is ready to use.
     */
    public interface OnLoadedHabitsListener {
        void onLoadedHabits(ArrayList<Habit> habits);
    }

    // endregion

    private static DatabaseAdapter instance = null;
    private String UID;
    private FirebaseFirestore db;
    private HabitList habitList = null;
    private HabitEventList habitEventList = null;
    private User user = null;

    /**
     * The singleton constructor for the DatabaseAdapter.
     */
    private DatabaseAdapter() {
        db = FirebaseFirestore.getInstance();
        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    /**
     * The getInstance method for the DatabaseAdapter singleton class.
     * @return The instance of the DatabaseAdapter.
     */
    public static DatabaseAdapter getInstance() {
        if (instance == null) {
            instance = new DatabaseAdapter();
        }
        return instance;
    }

    /**
     * Determines if the data in the DatabaseAdapter instance should be updated. Returns true if the
     * user has changed or any of the data elements are null.
     * @return Whether the DatabaseAdapter should update its data.
     */
    public boolean shouldUpdate() {
        String currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (!UID.equals(currentUserUID) || habitList == null || habitEventList == null || user == null) {
            UID = currentUserUID;
            return true;
        }
        return false;
    }

    /**
     * Call this method to check if a username is in the database. Need to do this when changing a
     * username or when signing up.
     * @param username The username to check the database for
     * @param callback The callback to obtain the result of if the username is in the database
     */
    public static void checkUsernameExists(String username, DatabaseAdapter.UsernameCheckCallback callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query conflicts = db.collection(Constants.PROFILE_COLLECTION_NAME)
                .whereEqualTo(Constants.USERNAME_FIELD_NAME, username);

        conflicts.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){
                    callback.onUsernameCheckCallback(false);
                }
                else{
                    callback.onUsernameCheckCallback(true);
                }
            }
        });
    }

    /**
     * Call this method to get the UID associated with a username
     * @param username The username to get the UID for
     * @param callback The callback which will provide the UID info
     */
    public void pullUIDFromUsername(String username, DatabaseAdapter.UIDCallback callback){
        Query profile = db.collection(Constants.PROFILE_COLLECTION_NAME)
                .whereEqualTo(Constants.USERNAME_FIELD_NAME, username);

        profile.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    callback.onUIDCallback(queryDocumentSnapshots.iterator().next().getId());
                }
                else{
                    callback.onUIDCallback(null);
                }
            }
        });
    }

    /**
     * Pulls all the data for the user (the HabitList, HabitEventList, and the User objects).
     * @param callback The callback used to indicate that this transaction is complete.
     */
    public void pullAll(DatabaseAdapter.OnLoadedListener callback) {
        pullHabitEventList(new OnLoadedListener() {
            @Override
            public void onLoaded() {
                pullHabitList(new OnLoadedListener() {
                    @Override
                    public void onLoaded() {
                        pullUser(new OnLoadedListener() {
                            @Override
                            public void onLoaded() {
                                callback.onLoaded();
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * Creates data (the HabitList, HabitEventList, and the User objects) for a user and pushes them
     * to the database asynchronously.
     * @param username The username to give this user.
     */
    public void createDataForNewUser(String username) {
        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        habitList = new HabitList();
        habitEventList = new HabitEventList();
        user = new User(username);

        pushHabits(habitList);
        pushHabitEvents(habitEventList);
        pushUser(user);
    }

    /**
     * Requests the HabitList from the database.
     * @param callback The callback used to indicate when the transaction is complete.
     */
    public void pullHabitList(DatabaseAdapter.OnLoadedListener callback) {
        DocumentReference documentReference = db.collection("Habits").document(UID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                habitList = documentSnapshot.toObject(HabitList.class);
                Log.d("DatabaseAdapter", "HabitList: " + String.valueOf(habitList.size()));
                callback.onLoaded();
            }
        });
    }

    /**
     * Requests the HabitList from the database for a specific user given their UID.
     * @param callback The callback used to indicate when the transaction is complete.
     */
    public void pullHabitsForUser(String userUID, DatabaseAdapter.OnLoadedHabitsListener callback) {
        DocumentReference documentReference = db.collection("Habits").document(userUID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ArrayList<Habit> habits = new ArrayList<>();
                HabitList habitList = documentSnapshot.toObject(HabitList.class);

                for (int i = 0; i < habitList.size(); i++) {
                    habits.add(habitList.getHabit(i));
                }

                callback.onLoadedHabits(habits);
            }
        });
    }

    /**
     * Requests the HabitEventList from the database.
     * @param callback The callback used to indicate when the transaction is complete.
     */
    public void pullHabitEventList(DatabaseAdapter.OnLoadedListener callback) {
        DocumentReference documentReference = db.collection("HabitEvents").document(UID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                habitEventList = documentSnapshot.toObject(HabitEventList.class);
                Log.d("DatabaseAdapter", "HabitEventList: " + String.valueOf(habitEventList.size()));
                callback.onLoaded();
            }
        });
    }

    /**
     * Requests the User from the database.
     * @param callback The callback used to indicate when the transaction is complete.
     */
    public void pullUser(DatabaseAdapter.OnLoadedListener callback) {
        DocumentReference documentReference = db.collection("Profiles").document(UID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                Log.d("DatabaseAdapter", "User: " + user.getUsername());
                callback.onLoaded();
            }
        });
    }

    // region Habit Methods

    /**
     * Returns the Habit at a given index in the HabitList.
     * @param index The index of the Habit.
     * @return The Habit at the given index.
     */
    public Habit habitAtIndex(int index) {
        return habitList.getHabit(index);
    }

    /**
     * Get the index of a habit in the habitlist
     *
     * This is useful for modifying parameters in a habitList (synced with server) using a non-synced habit
     * @param habit the habit which we are trying to match
     * @return index of habit if successful, -1 if not
     */
    public int indexForHabit(Habit habit) {
        for (int i = 0; i < habitList.size(); i++) {
            if (habitList.getHabit(i).equals(habit)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the number of Habits that the current user has.
     * @return The number of Habits that the current user has.
     */
    public int numberOfHabits() {
        return habitList.size();
    }

    /**
     * Adds a Habit to the user's list of Habits.
     * @param habit The Habit to add.
     */
    public void addHabit(Habit habit) {
        habitList.addHabit(habit);
        pushHabits(habitList);
    }

    /**
     * Removes a Habit from the user's list of Habits.
     * @param index The index of the Habit to remove.
     */
    public void removeHabit(int index) {
        habitList.removeHabit(index);
        pushHabits(habitList);
    }

    /**
     * Set a habit in the list without removing it
     * @param index the index of the habit you want to set
     * @param h the habit you'd like to set
     */
    public void setHabit(int index, Habit h) {
        habitList.setHabit(index, h);
        pushHabits(habitList);
    }

    /**
     * Replaces a Habit at an index.
     * @param index The index of the Habit to replace.
     * @param habit The new Habit to place at this index.
     */
    public void replaceHabit(int index, Habit habit) {
        habitList.replaceHabit(index, habit);
        pushHabits(habitList);
    }

    /**
     * Moves a Habit from one index to another.
     * @param fromIndex The index of the Habit to move.
     * @param toIndex The index of where the Habit should move to.
     */
    public void moveHabit(int fromIndex, int toIndex) {
        habitList.moveHabit(fromIndex, toIndex);
        pushHabits(habitList);
    }

    // endregion

    // region Habit Event Methods

    /**
     * Returns the HabitEvent at the given index.
     * @param index The index of the HabitEvent.
     * @return The HabitEvent at the given index.
     */
    public HabitEvent habitEventAtIndex(int index) {
        return habitEventList.getHabitEvent(index);
    }

    /**
     * Returns the number of HabitEvents this user has.
     * @return The number of HabitEvents this user has.
     */
    public int numberOfHabitEvents() {
        return habitEventList.size();
    }

    /**
     * Adds a HabitEvent to this user's list of HabitEvents.
     * @param habitEvent The HabitEvent to add.
     */
    public void addHabitEvent(HabitEvent habitEvent) {
        habitEventList.addHabitEvent(habitEvent);
        pushHabitEvents(habitEventList);
    }

    /**
     * Removes a HabitEvent from this user's list of HabitEvents.
     * @param index The HabitEvent to remove.
     */
    public void removeHabitEvent(int index) {
        habitEventList.removeHabitEvent(index);
        pushHabitEvents(habitEventList);
    }

    /**
     * Replaces a HabitEvent at a given index with a new HabitEvent.
     * @param index The HabitEvent to replace.
     * @param habitEvent The new HabitEvent.
     */
    public void replaceHabitEvent(int index, HabitEvent habitEvent) {
        habitEventList.replaceHabitEvent(index, habitEvent);
        pushHabitEvents(habitEventList);
    }

    /**
     * Moves a HabitEvent from one index to another.
     * @param fromIndex The index of the HabitEvent to move.
     * @param toIndex The index of the destination of the HabitEvent.
     */
    public void moveHabitEvent(int fromIndex, int toIndex) {
        habitEventList.moveHabitEvent(fromIndex, toIndex);
        pushHabitEvents(habitEventList);
    }

    /**
     * Returns the next UPID of the HabitEvents.
     * @return The next UPID of the HabitEvents.
     */
    public int nextHabitEventUPID() {
        return habitEventList.nextUPID();
    }

    // endregion

    // region User Methods

    /**
     * Returns the username of the current user.
     * @return The username of the current user.
     */
    public String userUsername() {
        return user.getUsername();
    }

    /**
     * Returns the friends list of the current user.
     * @return The friends list of the current user.
     */
    public ArrayList<String> userFriendList() {
        return user.getFriendList();
    }

    /**
     * Returns the friend requests list of the current user.
     * @return The friend requests list of the current user.
     */
    public ArrayList<String> userFriendRequests() {
        return user.getFriendRequests();
    }

    /**
     * Sends a friend request to the person who currently has the username. Make sure the
     * username is valid first!
     */
    public void sendUserFriendRequest(String username) {
        // make sure we aren't sending a friend request to ourself
        if (username.equals(user.getUsername())) {
            return;
        }

        // get the UID of the username to send the request from
        pullUIDFromUsername(username, new DatabaseAdapter.UIDCallback() {
            @Override
            public void onUIDCallback(String otherUID) {
                if (otherUID != null) {
                    //prevent sending friend requests to existing friends.
                    if (user.getFriendList().contains(otherUID)) {
                        return;
                    }

                    // now pull the user we want to send the friend request to
                    pullUser(otherUID, new DatabaseAdapter.ProfileCallback() {
                        @Override
                        public void onProfileCallback(User otherUser) {
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            // prevent sending multiple requests to the same person
                            if (!otherUser.getFriendRequests().contains(currentUser.getUid())) {
                                // add this users UID to the other users friend requests
                                otherUser.getFriendRequests().add(currentUser.getUid());
                            }
                            // push the other user back to the database
                            pushUser(otherUID, otherUser);
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
    public void acceptUserFriendRequest(int index) {
        // get the user who sent the friend request
        String acceptedFriendUID = user.getFriendRequests().get(index);
        // remove the request from the current user
        user.getFriendRequests().remove(index);
        pushUser(user);
        pullUser(acceptedFriendUID, new DatabaseAdapter.ProfileCallback() {
            @Override
            public void onProfileCallback(User friendUser) {
                // add this user to the requesters friends list
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                friendUser.getFriendList().add(currentUser.getUid());
                pushUser(acceptedFriendUID, friendUser);
            }
        });
    }

    /**
     * Deny a friend request by removing it from the friend request list. Push the changes to the
     * database.
     * @param index The index of the friend request to deny.
     */
    public void denyUserFriendRequest(int index) {
        user.getFriendRequests().remove(index);
        pushUser(user);
    }

    /**
     * Remove a friend from the User and push the changes to the database.
     * @param index THe index of the friend to remove.
     */
    public void removeUserFriend(int index) {
        user.getFriendList().remove(index);
        pushUser(user);
    }

    // endregion

    /**
     * Call this method to get the username associated with a UID
     * @param UID The UID to get the username for
     * @param callback The callback which will provide the username info
     */
    public void pullUsernameFromUID(String UID, DatabaseAdapter.UsernameCallback callback){
        DocumentReference profile = db.collection(Constants.PROFILE_COLLECTION_NAME).document(UID);

        profile.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                callback.onUsernameCallback(user.getUsername());
            }
        });
    }

    /**
     * Call with a user profile to update it in the database. This should be called whenever a data
     * member is changed in a user profile.
     * @param user The user profile to push to / update in the database
     */
    public void pushUser(User user){
        pushUser(UID, user);
    }

    /**
     * Call with a user profile to update it in the database. This should be called whenever a data
     * member is changed in a user profile.
     * @param user The user profile to push to / update in the database
     * @param UID The UID of the user profile being pushed
     */
    public void pushUser(String UID, User user){
        db.collection(Constants.PROFILE_COLLECTION_NAME).document(UID).set(user);
    }

    /**
     * Call this method to push the users habits into the database
     * @param habits the list of habits to push into the DB
     */
    public void pushHabits(HabitList habits){
        db.collection(Constants.HABIT_COLLECTION_NAME).document(UID).set(habits);
    }

    /**
     * Call this method to push the users habit events into the database
     * @param events The habit events to push into the DB
     */
    public void pushHabitEvents(HabitEventList events){
        db.collection(Constants.HABIT_EVENT_COLLECTION_NAME).document(UID).set(events);
    }

    /**
     * Call this when a user logs-on in order to retrieve their user profile from the database
     * @param callback A callback which will be used to update the user profile
     */
    public void pullUser(DatabaseAdapter.ProfileCallback callback){
        pullUser(UID, callback);
    }

    /**
     * Call this when a user logs-on in order to retrieve their user profile from the database
     * @param UID the UID of the user to pull
     * @param callback A callback which will be used to update the user profile
     */
    public void pullUser(String UID, DatabaseAdapter.ProfileCallback callback){
        DocumentReference profile = db.collection(Constants.PROFILE_COLLECTION_NAME).document(UID);
        profile.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                callback.onProfileCallback(user);
            }
        });
    }

    /**
     * Call this method to pull the users habits from the database
     * @param callback A callback which is called when the query completes
     */
    public void pullHabits(HabitCallback callback){
        pullHabits(UID, callback);
    }

    /**
     * Call this method to pull the users habits from the database
     * @param callback A callback which is called when the query completes
     * @param UID The uid of the user for which to pull habits
     */
    public void pullHabits(String UID, HabitCallback callback){
        DocumentReference habits = db.collection(Constants.HABIT_COLLECTION_NAME).document(UID);
        // get the users habits
        habits.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                HabitList hList = documentSnapshot.toObject(HabitList.class);
                callback.onHabitCallback(hList);
            }
        });
    }

    /**
     * Call this method to pull this users habit events from the database
     * @param callback A callback which is called when the query completes
     */
    public void pullHabitEvents(HabitEventCallback callback){
        pullHabitEvents(UID, callback);
    }

    /**
     * Call this method to pull another users habit events from the database.
     * @param UID The UID for which to pull the habits of
     * @param callback A callback which is called when the query completes
     */
    public void pullHabitEvents(String UID, HabitEventCallback callback){
        DocumentReference habitEvents = db.collection(Constants.HABIT_EVENT_COLLECTION_NAME)
                .document(UID);
        // get the users habits
        habitEvents.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                HabitEventList HEList = documentSnapshot.toObject(HabitEventList.class);
                callback.onHabitEventCallback(HEList);
            }
        });
    }

}

