package lancaster.model;

/**
 * Enum representing the status of a venue.
 * This helps in determining whether a venue is available for booking.
 */
public enum VenueStatus {

    /**
     * Venue is available for booking.
     */
    AVAILABLE("Venue is available for booking"),

    /**
     * Venue is currently reserved and cannot be booked.
     */
    RESERVED("Venue is currently reserved"),

    /**
     * Venue is not available for booking.
     */
    UNAVAILABLE("Venue is not available");

    private final String description;

    /**
     * Constructor to associate a description with each venue status.
     *
     * @param description A user-friendly description of the venue status.
     */
    VenueStatus(String description) {
        this.description = description;
    }

    /**
     * Retrieves the description of the venue status.
     *
     * @return The description of the venue status.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns a human-readable representation of the venue status.
     *
     * @return A formatted string representing the venue status.
     */
    @Override
    public String toString() {
        return name() + " (" + description + ")";
    }

    /**
     * Converts a string into a corresponding VenueStatus enum value.
     *
     * @param value The string representation of the venue status (case-insensitive).
     * @return The corresponding VenueStatus enum value.
     * @throws IllegalArgumentException If the provided value does not match any status.
     */
    public static VenueStatus fromString(String value) {
        for (VenueStatus status : VenueStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid VenueStatus: " + value);
    }
}
