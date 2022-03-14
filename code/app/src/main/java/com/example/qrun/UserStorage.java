package com.example.qrun;


import com.google.firebase.firestore.FirebaseFirestore;


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
}
