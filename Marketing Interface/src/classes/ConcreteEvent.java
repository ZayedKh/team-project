package classes;

import enums.EventStatus;

import java.time.LocalDateTime;

/**
 * Concrete implementation of the Event class.
 */
public class ConcreteEvent extends Event {


    /**
     * Constructs an Event object with the given details.
     *
     * @param eventId       Unique identifier for the event.
     * @param eventName     Name of the event.
     * @param eventDateTime Date and time of the event.
     * @param eventType     Type or category of the event.
     * @param status        Current status of the event.
     * @param lastUpdated   Timestamp of the last update to the event details.
     */

    public ConcreteEvent(String eventId, String eventName, LocalDateTime eventDateTime, String eventType,
                         EventStatus status, LocalDateTime lastUpdated) {
        super(eventId, eventName, eventDateTime, eventType, status, lastUpdated);
    }


    /**
     * Method to determine if the event is still active.
     * Implementing classes should provide the logic for checking event activity.
     *
     * @return true if the event is active, false otherwise.
     */

    @Override
    public boolean isEventActive() {
        return false;
    }
}