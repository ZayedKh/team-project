package lancaster.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Event {
    private int eventID;
    private Booking booking;
    private Room room;
    private String description;
    private EventStatus status;
    private Timestamp startTime;
    private Timestamp endTime;
    private Date date;


    /**
     * Constructor for an event
     * @param eventID       ID for the event
     * @param booking       Booking that the event is part of
     * @param room          The room the event is taking place in
     * @param description   Description of the event
     * @param status        Status of the event
     * @param startTime     Start time of the event
     * @param endTime       End time of the event
     * @param date          Date the event is taking place
     */
    public Event(int eventID, Booking booking, Room room, String description,
                 EventStatus status, Timestamp startTime, Timestamp endTime,
                 Date date){

        this.eventID = eventID;
        this.booking = booking;
        this.room = room;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
    }

    public int getEventID() {
        return eventID;
    }

    public Booking getBooking() {
        return booking;
    }

    public Room getRoom() {
        return room;
    }

    public String getDescription() {
        return description;
    }

    public EventStatus getStatus() {
        return status;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public Date getDate() {
        return date;
    }
}
