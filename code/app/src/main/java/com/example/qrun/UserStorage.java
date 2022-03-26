package com.example.qrun;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * This is the Firestore wrapper for the User Database
 */
public class UserStorage extends Storage{
    /**
     * Initialize the User Storage
     * @param db
     */
    public UserStorage(FirebaseFirestore db) {
        super(db,"User");
    }

    public interface getSortedArray<T> {
        public void getData(ArrayList<T> data);
    }
    public void sortBy(String sortField, @NonNull getSortedArray<User> comp){
        ArrayList<User> list = new ArrayList<User>();
        collectionReference.orderBy(sortField).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        list.add(new User(document));
                    }
                    comp.getData(list);
                } else {
                    Log.d("Storage get()", "get failed with ", task.getException());
                    comp.getData(null);
                }
            }
        });
    }

}
