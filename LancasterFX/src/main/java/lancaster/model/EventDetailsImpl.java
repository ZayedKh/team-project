package lancaster.model;

import lancaster.api.marketing.EventDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of EventDetails interface.
 * This class manages event details and provides functionalities
 * to retrieve and manipulate event data.
 */
public class EventDetailsImpl extends Event implements EventDetails {

    /**
     * List to store all events.
     */
    private final List<Event> eventList = new ArrayList<>();

    /**
     * Constructor to initialize an EventDetailsImpl object.
     *
     * @param eventId       Unique identifier for the event
     * @param eventName     Name of the event
     * @param eventDateTime Date and time of the event
     * @param eventType     Type/category of the event
     * @param status        Current status of the event
     * @param lastUpdated   Timestamp of the last update
     */
    public EventDetailsImpl(String eventId, String eventName, LocalDateTime eventDateTime, String eventType,
                            EventStatus status, LocalDateTime lastUpdated) {
        super(eventId, eventName, eventDateTime, eventType, status, lastUpdated);
    }

    /**
     * Checks if the event is active based on its status and scheduled time.
     *
     * @return true if the event is scheduled and its date is in the future, false otherwise.
     */
    @Override
    public boolean isEventActive() {
        return this.status == EventStatus.SCHEDULED && eventDateTime.isAfter(LocalDateTime.now());
    }

    /**
     * Retrieves all stored events.
     *
     * @return A list containing all events (returns a copy for immutability).
     */
    @Override
    public List<Event> getAllEvents() {
        return new ArrayList<>(eventList); // Return a copy to ensure immutability
    }

    /**
     * Retrieves an event by its unique identifier.
     *
     * @param eventId Unique ID of the event
     * @return The event object if found, otherwise null.
     */
    @Override
    public Event getEventById(String eventId) {
        return eventList.stream()
                .filter(event -> event.getEventId().equals(eventId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves a list of events occurring on a specific date.
     *
     * @param date The date to search for events
     * @return A list of events happening on the specified date.
     */
    @Override
    public List<Event> getEventsByDate(LocalDateTime date) {
        List<Event> eventsOnDate = new ArrayList<>();
        for (Event event : eventList) {
            if (event.getEventDateTime().toLocalDate().equals(date.toLocalDate())) {
                eventsOnDate.add(event);
            }
        }
        return eventsOnDate;
    }

    /**
     * Adds a new event to the event list.
     *
     * @param event The event to be added
     */
    public synchronized void addEvent(Event event) {
        if (event != null) {
            eventList.add(event);
        }
    }
}
