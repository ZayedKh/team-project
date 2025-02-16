package model;
public abstract class Event{
    private String eventId;
    private int days;
    private String status;
    private String eventType;
    private double cost;

    Event(String eventId, int days, String status, String eventType, double cost) {
        this.eventId = eventId;
        this.days = days;
        this.status = status;
        this.eventType = eventType;
        this.cost = cost;
    }

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
}