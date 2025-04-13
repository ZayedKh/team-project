package lancaster.marketingAPI;

import lancaster.model.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code RoomDAOImpl} class provides a concrete implementation of the {@link RoomDAO} interface.
 * <p>
 * This class is responsible for retrieving room details from the database by executing SQL queries.
 * It maps each row of the query result to a {@link Room} object and returns a list of these objects.
 * </p>
 *
 * This class is part of the marketing API and is used for accessing room data.
 */
public class RoomDAOImpl implements RoomDAO {

    /**
     * Retrieves all room records from the underlying database.
     * <p>
     * This method executes a SQL query to fetch room details including room ID, name, type, seating configurations,
     * capacities, facilities, booking priority, and usage restrictions. Each record is mapped to a {@link Room} object.
     * The method then returns a list containing all the room objects retrieved from the database.
     * </p>
     *
     * @param connection the {@code Connection} object used to interact with the database
     * @return a {@code List} of {@link Room} objects representing all available rooms
     * @throws RuntimeException if an {@link SQLException} occurs during the database access
     */
    @Override
    public List<Room> getAllRooms(Connection connection) {
        String query = """
                    SELECT room_id, room_name, room_type, seating_config_id, capacity, classroom_capacity, 
                           boardroom_capacity, presentation_capacity, facilities, booking_priority, usage_restrictions
                    FROM rooms;
                """;
        List<Room> rooms = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int roomId = rs.getInt("room_id");
                String roomName = rs.getString("room_name");
                String roomType = rs.getString("room_type");
                int capacity = rs.getInt("capacity");
                int classroomCapacity = rs.getInt("classroom_capacity");
                int boardroomCapacity = rs.getInt("boardroom_capacity");
                int presentationCapacity = rs.getInt("presentation_capacity");
                String facilities = rs.getString("facilities");
                String bookingPriority = rs.getString("booking_priority");
                String usageRestrictions = rs.getString("usage_restrictions");

                List<Integer> capacities = List.of(capacity, classroomCapacity, boardroomCapacity, presentationCapacity);
                Room room = new Room(roomId, roomName, capacities, facilities, bookingPriority, usageRestrictions);
                rooms.add(room);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving rooms", e);
        }

        return rooms;
    }
}
