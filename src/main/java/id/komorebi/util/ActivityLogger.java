package id.komorebi.util;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ActivityLogger {

    public static void log(int userId, String action, String details) {
        String sql = "INSERT INTO activity_logs (user_id, action, details) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, action);
            ps.setString(3, details);
            ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("[Logger] Failed to log activity: " + e.getMessage());
        }
    }
}