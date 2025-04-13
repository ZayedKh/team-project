package lancaster.boxOfficeInterface;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Data Access Object (DAO) interface for events.
 * <p>
 * This interface defines methods to retrieve event-related information from the database.
 * Implementations are responsible for providing the underlying database operations.
 * </p>
 */
public interface EventDAO {

    /**
     * Retrieves the start time of a specified event.
     *
     * @param conn the database connection to use for the query
     * @param eventID the unique identifier of the event
     * @return the start time of the event as a {@link LocalTime}
     * @throws SQLException if a database access error occurs
     */
    LocalTime getStartTime(Connection conn, int eventID) throws SQLException;

    /**
     * Retrieves the end time of a specified event.
     *
     * @param conn the database connection to use for the query
     * @param eventID the unique identifier of the event
     * @return the end time of the event as a {@link LocalTime}
     * @throws SQLException if a database access error occurs
     */
    LocalTime getEndTime(Connection conn, int eventID) throws SQLException;

    /**
     * Retrieves the duration of a specified event.
     *
     * @param conn the database connection to use for the query
     * @param eventID the unique identifier of the event
     * @return the duration of the event (typically in minutes) as an int
     * @throws SQLException if a database access error occurs
     */
    int getDuration(Connection conn, int eventID) throws SQLException;

    /**
     * Retrieves the room ID associated with a specified event.
     *
     * @param conn the database connection to use for the query
     * @param eventID the unique identifier of the event
     * @return the room ID where the event is held as an int
     * @throws SQLException if a database access error occurs
     */
    int getRoomID(Connection conn, int eventID) throws SQLException;

    /**
     * Retrieves event data as a {@link ResultSet} for a specified event.
     *
     * @param conn the database connection to use for the query
     * @param eventID the unique identifier of the event
     * @return a {@link ResultSet} containing the event data
     * @throws SQLException if a database access error occurs
     */
    ResultSet getData(Connection conn, int eventID) throws SQLException;

    /**
     * Retrieves the start date of a specified event.
     *
     * @param conn the database connection to use for the query
     * @param eventID the unique identifier of the event
     * @return the start date of the event as a {@link LocalDate}
     * @throws SQLException if a database access error occurs
     */
    LocalDate getEventStartDate(Connection conn, int eventID) throws SQLException;
}
