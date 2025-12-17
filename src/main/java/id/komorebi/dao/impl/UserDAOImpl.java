package id.komorebi.dao.impl;

import id.komorebi.dao.UserDAO;
import id.komorebi.model.Role;
import id.komorebi.model.User;
import id.komorebi.model.enums.UserStatus;
import id.komorebi.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {

    @Override
    public Optional<User> findById(int id) throws SQLException {
        String sql = "SELECT u.user_id, u.username, u.password_hash, u.fullname, u.role_id, r.role_name, u.status, u.deactivated_at, u.created_at " +
                     "FROM users u LEFT JOIN roles r ON u.role_id = r.role_id WHERE u.user_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Role role = new Role(rs.getInt("role_id"), rs.getString("role_name"));
                    User u = new User();
                    u.setUserId(rs.getInt("user_id"));
                    u.setUsername(rs.getString("username"));
                    u.setPasswordHash(rs.getString("password_hash"));
                    u.setFullname(rs.getString("fullname"));
                    u.setRole(role);
                    u.setStatus(UserStatus.valueOf(rs.getString("status")));
                    Timestamp dts = rs.getTimestamp("deactivated_at");
                    if (dts != null) u.setDeactivatedAt(dts.toLocalDateTime());
                    u.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return Optional.of(u);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) throws SQLException {
        String sql = "SELECT u.user_id, u.username, u.password_hash, u.fullname, u.role_id, r.role_name, u.status, u.deactivated_at, u.created_at " +
                     "FROM users u LEFT JOIN roles r ON u.role_id = r.role_id WHERE u.username = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Role role = new Role(rs.getInt("role_id"), rs.getString("role_name"));
                    User u = new User();
                    u.setUserId(rs.getInt("user_id"));
                    u.setUsername(rs.getString("username"));
                    u.setPasswordHash(rs.getString("password_hash"));
                    u.setFullname(rs.getString("fullname"));
                    u.setRole(role);
                    u.setStatus(UserStatus.valueOf(rs.getString("status")));
                    Timestamp dts = rs.getTimestamp("deactivated_at");
                    if (dts != null) u.setDeactivatedAt(dts.toLocalDateTime());
                    u.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return Optional.of(u);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT u.user_id, u.username, u.password_hash, u.fullname, u.role_id, r.role_name, u.status, u.deactivated_at, u.created_at " +
                     "FROM users u LEFT JOIN roles r ON u.role_id = r.role_id";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Role role = new Role(rs.getInt("role_id"), rs.getString("role_name"));
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setUsername(rs.getString("username"));
                u.setPasswordHash(rs.getString("password_hash"));
                u.setFullname(rs.getString("fullname"));
                u.setRole(role);
                u.setStatus(UserStatus.valueOf(rs.getString("status")));
                Timestamp dts = rs.getTimestamp("deactivated_at");
                if (dts != null) u.setDeactivatedAt(dts.toLocalDateTime());
                u.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                list.add(u);
            }
        }
        return list;
    }

    @Override
    public int insert(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password_hash, fullname, role_id, status, deactivated_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getFullname());
            ps.setInt(4, user.getRole().getRoleId());
            ps.setString(5, user.getStatus().name());
            if (user.getDeactivatedAt() != null) ps.setTimestamp(6, Timestamp.valueOf(user.getDeactivatedAt()));
            else ps.setNull(6, Types.TIMESTAMP);
            int affected = ps.executeUpdate();
            if (affected == 0) return -1;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    @Override
    public boolean update(User user) throws SQLException {
        String sql = "UPDATE users SET username=?, password_hash=?, fullname=?, role_id=?, status=?, deactivated_at=? WHERE user_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getFullname());
            ps.setInt(4, user.getRole().getRoleId());
            ps.setString(5, user.getStatus().name());
            if (user.getDeactivatedAt() != null) ps.setTimestamp(6, Timestamp.valueOf(user.getDeactivatedAt()));
            else ps.setNull(6, Types.TIMESTAMP);
            ps.setInt(7, user.getUserId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deactivate(int id) throws SQLException {
        String sql = "UPDATE users SET status = 'INACTIVE', deactivated_at = ? WHERE user_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean activate(int id) throws SQLException {
        String sql = "UPDATE users SET status = 'ACTIVE', deactivated_at = NULL WHERE user_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}