package lancaster.boxOfficeInterface;

import lancaster.model.Seat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@code SeatDAO} interface.
 * <p>
 * This class provides methods to retrieve seat information from the database including checking for accessible seating,
 * getting all seats by room, and filtering seats based on accessibility and wheelchair-friendliness.
 * </p>
 */
class SeatDAOImpl implements SeatDAO {

    /**
     * Checks whether the specified room has wheelchair-friendly seating available.
     * <p>
     * This method executes a query that counts the number of seats in the room where the column
     * {@code is_wheelchair_friendly} is set to {@code TRUE}.
     * </p>
     *
     * @param conn   the {@code Connection} object used for the database query
     * @param roomID the unique identifier of the room
     * @return {@code true} if there is at least one wheelchair-friendly seat; {@code false} otherwise
     * @throws SQLException if a database access error occurs
     */
    @Override
    public boolean hasAccessableSeating(Connection conn, int roomID) throws SQLException {

        String query = "SELECT COUNT(*) FROM Seats WHERE room_id = ? AND is_wheelchair_friendly = TRUE";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, roomID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    /**
     * Retrieves all seats for a specific room.
     * <p>
     * This method executes a query to obtain all seat records for the given room ordered by row number and seat number.
     * </p>
     *
     * @param conn   the {@code Connection} object used for the database query
     * @param roomId the unique identifier of the room
     * @return a {@code List} of {@code Seat} objects representing all seats in the room
     * @throws SQLException if a database access error occurs
     */
    @Override
    public List<Seat> getSeatsByRoomId(Connection conn, int roomId) throws SQLException {
        String query = "SELECT * FROM Seats WHERE room_id = ? ORDER BY row_number, seat_number";
        return executeSeatQuery(conn, query, roomId);
    }

    /**
     * Retrieves only the accessible seats for a specific room.
     * <p>
     * This method executes a query to obtain seat records for the given room where the column {@code is_accessible}
     * is set to {@code TRUE}.
     * </p>
     *
     * @param conn   the {@code Connection} object used for the database query
     * @param roomId the unique identifier of the room
     * @return a {@code List} of {@code Seat} objects representing the accessible seats in the room
     * @throws SQLException if a database access error occurs
     */
    @Override
    public List<Seat> getAccessibleSeats(Connection conn, int roomId) throws SQLException {

        String query = "SELECT * FROM Seats WHERE room_id = ? AND is_accessible = TRUE";
        return executeSeatQuery(conn, query, roomId);
    }

    /**
     * Retrieves only the wheelchair-friendly seats for a specific room.
     * <p>
     * This method executes a query to obtain seat records for the given room where the column
     * {@code is_wheelchair_friendly} is set to {@code TRUE}.
     * </p>
     *
     * @param conn   the {@code Connection} object used for the database query
     * @param roomId the unique identifier of the room
     * @return a {@code List} of {@code Seat} objects representing the wheelchair-friendly seats in the room
     * @throws SQLException if a database access error occurs
     */
    @Override
    public List<Seat> getWheelchairSeats(Connection conn, int roomId) throws SQLException {
        String query = "SELECT * FROM Seats WHERE room_id = ? AND is_wheelchair_friendly = TRUE";
        return executeSeatQuery(conn, query, roomId);
    }

    /**
     * Executes a seat query and maps the result set to a list of {@code Seat} objects.
     * <p>
     * This is a helper method used by the public methods to execute queries that retrieve seat information.
     * It processes the {@code ResultSet} to create a list of {@code Seat} objects.
     * </p>
     *
     * @param conn   the {@code Connection} object used for the database query
     * @param query  the SQL query string to be executed
     * @param roomId the unique identifier of the room used in the query
     * @return a {@code List} of {@code Seat} objects
     * @throws SQLException if a database access error occurs
     */
    private List<Seat> executeSeatQuery(Connection conn, String query, int roomId) throws SQLException {
        List<Seat> seats = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Seat seat = new Seat();
                seat.setSeatId(rs.getInt("seat_id"));
                seat.setRowNumber(rs.getInt("row_number"));
                seat.setSeatNumber(rs.getInt("seat_number"));
                seat.setAccessible(rs.getBoolean("is_accessible"));
                seat.setWheelchairFriendly(rs.getBoolean("is_wheelchair_friendly"));
                seats.add(seat);
            }
        }
        return seats;
    }
}
