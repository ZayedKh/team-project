package classes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Booking {
    private static Map<Integer, Booking> bookings = new HashMap<>();

    private int bookingID;
    private String eventID;
    private String clientName;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;

    public Booking(int bookingID, String eventID, String clientName, LocalDate startDate, LocalDate endDate,
                   LocalDateTime startTime, LocalDateTime endTime, String status) {
        this.bookingID = bookingID;
        this.eventID = eventID;
        this.clientName = clientName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    // Create a booking and store it in a map
    public void createBooking() {
        if (!bookings.containsKey(bookingID)) {
            bookings.put(bookingID, this);
            System.out.println("Booking created successfully: " + bookingID);
        } else {
            System.out.println("Booking ID already exists. Choose a different ID.");
        }
    }

    // Update booking details
    public void updateBooking(String newStatus) {
        if (bookings.containsKey(bookingID)) {
            this.status = newStatus;
            bookings.put(bookingID, this);
            System.out.println("Booking updated successfully. New status: " + newStatus);
        } else {
            System.out.println("Booking ID not found.");
        }
    }

    // Process a booking (e.g., mark it as confirmed)
    public void processBooking() {
        if (bookings.containsKey(bookingID)) {
            this.status = "Confirmed";
            bookings.put(bookingID, this);
            System.out.println("Booking processed and confirmed: " + bookingID);
        } else {
            System.out.println("Booking not found.");
        }
    }

    // Cancel a booking
    public void cancelBooking() {
        if (bookings.containsKey(bookingID)) {
            bookings.remove(bookingID);
            System.out.println("Booking canceled: " + bookingID);
        } else {
            System.out.println("Booking not found.");
        }
    }

    // Getter methods
    public int getBookingID() {
        return bookingID;
    }

    public String getEventID() {
        return eventID;
    }

    public String getClientName() {
        return clientName;
    }

    public String getStatus() {
        return status;
    }
}

