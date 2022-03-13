package com.example.qrun;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Storage {
    public interface StoreOnComplete {
        void addFin(boolean isSuccess);
    }
    public  interface GetOnComplete {
        void getFin(Map<String, Object> data);
    }
    FirebaseFirestore db;
    CollectionReference collectionReference = null;
    public Storage(FirebaseFirestore db, String colName) {
        this.db = db;
        collectionReference = this.db.collection(colName);
    }
    public CollectionReference getCol() {
        return collectionReference;
    }
    public void add(String docName, HashMap<?, ?> value, @NonNull StoreOnComplete comp) {
        collectionReference
            .document(docName)
            .set(value)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // These are a method which gets executed when the task is succeeded
                    Log.d("storage add()", "Document is successfully added!");
                    comp.addFin(true);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // These are a method which gets executed if there’s any problem
                    Log.w("storage add()", "Error updating document", e);
                    comp.addFin(false);
                }
            });
    }
    public void update(String docName, @NonNull HashMap<String, Object> data, @NonNull StoreOnComplete comp) {
        collectionReference.document(docName).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("storage update()", "DocumentSnapshot successfully updated!");
                comp.addFin(true);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("storage update()", "Error updating document", e);
                comp.addFin(false);
            }
        });
    }
    public void get(String docName, @NonNull GetOnComplete comp) {
        collectionReference.document(docName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        comp.getFin(document.getData());
                    } else {
                        Log.d("Storage get()", "No such document");
                        comp.getFin(null);
                    }
                } else {
                    Log.d("Storage get()", "get failed with ", task.getException());
                    comp.getFin(null);
                }
            }
        });
    }
    public void delete(String docName, @NonNull StoreOnComplete comp) {
        collectionReference.document(docName).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("storage delete()", "DocumentSnapshot successfully deleted!");
                comp.addFin(true);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("storage delete()", "Error deleting document", e);
                comp.addFin(false);
            }
        });
    }

}
