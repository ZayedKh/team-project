package com.lancaster.model;
import java.time.LocalDate;

public class BookingDetails {
    private LocalDate date;
    private String room;
    private String startTime;
    private String endTime;
    private String eventName;
    private String clientName;

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public String getRoom() {
        return room;
    }
    public void setRoom(String room) {
        this.room = room;
    }
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getEventName() {
        return eventName;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    public String getClientName() {
        return clientName;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
}
}
