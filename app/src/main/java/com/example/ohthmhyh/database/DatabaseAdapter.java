package com.example.ohthmhyh.database;

import android.util.Log;

import com.example.ohthmhyh.entities.Habit;
import com.example.ohthmhyh.entities.HabitEvent;
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
 * The class manages how data is put into and pulled out of the database. Make an instance
 * of this class to be able to get or push the HabitList, HabitEventList, and User objects
 * to/from firebase.
 */
public class DatabaseAdapter{

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

    public interface OnLoadedListener {
        void onLoaded();
    }

    public interface OnLoadedHabitsListener {
        void onLoadedHabits(ArrayList<Habit> habits);
    }


    private static DatabaseAdapter instance = null;
    private String UID;
    private FirebaseFirestore db;
    private HabitList habitList = null;
    private HabitEventList habitEventList = null;
    private User user = null;


    private DatabaseAdapter() {
        db = FirebaseFirestore.getInstance();
        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

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
        Query conflicts = db.collection("Profiles").whereEqualTo("username", username);

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
        Query profile = db.collection("Profiles").whereEqualTo("username", username);

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


    public void pullAll(DatabaseAdapter.OnLoadedListener callback) {
        if (UID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            Log.d("DatabaseAdapter'", String.valueOf(habitList.size()));
            Log.d("DatabaseAdapter'", String.valueOf(habitEventList.size()));
            Log.d("DatabaseAdapter'", user.getUsername());
            callback.onLoaded();
        } else {
            UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseAdapter.this.pullHabitEventList(new OnLoadedListener() {
                @Override
                public void onLoaded() {
                    DatabaseAdapter.this.pullHabitList(new OnLoadedListener() {
                        @Override
                        public void onLoaded() {
                            DatabaseAdapter.this.pullUser(new OnLoadedListener() {
                                @Override
                                public void onLoaded() {
                                    Log.d("DatabaseAdapter", String.valueOf(habitList.size()));
                                    Log.d("DatabaseAdapter", String.valueOf(habitEventList.size()));
                                    Log.d("DatabaseAdapter", user.getUsername());
                                    callback.onLoaded();
                                }
                            });
                        }
                    });
                }
            });
        }
    }


    public void createDataForNewUser(String username) {
        habitList = new HabitList();
        habitEventList = new HabitEventList();
        user = new User(username);

        pushHabits(habitList);
        pushHabitEvents(habitEventList);
        pushUser(user);
    }


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



    public User getUser() {
        return user;
    }


    /**
     * ----------------------- Habit methods. -----------------------
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

    public int numberOfHabits() {
        return habitList.size();
    }

    public void addHabit(Habit habit) {
        habitList.addHabit(habit);
        pushHabits(habitList);
    }

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

    public void replaceHabit(int index, Habit habit) {
        habitList.replaceHabit(index, habit);
        pushHabits(habitList);
    }

    public void moveHabit(int fromIndex, int toIndex) {
        habitList.moveHabit(fromIndex, toIndex);
        pushHabits(habitList);
    }


    /**
     * ----------------------- HabitEvent methods. -----------------------
     */


    public HabitEvent habitEventAtIndex(int index) {
        return habitEventList.getHabitEvent(index);
    }

    public int numberOfHabitEvents() {
        return habitEventList.size();
    }

    public void addHabitEvent(HabitEvent habitEvent) {
        habitEventList.addHabitEvent(habitEvent);
        pushHabitEvents(habitEventList);
    }

    public void removeHabitEvent(int index) {
        habitEventList.removeHabitEvent(index);
        pushHabitEvents(habitEventList);
    }

    public void replaceHabitEvent(int index, HabitEvent habitEvent) {
        habitEventList.replaceHabitEvent(index, habitEvent);
        pushHabitEvents(habitEventList);
    }

    public void moveHabitEvent(int fromIndex, int toIndex) {
        habitEventList.moveHabitEvent(fromIndex, toIndex);
        pushHabitEvents(habitEventList);
    }

    public int nextHabitEventUPID() {
        return habitEventList.nextUPID();
    }


    /**
     * ----------------------- User methods. -----------------------
     */

    public String userUsername() {
        return user.getUsername();
    }

    public ArrayList<String> userFriendList() {
        return user.getFriendList();
    }

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

    /**
     * Changes the user's bio in the database to be the given String.
     * @param bio The new bio.
     */
    public void updateUserBio(String bio) {
        user.setBio(bio);
        pushUser(user);
    }


    /**
     * Call this method to get the username associated with a UID
     * @param UID The UID to get the username for
     * @param callback The callback which will provide the username info
     */
    public void pullUsernameFromUID(String UID, DatabaseAdapter.UsernameCallback callback){
        DocumentReference profile = db.collection("Profiles").document(UID);

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
        db.collection("Profiles").document(UID).set(user);
    }

    /**
     * Call this method to push the users habits into the database
     * @param habits the list of habits to push into the DB
     */
    public void pushHabits(HabitList habits){
        db.collection("Habits").document(UID).set(habits);
    }


    /**
     * Call this method to push the users habit events into the database
     * @param events The habit events to push into the DB
     */
    public void pushHabitEvents(HabitEventList events){
        db.collection("HabitEvents").document(UID).set(events);
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
        DocumentReference profile = db.collection("Profiles").document(UID);
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
        DocumentReference habits = db.collection("Habits").document(UID);
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
        DocumentReference habitEvents = db.collection("HabitEvents").document(UID);
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

//////////////////////////////////////////////////////////////////
// works, but probably not super efficient. Kept for posterity ///
//////////////////////////////////////////////////////////////////

//    private void pushHabits(User user, WriteBatch batch){
//        DocumentReference habits = db.collection("Habits").document(UID);
//        ArrayList<Map<String, Object>> allHabits = new ArrayList<>();
//
//        // make an arraylist with the essence of each habit
//        for(int i=0; i<user.getHabitList().size(); i++){
//            Map<String, Object> habitData = new HashMap<>();
//            habitData.put("name", user.getHabitList().get(i).getName());
//            habitData.put("description", user.getHabitList().get(i).getDescription());
//            habitData.put("startDate", user.getHabitList().get(i).getStartDate());
//            habitData.put("schedule", user.getHabitList().get(i).getSchedule());
//            allHabits.add(habitData);
//        }
//
//        // next line obtained through cosmic rituals
//        HashMap<String, ArrayList<Map<String, Object>>> field = new HashMap<>();
//        field.put("habits", allHabits);
//        batch.set(habits, field);
//    }

