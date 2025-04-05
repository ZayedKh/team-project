package lancaster.boxOfficeInterface;

import lancaster.model.Booking;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface DailySheetDAO {
    List<Booking> getDailySheet(Connection conn, LocalDate date) throws SQLException;
}
