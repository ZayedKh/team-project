package lancaster.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a booking for a venue space within the application.
 * <p>
 * This class encapsulates details about a booking, including the space name, start and end dates and times,
 * the person who made the booking, and any configuration information associated with the booking.
 * </p>
 */
public class Booking {
    private String space;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String bookedBy;
    private String configuration;

    /**
     * Constructs a new {@code Booking} instance with the specified booking details.
     *
     * @param space         the name of the space being booked.
     * @param startDate     the start date of the booking.
     * @param endDate       the end date of the booking.
     * @param startTime     the start time of the booking.
     * @param endTime       the end time of the booking.
     * @param bookedBy      the name of the person who made the booking.
     * @param configuration the configuration details associated with the booking.
     */
    public Booking(String space, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, String bookedBy, String configuration) {
        this.space = space;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bookedBy = bookedBy;
        this.startDate = startDate;
        this.endDate = endDate;
        this.configuration = configuration;
    }

    /**
     * Returns the name of the booked space.
     *
     * @return the space name.
     */
    public String getSpace() {
        return space;
    }

    /**
     * Returns the start date of the booking.
     *
     * @return the start date.
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Returns the start time of the booking.
     *
     * @return the start time.
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Returns the end date of the booking.
     *
     * @return the end date.
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Returns the end time of the booking.
     *
     * @return the end time.
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Returns the identifier or name of the person who made the booking.
     *
     * @return the booker's name.
     */
    public String getBookedBy() {
        return bookedBy;
    }

    /**
     * Returns the configuration details associated with the booking.
     *
     * @return the booking configuration.
     */
    public String getConfiguration() {
        return configuration;
    }

    /**
     * Sets the name of the booked space.
     *
     * @param space the new space name.
     */
    public void setSpace(String space) {
        this.space = space;
    }

    /**
     * Sets the start date of the booking.
     *
     * @param startDate the new start date.
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Sets the start time of the booking.
     *
     * @param startTime the new start time.
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Sets the end time of the booking.
     *
     * @param endTime the new end time.
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Sets the identifier or name of the person who made the booking.
     *
     * @param bookedBy the new booker's name.
     */
    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }

    /**
     * Sets the configuration details associated with the booking.
     *
     * @param configuration the new configuration details.
     */
    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }
}
