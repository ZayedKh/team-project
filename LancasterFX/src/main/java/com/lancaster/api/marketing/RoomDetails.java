package com.lancaster.api.marketing;

import java.util.List;

/**
 * Interface defining the contract for managing room details.
 */
public interface RoomDetails {

    /**
     * Retrieves all available rooms.
     * @return A list of all rooms.
     */
    List<Room> getAllRooms();

    /**
     * Retrieves a specific room by its ID.
     * @param roomId The unique ID of the room.
     * @return The matching Room object, or null if not found.
     */
    Room getRoomById(String roomId);

    /**
     * Adds a new room to the list.
     * @param room The Room object to add.
     */
    void addRoom(Room room);

    /**
     * Removes a room from the list by its ID.
     * @param roomId The unique ID of the room.
     * @return true if removed successfully, false otherwise.
     */
    boolean removeRoom(String roomId);

    /**
     * Gets the name of a room.
     * @param roomId The unique ID of the room.
     * @return The name of the room, or null if not found.
     */
    String getRoomName(String roomId);

    /**
     * Gets the capacity of a room.
     * @param roomId The unique ID of the room.
     * @return The capacity of the room, or -1 if not found.
     */
    int getRoomCapacity(String roomId);

    /**
     * Gets the list of facilities available in a room.
     * @param roomId The unique ID of the room.
     * @return The list of facilities, or null if not found.
     */
    List<String> getRoomFacilities(String roomId);

    /**
     * Gets the booking priority of a room.
     * @param roomId The unique ID of the room.
     * @return The booking priority of the room, or null if not found.
     */
    RoomBookingPriority getRoomBookingPriority(String roomId);

    /**
     * Gets the usage restrictions of a room.
     * @param roomId The unique ID of the room.
     * @return The usage restrictions, or null if not found.
     */
    String getRoomUsageRestrictions(String roomId);
}
