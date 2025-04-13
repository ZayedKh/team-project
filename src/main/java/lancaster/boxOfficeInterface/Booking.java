package lancaster.boxOfficeInterface;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a booking for a specific venue space.
 * <p>
 * A Booking stores information about the booked venue space including the date and time
 * details, the person who made the booking, and the seating configuration.
 * </p>
 */
public class Booking {
    private String space;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String bookedBy;
    private String configuration;

    /**
     * Constructs a Booking with the specified details.
     *
     * @param space         the venue space being booked
     * @param date          the date of the booking
     * @param startTime     the start time of the booking
     * @param endTime       the end time of the booking
     * @param bookedBy      the name of the person who made the booking
     * @param configuration the seating or space configuration for the booking
     */
    public Booking(String space, LocalDate date, LocalTime startTime, LocalTime endTime, String bookedBy, String configuration) {
        this.space = space;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bookedBy = bookedBy;
        this.date = date;
        this.configuration = configuration;
    }

    /**
     * Returns the venue space for this booking.
     *
     * @return the space as a String
     */
    public String getSpace() {
        return space;
    }

    /**
     * Returns the date of this booking.
     *
     * @return the booking date as a LocalDate
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the start time of this booking.
     *
     * @return the start time as a LocalTime
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Returns the end time of this booking.
     *
     * @return the end time as a LocalTime
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Returns the name of the person who made the booking.
     *
     * @return the name as a String
     */
    public String getBookedBy() {
        return bookedBy;
    }

    /**
     * Returns the seating or space configuration for this booking.
     *
     * @return the configuration as a String
     */
    public String getConfiguration() {
        return configuration;
    }

    /**
     * Sets the venue space for this booking.
     *
     * @param space the new venue space
     */
    public void setSpace(String space) {
        this.space = space;
    }

    /**
     * Sets the date for this booking.
     *
     * @param date the new booking date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Sets the start time for this booking.
     *
     * @param startTime the new start time as a LocalTime
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Sets the end time for this booking.
     *
     * @param endTime the new end time as a LocalTime
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Sets the name of the person who made the booking.
     *
     * @param bookedBy the new booking person's name
     */
    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }

    /**
     * Sets the seating or space configuration for this booking.
     *
     * @param configuration the new configuration
     */
    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }
}
