package MarketingInterface;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface representing the details of an event.
 * Provides read-only access to event properties.
 */
public interface EventDetails {

    /**
     * Gets the unique identifier of the event.
     *
     * @return the event ID as a string.
     */
    String getEventId();

    /**
     * Gets the name of the event.
     *
     * @return the event name.
     */
    String getEventName();

    /**
     * Gets the scheduled date and time of the event.
     *
     * @return the event date and time as an immutable {@link LocalDateTime} object.
     */
    LocalDateTime getEventDateTime();

    /**
     * Gets the type/category of the event.
     *
     * @return the event type.
     */
    String getEventType();

    /**
     * Gets the current status of the event.
     *
     * @return the event status as an {@link EventStatus} enum.
     */
    EventStatus getStatus();

    /**
     * Gets the timestamp of the last update to the event.
     *
     * @return the last updated timestamp as a {@link LocalDateTime} object.
     */
    LocalDateTime getLastUpdated();

    /**
     * Retrieves a list of all events.
     *
     * @return an unmodifiable list of all events.
     */
    List<Event> getAllEvents();

    /**
     * Retrieves a specific event by its unique identifier.
     *
     * @param eventId The unique ID of the event.
     * @return the matching {@link Event} object, or {@code null} if not found.
     */
    Event getEventById(String eventId);

    /**
     * Retrieves a list of events occurring on a specific date.
     *
     * @param date The date to filter events by.
     * @return an unmodifiable list of events occurring on the given date.
     */
    List<Event> getEventsByDate(LocalDateTime date);
}
