package com.lancaster.app.model;

public class Event {
    /*
    com.lancaster.app.api.model.Event class - has 5 attributes currently.
    Undecided whether it should be abstract, and more specific events can inherit...
     */

    // event id for efficient database retrieval and organisation
    private String eventId;

    // days to indicate length of bookings
    private int days;

    // status - confirmed, pending, canceled etc.
    private String status;

    // type of event - rehearsal, meeting etc.
    private String eventType;

    // cost
    private double cost;



    // constructor to initialize new event object
    Event(String eventId, int days, String status, String eventType, double cost) {
        this.eventId = eventId;
        this.days = days;
        this.status = status;
        this.eventType = eventType;
        this.cost = cost;
    }


    // getters and setters for attributes
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days){
        this.days = days;
    }

    public double getCost(){
        return cost;
    }

    public void setCost(double cost){
        this.cost = cost;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getEventType(){
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
