package lancaster.boxOfficeInterface;

import lancaster.model.Booking;
import lancaster.model.Seat;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * The BoxOfficeJDBC class provides methods to access data required by box office through JDBC.
 * <p>
 * This class creates a connection to a MySQL database and exposes methods to query seating configurations,
 * event details, room information, and daily sheets. It uses various DAO implementations to perform these operations.
 * </p>
 */
public class BoxOfficeJDBC {
    private final Connection connection;
    private final SeatDAOImpl seatingConfigDAO;
    private final EventDAOImpl eventDAO;
    private final RoomDAOImpl roomDAO;
    private final DailySheetDAOImpl dailySheetDAO;

    /**
     * Constructs a new BoxOfficeJDBC object by establishing a database connection and initializing DAOs.
     *
     * @throws SQLException           if a database access error occurs
     * @throws ClassNotFoundException if the JDBC driver class cannot be found
     */
    public BoxOfficeJDBC() throws SQLException, ClassNotFoundException {
        String url = "jdbc:mysql://sst-stuproj00:3306/in2033t44";
        String username = "in2033t44_a";
        String password = "wcYtgG2jphQ";

        Class.forName("com.mysql.cj.jdbc.Driver");

        this.connection = DriverManager.getConnection(url, username, password);
        this.seatingConfigDAO = new SeatDAOImpl();
        this.roomDAO = new RoomDAOImpl();
        this.eventDAO = new EventDAOImpl();
        this.dailySheetDAO = new DailySheetDAOImpl();
    }

    /**
     * Checks whether the specified room has accessible seating.
     *
     * @param roomId the ID of the room
     * @return true if accessible seating is available, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean hasAccessibleSeating(int roomId) throws SQLException {
        return seatingConfigDAO.hasAccessableSeating(connection, roomId);
    }

    /**
     * Retrieves all seats for the specified room.
     *
     * @param roomId the ID of the room
     * @return a List of Seat objects
     * @throws SQLException if a database error occurs
     */
    public List<Seat> getSeatsByRoomId(int roomId) throws SQLException {
        return seatingConfigDAO.getSeatsByRoomId(connection, roomId);
    }

    /**
     * Retrieves accessible seats for the specified room.
     *
     * @param roomId the ID of the room
     * @return a List of accessible Seat objects
     * @throws SQLException if a database error occurs
     */
    public List<Seat> getAccessibleSeats(int roomId) throws SQLException {
        return seatingConfigDAO.getAccessibleSeats(connection, roomId);
    }

    /**
     * Retrieves wheelchair accessible seats for the specified room.
     *
     * @param roomId the ID of the room
     * @return a List of wheelchair accessible Seat objects
     * @throws SQLException if a database error occurs
     */
    public List<Seat> getWheelchairSeats(int roomId) throws SQLException {
        return seatingConfigDAO.getWheelchairSeats(connection, roomId);
    }

    /**
     * Retrieves the start time for the event with the specified event ID.
     *
     * @param eventID the ID of the event
     * @return the start time as a LocalTime object
     * @throws SQLException if a database error occurs
     */
    public LocalTime getEventStartTime(int eventID) throws SQLException {
        return eventDAO.getStartTime(connection, eventID);
    }

    /**
     * Retrieves the end time for the event with the specified event ID.
     *
     * @param eventID the ID of the event
     * @return the end time as a LocalTime object
     * @throws SQLException if a database error occurs
     */
    public LocalTime getEventEndTime(int eventID) throws SQLException {
        return eventDAO.getEndTime(connection, eventID);
    }

    /**
     * Retrieves the date of the event with the specified event ID.
     *
     * @param eventID the ID of the event
     * @return the event date as a LocalDate
     * @throws SQLException if a database error occurs
     */
    public LocalDate getEventDate(int eventID) throws SQLException {
        return eventDAO.getEventStartDate(connection, eventID);
    }

    /**
     * Retrieves the duration of the event with the specified event ID.
     *
     * @param eventID the ID of the event
     * @return the event duration as an integer (e.g., in minutes)
     * @throws SQLException if a database error occurs
     */
    public int getEventDuration(int eventID) throws SQLException {
        return eventDAO.getDuration(connection, eventID);
    }

    /**
     * Retrieves the room ID where the event with the specified event ID is held.
     *
     * @param eventID the ID of the event
     * @return the room ID as an integer
     * @throws SQLException if a database error occurs
     */
    public int getEventRoomID(int eventID) throws SQLException {
        return eventDAO.getRoomID(connection, eventID);
    }

    /**
     * Retrieves a ResultSet containing data for the event with the specified event ID.
     *
     * @param eventID the ID of the event
     * @return a ResultSet containing event data
     * @throws SQLException if a database error occurs
     */
    public ResultSet getEventData(int eventID) throws SQLException {
        return eventDAO.getData(connection, eventID);
    }

    /**
     * Retrieves the last booking time for the specified room.
     *
     * @param roomId the ID of the room
     * @return the last booking time as a Timestamp
     * @throws SQLException if a database error occurs
     */
    public Timestamp getLastBookingTime(int roomId) throws SQLException {
        return roomDAO.getLastBookingTime(connection, roomId);
    }

    /**
     * Retrieves the capacity for the specified room.
     *
     * @param roomID the ID of the room
     * @return the room capacity as an integer
     * @throws SQLException if a database error occurs
     */
    public int getRoomCapacity(int roomID) throws SQLException {
        return roomDAO.getRoomCapacity(connection, roomID);
    }

    /**
     * Retrieves the daily sheet (list of bookings) for a given date.
     *
     * @param date the date for which to retrieve the daily sheet
     * @return a List of Booking objects for the specified date
     * @throws SQLException if a database error occurs
     */
    public List<Booking> getDailySheet(LocalDate date) throws SQLException {
        return dailySheetDAO.getDailySheet(connection, date);
    }

    /**
     * The main method for testing the BoxOfficeJDBC class.
     *
     * @param args the command line arguments
     */
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

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
