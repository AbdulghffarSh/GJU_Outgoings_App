package com.abdulghffar.gju_outgoings_app.objects;

import java.io.Serializable;

public class post implements Serializable {
    String postID;
    user user;
    String image;
    String tag;
    String title;
    String body;
    String timeStamp;

    public post() {
    }

    public post(String postID, user user, String image, String tag, String title, String body, String timeStamp) {
        this.postID = postID;
        this.user = user;
        this.image = image;
        this.tag = tag;
        this.title = title;
        this.body = body;
        this.timeStamp = timeStamp;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public void setUser(user user) {
        this.user = user;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPostID() {
        return postID;
    }

    public user getUser() {
        return user;
    }

    public String getImage() {
        return image;
    }

    public String getTag() {
        return tag;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}
