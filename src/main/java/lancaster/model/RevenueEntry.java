package lancaster.model;

/**
 * The {@code RevenueEntry} class represents an entry containing revenue information for a specific event or booking.
 * <p>
 * This class holds details such as the venue, event date, booking type, room rate revenue, ticket sales revenue,
 * and the computed total revenue. The total revenue is automatically updated whenever the room rate or ticket sales are modified.
 * </p>
 */
public class RevenueEntry {
    private String venue;
    private String date;
    private String bookingType;
    private double roomRate;
    private double ticketSales;
    private double totalRevenue;

    /**
     * Constructs a new {@code RevenueEntry} with the specified details.
     * <p>
     * The total revenue is automatically calculated as the sum of the room hire revenue (roomRate) and ticket sales revenue (ticketSales).
     * </p>
     *
     * @param venue       the name of the venue hosting the event
     * @param date        the date of the event in string format (e.g., "2025-04-10")
     * @param bookingType the type of booking for the event (e.g., "Hourly", "Full Day")
     * @param roomRate    the revenue generated from room hire
     * @param ticketSales the revenue generated from ticket sales
     */
    public RevenueEntry(String venue, String date, String bookingType, double roomRate, double ticketSales) {
        this.venue = venue;
        this.date = date;
        this.bookingType = bookingType;
        this.roomRate = roomRate;
        this.ticketSales = ticketSales;
        this.totalRevenue = roomRate + ticketSales;
    }

    /**
     * Retrieves the venue name associated with this revenue entry.
     *
     * @return the venue name
     */
    public String getVenue() {
        return venue;
    }

    /**
     * Sets the venue name for this revenue entry.
     *
     * @param venue the name of the venue
     */
    public void setVenue(String venue) {
        this.venue = venue;
    }

    /**
     * Retrieves the date of the event as a String.
     *
     * @return the event date in string format
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date of the event.
     *
     * @param date the date of the event in string format (e.g., "2025-04-10")
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Retrieves the booking type for the event.
     *
     * @return the booking type (e.g., "Hourly", "Full Day")
     */
    public String getBookingType() {
        return bookingType;
    }

    /**
     * Sets the booking type for this revenue entry.
     *
     * @param bookingType the booking type (e.g., "Hourly", "Full Day")
     */
    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    /**
     * Retrieves the revenue generated from room hire.
     *
     * @return the room rate revenue
     */
    public double getRoomRate() {
        return roomRate;
    }

    /**
     * Sets the revenue generated from room hire.
     * <p>
     * Updates the total revenue automatically after setting the new room rate.
     * </p>
     *
     * @param roomRate the room hire revenue
     */
    public void setRoomRate(double roomRate) {
        this.roomRate = roomRate;
        updateTotalRevenue();
    }

    /**
     * Retrieves the revenue generated from ticket sales.
     *
     * @return the ticket sales revenue
     */
    public double getTicketSales() {
        return ticketSales;
    }

    /**
     * Sets the revenue generated from ticket sales.
     * <p>
     * Updates the total revenue automatically after setting the new ticket sales amount.
     * </p>
     *
     * @param ticketSales the revenue generated from ticket sales
     */
    public void setTicketSales(double ticketSales) {
        this.ticketSales = ticketSales;
        updateTotalRevenue();
    }

    /**
     * Retrieves the total revenue calculated as the sum of room hire and ticket sales revenues.
     *
     * @return the total revenue
     */
    public double getTotalRevenue() {
        return totalRevenue;
    }

    /**
     * Updates the total revenue by summing the current room rate and ticket sales.
     * <p>
     * This method is called internally when either the room rate or ticket sales are modified.
     * </p>
     */
    private void updateTotalRevenue() {
        this.totalRevenue = this.roomRate + this.ticketSales;
    }
}
