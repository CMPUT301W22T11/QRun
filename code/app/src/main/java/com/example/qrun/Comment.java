package com.example.qrun;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Comment {

    private String uid;
    private String comment;
    private String qid;

    public Comment(String qid, String uid, String comment) {
        this.uid = uid;
        this.comment = comment;
        this.qid = qid;
    }

    public String getUid() {
        return uid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getQid() {
        return qid;
    }
}
