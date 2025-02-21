package MarketingInterface;

import java.util.Collections;
import java.util.List;

/**
 * Abstract class representing details of a room.
 * This class provides a base structure for managing room properties and availability.
 */
public abstract class Room {
    protected String roomId;
    protected String roomName;
    protected int capacity;
    protected List<String> facilities;
    protected RoomBookingPriority bookingPriority;
    protected String usageRestrictions;

    /**
     * Constructs a new Room object with the given details.
     *
     * @param roomId            Unique identifier for the room.
     * @param roomName          Name of the room.
     * @param capacity          Maximum capacity of the room.
     * @param facilities        List of facilities available in the room.
     * @param bookingPriority   Booking priority level of the room.
     * @param usageRestrictions Any restrictions on room usage.
     */
    public Room(String roomId, String roomName, int capacity, List<String> facilities,
                RoomBookingPriority bookingPriority, String usageRestrictions) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.capacity = capacity;
        this.facilities = facilities != null ? Collections.unmodifiableList(facilities) : Collections.emptyList();
        this.bookingPriority = bookingPriority;
        this.usageRestrictions = usageRestrictions;
    }

    /**
     * Gets the unique room ID.
     *
     * @return The room ID as a string.
     */
    public String getRoomId() {
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
    public int getCapacity() {
        return capacity;
    }

    /**
     * Gets the list of facilities available in the room.
     *
     * @return An unmodifiable list of facilities.
     */
    public List<String> getFacilities() {
        return facilities;
    }

    /**
     * Gets the booking priority of the room.
     *
     * @return The booking priority as a {@link RoomBookingPriority} enum.
     */
    public RoomBookingPriority getBookingPriority() {
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
    public abstract boolean isRoomAvailable();

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
                ", capacity=" + capacity +
                ", facilities=" + facilities +
                ", bookingPriority=" + bookingPriority +
                ", usageRestrictions='" + usageRestrictions + '\'' +
                '}';
    }
}
