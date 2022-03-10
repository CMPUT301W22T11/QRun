package com.example.qrun;

import com.google.firebase.firestore.FirebaseFirestore;

public class QRStorage extends Storage{
    public QRStorage(FirebaseFirestore db) {
        super(db,"QR");
    }
}
