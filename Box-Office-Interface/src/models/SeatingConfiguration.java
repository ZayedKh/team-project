package models;

/**
 * Abstract class for tracking seating configurations.
 * Author: Mohib Ishrat
 * Version: 1.0
 * Date: 20/02/2025
 */
public abstract class SeatingConfiguration {

    private String eventID; // Unique identifier for the event
    private int totalSeats; // Total number of seats available for the event
    private int restrictedViewSeats; // Number of seats with restricted view
    private int accessibleSeats; // Number of seats reserved for accessibility needs
    private boolean hasAccessibleSeating; // Indicates whether accessible seating is available

    /**
     * Constructor for SeatingConfiguration.
     * @param eventID Unique identifier for the event
     * @param totalSeats Total number of seats available for the event
     * @param restrictedViewSeats Number of seats with restricted view
     * @param accessibleSeats Number of seats reserved for accessibility needs
     * @param hasAccessibleSeating Indicates whether accessible seating is available
     */
    public SeatingConfiguration(String eventID, int totalSeats, int restrictedViewSeats, int accessibleSeats, boolean hasAccessibleSeating) {
        this.eventID = eventID; // Unique event identifier
        this.totalSeats = totalSeats; // Total seats in venue
        this.restrictedViewSeats = restrictedViewSeats; // Restricted view seats count
        this.accessibleSeats = accessibleSeats; // Accessible seats count
        this.hasAccessibleSeating = hasAccessibleSeating; // Whether venue has accessible seating
    }

    /**
     * Abstract method to update seating configuration.
     * @param newTotalSeats New total number of seats
     * @param newRestrictedSeats New number of restricted view seats
     * @param newAccessibleSeats New number of accessible seats
     */
    public abstract void updateSeatingConfiguration(int newTotalSeats, int newRestrictedSeats, int newAccessibleSeats);

    /**
     * Gets the event ID.
     * @return The event ID
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * Gets the total number of seats.
     * @return The total number of seats
     */
    public int getTotalSeats() {
        return totalSeats;
    }

    /**
     * Gets the number of restricted view seats.
     * @return The number of restricted view seats
     */
    public int getRestrictedViewSeats() {
        return restrictedViewSeats;
    }

    /**
     * Gets the number of accessible seats.
     * @return The number of accessible seats
     */
    public int getAccessibleSeats() {
        return accessibleSeats;
    }

    /**
     * Checks if accessible seating is available.
     * @return True if accessible seating is available, false otherwise
     */
    public boolean hasAccessibleSeating() {
        return hasAccessibleSeating;
    }

    /**
     * Associates a seating configuration with an event.
     * @param eventID The event ID to associate with
     */
    public void associateWithEvent(String eventID) {
        this.eventID = eventID;
    }
}