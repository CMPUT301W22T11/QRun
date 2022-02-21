package com.example.qrun;

import android.location.Location;

import java.util.ArrayList;

public class User {
    private ArrayList<QR> qrStore = new ArrayList<QR>() ;
    private QR qrCode;
    public void addQR(QR qr){
        qrStore.add(qr)
    }
    public ArrayList<QR> getQR(){
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
    public void setQR(QR qr){
        qrCode = qr;
    }
    public QR getQR(){
        return qrCode;
    }

}
