package lancaster.boxOfficeInterface;

import lancaster.model.Booking;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of the {@link DailySheetDAO} interface.
 * <p>
 * This class retrieves a daily sheet of bookings from the database.
 * It queries multiple tables including events, rooms, bookings, and seating configurations to build
 * a list of Booking objects for a specific date.
 * </p>
 */
public class DailySheetDAOImpl implements DailySheetDAO {

    /**
     * Retrieves a list of bookings for the specified date.
     * <p>
     * The method executes a SQL query that joins the events, rooms, bookings, and seating_configurations tables.
     * It filters events by the given event date and orders the results by room name and start time.
     * For each row in the result set, a Booking object is created using details such as the room name,
     * start time, end time, customer name, and seating configuration.
     * </p>
     *
     * @param conn the database connection to be used for the query
     * @param date the date for which the daily sheet of bookings is retrieved
     * @return a list of Booking objects representing the bookings for the specified date
     * @throws SQLException if a database access error occurs during the query execution
     */
    @Override
    public List<Booking> getDailySheet(Connection conn, LocalDate date) throws SQLException {
        String query = """
                    SELECT 
                        r.room_name,
                        e.event_date,
                        e.start_time,
                        e.end_time,
                        b.end_date,
                        b.customer_name,
                        sc.configuration_name
                    FROM events e
                    JOIN rooms r ON e.room_id = r.room_id
                    JOIN bookings b ON e.booking_id = b.booking_id
                    JOIN seating_configurations sc ON e.seating_config_id = sc.seating_config_id
                    WHERE e.event_date = ?
                    ORDER BY r.room_name, e.start_time;
                """;

        List<Booking> sheet = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String roomName = rs.getString("room_name");
                LocalDate endDate = rs.getDate("end_date").toLocalDate();
                LocalTime startTime = rs.getTime("start_time").toLocalTime();
                LocalTime endTime = rs.getTime("end_time").toLocalTime();
                String customerName = rs.getString("customer_name");
                String configName = rs.getString("configuration_name");
                sheet.add(new Booking(roomName, date, endDate, startTime, endTime, customerName, configName));
            }
        }

        return sheet;
    }
}
