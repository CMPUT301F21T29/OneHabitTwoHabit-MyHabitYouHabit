package com.example.ohthmhyh;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firestore.v1.WriteResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseAdapter{

    /**
     * A callback used when retrieving the users profile from the database.
     */
    public interface ProfileCallback{
        void onProfileCallback(User profile);
    }

    /**
     * A callback used when checking if a username is already in use.
     */
    public interface UsernameCheckCallback{
        void onUsernameCheckCallback(boolean usernameExists);
    }

    /**
     * A callback used when retrieving a users habits from the database.
     */
    private interface HabitCallback{
        void onHabitCallback(ArrayList<Habit> habits);
    }


    public interface HabitEventCallback{
//        void onHabitEventCallback(ArrayList<HabitEvent> habitEvents);
    }


//    public interface FollowingCallback{
//        void onFollowingCallback(ArrayList<String> following);
//    }

    private static FirebaseFirestore db;
    private static String UID;


    /**
     * Create an instance of the database adapter. UID will automatically be set. This is the
     * constructor you should probably be using.
     */
    public DatabaseAdapter(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UID = user.getUid();
        db = FirebaseFirestore.getInstance();
    }


    /**
     * Create an instance of the database adapter, specifying a UID. Ideally, this should only be
     * used while testing as specifying the wrong UID can cause database errors.
     */
    public DatabaseAdapter(String UID){
        this.UID = UID;
        db = FirebaseFirestore.getInstance();
    }


    /**
     * Call this method to check if a username is in the database. Need to do this when changing a
     * username or when signing up.
     * @param username The username to check the database for
     * @
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
     * Call this method to push the users habits into the database
     * @param user The user to pull the habits from
     * @param batch the database batch write to put the habits into.
     */
    private void pushHabits(User user, WriteBatch batch){
        DocumentReference habits = db.collection("Habits").document(UID);

        HashMap<String, ArrayList<Habit>> field = new HashMap<>();
        field.put("habits", user.getHabitList());

        batch.set(habits, field);
    }


    /**
     * Call this method to push the users habit events into the database
     * @param user The user to pull the habit events from
     * @param batch the database batch write to put the habits into.
     */
    private void pushHabitEvents(User user, WriteBatch batch){
        //TODO: set up pushing habit events. Need Stu's code first tho.
    }


    /**
     * Call with a user profile to update it in the database. This should be called whenever a data
     * member is changed in a user profile.
     * @param user The user profile to push to / update in the database
     */
    public void updateUser(User user){
        WriteBatch batch = db.batch();
        DocumentReference profile = db.collection("Profiles").document(UID);

        // make a hashmap with the essence of a user
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", user.getUsername());
        userData.put("UHIDcounter", user.getUHIDCounter());
        userData.put("UPIDcounter", user.getUPIDCounter());

        // add the habits and habit events to the batch write too
        batch.set(profile, userData);
        pushHabits(user, batch);
        pushHabitEvents(user, batch);
        // finalize the write
        batch.commit();
    }


    /**
     * Call this method to pull the users habits from the database
     * @param callback A callback which is called when the query completes
     */
    private void pullHabits(HabitCallback callback){
        DocumentReference habits = db.collection("Habits").document(UID);
        // get the users habits
        habits.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //ArrayList<Habit> habitList = new ArrayList<>();

                // next line obtained through cosmic rituals
                HashMap<String, ArrayList<Habit>> habitslist = (HashMap<String, ArrayList<Habit>>) documentSnapshot.get("Habits");
                callback.onHabitCallback(habitslist.get("habits"));
            }
        });
    }


    /**
     * Call this method to pull the users habit events from the database
     * @param callback A callback which is called when the query completes
     */
    private void pullHabitEvents(HabitEventCallback callback){
        //TODO: set up pulling habit events. Need Stu's code first tho.
    }


    /**
     * Call this when a user logs-on in order to retrieve their user profile from the database
     * @param callback A callback which will be used to update the user profile
     */
    public void getUser(DatabaseAdapter.ProfileCallback callback){
        DocumentReference profile = db.collection("Profiles").document(UID);
        profile.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = new User();
                // rebuild the user object from the database query
                user.setUsername(documentSnapshot.get("username", String.class));
                user.setUHIDCounter(documentSnapshot.get("UHIDcounter", Integer.class));
                user.setUPIDCounter(documentSnapshot.get("UPIDcounter", Integer.class));

                // retrieve the users habits from the database
                pullHabits(new HabitCallback() {
                    @Override
                    public void onHabitCallback(ArrayList<Habit> habits) {
                        user.setHabitList(habits);
                    }
                });

                //TODO: pull the users habit events. Need Stu's code first.

                callback.onProfileCallback(user);
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

