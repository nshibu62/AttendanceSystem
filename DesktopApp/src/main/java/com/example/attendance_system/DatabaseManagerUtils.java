package com.example.attendance_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing database connections.
 */
public class DatabaseManagerUtils {

    // JDBC URL, username, and password
    private static final String JDBC_URL = "jdbc:mysql://34.173.191.42:3306/attendancedb"; // JDBC URL
    private static final String USERNAME = "root"; // JDBC username
    private static final String PASSWORD = "monK3yban@naBread3!@3%5"; // JDBC password

    /**
     * Establishes a connection to the database.
     * @return A Connection object representing the database connection.
     * @throws SQLException If a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    /**
     * Closes the database connection.
     * @param connection The Connection object to be closed.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
