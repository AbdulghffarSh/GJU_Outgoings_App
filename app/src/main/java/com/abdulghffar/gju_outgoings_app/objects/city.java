package com.abdulghffar.gju_outgoings_app.objects;

import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class city {
    String cityName;
    String zipCode;
    HashMap<String, DocumentReference> universities;
    List<String> pics;

    public city() {
    }

    public city(String cityName, String zipCode, HashMap<String, DocumentReference> universities, List<String> pics) {
        this.cityName = cityName;
        this.zipCode = zipCode;
        this.universities = universities;
        this.pics = pics;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setUniversities(HashMap<String, DocumentReference> universities) {
        this.universities = universities;
    }

    public String getCityName() {
        return cityName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public HashMap<String, DocumentReference> getUniversities() {
        return universities;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public List<String> getPics() {
        return pics;
    }
}
