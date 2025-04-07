package lancaster.boxOfficeInterface;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a booking for a space within a given date and time.
 * Contains details about the space, booking date, time, who booked it,
 * and the room configuration.
 */
public class Booking {
    private String space;             // The space or room being booked
    private LocalDate date;          // The date of the booking
    private LocalTime startTime;     // The starting time of the booking
    private LocalTime endTime;       // The ending time of the booking
    private String bookedBy;         // The person or group who made the booking
    private String configuration;    // The room configuration (e.g., theatre, classroom, etc.)

    /**
     * Constructs a new Booking instance with the given details.
     *
     * @param space         the space being booked
     * @param date          the date of the booking
     * @param startTime     the starting time of the booking
     * @param endTime       the ending time of the booking
     * @param bookedBy      who made the booking
     * @param configuration the configuration of the space
     */
    public Booking(String space, LocalDate date, LocalTime startTime, LocalTime endTime, String bookedBy, String configuration) {
        this.space = space;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bookedBy = bookedBy;
        this.configuration = configuration;
    }

    /**
     * @return the space being booked
     */
    public String getSpace() {
        return space;
    }

    /**
     * @return the date of the booking
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @return the starting time of the booking
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * @return the ending time of the booking
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * @return the name or identifier of who made the booking
     */
    public String getBookedBy() {
        return bookedBy;
    }

    /**
     * @return the configuration of the booked space
     */
    public String getConfiguration() {
        return configuration;
    }

    /**
     * Sets the space being booked.
     *
     * @param space the space to set
     */
    public void setSpace(String space) {
        this.space = space;
    }

    /**
     * Sets the date of the booking.
     *
     * @param date the date to set
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Sets the starting time of the booking.
     *
     * @param startTime the start time to set
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Sets the ending time of the booking.
     *
     * @param endTime the end time to set
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Sets who made the booking.
     *
     * @param bookedBy the name or identifier of the person who booked
     */
    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }

    /**
     * Sets the configuration of the booked space.
     *
     * @param configuration the configuration to set
     */
    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }
}
