package com.example.qrun;


import java.util.Date;
import java.util.UUID;


/**
 * This is the comment class
 */
public class Comment{
    private String uid;
    private String qid;
    private String comment;
    private String commentId;
    private Date date;
    private Long unixTime;

    /**
     * constructor to create initial comment
     * @param qid QR Hash String
     * @param uid username
     * @param comment content of the comment
     */
    public Comment(String qid,String uid, String comment) {//
        this.uid = uid;
        this.comment = comment;
        this.qid = qid;
        date = new Date();
        this.unixTime = date.getTime()/1000L;//convert date into unixTime
        this.commentId = UUID.randomUUID().toString();
    }

    /**
     * constructor to make commentId consistent
     * @param qid QR Hash String
     * @param uid username
     * @param comment content of the comment
     * @param commentId the comment id
     */
    public Comment(String qid, String uid, String comment, String commentId) {
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