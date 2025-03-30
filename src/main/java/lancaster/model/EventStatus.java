package lancaster.model;

/**
 * Enum representing different statuses an event can have.
 */
public enum EventStatus {

    /**
     * Event is scheduled to occur as planned.
     */
    SCHEDULED("Event is scheduled"),

    /**
     * Event has been canceled and will not take place.
     */
    CANCELED("Event has been canceled"),

    /**
     * Event has been rescheduled to a different date/time.
     */
    RESCHEDULED("Event has been rescheduled");

    private final String description;

    /**
     * Constructor to associate a description with each event status.
     *
     * @param description A user-friendly description of the event status.
     */
    EventStatus(String description) {
        this.description = description;
    }

    /**
     * Retrieves the description of the event status.
     *
     * @return The description of the event status.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns a human-readable representation of the event status.
     *
     * @return A string representing the event status.
     */
    @Override
    public String toString() {
        return name() + " (" + description + ")";
    }
}
