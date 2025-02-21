package classes;

import java.time.LocalDateTime;

/**
 * This is an object for an event happening at a specific time
 */
public abstract class Event {
    String eventID;
    String eventName;
    LocalDateTime startTime;
    LocalDateTime endTime;
    String eventType;
    String eventStatus;
    String eventVenue;

    /**
     * The constructor to create an event
     * @param eventID       The eventID stored in database
     * @param eventName     The name of the event taking place, this could be a movie name or such
     * @param startTime     This is the start time of event including the date
     * @param endTime       This is the end time of event including the date
     * @param eventType     This is the type of event taking place
     * @param eventStatus   This is the status of the event i.e. confirmed, pending etc.
     * @param eventVenue    This is the venue where the event is taking place
     */
    public Event(String eventID, String eventName, LocalDateTime startTime, LocalDateTime
            endTime, String eventType, String eventStatus, String eventVenue) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventType = eventType;
        this.eventStatus = eventStatus;
        this.eventVenue = eventVenue;
    }

    /**
     * Gets the eventID that is stored in database
     * @return The eventID
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * Gets the name of the event
     * @return The name of event
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Gets the start time and date of event
     * @return The start time of event
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Gets the end time and date of event
     * @return The end time of event
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Gets the status of the event
     * @return The status of event
     */
    public String getEventStatus() {
        return eventStatus;
    }

    /**
     * gets the type of event
     * @return The event type
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Gets the venue the event is taking place
     * @return The event venue
     */
    public String getEventVenue() {
        return eventVenue;
    }
}
