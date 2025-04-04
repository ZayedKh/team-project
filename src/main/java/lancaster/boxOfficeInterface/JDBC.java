package lancaster.boxOfficeInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC {
    private final Connection connection;


    public JDBC() throws SQLException, ClassNotFoundException {
        String url = "jdbc:mysql://localhost:3306/";
        String username = "in2033t44_a";
        String password = "wcYtgG2jphQ";

        Class.forName("com.mysql.cj.jdbc.Driver");

        this.connection = DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) {

    }
}
