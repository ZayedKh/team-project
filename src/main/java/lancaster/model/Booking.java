package lancaster.model;

import java.util.Date;

/**
 * Model for a booking from a client. Holds information such as name of show, start and end
 * date.
 */
public class Booking {
    private final int bookingID;
    private int clientID;
    private String name;
    private Date startDate;
    private Date endDate;
    private boolean confirmed;

    /**
     * Constructor for a booking
     * @param bookingID ID for booking
     * @param clientID  ID of client
     * @param name      Name of the booking for what events are taking place
     * @param startDate Start date of when usage of rooms
     * @param endDate   End date of last usage of rooms
     * @param confirmed Is the booking confirmed by manager and client
     */
    public Booking(int bookingID, int clientID, String name, Date startDate, Date endDate, boolean confirmed) {
        this.bookingID = bookingID;
        this.clientID = clientID;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.confirmed = confirmed;
    }

    public int getBookingID() {
        return bookingID;
    }

    public int getClientID() {
        return clientID;
    }

    public String getName() {
        return name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}