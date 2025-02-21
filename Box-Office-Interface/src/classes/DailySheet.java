package classes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * This is a daily sheet object containing details on a specific day. This is very basic
 * at the moment and can definitely be improved
 */
public class DailySheet {
    LocalDate date;
    ArrayList<Event> events = new ArrayList<>();
    LocalTime openTime;
    LocalTime closeTime;

    /**
     * The constructor for a daily sheet
     * @param date      The date that the daily sheet is created for
     * @param events    The events for that date provided
     * @param openTime  The opening time for the building on date
     * @param closeTime The closing time for the building on date
     */
    public DailySheet(LocalDate date, ArrayList<Event> events, LocalTime openTime,
                      LocalTime closeTime){
        this.date = date;
        this.events = events;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    /**
     * Get the date of daily sheet
     * @return The date of sheet
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Get the events of the daily sheet
     * @return The events of sheet
     */
    public ArrayList<Event> getEvents() {
        return events;
    }

    /**
     * Get the opening time of the daily sheet
     * @return The opening time of sheet
     */
    public LocalTime getOpenTime() {
        return openTime;
    }

    /**
     * Get the closing time of the daily sheet
     * @return The closing time of sheet
     */
    public LocalTime getCloseTime() {
        return closeTime;
    }
}
