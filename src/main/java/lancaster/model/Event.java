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
 * Class representing event details.
 * This class defines the essential attributes and behaviors related to an event.
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


    public Event(int event_id, int booking_id, int room_id, int seating_configurations_id, String name,
                 Date event_date, Time start_time, Time end_time){
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

    public int getEvent_id() {
        return event_id;
    }

    public int getBooking_id() {
        return booking_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public int getSeating_configurations_id() {
        return seating_configurations_id;
    }

    public String getName() {
        return name;
    }

    public Date getEvent_date() {
        return event_date;
    }

    public Time getStart_time() {
        return start_time;
    }

    public Time getEnd_time() {
        return end_time;
    }
}
