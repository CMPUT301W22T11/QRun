package com.example.qrun;


import java.util.ArrayList;

public class UserOld {
    private int location;
    private String username;
    private ArrayList<Integer> QrCodes;
    public UserOld(int location, String username){
        this.location = location;
        this.username = username;
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

    public void deleteQRcodes(int QrCode){
        this.QrCodes.remove(QrCode);
    }
}
