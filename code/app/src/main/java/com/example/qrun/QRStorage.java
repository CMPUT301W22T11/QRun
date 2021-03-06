package com.example.qrun;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

/**
 * This is the Firestore Wrapper for QRGame
 */
public class QRStorage extends Storage{
    public QRStorage(FirebaseFirestore db) {
        super(db,"QR");
    }

    /**
     * create new document with autogenerated id
     * @param value document fields
     * @param comp store if accomplished
     */
    public void add(HashMap<?, ?> value, @NonNull StoreOnComplete comp) {
        String id = this.getCol().document().getId();
        this.add(id, value, comp);
    }

    /**
     * Delete the QR based on the QRGame
     * @param qr the qr that is going to be deleted
     * @param comp lambda expression to check if QR has been successfully deleted or not
     */
    public void delete(@NonNull QRGame qr, @NonNull StoreOnComplete comp) {
        QRStorage temp = this;
        String username = qr.getUsername();
        String hexString = qr.getHexString();
        this.collectionReference.whereEqualTo("username", username)
                .whereEqualTo("hexString", hexString)
                .get().addOnCompleteListener((task) -> {
                if(task.isSuccessful()) {
                    long point = qr.getPoints();
                    QuerySnapshot doc = task.getResult();
                    Object path = doc.getDocuments().get(0).get("PicPath");
                    this.collectionReference.document(doc.getDocuments().get(0).getId())
                            .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("QR Delete", "DocumentSnapshot successfully deleted!");
                            UserStorage storage = new UserStorage(db);

                            if(path != null) {
                                String actualPath = (String)path;
                                if(actualPath.length() != 0) {
                                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                    StorageReference imgRef = storageRef.child((String) path);
                                    imgRef.delete().addOnCompleteListener((l) -> {
                                        storage.updateUser(username, temp, comp);
                                    });
                                }
                                else {
                                    storage.updateUser(username, temp, comp);
                                }
                            }
                            else {
                                storage.updateUser(username, temp, comp);
                            }
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
                else {
                    comp.addFin(false);
                }
        });
    }

}
