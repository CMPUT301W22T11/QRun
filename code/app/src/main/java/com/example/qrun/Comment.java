package com.example.qrun;


import java.util.Date;

public class Comment{
    private String uid;
    private String qid;
    private String comment;
    private Date date;
    public Comment(String qid,String uid, String comment) {
        this.uid = uid;
        this.comment = comment;
        this.qid = qid;
        this.date = new Date();
    }

    public String getUid() {
        return uid;
    }

    public String getQid() {
        return qid;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
}
