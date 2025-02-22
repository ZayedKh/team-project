package classes;

import enums.VenueStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class ConcreteVenue extends Venue {

    public ConcreteVenue(String venueId, String venueName, int capacity, VenueStatus availabilityStatus,
                  List<String> availableTimeSlots, LocalDateTime reservationDeadLine) {
        super(venueId, venueName, capacity, availabilityStatus, availableTimeSlots, reservationDeadLine);
    }

    @Override
    public boolean isVenueAvailable(String venueId, LocalDateTime dateTime) {
        return false;
    }
}
