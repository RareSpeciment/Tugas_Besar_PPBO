package id.komorebi.dao.impl;

import id.komorebi.dao.ActivityLogDAO;
import id.komorebi.model.ActivityLog;
import id.komorebi.model.User;
import id.komorebi.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityLogDAOImpl implements ActivityLogDAO {
    @Override
    public int insert(ActivityLog log) throws SQLException {
        String sql = "INSERT INTO activity_logs (user_id, action, details, created_at) VALUES (?, ?, ?, NOW())";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, log.getUser().getUserId());
            ps.setString(2, log.getAction());
            ps.setString(3, log.getDetails());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    @Override
    public List<ActivityLog> findAll() throws SQLException {
        List<ActivityLog> list = new ArrayList<>();
        String sql = "SELECT a.log_id, a.user_id, u.username, a.action, a.details, a.created_at FROM activity_logs a LEFT JOIN users u ON a.user_id = u.user_id ORDER BY a.created_at DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ActivityLog al = new ActivityLog();
                al.setLogId(rs.getInt("log_id"));
                if (rs.getInt("user_id") != 0) {
                    User u = new User();
                    u.setUserId(rs.getInt("user_id"));
                    u.setUsername(rs.getString("username"));
                    al.setUser(u);
                }
                al.setAction(rs.getString("action"));
                al.setDetails(rs.getString("details"));
                al.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                list.add(al);
            }
        }
        return list;
    }

    @Override
    public List<ActivityLog> findByUserId(int userId) throws SQLException {
        List<ActivityLog> list = new ArrayList<>();
        String sql = "SELECT a.log_id, a.user_id, u.username, a.action, a.details, a.created_at FROM activity_logs a LEFT JOIN users u ON a.user_id = u.user_id WHERE a.user_id = ? ORDER BY a.created_at DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ActivityLog al = new ActivityLog();
                    al.setLogId(rs.getInt("log_id"));
                    User u = new User();
                    u.setUserId(rs.getInt("user_id"));
                    u.setUsername(rs.getString("username"));
                    al.setUser(u);
                    al.setAction(rs.getString("action"));
                    al.setDetails(rs.getString("details"));
                    al.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    list.add(al);
                }
            }
        }
        return list;
    }
}