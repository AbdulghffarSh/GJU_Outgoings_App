package com.abdulghffar.gju_outgoings_app.objects;

import java.util.HashMap;

public class city {
    String cityName;
    String zipCode;
    HashMap<String, Object> universities;

    public city() {
    }

    public city(String cityName, String zipCode, HashMap<String, Object> universities) {
        this.cityName = cityName;
        this.zipCode = zipCode;
        this.universities = universities;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setUniversities(HashMap<String, Object> universities) {
        this.universities = universities;
    }

    public String getCityName() {
        return cityName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public HashMap<String, Object> getUniversities() {
        return universities;
    }
}
