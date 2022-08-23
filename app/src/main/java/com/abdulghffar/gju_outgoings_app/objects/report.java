package com.abdulghffar.gju_outgoings_app.objects;

import java.util.ArrayList;
import java.util.Map;

public class report {
    String commentText;
    String reference;
    String timeStamp;
    String uid;
    ArrayList<String> users;

    public report() {
    }

    public report(String commentText, String reference, String timeStamp, String uid, ArrayList<String> users) {
        this.commentText = commentText;
        this.reference = reference;
        this.timeStamp = timeStamp;
        this.uid = uid;
        this.users = users;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

    public String getCommentText() {
        return commentText;
    }

    public String getReference() {
        return reference;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getUid() {
        return uid;
    }

    public ArrayList<String> getReportedBy() {
        return users;
    }
}
