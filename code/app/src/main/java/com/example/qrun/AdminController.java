package com.example.qrun;

import android.content.Intent;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdminController {
    Integer numberOfQR = 0;
    QRStorage qrStorage;
    UserStorage userStorage;
    CommentStorage commentStorage;
    Storage.StoreOnComplete comp;
    private final Object numberOfQRMutx = new Object();
    public AdminController() {
        qrStorage = new QRStorage(FirebaseFirestore.getInstance());
        userStorage = new UserStorage(FirebaseFirestore.getInstance());
        commentStorage = new CommentStorage(FirebaseFirestore.getInstance());
    }
    /**
     * Delete the QR along with its pictures and update the user as well
     * @param hexString the QR to be deleted
     * @param comp lambda function for checking if compression is working or not
     */
    public void deleteQRBatch(String hexString, Storage.StoreOnComplete comp) {
        this.comp = comp;
        commentStorage.getCol().whereEqualTo("qrCommented", hexString)
                .get().addOnCompleteListener((task) -> {
                    if(task.isSuccessful()) {
                        WriteBatch batch = FirebaseFirestore.getInstance().batch();
                        for(QueryDocumentSnapshot i : task.getResult()) {
                            batch.delete(commentStorage.getCol().document(i.getId()));
                        }
                        batch.commit().addOnCompleteListener((task2) -> {
                            if(task2.isSuccessful()) {
                                qrStorage.getCol().whereEqualTo("hexString", hexString)
                                        .get().addOnCompleteListener((task3) -> {
                                            if(task3.isSuccessful()) {
                                                numberOfQR = task3.getResult().getDocuments().size();
                                                for(QueryDocumentSnapshot i : task3.getResult()) {
                                                    QRGame qrGame = new QRGame(i);
                                                    qrStorage.delete(qrGame, isSuccess -> {
                                                        if(isSuccess) {
                                                            synchronized (numberOfQRMutx) {
                                                                numberOfQR--;
                                                                if(numberOfQR == 0) {
                                                                    comp.addFin(true);
                                                                }
                                                            }
                                                        }
                                                        else {
                                                            comp.addFin(false);
                                                        }
                                                    });
                                                }
                                            }
                                });
                            }
                            else {
                                comp.addFin(false);
                            }
                        });
                    }
                    else {
                        comp.addFin(false);
                    }
        });
    }

    /**
     * Delete the user alongs with its assets
     * @param user User to be deleted
     * @param comp lambda function for checking if compression is working or not
     */
    public void deleteUser(User user, Storage.StoreOnComplete comp) {
        this.comp = comp;
        ArrayList<String> picPath = new ArrayList<>();
        commentStorage.getCol().whereEqualTo("commenter", user.getUsername())
                .get().addOnCompleteListener((task) -> {
            if(task.isSuccessful()) {
                WriteBatch batch = FirebaseFirestore.getInstance().batch();
                for(QueryDocumentSnapshot i : task.getResult()) {
                    batch.delete(commentStorage.getCol().document(i.getId()));
                }
                qrStorage.getCol().whereEqualTo("username", user.getUsername())
                        .get().addOnCompleteListener((task2) -> {
                            if (task2.isSuccessful()) {
                                numberOfQR = 0;
                                for(QueryDocumentSnapshot i : task2.getResult()) {
                                    Object picPathTemp = i.get("PicPath");
                                    if(picPathTemp != null) {
                                        String pic = (String)picPathTemp;
                                        if(pic.length() != 0) {
                                            picPath.add(pic);
                                        }
                                    }
                                    batch.delete(qrStorage.getCol().document(i.getId()));
                                }
                                batch.delete(userStorage.getCol().document(user.getUsername()));
                                batch.commit().addOnCompleteListener((task3) -> {
                                    if(task3.isSuccessful()) {
                                        numberOfQR = picPath.size();
                                        if(picPath.size() == 0) comp.addFin(true);
                                        for(String i : picPath) {
                                            StorageReference pictureDeletion = FirebaseStorage
                                                    .getInstance().getReference()
                                                    .child(i);
                                            pictureDeletion.delete().addOnCompleteListener((task4) -> {
                                                if(task4.isSuccessful()) {
                                                    synchronized (numberOfQRMutx) {
                                                        numberOfQR--;
                                                        if(numberOfQR == 0) {
                                                            comp.addFin(true);
                                                        }
                                                    }
                                                }
                                                else {
                                                    comp.addFin(false);
                                                }
                                            });
                                        }
                                    }
                                    else {
                                        comp.addFin(false);
                                    }
                                });
                            }
                            else {
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
