package lancaster.model;

import java.util.List;

/**
 * Class representing details of a room.
 * This class provides a base structure for managing room properties and availability.
 */
public class Room {
    protected int roomId;
    protected String roomName;
    protected List<Integer> capacities;
    protected String facilities;
    protected String bookingPriority;
    protected String usageRestrictions;

    /**
     * Constructs a new Room object with the given details.
     *
     * @param roomId            Unique identifier for the room.
     * @param roomName          Name of the room.
     * @param capacities        Maximum capacity of the room.
     * @param facilities        List of facilities available in the room.
     * @param bookingPriority   Booking priority level of the room.
     * @param usageRestrictions Any restrictions on room usage.
     */
    public Room(int roomId, String roomName, List<Integer> capacities, String facilities,
                String bookingPriority, String usageRestrictions) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.capacities = capacities;
        this.facilities = facilities;
        this.bookingPriority = bookingPriority;
        this.usageRestrictions = usageRestrictions;
    }

    /**
     * Gets the unique room ID.
     *
     * @return The room ID as a string.
     */
    public int getRoomId() {
        return roomId;
    }

    /**
     * Gets the name of the room.
     *
     * @return The room name.
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * Gets the capacity of the room.
     *
     * @return The maximum capacity of the room.
     */
    public List<Integer> getCapacities() {
        return capacities;
    }

    /**
     * Gets the list of facilities available in the room.
     *
     * @return An unmodifiable list of facilities.
     */
    public String getFacilities() {
        return facilities;
    }

    /**
     * Gets the booking priority of the room.
     *
     * @return The booking priority as a {@link RoomBookingPriority} enum.
     */
    public String getBookingPriority() {
        return bookingPriority;
    }

    /**
     * Gets the usage restrictions for the room.
     *
     * @return A string describing any usage restrictions.
     */
    public String getUsageRestrictions() {
        return usageRestrictions;
    }

    /**
     * Checks whether the room is available for booking.
     *
     * @return {@code true} if the room is available, otherwise {@code false}.
     */
    public boolean isRoomAvailable() {
        // Implement the logic to check room availability
        return true; // Placeholder implementation
    }

    /**
     * Returns a string representation of the room object.
     *
     * @return A formatted string containing room details.
     */
    @Override
    public String toString() {
        return "Room{" +
                "roomId='" + roomId + '\'' +
                ", roomName='" + roomName + '\'' +
                ", capacity=" + capacities +
                ", facilities=" + facilities +
                ", bookingPriority=" + bookingPriority +
                ", usageRestrictions='" + usageRestrictions + '\'' +
                '}';
    }
}