package com.example.qrun;

import android.location.Location;

import java.util.ArrayList;

public class User {
    private ArrayList<QR> qrStore = new ArrayList<QR>() ;
    private QR qrGameStatus;
    private int location;
    private String username;
    private ArrayList<Integer> QrCodes;
    public User(int location, String username){
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
    public QR getQR(){
        return qrCode;
    }


    public int getLocation(){
        return location;
    }

    public void setLocation(int location){
        this.location = location;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
