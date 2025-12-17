package id.komorebi.dao.impl;

import id.komorebi.dao.InventoryLogDAO;
import id.komorebi.model.Inventory;
import id.komorebi.model.InventoryLog;
import id.komorebi.model.User;
import id.komorebi.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryLogDAOImpl implements InventoryLogDAO {

    @Override
    public InventoryLog findById(int id) throws SQLException {
        String sql = "SELECT l.log_id, l.ingredient_id, i.ingredient_name, l.user_id, u.username, l.change_amount, l.note, l.created_at " +
                     "FROM inventory_logs l LEFT JOIN inventory i ON l.ingredient_id = i.ingredient_id LEFT JOIN users u ON l.user_id = u.user_id WHERE l.log_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Inventory inv = new Inventory();
                    inv.setIngredientId(rs.getInt("ingredient_id"));
                    inv.setIngredientName(rs.getString("ingredient_name"));
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    InventoryLog log = new InventoryLog();
                    log.setLogId(rs.getInt("log_id"));
                    log.setIngredient(inv);
                    log.setUser(user);
                    log.setChangeAmount(rs.getBigDecimal("change_amount"));
                    log.setNote(rs.getString("note"));
                    log.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return log;
                }
            }
        }
        return null;
    }

    @Override
    public List<InventoryLog> findByIngredientId(int ingredientId) throws SQLException {
        List<InventoryLog> list = new ArrayList<>();
        String sql = "SELECT l.log_id, l.ingredient_id, i.ingredient_name, l.user_id, u.username, l.change_amount, l.note, l.created_at " +
                     "FROM inventory_logs l LEFT JOIN inventory i ON l.ingredient_id = i.ingredient_id LEFT JOIN users u ON l.user_id = u.user_id WHERE l.ingredient_id = ? ORDER BY l.created_at DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, ingredientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Inventory inv = new Inventory();
                    inv.setIngredientId(rs.getInt("ingredient_id"));
                    inv.setIngredientName(rs.getString("ingredient_name"));
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    InventoryLog log = new InventoryLog();
                    log.setLogId(rs.getInt("log_id"));
                    log.setIngredient(inv);
                    log.setUser(user);
                    log.setChangeAmount(rs.getBigDecimal("change_amount"));
                    log.setNote(rs.getString("note"));
                    log.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    list.add(log);
                }
            }
        }
        return list;
    }

    @Override
    public List<InventoryLog> findAll() throws SQLException {
        List<InventoryLog> list = new ArrayList<>();
        String sql = "SELECT l.log_id, l.ingredient_id, i.ingredient_name, l.user_id, u.username, l.change_amount, l.note, l.created_at " +
                     "FROM inventory_logs l LEFT JOIN inventory i ON l.ingredient_id = i.ingredient_id LEFT JOIN users u ON l.user_id = u.user_id ORDER BY l.created_at DESC";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Inventory inv = new Inventory();
                inv.setIngredientId(rs.getInt("ingredient_id"));
                inv.setIngredientName(rs.getString("ingredient_name"));
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                InventoryLog log = new InventoryLog();
                log.setLogId(rs.getInt("log_id"));
                log.setIngredient(inv);
                log.setUser(user);
                log.setChangeAmount(rs.getBigDecimal("change_amount"));
                log.setNote(rs.getString("note"));
                log.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                list.add(log);
            }
        }
        return list;
    }

    @Override
    public int insert(InventoryLog log) throws SQLException {
        String sql = "INSERT INTO inventory_logs (ingredient_id, user_id, change_amount, note) VALUES (?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, log.getIngredient().getIngredientId());
            ps.setInt(2, log.getUser().getUserId());
            ps.setBigDecimal(3, log.getChangeAmount());
            ps.setString(4, log.getNote());
            int affected = ps.executeUpdate();
            if (affected == 0) return -1;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }
}