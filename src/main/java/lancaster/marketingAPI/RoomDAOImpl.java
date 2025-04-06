package lancaster.marketingAPI;

import lancaster.model.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDAOImpl implements RoomDAO {

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