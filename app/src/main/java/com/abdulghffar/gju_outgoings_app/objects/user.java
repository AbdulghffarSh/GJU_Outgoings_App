package com.abdulghffar.gju_outgoings_app.objects;

import java.io.Serializable;

public class user implements Serializable {
    String uid;
    String approval;
    String email;
    String gender;
    String major;
    String name;
    String profilePic;
    String registrationTimeStamp;
    String role;
    String studentID;
    String fcmToken;

    public user() {
    }

    public user(String uid, String approval, String email, String gender, String major, String name, String profilePic, String registrationTimeStamp, String role, String studentID, String fcmToken) {
        this.uid = uid;
        this.approval = approval;
        this.email = email;
        this.gender = gender;
        this.major = major;
        this.name = name;
        this.profilePic = profilePic;
        this.registrationTimeStamp = registrationTimeStamp;
        this.role = role;
        this.studentID = studentID;
        this.fcmToken = fcmToken;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void setRegistrationTimeStamp(String registrationTimeStamp) {
        this.registrationTimeStamp = registrationTimeStamp;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getUid() {
        return uid;
    }

    public String getApproval() {
        return approval;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getMajor() {
        return major;
    }

    public String getName() {
        return name;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getRegistrationTimeStamp() {
        return registrationTimeStamp;
    }

    public String getRole() {
        return role;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
