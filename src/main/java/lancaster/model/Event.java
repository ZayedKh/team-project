package lancaster.model;

import lancaster.utils.DBUtils;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Represents an event within the system.
 * <p>
 * The {@code Event} class encapsulates the details of an event, including its identifiers,
 * associated booking and room details, event name, date, and time information.
 * It retrieves additional room information from the database based on the provided room identifier.
 * </p>
 */
public class Event {
    private int event_id;
    private int booking_id;
    private int room_id;
    private String room_name;
    private int seating_configurations_id;
    private String name;
    private Date event_date;
    private Time start_time;
    private Time end_time;

    /**
     * Constructs a new {@code Event} instance with the specified details.
     * <p>
     * This constructor initializes the event with its identifiers, booking information, room
     * configurations, name, date, and start/end times. It also retrieves the room name from the
     * database using the provided {@code room_id}.
     * </p>
     *
     * @param event_id                    the unique identifier for the event.
     * @param booking_id                  the identifier linking the event with a booking.
     * @param room_id                     the unique identifier for the room where the event takes place.
     * @param seating_configurations_id   the identifier for seating configurations related to the event.
     * @param name                        the name of the event.
     * @param event_date                  the date on which the event is scheduled.
     * @param start_time                  the starting time of the event.
     * @param end_time                    the ending time of the event.
     * @throws RuntimeException if an error occurs while retrieving the room name from the database.
     */
    public Event(int event_id, int booking_id, int room_id, int seating_configurations_id, String name,
                 Date event_date, Time start_time, Time end_time) {
        this.event_id = event_id;
        this.booking_id = booking_id;
        try {
            DBUtils db = new DBUtils();
            this.room_name = db.getRoomName(room_id);
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.room_id = room_id;
        this.seating_configurations_id = seating_configurations_id;
        this.name = name;
        this.event_date = event_date;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    /**
     * Returns the unique identifier of the event.
     *
     * @return the event ID.
     */
    public int getEvent_id() {
        return event_id;
    }

    /**
     * Returns the booking identifier associated with the event.
     *
     * @return the booking ID.
     */
    public int getBooking_id() {
        return booking_id;
    }

    /**
     * Returns the unique identifier of the room where the event takes place.
     *
     * @return the room ID.
     */
    public int getRoom_id() {
        return room_id;
    }

    /**
     * Returns the name of the room where the event is held.
     *
     * @return the room name.
     */
    public String getRoom_name() {
        return room_name;
    }

    /**
     * Returns the identifier for the seating configuration related to the event.
     *
     * @return the seating configurations ID.
     */
    public int getSeating_configurations_id() {
        return seating_configurations_id;
    }

    /**
     * Returns the name of the event.
     *
     * @return the event name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the date on which the event is scheduled.
     *
     * @return the event date.
     */
    public Date getEvent_date() {
        return event_date;
    }

    /**
     * Returns the starting time of the event.
     *
     * @return the start time.
     */
    public Time getStart_time() {
        return start_time;
    }

    /**
     * Returns the ending time of the event.
     *
     * @return the end time.
     */
    public Time getEnd_time() {
        return end_time;
    }
}
