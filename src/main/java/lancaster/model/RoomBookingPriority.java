package lancaster.model;

/**
 * Enum representing room booking priority levels.
 * This determines how urgently a room should be allocated based on its importance.
 */
public enum RoomBookingPriority {
    HIGH("High priority booking"),
    MEDIUM("Medium priority booking"),
    LOW("Low priority booking");

    private final String description;

    /**
     * Constructor for the RoomBookingPriority enum.
     *
     * @param description A brief description of the priority level.
     */
    RoomBookingPriority(String description) {
        this.description = description;
    }

    /**
     * Gets the description of the booking priority.
     *
     * @return The description of the priority level.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns a string representation of the enum value.
     *
     * @return The name of the enum in a readable format.
     */
    @Override
    public String toString() {
        return name() + " (" + description + ")";
    }

    /**
     * Converts a string into a corresponding RoomBookingPriority enum value.
     *
     * @param value The string representation of the priority level (case-insensitive).
     * @return The corresponding RoomBookingPriority enum value.
     * @throws IllegalArgumentException If the provided value does not match any priority.
     */
    public static RoomBookingPriority fromString(String value) {
        for (RoomBookingPriority priority : RoomBookingPriority.values()) {
            if (priority.name().equalsIgnoreCase(value)) {
                return priority;
            }
        }
        throw new IllegalArgumentException("Invalid RoomBookingPriority: " + value);
    }
}
