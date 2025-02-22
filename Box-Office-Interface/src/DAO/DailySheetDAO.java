package DAO;

import models.ConcreteEvent;
import models.DailySheet;
import models.Event;
import interfaces.DBConnect;
import interfaces.SheetInterface;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class DailySheetDAO implements SheetInterface, DBConnect {

    /**
     * This will generate a daily report based of the current date
     * @param connection The connection to the database
     * @return The daily sheet of current date
     */
    @Override
    public DailySheet generateTodaySheet(Connection connection) {
        LocalDate today = LocalDate.now();
        return generateDateSheet(today, connection);
    }

    /**
     * This will generate a daily report of the specified date
     * @param date The date of daily sheet being generated
     * @param connection The connection to the database
     * @return The daily sheet of specified date
     */
    @Override
    public DailySheet generateDateSheet(LocalDate date, Connection connection) {

        ArrayList<Event> eventList = new ArrayList<>();
        String sql = "SELECT * FROM events WHERE DATE(?) = DATE(startDate)";

        try {
            
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setTimestamp(1, Timestamp.valueOf(String.valueOf(date)));

            ResultSet rs = statement.executeQuery();
            eventList.add(new ConcreteEvent(rs.getString("eventID"),
                    rs.getString("eventName"),
                    rs.getTimestamp("startDate").toLocalDateTime(),
                    rs.getTimestamp("endDate").toLocalDateTime(),
                    rs.getString("eventType"),
                    rs.getString("eventStatus"),
                    rs.getString("eventVenue")));

        } catch (SQLException e) {

            throw new RuntimeException(e);

        }
        return new DailySheet(date, eventList, LocalTime.parse("8:00"),
                LocalTime.parse("20:00"));
    }

    /**
     * Establishes a connection between the user and the operation database with the
     * correct url, username and password for data details
     * @param url The url of the mySQL database
     * @param username The username of the database
     * @param password The password of the database
     * @return A connection between the user and database
     */
    @Override
    public Connection connect(String url, String username, String password) {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(url, username, password);

        } catch (SQLException | ClassNotFoundException e) {

            throw new RuntimeException(e);

        }
    }

    /**
     * Closes the connection between the user and the database
     * +----------------------------------+
     * |  PLEASE CLOSE AFTER CONNECTING!! |
     * +----------------------------------+re
     * @param connection The connection being used for connection
     */
    @Override
    public void disconnect(Connection connection) {
        try {

            connection.close();

        } catch (SQLException e) {

            throw new RuntimeException(e);

        }
    }
}
