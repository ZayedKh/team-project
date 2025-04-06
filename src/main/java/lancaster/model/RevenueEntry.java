package lancaster.model;


public class RevenueEntry {
    private String venue;
    private String date;
    private String bookingType;
    private double roomRate;
    private double ticketSales;
    private double totalRevenue;

    /**
     * Constructs a new revenue entry with the specified details.
     * The total revenue is automatically calculated from room hire and ticket sales.
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

    // Getters and setters
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getBookingType() { return bookingType; }
    public void setBookingType(String bookingType) { this.bookingType = bookingType; }
    public double getRoomRate() { return roomRate; }
    public void setRoomRate(double roomRate) {
        this.roomRate = roomRate;
        updateTotalRevenue();
    }
    public double getTicketSales() { return ticketSales; }
    public void setTicketSales(double ticketSales) {
        this.ticketSales = ticketSales;
        updateTotalRevenue();
    }
    public double getTotalRevenue() { return totalRevenue; }

    private void updateTotalRevenue() {
        this.totalRevenue = this.roomRate + this.ticketSales;
    }
}