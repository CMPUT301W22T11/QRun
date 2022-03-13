package com.example.qrun;


import java.util.Date;
import java.util.UUID;

public class Comment{
    private String uid;
    private String qid;
    private String comment;
    private String commentId;
    private Date date;
    private Long unixTime;
    public Comment(String qid,String uid, String comment) {//constructor to create initial comment
        this.uid = uid;
        this.comment = comment;
        this.qid = qid;
        date = new Date();
        this.unixTime = date.getTime()/1000L;//convert date into unixTime
        this.commentId = UUID.randomUUID().toString();
    }

    public Comment(String qid, String uid, String comment, String commentId) {//constructor to make commentId consistent
        this.uid = uid;
        this.qid = qid;
        this.comment = comment;
        this.commentId = commentId;
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

    public Long getDate() {
        return unixTime;
    }

    public String getCommentId() {
        return commentId;
    }
}