package lancaster.boxOfficeInterface;

import lancaster.model.Booking;
import lancaster.model.Seat;
import lancaster.utils.DBUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A test class to execute and demonstrate various database operations for the Box Office system.
 * <p>
 * This class contains a {@code main} method that tests functionality from various modules including:
 * <ul>
 *   <li>{@code BoxOfficeJDBC}: for booking and seating operations</li>
 *   <li>{@code DBUtils}: for retrieving room names, generating daily sheets, and checking event schedules</li>
 *   <li>{@code Seat}: used to represent individual seat data</li>
 *   <li>{@code Booking}: used to represent booking records</li>
 * </ul>
 * </p>
 *
 * <p>
 * Methods from the {@code BoxOfficeJDBC} and {@code DBUtils} classes are invoked to demonstrate:
 * <ul>
 *   <li>Retrieving room names</li>
 *   <li>Generating and printing a daily sheet of {@code Booking} objects</li>
 *   <li>Fetching seating configurations, including accessible and wheelchair-friendly seats</li>
 *   <li>Obtaining event timing information such as start time, end time, duration, and event date</li>
 *   <li>Getting room capacity and last booking time details</li>
 *   <li>Checking if an event is scheduled for a specific time period</li>
 * </ul>
 * </p>
 *
 * @event This class is designed for testing.
 */
public class Test {
    /**
     * The main entry point of the application.
     *
     * @param args command-line arguments (not used)
     * @throws SQLException if a database access error occurs during the operation
     * @throws ClassNotFoundException if the JDBC driver class is not found
     * @throws IOException if there is an error reading configuration or data files
     *
     * @code The {@code main} method demonstrates database operations including retrieving room names,
     * generating daily sheets via {@code DBUtils}, and interacting with event and room configuration methods
     * from {@code BoxOfficeJDBC}.
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        BoxOfficeJDBC db = new BoxOfficeJDBC();
        DBUtils dbUtils = new DBUtils();

        List<String> roomNames = dbUtils.getRoomNames();

        System.out.println("Room Names: " + roomNames);

        LocalDate localDate = LocalDate.now();

        List<Booking> bookings = dbUtils.generateDailySheets(localDate);

        for (Booking booking : bookings) {
            System.out.println(booking.getBookedBy());
        }

        System.out.println("TESTING SEATING CONFIG");
        List<Seat> wheelchairSeats = db.getWheelchairSeats(1);
        boolean hasAccessibleSeats = db.hasAccessibleSeating(1);
        System.out.println(hasAccessibleSeats);

        for (Seat seat : wheelchairSeats) {
            String seatInfo = seat.toString();
            System.out.println(seatInfo);
        }
        System.out.println();

        System.out.println("TESTING  BOOKING CONFIG");
        LocalTime startDate = db.getEventStartTime(1);
        System.out.println("Start date of event ID: 1: " + startDate);
        System.out.println("End date of event ID: 1: " + db.getEventEndTime(1));
        System.out.println("Duration of event ID 1: " + db.getEventDuration(1) + " minutes");
        System.out.println("Date of event ID 1: " + db.getEventDate(1));
        System.out.println();

        System.out.println("TESTING ROOM CONFIG");
        System.out.println(db.getRoomCapacity(10));
        System.out.println(db.getLastBookingTime(10));
        System.out.println();

        LocalDate date = LocalDate.of(2025, 4, 10);
        System.out.println("Get daily sheet for " + date);
        List<Booking> dailySheets = db.getDailySheet(date);

        for (Booking booking : dailySheets) {
            System.out.println(booking.getBookedBy());
            System.out.println(booking.getStartDate().toString());
            System.out.println(booking.getConfiguration());
        }

        LocalTime startTime = LocalTime.of(17, 59, 59);
        LocalTime endTime = LocalTime.of(19, 59, 59);
        LocalDate date21 = LocalDate.of(2025, 04, 10);

        System.out.println(dbUtils.isEventScheduled(date21, startTime, endTime));
    }
}
