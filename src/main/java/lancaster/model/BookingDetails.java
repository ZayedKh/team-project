package lancaster.model;

import java.time.LocalDate;

/**
 * Represents the details of a booking event including the date, room, time, event, client, and room type.
 * <p>
 * This class is a simple data model with getters and setters for managing details such as the booking date,
 * room, start and end times, event name, client's name, and the type of room booked.
 * </p>
 */
public class BookingDetails {
    private LocalDate date;
    private String room;
    private String startTime;
    private String endTime;
    private String eventName;
    private String clientName;
    private String roomType;

    /**
     * Returns the booking date.
     *
     * @return the date of the booking.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the booking date.
     *
     * @param date the date to set for the booking.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Returns the room associated with the booking.
     *
     * @return the room name.
     */
    public String getRoom() {
        return room;
    }

    /**
     * Sets the room for the booking.
     *
     * @param room the room name to set.
     */
    public void setRoom(String room) {
        this.room = room;
    }

    /**
     * Returns the start time of the booking.
     *
     * @return the start time as a String.
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the booking.
     *
     * @param startTime the start time to set, represented as a String.
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Returns the end time of the booking.
     *
     * @return the end time as a String.
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the booking.
     *
     * @param endTime the end time to set, represented as a String.
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * Returns the event name associated with the booking.
     *
     * @return the name of the event.
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Sets the event name for the booking.
     *
     * @param eventName the event name to set.
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Returns the name of the client who made the booking.
     *
     * @return the client's name.
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * Sets the name of the client for the booking.
     *
     * @param clientName the client's name to set.
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * Sets the type of room for the booking.
     *
     * @param roomType the room type to set.
     */
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    /**
     * Returns the type of room associated with the booking.
     *
     * @return the room type.
     */
    public String getRoomType() {
        return roomType;
    }
}
