package lancaster.boxOfficeInterface;

import lancaster.model.Seat;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object (DAO) interface for seat-related operations.
 * <p>
 * This interface defines methods to query and retrieve seat details from the data source.
 * Implementations should provide concrete database operations for seats.
 * </p>
 */
public interface SeatDAO {

    /**
     * Checks whether a room has accessible seating available.
     *
     * @param conn   the {@code Connection} object used for the database query
     * @param roomID the unique identifier of the room
     * @return {@code true} if accessible seating is available, {@code false} otherwise
     * @throws SQLException if a database access error occurs during the query
     */
    boolean hasAccessableSeating(Connection conn, int roomID) throws SQLException;

    /**
     * Retrieves a list of seats for the specified room.
     *
     * @param conn   the {@code Connection} object used for the database query
     * @param roomId the unique identifier of the room
     * @return a {@code List} of {@code Seat} objects representing all seats in the room
     * @throws SQLException if a database access error occurs during the query
     */
    List<Seat> getSeatsByRoomId(Connection conn, int roomId) throws SQLException;

    /**
     * Retrieves a list of accessible seats for the specified room.
     *
     * @param conn   the {@code Connection} object used for the database query
     * @param roomId the unique identifier of the room
     * @return a {@code List} of {@code Seat} objects representing the accessible seats
     * @throws SQLException if a database access error occurs during the query
     */
    List<Seat> getAccessibleSeats(Connection conn, int roomId) throws SQLException;

    /**
     * Retrieves a list of wheelchair-friendly seats for the specified room.
     *
     * @param conn   the {@code Connection} object used for the database query
     * @param roomId the unique identifier of the room
     * @return a {@code List} of {@code Seat} objects representing the wheelchair-friendly seats
     * @throws SQLException if a database access error occurs during the query
     */
    List<Seat> getWheelchairSeats(Connection conn, int roomId) throws SQLException;
}
