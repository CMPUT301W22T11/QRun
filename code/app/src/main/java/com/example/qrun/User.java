package com.example.qrun;

import android.location.Location;

import java.util.ArrayList;

public class User {
    private ArrayList<QR> qrStore = new ArrayList<QR>() ;
    private QR qrGameStatus;
    private Location location;
    private String username;

    public User(Location location, String username){
        this.location = location;
        this.username = username;
    }
    public void addQR(QR qr){
        qrStore.add(qr);
    }
    public ArrayList<QR> getStoredQr(){
        return qrStore;
    }
    public void removeQR(QR qr){
        qrStore.remove(qr);
    }
    public int getTotalQR(){
        return qrStore.size();
    }
    public User getUser(){
        return this;
    }
    public void setQrGameStatus(QR qr){
        qrGameStatus = qr;
    }
    public QR getQRGameStatus(){
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
