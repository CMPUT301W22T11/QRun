package com.example.qrun;



public class Comment{
    private String uid;
    private String qid;
    private String comment;
    public Comment(String qid,String uid, String comment) {
        this.uid = uid;
        this.comment = comment;
        this.qid = qid;
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
