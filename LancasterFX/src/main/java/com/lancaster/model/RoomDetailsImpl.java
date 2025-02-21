package com.lancaster.model;

import com.lancaster.api.marketing.RoomDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the RoomDetails interface.
 * This class manages room details and provides methods to retrieve,
 * add, and remove room information.
 */
public class RoomDetailsImpl implements RoomDetails {

    /**
     * List to store all room objects.
     */
    private final List<Room> roomList = new ArrayList<>();

    /**
     * Retrieves all available rooms.
     *
     * @return A list of all rooms (returns a copy for immutability).
     */
    @Override
    public List<Room> getAllRooms() {
        return new ArrayList<>(roomList); // Return a copy to ensure immutability
    }

    /**
     * Retrieves a room by its unique identifier.
     *
     * @param roomId Unique ID of the room.
     * @return The room object if found, otherwise null.
     */
    @Override
    public Room getRoomById(String roomId) {
        return roomList.stream()
                .filter(room -> room.getRoomId().equals(roomId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds a new room to the list.
     *
     * @param room The room to be added.
     */
    @Override
    public synchronized void addRoom(Room room) {
        if (room != null) {
            roomList.add(room);
        }
    }

    /**
     * Removes a room from the list by its ID.
     *
     * @param roomId Unique ID of the room to be removed.
     * @return true if the room was successfully removed, false otherwise.
     */
    @Override
    public synchronized boolean removeRoom(String roomId) {
        return roomList.removeIf(room -> room.getRoomId().equals(roomId));
    }

    /**
     * Retrieves the name of a room by its ID.
     *
     * @param roomId Unique ID of the room.
     * @return The name of the room if found, otherwise null.
     */
    @Override
    public String getRoomName(String roomId) {
        Room room = getRoomById(roomId);
        return (room != null) ? room.getRoomName() : null;
    }

    /**
     * Retrieves the capacity of a room by its ID.
     *
     * @param roomId Unique ID of the room.
     * @return The capacity of the room if found, otherwise -1.
     */
    @Override
    public int getRoomCapacity(String roomId) {
        Room room = getRoomById(roomId);
        return (room != null) ? room.getCapacity() : -1;
    }

    /**
     * Retrieves the facilities available in a room by its ID.
     *
     * @param roomId Unique ID of the room.
     * @return A list of facilities if found, otherwise null.
     */
    @Override
    public List<String> getRoomFacilities(String roomId) {
        Room room = getRoomById(roomId);
        return (room != null) ? room.getFacilities() : null;
    }

    /**
     * Retrieves the booking priority of a room by its ID.
     *
     * @param roomId Unique ID of the room.
     * @return The room booking priority if found, otherwise null.
     */
    @Override
    public RoomBookingPriority getRoomBookingPriority(String roomId) {
        Room room = getRoomById(roomId);
        return (room != null) ? room.getBookingPriority() : null;
    }

    /**
     * Retrieves the usage restrictions of a room by its ID.
     *
     * @param roomId Unique ID of the room.
     * @return The usage restrictions if found, otherwise null.
     */
    @Override
    public String getRoomUsageRestrictions(String roomId) {
        Room room = getRoomById(roomId);
        return (room != null) ? room.getUsageRestrictions() : null;
    }
}
