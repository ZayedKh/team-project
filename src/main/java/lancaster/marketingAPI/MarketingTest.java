package lancaster.marketingAPI;

import lancaster.marketingAPI.MarketingJDBC;
import lancaster.model.Room;
import lancaster.utils.DBUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code MarketingTest} class serves as a simple test driver for the marketing-related database operations.
 * <p>
 * It is designed to demonstrate the retrieval and display of all available rooms from the database using the
 * {@link MarketingJDBC} class. This helps in verifying that the marketing module is correctly connecting to the
 * database and returning valid room records.
 * </p>
 *
 * <p>
 * For demonstration purposes, the {@code main} method instantiates the {@code MarketingJDBC} object,
 * retrieves a list of {@link Room} objects, and then prints each room to the console.
 * </p>
 *
 * @event This class is intended for testing and demonstration purposes within the marketing API module.
 */
public class MarketingTest {

    /**
     * The main entry point for executing the marketing module tests.
     * <p>
     * This method demonstrates the process of retrieving all room records from the database via the
     * {@link MarketingJDBC} class. It prints the details of each room to the standard output.
     * </p>
     *
     * @param args command-line arguments (not used)
     * @throws SQLException if a database access error occurs during execution
     * @throws ClassNotFoundException if the required JDBC driver class is not found
     * @throws IOException if an I/O error occurs when accessing configuration or data files
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        MarketingJDBC marketingJDBC = new MarketingJDBC();

        List<Room> rooms = marketingJDBC.getAllRooms();

        for (Room room : rooms) {
            System.out.println(room);
        }
    }
}
