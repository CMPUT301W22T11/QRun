package com.example.qrun;


import com.google.firebase.firestore.FirebaseFirestore;

public class UserStorage extends Storage{
    public UserStorage(FirebaseFirestore db) {
        super(db,"User");
    }
}
