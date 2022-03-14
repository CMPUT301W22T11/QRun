package com.example.qrun;

import android.location.Location;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


/**
 * This is the main User class
 */
public class User {
    private ArrayList<QRGame> qrStore = new ArrayList<>() ;
    private QRGame qrGameStatus;
    private Location location;
    private String username;

    public User(Location location, String username){
        this.location = location;
        this.username = username;
    }

    /**
     * Initialize user by its Document Snapshot
     * @param document
     */
    public User(DocumentSnapshot document){
        this.username = document.getId();
    }
    public void addQR(QRGame qr){
        qrStore.add(qr);
    }
    public ArrayList<QRGame> getStoredQr(){
        return qrStore;
    }
    public void removeQR(QRGame qr){
        qrStore.remove(qr);
    }
    public int getTotalQR(){
        return qrStore.size();
    }
    public User getUser(){
        return this;
    }
    public void setQrGameStatus(QRGame qr){
        qrGameStatus = qr;
    }
    public QRGame getQRGameStatus(){
        return qrGameStatus;
    }


    public Location getLocation(){
        return location;
    }

    public void setLocation(Location location){
        this.location = location;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
