package models;

import java.time.LocalDateTime;

/**
 * Concrete implementation of the Event class.
 */
public class ConcreteEvent extends Event {

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
    public ConcreteEvent(String eventID, String eventName, LocalDateTime startTime,
                         LocalDateTime endTime, String eventType, String eventStatus, String eventVenue) {
        super(eventID, eventName, startTime, endTime, eventType, eventStatus, eventVenue);
    }
}