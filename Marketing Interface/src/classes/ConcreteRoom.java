package classes;

import enums.RoomBookingPriority;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ConcreteRoom extends Room{

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
    public ConcreteRoom(String roomId, String roomName, int capacity, List<String> facilities, RoomBookingPriority bookingPriority, String usageRestrictions) {
        super(roomId, roomName, capacity, facilities, bookingPriority, usageRestrictions);
    }

    @Override
    public boolean isRoomAvailable() {
        return false;
    }
}
