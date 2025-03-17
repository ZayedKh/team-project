package classes;

import java.util.List;

/**
 * SeatingPlan class representing the seating configuration for an event.
 */
public class SeatingPlan {
    private int venueID;
    private int bookingID;
    private String configuration;
    private List<Integer> restrictedSeats;
    private int wheelchairSpaces;

    public SeatingPlan(int venueID, int bookingID, String configuration, List<Integer> restrictedSeats, int wheelchairSpaces) {
        this.venueID = venueID;
        this.bookingID = bookingID;
        this.configuration = configuration;
        this.restrictedSeats = restrictedSeats;
        this.wheelchairSpaces = wheelchairSpaces;
    }

    public void updateSeating(String newConfiguration) {
        this.configuration = newConfiguration;
        System.out.println("Seating configuration updated to: " + newConfiguration);
    }

    public void markRestrictedSeats(List<Integer> seats) {
        this.restrictedSeats.addAll(seats);
        System.out.println("Restricted seats updated: " + seats);
    }

    public boolean checkAccessibility() {
        return wheelchairSpaces > 0;
    }

    public List<Integer> getRestrictedSeats() {
        return restrictedSeats;
    }

    public int getVenueID() {
        return venueID;
    }

    public int getBookingID() {
        return bookingID;
    }

    public String getConfiguration() {
        return configuration;
    }

    public int getWheelchairSpaces() {
        return wheelchairSpaces;
    }
}