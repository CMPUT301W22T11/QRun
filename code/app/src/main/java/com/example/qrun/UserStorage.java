package com.example.qrun;


import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * This is the Firestore wrapper for the User Database
 */
public class UserStorage extends Storage{
    public interface getSortedArray<T> {
        void getData(ArrayList<T> data);
    }
    /**
     * Initialize the User Storage
     * @param db
     */
    public UserStorage(FirebaseFirestore db) {
        super(db,"User");
    }
    public void updateUser(@NonNull String username, @NonNull QRStorage qrStorage, @NonNull StoreOnComplete fn) {
        this.get(username, (userData) -> {
            if(userData != null) {
                qrStorage.getCol().whereEqualTo("username", username)
                        .orderBy("points", Query.Direction.DESCENDING)
                        .get()
                        .addOnCompleteListener((task) -> {
                            if (task.isSuccessful()) {
                                QuerySnapshot document = task.getResult();
                                userData.put("totalscannedqr", document.size());
                                long totalpoint = 0;
                                for (QueryDocumentSnapshot i : document) {
                                    totalpoint += (long) i.get("points");
                                }
                                userData.put("totalpoints", totalpoint);
                                if(document.size() != 0) {
                                    userData.put("highestQR", document.getDocuments().get(0).get("points"));
                                }
                                else {
                                    userData.put("highestQR", 0);
                                }
                                this.update(username, userData, (isUserSuccess) -> {
                                    if (isUserSuccess) {
                                        fn.addFin(true);
                                    } else {
                                        fn.addFin(false);
                                    }
                                });
                            }
                        })
                        .addOnFailureListener((l) -> fn.addFin(false));
            }
            else {
                fn.addFin(false);
            }
        });
    }
}
