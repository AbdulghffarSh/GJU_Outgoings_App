package com.abdulghffar.gju_outgoings_app.objects;

public class staff {
    String name;
    String email;
    String info;
    String profilePic;

    public staff() {
    }

    public staff(String name, String email, String info, String profilePic) {
        this.name = name;
        this.email = email;
        this.info = info;
        this.profilePic = profilePic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getInfo() {
        return info;
    }

    public String getProfilePic() {
        return profilePic;
    }
}
