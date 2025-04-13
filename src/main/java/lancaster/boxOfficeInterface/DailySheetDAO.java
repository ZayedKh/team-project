package lancaster.boxOfficeInterface;

import lancaster.model.Booking;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * The DailySheetDAO interface provides a method to retrieve a daily sheet of bookings.
 * <p>
 * Implementations of this interface defines database for bookings
 * on a specific date.
 * </p>
 */
public interface DailySheetDAO {
    /**
     * Retrieves a list of bookings for a given date.
     *
     * @param conn the database connection to use for querying
     * @param date the date for which to retrieve the daily sheet of bookings
     * @return a List of Booking objects representing the bookings for the specified date
     * @throws SQLException if a database access error occurs
     */
    List<Booking> getDailySheet(Connection conn, LocalDate date) throws SQLException;
}
