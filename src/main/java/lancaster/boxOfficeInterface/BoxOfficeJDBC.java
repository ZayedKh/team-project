package lancaster.boxOfficeInterface;

import lancaster.model.Booking;
import lancaster.model.Seat;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Properties;


/**
 * The {@code BoxOfficeJDBC} class utilises the methods that need a connection to the database.
 * <p>
 * This class uses all of the methods that will require a {@code connection} to the database via JDBC
 * </p>
 *
 */
public class BoxOfficeJDBC {
    private final Connection connection;
    private final SeatDAOImpl seatingConfigDAO;
    private final EventDAOImpl eventDAO;
    private final RoomDAOImpl roomDAO;
    private final DailySheetDAOImpl dailySheetDAO;


    /**
     * Constructor for DBUtils that creates a connection between the user and the database
     * @throws SQLException             If a database access error occurs
     * @throws IOException              If an error occurs reading from input stream
     * @throws ClassNotFoundException   If the jdbc class cannot be found
     */
    public BoxOfficeJDBC() throws SQLException, ClassNotFoundException, IOException {
        Properties properties = new Properties();

        //read the credentials on given config file
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                throw new IOException("Unable to load config.properties file");
            }
            properties.load(input);
        }


        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        Class.forName("com.mysql.cj.jdbc.Driver");

        this.connection = DriverManager.getConnection(url, username, password);
        this.seatingConfigDAO = new SeatDAOImpl();
        this.roomDAO = new RoomDAOImpl();
        this.eventDAO = new EventDAOImpl();
        this.dailySheetDAO = new DailySheetDAOImpl();
    }

    /**
     * Checks whether the specified room has wheelchair-friendly seating available.
     * <p>
     * This method executes a query that counts the number of seats in the room where the column
     * {@code is_wheelchair_friendly} is set to {@code TRUE}.
     * </p>
     *
     * @param roomId the unique identifier of the room
     * @return {@code true} if there is at least one wheelchair-friendly seat; {@code false} otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean hasAccessibleSeating(int roomId) throws SQLException {
        return seatingConfigDAO.hasAccessableSeating(connection, roomId);
    }

    /**
     * Retrieves all seats for a specific room.
     * <p>
     * This method executes a query to obtain all seat records for the given room ordered by row number and seat number.
     * </p>
     *
     * @param roomId the unique identifier of the room
     * @return a {@code List} of {@code Seat} objects representing all seats in the room
     * @throws SQLException if a database access error occurs
     */
    public List<Seat> getSeatsByRoomId(int roomId) throws SQLException {
        return seatingConfigDAO.getSeatsByRoomId(connection, roomId);
    }

    /**
     * Retrieves only the accessible seats for a specific room.
     * <p>
     * This method executes a query to obtain seat records for the given room where the column {@code is_accessible}
     * is set to {@code TRUE}.
     * </p>
     *
     * @param roomId the unique identifier of the room
     * @return a {@code List} of {@code Seat} objects representing the accessible seats in the room
     * @throws SQLException if a database access error occurs
     */
    public List<Seat> getAccessibleSeats(int roomId) throws SQLException {
        return seatingConfigDAO.getAccessibleSeats(connection, roomId);
    }

    /**
     * Retrieves only the wheelchair-friendly seats for a specific room.
     * <p>
     * This method executes a query to obtain seat records for the given room where the column
     * {@code is_wheelchair_friendly} is set to {@code TRUE}.
     * </p>
     *
     * @param roomId the unique identifier of the room
     * @return a {@code List} of {@code Seat} objects representing the wheelchair-friendly seats in the room
     * @throws SQLException if a database access error occurs
     */
    public List<Seat> getWheelchairSeats(int roomId) throws SQLException {
        return seatingConfigDAO.getWheelchairSeats(connection, roomId);
    }

    /**
     * Retrieves the start time of the specified event.
     *
     * @param eventID the unique identifier of the event
     * @return the start time of the event as a {@link LocalTime}, or null if not found
     * @throws SQLException if a database access error occurs
     */
    public LocalTime getEventStartTime(int eventID) throws SQLException {
        return eventDAO.getStartTime(connection, eventID);
    }

    /**
     * Retrieves the end time of the specified event.
     *
     * @param eventID the unique identifier of the event
     * @return the end time of the event as a {@link LocalTime}, or null if not found
     * @throws SQLException if a database access error occurs
     */
    public LocalTime getEventEndTime(int eventID) throws SQLException {
        return eventDAO.getEndTime(connection, eventID);
    }

    /**
     * Retrieves the start date of the specified event.
     *
     * @param eventID the unique identifier of the event
     * @return the start date of the event as a {@link LocalDate}, or null if not found
     * @throws SQLException if a database access error occurs
     */
    public LocalDate getEventDate(int eventID) throws SQLException {
        return eventDAO.getEventStartDate(connection, eventID);
    }

    /**
     * Retrieves the duration of the specified event in minutes.
     * <p>
     * The duration is calculated as the difference between the event's start and end times.
     * </p>
     *
     * @param eventID the unique identifier of the event
     * @return the duration of the event in minutes, or -1 if the event could not be found or calculated
     * @throws SQLException if a database access error occurs
     */
    public int getEventDuration(int eventID) throws SQLException {
        return eventDAO.getDuration(connection, eventID);
    }

    /**
     * Retrieves the room ID associated with the specified event.
     *
     * @param eventID the unique identifier of the event
     * @return the room ID as an int, or -1 if not found
     * @throws SQLException if a database access error occurs
     */
    public int getEventRoomID(int eventID) throws SQLException {
        return eventDAO.getRoomID(connection, eventID);
    }

    /**
     * Retrieves all data for the specified event.
     * <p>
     * The result is provided as a {@link ResultSet} containing all columns for the event.
     * </p>
     *
     * @param eventID the unique identifier of the event
     * @return a {@link ResultSet} containing the event data
     * @throws SQLException if a database access error occurs
     */
    public ResultSet getEventData(int eventID) throws SQLException {
        return eventDAO.getData(connection, eventID);
    }

    /**
     * Retrieves the last booking time for a room specified by the roomId.
     * <p>
     * The method queries the room_booking_schedule table to find the most recent booking
     * for the specified room. It orders the results by booking_date and end_time in descending order
     * and combines the booking date and end time into a single Timestamp.
     * </p>
     *
     * @param roomId     the unique identifier of the room
     * @return a {@link Timestamp} representing the last booking time, or null if no booking is found
     * @throws SQLException if a database access error occurs during the query
     */
    public Timestamp getLastBookingTime(int roomId) throws SQLException {
        return roomDAO.getLastBookingTime(connection, roomId);
    }

    /**
     * Retrieves the capacity of a room specified by the roomID.
     *
     * @param roomID the unique identifier of the room
     * @return the capacity of the room as an int
     * @throws SQLException if the roomID is not found or a database access error occurs
     */
    public int getRoomCapacity(int roomID) throws SQLException {
        return roomDAO.getRoomCapacity(connection, roomID);
    }

    /**
     * Retrieves a list of bookings for the specified date.
     * <p>
     * The method executes a SQL query that joins the events, rooms, bookings, and seating_configurations tables.
     * It filters events by the given event date and orders the results by room name and start time.
     * For each row in the result set, a Booking object is created using details such as the room name,
     * start time, end time, customer name, and seating configuration.
     * </p>
     *
     * @param date the date for which the daily sheet of bookings is retrieved
     * @return a list of Booking objects representing the bookings for the specified date
     * @throws SQLException if a database access error occurs during the query execution
     */
    public List<Booking> getDailySheet(LocalDate date) throws SQLException {
        return dailySheetDAO.getDailySheet(connection, date);
    }

    public static void main(String[] args) {
        try {
            BoxOfficeJDBC boxOfficeJDBC = new BoxOfficeJDBC();
            System.out.println("BoxOfficeJDBC initialized successfully.");

            // Example usage
            int roomId = 1;
            boolean hasAccessibleSeating = boxOfficeJDBC.hasAccessibleSeating(roomId);
            System.out.println("Room " + roomId + " has accessible seating: " + hasAccessibleSeating);

            List<Seat> seats = boxOfficeJDBC.getSeatsByRoomId(roomId);
            System.out.println("Seats in room " + roomId + ": " + seats);

        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}
