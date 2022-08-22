package com.abdulghffar.gju_outgoings_app.objects;

public class user {
    String Uid;
    String approval;
    String email;
    String gender;
    String major;
    String name;
    String profilePic;
    String registrationTimeStamp;
    String role;
    String studentID;

    public user() {
    }

    public user(String Uid, String approval, String email, String gender, String major, String name, String profilePic, String registrationTimeStamp, String role, String studentID) {
        this.Uid = Uid;
        this.approval = approval;
        this.email = email;
        this.gender = gender;
        this.major = major;
        this.name = name;
        this.profilePic = profilePic;
        this.registrationTimeStamp = registrationTimeStamp;
        this.role = role;
        this.studentID = studentID;
    }

    public void setUid(String Uid) {
        Uid = Uid;
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
        return Uid;
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
}
