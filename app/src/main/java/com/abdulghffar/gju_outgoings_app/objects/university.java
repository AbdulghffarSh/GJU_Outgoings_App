package com.abdulghffar.gju_outgoings_app.objects;

import java.util.List;

public class university {
    String universityName;
    String cityName;
    String note;
    List<String> pics;

    public university() {
    }

    public university(String universityName, String cityName, String note, List<String> pics) {
        this.universityName = universityName;
        this.cityName = cityName;
        this.note = note;
        this.pics = pics;
    }

    public String getUniversityName() {
        return universityName;
    }

    public String getCityName() {
        return cityName;
    }

    public String getNote() {
        return note;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }
}
