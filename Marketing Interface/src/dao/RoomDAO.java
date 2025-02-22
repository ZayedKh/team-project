package dao;

import classes.ConcreteRoom;
import classes.Room;
import enums.RoomBookingPriority;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) class for managing Room entities in the database.
 * This class provides methods for creating, retrieving, updating, and deleting Room records.
 */
public class RoomDAO {

    private final Connection connection;

    /**
     * Constructs a RoomDAO with a given database connection.
     *
     * @param connection The database connection to be used for operations.
     */
    public RoomDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves a room from the database by its unique ID.
     *
     * @param roomId The ID of the room to retrieve.
     * @return The Room object if found, otherwise null.
     * @throws SQLException If a database access error occurs.
     */
    public Room getRoomById(String roomId) throws SQLException {
        String query = "SELECT * FROM rooms WHERE room_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, roomId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ConcreteRoom(
                        rs.getString("room_id"),
                        rs.getString("room_name"),
                        rs.getInt("capacity"),
                        List.of(rs.getString("facilities").split(",")),
                        RoomBookingPriority.valueOf(rs.getString("booking_priority")),
                        rs.getString("usage_restrictions")
                );
            }
        }
        return null;
    }

    /**
     * Retrieves all rooms from the database.
     *
     * @return A list of all Room objects.
     * @throws SQLException If a database access error occurs.
     */
    public List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                rooms.add(new ConcreteRoom(
                        rs.getString("room_id"),
                        rs.getString("room_name"),
                        rs.getInt("capacity"),
                        List.of(rs.getString("facilities").split(",")),
                        RoomBookingPriority.valueOf(rs.getString("booking_priority")),
                        rs.getString("usage_restrictions")
                ));
            }
        }
        return rooms;
    }

    /**
     * Adds a new room to the database.
     *
     * @param room The Room object to be added.
     * @throws SQLException If a database access error occurs.
     */
    public void addRoom(Room room) throws SQLException {
        String query = "INSERT INTO rooms (room_id, room_name, capacity, facilities, booking_priority, usage_restrictions) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, room.getRoomId());
            stmt.setString(2, room.getRoomName());
            stmt.setInt(3, room.getCapacity());
            stmt.setString(4, String.join(",", room.getFacilities()));
            stmt.setString(5, room.getBookingPriority().name());
            stmt.setString(6, room.getUsageRestrictions());
            stmt.executeUpdate();
        }
    }

    /**
     * Updates an existing room's details in the database.
     *
     * @param room The Room object with updated details.
     * @throws SQLException If a database access error occurs.
     */
    public void updateRoom(Room room) throws SQLException {
        String query = "UPDATE rooms SET room_name = ?, capacity = ?, facilities = ?, booking_priority = ?, usage_restrictions = ? WHERE room_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, room.getRoomName());
            stmt.setInt(2, room.getCapacity());
            stmt.setString(3, String.join(",", room.getFacilities()));
            stmt.setString(4, room.getBookingPriority().name());
            stmt.setString(5, room.getUsageRestrictions());
            stmt.setString(6, room.getRoomId());
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes a room from the database by its ID.
     *
     * @param roomId The ID of the room to delete.
     * @throws SQLException If a database access error occurs.
     */
    public void deleteRoom(String roomId) throws SQLException {
        String query = "DELETE FROM rooms WHERE room_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, roomId);
            stmt.executeUpdate();
        }
    }
}
