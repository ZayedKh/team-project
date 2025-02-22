package interfaces;

import java.sql.Connection;

/**
 * This is an interface for database connection, which only currently contains a connect
 * and disconnect methods
 */
public interface DBConnect {
    /**
     * Establishes a connection between the user and the operation database with the
     * correct url, username and password for data details
     * @param url The url of the mySQL database
     * @param username The username of the database
     * @param password The password of the database
     * @return A connection between the user and database
     */
    Connection connect(String url, String username, String password);

    /**
     * Closes the connection between the user and the database
     * +----------------------------------+
     * |  PLEASE CLOSE AFTER CONNECTING!! |
     * +----------------------------------+
     * @param connection The connection being used for connection
     */
    void disconnect(Connection connection);
}
