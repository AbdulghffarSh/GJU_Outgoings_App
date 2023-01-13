package com.abdulghffar.gju_outgoings_app.objects;


import java.util.ArrayList;
import java.util.Date;

public class event {
    Date startDate;
    Date endDate;
    String title;
    String description;

    public event() {
    }

    public event(Date startDate, Date endDate, String title, String description) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.description = description;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public static ArrayList<event> filterEvents(ArrayList<event> events){
        Date todayDate = new Date();
        events.removeIf(event -> event.getEndDate().before(todayDate));
        System.out.println("changed");

        return events;
    }

}
