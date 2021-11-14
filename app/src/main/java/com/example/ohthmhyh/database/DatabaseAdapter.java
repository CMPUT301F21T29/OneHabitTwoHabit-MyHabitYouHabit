package com.example.ohthmhyh.database;

import com.example.ohthmhyh.entities.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

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

    // instance variables
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
     * @param UID force the database to use this UID for pushing/pulling data
     */
    public DatabaseAdapter(String UID){
        this.UID = UID;
        db = FirebaseFirestore.getInstance();
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

