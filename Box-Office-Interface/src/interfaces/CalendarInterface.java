package interfaces;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import classes.*;

public interface CalendarInterface {

    /**
     * This will get the event for all the events in a single day
     * @param connection The connection to the database
     * @param date The date being searched for
     * @return A list of events for that date
     */
    ArrayList<ConcreteEvent> getEventForDate(Connection connection, LocalDateTime date);

    /**
     * This will return all events that are in the current database no matter the
     * conditions that they are in
     * @param connection The connection to the database
     * @return All the events currently listed
     */
    ArrayList<ConcreteEvent> getAllEvents(Connection connection);

    /**
     * This will return all events in the database by ordering by the closest events
     * coming up by date
     * @param connection The connection to the database
     * @return All events in the order of dates
     */
    ArrayList<ConcreteEvent> getMostRecentEvents(Connection connection);

    /**
     * Gets all the events in a certain month
     * @param connection The connection to the database
     * @param month A time. Month variable such as APRIL
     * @return All events in that month
     */
    ArrayList<ConcreteEvent> getEventByMonth(Connection connection, Month month);

}
