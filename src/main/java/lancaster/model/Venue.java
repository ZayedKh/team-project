package lancaster.model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Abstract class representing the availability of a venue.
 * This class defines the essential properties and behaviors
 * related to venue scheduling and reservation.
 */
public abstract class Venue {
    protected String venueId;
    protected String venueName;
    protected int capacity;
    protected VenueStatus availabilityStatus;
    protected List<String> availableTimeSlots;
    protected LocalDateTime reservationDeadline;

    /**
     * Constructs a Venue object with specified details.
     *
     * @param venueId             Unique identifier for the venue.
     * @param venueName           Name of the venue.
     * @param capacity            Maximum capacity of the venue.
     * @param availabilityStatus  Current availability status of the venue.
     * @param availableTimeSlots  List of available time slots for booking.
     * @param reservationDeadline Deadline for making reservations.
     */
    public Venue(String venueId, String venueName, int capacity, VenueStatus availabilityStatus,
                 List<String> availableTimeSlots, LocalDateTime reservationDeadline) {
        this.venueId = venueId;
        this.venueName = venueName;
        this.capacity = capacity;
        this.availabilityStatus = availabilityStatus;
        // Ensure the availableTimeSlots list is immutable to maintain data integrity
        this.availableTimeSlots = availableTimeSlots != null ?
                Collections.unmodifiableList(availableTimeSlots) : Collections.emptyList();
        this.reservationDeadline = reservationDeadline;
    }

    /**
     * Checks if the venue is available at a given date and time.
     * This method must be implemented by any subclass.
     *
     * @param venueId Unique identifier for the venue.
     * @param dateTime The date and time to check availability for.
     * @return true if the venue is available, false otherwise.
     */
    public abstract boolean isVenueAvailable(String venueId, LocalDateTime dateTime);

    /**
     * Gets the unique venue ID.
     *
     * @return The venue ID.
     */
    public String getVenueId() {
        return venueId;
    }

    /**
     * Gets the name of the venue.
     *
     * @return The venue name.
     */
    public String getVenueName() {
        return venueName;
    }

    /**
     * Gets the maximum capacity of the venue.
     *
     * @return The venue capacity.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Gets the current availability status of the venue.
     *
     * @return The venue's availability status.
     */
    public VenueStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    /**
     * Gets the list of available time slots for the venue.
     *
     * @return A list of available time slots.
     */
    public List<String> getAvailableTimeSlots() {
        return availableTimeSlots;
    }

    /**
     * Gets the reservation deadline for the venue.
     *
     * @return The reservation deadline.
     */
    public LocalDateTime getReservationDeadline() {
        return reservationDeadline;
    }

    /**
     * Returns a string representation of the venue, displaying key details.
     *
     * @return A formatted string with venue details.
     */
    @Override
    public String toString() {
        return "Venue{" +
                "venueId='" + venueId + '\'' +
                ", venueName='" + venueName + '\'' +
                ", capacity=" + capacity +
                ", availabilityStatus=" + availabilityStatus +
                ", availableTimeSlots=" + availableTimeSlots +
                ", reservationDeadline=" + reservationDeadline +
                '}';
    }
}
