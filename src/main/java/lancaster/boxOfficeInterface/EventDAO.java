package lancaster.boxOfficeInterface;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface EventDAO {
    LocalTime getStartTime(Connection conn, int eventID) throws SQLException;

    LocalTime getEndTime(Connection conn, int eventID) throws SQLException;

    int getDuration(Connection conn, int eventID) throws SQLException;

    int getRoomID(Connection conn, int eventID) throws SQLException;

    ResultSet getData(Connection conn, int eventID) throws SQLException;

    LocalDate getEventStartDate(Connection conn, int eventID) throws SQLException;
}
