package com.abdulghffar.gju_outgoings_app.objects;

public class comment {
    String commentText;
    String Uid;
    String timeStamp;
    user user;
    String reference;

    public comment() {
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public void setUid(String Uid) {
        this.Uid = Uid;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getCommentText() {
        return commentText;
    }

    public String getUid() {
        return Uid;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setUser(user user) {
        this.user = user;
    }

    public user getUser() {
        return user;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public comment(String commentText, String Uid, String timeStamp, user user, String reference) {
        this.commentText = commentText;
        this.Uid = Uid;
        this.timeStamp = timeStamp;
        this.user = user;
        this.reference = reference;
    }
}
