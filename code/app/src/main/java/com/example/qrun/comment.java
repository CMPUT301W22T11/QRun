package com.example.qrun;

import java.util.Date;

public class comment implements commentable{
    private String uid;
    private Date date;
    private String message;
    public comment(String uid, String message) {
        this.uid = uid;
        this.message = message;
    }
    public String getUid() {
        return uid;
    }


    public Date getDate() {
        return date;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
