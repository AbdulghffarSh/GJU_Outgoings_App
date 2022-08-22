package com.abdulghffar.gju_outgoings_app.objects;

public class comment {
    String commentText;
    String UID;
    String timeStamp;

    public comment(){}

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getCommentText() {
        return commentText;
    }

    public String getUID() {
        return UID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public comment(String commentText, String UID, String timeStamp) {
        this.commentText = commentText;
        this.UID = UID;
        this.timeStamp = timeStamp;
    }
}
