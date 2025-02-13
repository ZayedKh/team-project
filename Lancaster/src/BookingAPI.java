// API for managing booking functions, this allows marketing to book large groups & meetings
// Can be used by marketing and box office
public interface BookingAPI {
   Booking  createBooking(int bookingId, String newDetails);
   void updateBooking(int bookingId, String newDetails);
   void cancelBooking(int bookingId);
}
