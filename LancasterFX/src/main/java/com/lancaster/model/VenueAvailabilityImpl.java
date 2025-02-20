package com.lancaster.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of the VenueAvailability interface.
 * Manages venue details and provides availability checks.
 */
public class VenueAvailabilityImpl implements VenueAvailability {

    private final List<Venue> venues; // Stores the list of all venues

    /**
     * Constructs a VenueAvailabilityImpl instance with a list of venues.
     *
     * @param venues A list of venues to manage.
     */
    public VenueAvailabilityImpl(List<Venue> venues) {
        this.venues = venues != null ? new ArrayList<>(venues) : new ArrayList<>();
    }

    @Override
    public String getVenueId() {
        throw new UnsupportedOperationException("Operation not supported for multiple venues.");
    }

    @Override
    public String getVenueName() {
        throw new UnsupportedOperationException("Operation not supported for multiple venues.");
    }

    @Override
    public int getCapacity() {
        throw new UnsupportedOperationException("Operation not supported for multiple venues.");
    }

    @Override
    public VenueStatus getAvailabilityStatus() {
        throw new UnsupportedOperationException("Operation not supported for multiple venues.");
    }

    @Override
    public List<String> getAvailableTimeSlots() {
        throw new UnsupportedOperationException("Operation not supported for multiple venues.");
    }

    @Override
    public LocalDateTime getReservationDeadline() {
        throw new UnsupportedOperationException("Operation not supported for multiple venues.");
    }

    @Override
    public boolean isVenueAvailable(String venueId, LocalDateTime dateTime) {
        if (venueId == null || dateTime == null) {
            return false;
        }
        for (Venue venue : venues) {
            if (venue.getVenueId().equals(venueId) &&
                    venue.getAvailabilityStatus() == VenueStatus.AVAILABLE &&
                    dateTime.isBefore(venue.getReservationDeadline())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Venue> getAllVenues() {
        return Collections.unmodifiableList(venues);
    }

    @Override
    public Venue getVenueById(String venueId) {
        if (venueId == null) {
            return null;
        }
        return venues.stream()
                .filter(venue -> venue.getVenueId().equals(venueId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Venue> getVenuesByDate(LocalDateTime date) {
        if (date == null) {
            return Collections.emptyList();
        }
        List<Venue> availableVenues = new ArrayList<>();
        for (Venue venue : venues) {
            if (venue.getAvailabilityStatus() == VenueStatus.AVAILABLE &&
                    date.isBefore(venue.getReservationDeadline())) {
                availableVenues.add(venue);
            }
        }
        return Collections.unmodifiableList(availableVenues);
    }

    @Override
    public String toString() {
        return "VenueAvailabilityImpl{" +
                "venues=" + venues +
                '}';
    }
}
