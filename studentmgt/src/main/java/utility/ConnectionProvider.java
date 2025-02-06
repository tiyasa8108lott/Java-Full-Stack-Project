package utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {

    // Avoid static connection, let each DAO method create and close connections independently
    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/attendence_db";
            String username = "root";
            String password = "tiyasadb";
            return DriverManager.getConnection(url, username, password); // Return a new connection
        } catch (SQLException e) {
            System.out.println("Error establishing database connection: " + e.getMessage());
            return null; // Return null in case of error
        }
    }
}
