package lancaster.boxOfficeInterface;

import lancaster.model.Booking;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DailySheetDAOImpl implements DailySheetDAO {
    @Override
    public List<Booking> getDailySheet(Connection conn, LocalDate date) throws SQLException {
        String query = """
                    SELECT 
                        r.room_name,
                        e.event_date,
                        e.start_time,
                        e.end_time,
                        b.customer_name,
                        sc.configuration_name
                    FROM events e
                    JOIN rooms r ON e.room_id = r.room_id
                    JOIN bookings b ON e.booking_id = b.booking_id
                    JOIN seating_configurations sc ON e.seating_config_id = sc.seating_config_id
                    WHERE e.event_date = ?
                    ORDER BY r.room_name, e.start_time;
                """;

        List<Booking> sheet = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String roomName = rs.getString("room_name");
                LocalTime startTime = rs.getTime("start_time").toLocalTime();
                LocalTime endTime = rs.getTime("end_time").toLocalTime();
                String customerName = rs.getString("customer_name");
                String configName = rs.getString("configuration_name");
                sheet.add(new Booking(roomName, date, startTime, endTime, customerName, configName));
            }
        }

        return sheet;
    }
}
