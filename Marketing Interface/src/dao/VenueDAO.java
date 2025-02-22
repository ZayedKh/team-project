package dao;

import classes.Venue;
import enums.VenueStatus;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import classes.ConcreteVenue;

/**
 * Data Access Object (DAO) class for managing Venue entities in the database.
 */
public class VenueDAO {
    private final Connection connection;

    /**
     * Constructs a VenueDAO with the specified database connection.
     *
     * @param connection The database connection to be used by this DAO.
     */
    public VenueDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Adds a new venue to the database.
     *
     * @param venue The venue to be added.
     * @throws SQLException If a database access error occurs.
     */
    public void addVenue(Venue venue) throws SQLException {
        String sql = "INSERT INTO venues (venueId, venueName, capacity, availabilityStatus, reservationDeadline) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, venue.getVenueId());
            stmt.setString(2, venue.getVenueName());
            stmt.setInt(3, venue.getCapacity());
            stmt.setString(4, venue.getAvailabilityStatus().name());
            stmt.setTimestamp(5, Timestamp.valueOf(venue.getReservationDeadline()));
            stmt.executeUpdate();
        }
    }

    /**
     * Retrieves a venue from the database by its unique identifier.
     *
     * @param venueId The unique identifier of the venue.
     * @return The venue object if found, otherwise null.
     * @throws SQLException If a database access error occurs.
     */
    public Venue getVenueById(String venueId) throws SQLException {
        String sql = "SELECT * FROM venues WHERE venueId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, venueId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToVenue(rs);
            }
        }
        return null;
    }

    /**
     * Retrieves all venues from the database.
     *
     * @return A list of all venues.
     * @throws SQLException If a database access error occurs.
     */
    public List<Venue> getAllVenues() throws SQLException {
        List<Venue> venues = new ArrayList<>();
        String sql = "SELECT * FROM venues";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                venues.add(mapRowToVenue(rs));
            }
        }
        return venues;
    }

    /**
     * Updates an existing venue in the database.
     *
     * @param venue The venue with updated details.
     * @throws SQLException If a database access error occurs.
     */
    public void updateVenue(Venue venue) throws SQLException {
        String sql = "UPDATE venues SET venueName = ?, capacity = ?, availabilityStatus = ?, reservationDeadline = ? WHERE venueId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, venue.getVenueName());
            stmt.setInt(2, venue.getCapacity());
            stmt.setString(3, venue.getAvailabilityStatus().name());
            stmt.setTimestamp(4, Timestamp.valueOf(venue.getReservationDeadline()));
            stmt.setString(5, venue.getVenueId());
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes a venue from the database by its unique identifier.
     *
     * @param venueId The unique identifier of the venue to be deleted.
     * @throws SQLException If a database access error occurs.
     */
    public void deleteVenue(String venueId) throws SQLException {
        String sql = "DELETE FROM venues WHERE venueId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, venueId);
            stmt.executeUpdate();
        }
    }

    /**
     * Maps a row from the ResultSet to a Venue object.
     *
     * @param rs The ResultSet containing venue data.
     * @return The mapped Venue object.
     * @throws SQLException If a database access error occurs.
     */
    private Venue mapRowToVenue(ResultSet rs) throws SQLException {
        String venueId = rs.getString("venueId");
        String venueName = rs.getString("venueName");
        int capacity = rs.getInt("capacity");
        VenueStatus availabilityStatus = VenueStatus.valueOf(rs.getString("availabilityStatus"));
        LocalDateTime reservationDeadline = rs.getTimestamp("reservationDeadline").toLocalDateTime();

        return new ConcreteVenue(venueId, venueName, capacity, availabilityStatus, new ArrayList<>(), reservationDeadline);
    }
}