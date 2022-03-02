package com.example.qrun;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Comment {

    private String uid;
    private String comment;

    public Comment(String uid, String comment) {
        this.uid = uid;
        this.comment = comment;
    }

    public String getUid() {
        return uid;
    }

//    public void setUid(String uid) {
//        this.uid = uid;
//    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
