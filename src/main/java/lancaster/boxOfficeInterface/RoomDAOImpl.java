package lancaster.boxOfficeInterface;

import java.sql.*;

public class RoomDAOImpl implements RoomDAO {


    @Override
    public int getRoomCapacity(Connection conn, int roomID) throws SQLException {
        String query = "SELECT capacity FROM rooms WHERE room_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, roomID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("capacity");
            } else {
                throw new SQLException("Room ID not found: " + roomID);
            }
        }
    }

    @Override
    public Timestamp getLastBookingTime(Connection connection, int roomId) throws SQLException {
        String query = "SELECT booking_date, end_time FROM room_booking_schedule WHERE room_id = ? ORDER BY booking_date DESC, end_time DESC LIMIT 1";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, roomId);  // Set the room_id as the parameter

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Retrieve the event date and end time
                Date eventDate = rs.getDate("booking_date");
                Time endTime = rs.getTime("end_time");

                // Combine event date and end time into a single timestamp
                Timestamp lastBookingTime = new Timestamp(eventDate.getTime() + endTime.getTime());
                return lastBookingTime;
            }
        }

        // If no booking found, return null or handle accordingly
        return null;
    }
}
