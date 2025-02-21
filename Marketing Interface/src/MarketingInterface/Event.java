package MarketingInterface;

import java.time.LocalDateTime;

/**
 * Abstract class representing event details.
 * This class defines the essential attributes and behaviors related to an event.
 */
public abstract class Event {
    protected String eventId;
    protected String eventName;
    protected LocalDateTime eventDateTime;
    protected String eventType;
    protected EventStatus status;
    protected LocalDateTime lastUpdated;

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
    public Event(String eventId, String eventName, LocalDateTime eventDateTime, String eventType,
                 EventStatus status, LocalDateTime lastUpdated) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDateTime = eventDateTime;
        this.eventType = eventType;
        this.status = status;
        this.lastUpdated = lastUpdated;
    }

    /**
     * Retrieves the unique event ID.
     *
     * @return The event ID.
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Retrieves the name of the event.
     *
     * @return The event name.
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Retrieves the date and time when the event is scheduled.
     *
     * @return The event date and time.
     */
    public LocalDateTime getEventDateTime() {
        return eventDateTime;
    }

    /**
     * Retrieves the type or category of the event.
     *
     * @return The event type.
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Retrieves the current status of the event.
     *
     * @return The event status.
     */
    public EventStatus getStatus() {
        return status;
    }

    /**
     * Retrieves the timestamp of the last update made to the event details.
     *
     * @return The last updated timestamp.
     */
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Abstract method to determine if the event is still active.
     * Implementing classes should provide the logic for checking event activity.
     *
     * @return true if the event is active, false otherwise.
     */
    public abstract boolean isEventActive();

    /**
     * Returns a string representation of the event details.
     *
     * @return A formatted string with event details.
     */
    @Override
    public String toString() {
        return "Event{" +
                "eventId='" + eventId + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventDateTime=" + eventDateTime +
                ", eventType='" + eventType + '\'' +
                ", status=" + status +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
