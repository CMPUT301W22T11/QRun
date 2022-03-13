package com.example.qrun;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;


public class QRStorage extends Storage{
    public QRStorage(FirebaseFirestore db) {
        super(db,"QR");
    }
    public void add(HashMap<?, ?> value, @NonNull StoreOnComplete comp) {
        String id = this.getCol().document().getId();
        this.add(id, value, comp);
    }
    public void delete(QRGame qr, @NonNull StoreOnComplete comp) {
        String username = qr.getUsername();
        String hexString = qr.getHexString();
        this.collectionReference.whereEqualTo("username", username)
                .whereEqualTo("hexString", hexString)
                .get().addOnCompleteListener((task) -> {
                if(task.isSuccessful()) {
                    long point = qr.getPoints();
                    QuerySnapshot doc = task.getResult();
                    this.collectionReference.document(doc.getDocuments().get(0).getId()).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("QR Delete", "DocumentSnapshot successfully deleted!");
                            UserStorage storage = new UserStorage(db);
                            storage.get(username, data -> {
                                if(data != null) {
                                    long totalpoints = (long)data.get("totalpoints");
                                    long scanned = (long)data.get("totalscannedqr");
                                    totalpoints -= point;
                                    scanned--;
                                    data.put("totalpoints", totalpoints);
                                    data.put("totalscannedqr", scanned);
                                    storage.update(username, data, isSuccess -> {
                                        if(isSuccess) comp.addFin(true);
                                        else comp.addFin(false);
                                    });
                                }
                                else {
                                    comp.addFin(false);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("QR Delete", "Error deleting document", e);
                            comp.addFin(false);
                        }
                    });
                }
        });
    }

}
