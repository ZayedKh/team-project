package interfaces;

import java.sql.Connection;
import java.time.LocalDate;
import classes.DailySheet;

public interface SheetInterface {
    /**
     * This will generate a daily sheet of the current date
     * @param connection The connection to the database
     * @return The daily sheet of the current date
     */
    DailySheet generateTodaySheet(Connection connection);

    /**
     * This will generate a daily sheet of a specific date
     * @param date The date of daily sheet being generated
     * @param connection The connection to the database
     * @return The daily sheet of specified date
     */
    DailySheet generateDateSheet(LocalDate date, Connection connection);

}
