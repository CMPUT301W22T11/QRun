package com.example.qrun;

import android.location.Location;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;


/**
 * This is the main User class
 */
public class User {
    private String username;
    private String email, name, phoneNumber;
    private long totalscannedqr = 0, totalsum = 0, uniqueqr = 0;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User(String username){
        this.username = username;
    }

    public long getTotalscannedqr() {
        return totalscannedqr;
    }

    public void setTotalscannedqr(long totalscannedqr) {
        this.totalscannedqr = totalscannedqr;
    }

    public long getTotalsum() {
        return totalsum;
    }

    public void setTotalsum(long totalsum) {
        this.totalsum = totalsum;
    }

    public long getUniqueqr() {
        return uniqueqr;
    }

    public void setUniqueqr(long uniqueqr) {
        this.uniqueqr = uniqueqr;
    }
    public User(String username, Map<String, Object> document){
        this.username = username;
        getters(document);
    }
    /**
     * Initialize user by its Document Snapshot
     * @param document
     */
    public User(DocumentSnapshot document){
        this.username = document.getId();
        getters(document.getData());
    }
    public void getters(Map<String, Object> document) {
        name = (String) document.get("name");
        email = (String) document.get("email");
        phoneNumber = (String) document.get("phone");
        Object temp = document.get("totalscannedqr");
        if(temp != null) {
            totalscannedqr = (long)temp;
        }
        temp = document.get("totalpoints");
        if(temp != null) {
            totalsum = (long)temp;
        }
        temp = document.get("highestQR");
        if(temp != null) {
            uniqueqr = (long)temp;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
