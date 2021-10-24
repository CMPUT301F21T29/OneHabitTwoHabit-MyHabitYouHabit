package com.example.ohthmhyh;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DatabaseAdapter{

    private FirebaseFirestore db;
    private String UID;


    public DatabaseAdapter(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UID = user.getUid();
        db = FirebaseFirestore.getInstance();
    }

    public DatabaseAdapter(String UID){
        this.UID = UID;
        db = FirebaseFirestore.getInstance();
    }


    public void pushHabit(Habit habit){

    }


    public ArrayList<Habit> pullHabits(){

        return new ArrayList<Habit>();
    }


//    public void pushHabitEvent(HabitEvent habitEvent){
//
//    }
//
//
//    public ArrayList<HabitEvent> pullHabitEvents(){
//
//        return new ArrayList<HabitEvent>();
//    }


    public void updateUser(User profile){
        Pair<String, User> userPair = new Pair<>(UID, profile);

        db.collection("Profiles")
                .add(userPair)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }


    public User getUser(){
        CollectionReference profiles = db.collection("Profiles");
        Query query = profiles.whereEqualTo("first", UID);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.e("HERE", "HHHHHHEEEEEEEEEEERRRRRRREEEEEEE");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.e("AYY GOT DATA!", document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        return null;
    }


    public ArrayList<User> getFollowing(){

        return null;
    }


}

