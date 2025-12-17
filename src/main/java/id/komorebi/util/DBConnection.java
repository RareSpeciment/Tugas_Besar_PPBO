package id.komorebi.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/komorebi_pos?useSSL=false&autoReconnect=true&characterEncoding=utf8";

    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection connection;

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect();
        }
        return connection;
    }

    private static void connect() throws SQLException {
        try {
            System.out.println("[DB] Loading MySQL driver...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("[DB] Connecting to: " + URL);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("[DB] ✓ Successfully connected to MySQL!");
            System.out.println("[DB] Database version: " + connection.getMetaData().getDatabaseProductVersion());
        } catch (ClassNotFoundException e) {
            System.err.println("[DB] ✗ MySQL driver not found! Error: " + e.getMessage());
            throw new SQLException("MySQL driver not found!", e);
        } catch (SQLException e) {
            System.err.println("[DB] ✗ Connection failed! Error: " + e.getMessage());
            throw e;
        }
    }
}