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
    public interface HabitCallback{
        void onHabitCallback(ArrayList<Habit> habits);
    }


//    public interface FollowingCallback{
//        void onFollowingCallback(ArrayList<String> following);
//    }


//    public interface HabitEventsCallback{
//        void onHabitEventsCallback(ArrayList<HabitEvent> habitEvents);
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
     * Call with a user profile to update it in the database. This should be called whenever a data
     * member is changed in a user profile.
     * @param user The user profile to push to / update in the database
     */
    public void updateUser(User user){
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", user.getUsername());
        userData.put("UHIDcounter", user.getUHIDCounter());
        userData.put("UPIDcounter", user.getUPIDCounter());

        db.collection("Profiles").document(UID).set(userData);
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
                user.setUsername(documentSnapshot.get("username", String.class));
                user.setUHIDCounter(documentSnapshot.get("UHIDcounter", Integer.class));
                user.setUPIDCounter(documentSnapshot.get("UPIDcounter", Integer.class));
                callback.onProfileCallback(user);
            }
        });
    }





////////////////////////////////////////////////
//// warning: stuff below is likely broken! ////
////////////////////////////////////////////////

//    // get the habits of another user by UID
//    public void getHabits(String UID, DatabaseAdapter.HabitCallback callback){
//        DocumentReference profile = db.collection("Profiles").document(UID);
//        profile.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                User profile = documentSnapshot.toObject(User.class);
//                callback.onHabitCallback(profile.getHabitList());
//            }
//        });
//    }

//    public void pushHabit(Habit habit){
//        db.collection("Habits")
//                .document(UID)
//                .collection("Habits")
//                .document(Integer.toString(habit.getUHID()))
//                .set(habit)
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e(TAG, "Error adding document", e);
//                    }
//                });
//    }


//    public void pushHabitEvent(HabitEvent habitEvent){
//
//    }
//
//
//    public ArrayList<HabitEvent> pullHabitEvents(){
//
//        return new ArrayList<HabitEvent>();
//    }

}

