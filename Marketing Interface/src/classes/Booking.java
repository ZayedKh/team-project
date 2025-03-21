package classes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Booking {
    private final static Map<Integer, Booking> bookings = new HashMap<>();

    private final int bookingID;
    private final String eventID;
    private final String clientName;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
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


    public static Booking createBooking(int bookingID, String eventID, String clientName, LocalDate startDate,
                                        LocalDate endDate, LocalDateTime startTime, LocalDateTime endTime) {
        if (bookings.containsKey(bookingID)) {
            System.out.println("Booking ID already exists. Choose a different ID.");
            return null; // Prevents duplicate bookings
        }

        Booking newBooking = new Booking(bookingID, eventID, clientName, startDate, endDate, startTime, endTime, "Pending");
        bookings.put(bookingID, newBooking);
        System.out.println("Booking created successfully: " + bookingID);
        return newBooking;
    }

    public void updateBooking(String newStatus) {
        if (bookings.containsKey(bookingID)) {
            this.status = newStatus;
            bookings.put(bookingID, this);
            System.out.println("Booking updated successfully. New status: " + newStatus);
        } else {
            System.out.println("Booking ID not found.");
        }
    }

    public void processBooking() {
        if (bookings.containsKey(bookingID)) {
            this.status = "Confirmed";
            bookings.put(bookingID, this);
            System.out.println("Booking processed and confirmed: " + bookingID);
        } else {
            System.out.println("Booking not found.");
        }
    }

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
