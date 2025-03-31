import java.sql.*;

/*
 * Nicholas Kowalski
 * CEN3024C
 * 30Mar2025
 * Class: DatabaseConnection
 * This class stores the connection requirements for the user to connect to a supplied db file using sqlite.
 * This class does not outline a prefilled location but rather what the user enters.
 */
public class DatabaseConnection {
    private static String dbLoc = null;

    public static void setDatabaseLocation(String path) {
        dbLoc = path;
    }

    public static Connection getConnection() {
        try {
            String url = "jdbc:sqlite:" + dbLoc;
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }
}