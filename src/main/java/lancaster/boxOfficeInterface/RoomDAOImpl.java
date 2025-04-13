package lancaster.boxOfficeInterface;

import java.sql.*;

/**
 * Implementation of the {@link RoomDAO} interface.
 * <p>
 * This class provides methods to retrieve room details such as capacity and the last booking time
 * from the database. It implements the data access operations defined by the {@link RoomDAO} interface.
 * </p>
 */
public class RoomDAOImpl implements RoomDAO {

    /**
     * Retrieves the capacity of a room specified by the roomID.
     *
     * @param conn   the database connection to use for the query
     * @param roomID the unique identifier of the room
     * @return the capacity of the room as an int
     * @throws SQLException if the roomID is not found or a database access error occurs
     */
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

    /**
     * Retrieves the last booking time for a room specified by the roomId.
     * <p>
     * The method queries the room_booking_schedule table to find the most recent booking
     * for the specified room. It orders the results by booking_date and end_time in descending order
     * and combines the booking date and end time into a single Timestamp.
     * </p>
     *
     * @param connection the database connection to use for the query
     * @param roomId     the unique identifier of the room
     * @return a {@link Timestamp} representing the last booking time, or null if no booking is found
     * @throws SQLException if a database access error occurs during the query
     */
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
