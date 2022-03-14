package com.example.qrun;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class CommentStorage extends Storage {
    public CommentStorage(FirebaseFirestore db) {
        super(db,"Comment");
    }
    public void add(HashMap<?, ?> value, @NonNull StoreOnComplete comp) {
        String id = this.getCol().document().getId();
        this.add(id, value, comp);
    }

}
