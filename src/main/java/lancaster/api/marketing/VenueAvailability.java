package lancaster.api.marketing;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface representing venue details and availability status.
 * Provides structured access to venue properties and availability checks.
 */
public interface VenueAvailability {

    /**
     * Gets the unique identifier of the venue.
     *
     * @return the venue ID as a string.
     */
    String getVenueId();

    /**
     * Gets the name of the venue.
     *
     * @return the venue name.
     */
    String getVenueName();

    /**
     * Gets the maximum capacity of the venue.
     *
     * @return the capacity of the venue.
     */
    int getCapacity();

    /**
     * Gets the current availability status of the venue.
     *
     * @return the venue's availability status as a {@link VenueStatus} enum.
     */
    VenueStatus getAvailabilityStatus();

    /**
     * Gets the list of available time slots for booking.
     *
     * @return an unmodifiable list of available time slots.
     */
    List<String> getAvailableTimeSlots();

    /**
     * Gets the reservation deadline for the venue.
     *
     * @return the deadline for making reservations as a {@link LocalDateTime} object.
     */
    LocalDateTime getReservationDeadline();

    /**
     * Checks whether the venue is available at a specified date and time.
     *
     * @param venueId  The unique ID of the venue.
     * @param dateTime The date and time for which availability is being checked.
     * @return {@code true} if the venue is available, otherwise {@code false}.
     */
    boolean isVenueAvailable(String venueId, LocalDateTime dateTime);

    /**
     * Retrieves a list of all venues.
     *
     * @return an unmodifiable list of all venues.
     */
    List<Venue> getAllVenues();

    /**
     * Retrieves a specific venue by its unique identifier.
     *
     * @param venueId The unique ID of the venue.
     * @return the matching {@link Venue} object, or {@code null} if not found.
     */
    Venue getVenueById(String venueId);

    /**
     * Retrieves a list of venues available on a specific date.
     *
     * @param date The date to filter venues by.
     * @return an unmodifiable list of venues available on the given date.
     */
    List<Venue> getVenuesByDate(LocalDateTime date);
}
