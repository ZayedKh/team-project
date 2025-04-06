package lancaster.api.marketing;

import lancaster.model.Room;
import lancaster.model.RoomBookingPriority;

import java.util.List;

/**
 * Interface defining the contract for managing room details.
 */
public interface RoomDetails {

    /**
     * Retrieves all available rooms.
     *
     * @return A list of all rooms.
     */
    List<Room> getAllRooms();


    /**
     * Adds a new room to the list.
     *
     * @param room The Room object to add.
     */
    void addRoom(Room room);


    /**
     * Gets the name of a room.
     *
     * @param roomId The unique ID of the room.
     * @return The name of the room, or null if not found.
     */
    String getRoomName(String roomId);

    /**
     * Gets the capacity of a room.
     *
     * @param roomId The unique ID of the room.
     * @return The capacity of the room, or -1 if not found.
     */
    Object getRoomCapacity(String roomId);

    /**
     * Gets the list of facilities available in a room.
     *
     * @param roomId The unique ID of the room.
     * @return The list of facilities, or null if not found.
     */
    String getRoomFacilities(String roomId);

    /**
     * Gets the booking priority of a room.
     *
     * @param roomId The unique ID of the room.
     * @return The booking priority of the room, or null if not found.
     */
    RoomBookingPriority getRoomBookingPriority(String roomId);

    /**
     * Gets the usage restrictions of a room.
     *
     * @param roomId The unique ID of the room.
     * @return The usage restrictions, or null if not found.
     */
    String getRoomUsageRestrictions(String roomId);
}
