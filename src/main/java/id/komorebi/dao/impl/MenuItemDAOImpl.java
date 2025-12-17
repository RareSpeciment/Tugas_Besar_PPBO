package id.komorebi.dao.impl;

import id.komorebi.dao.MenuItemDAO;
import id.komorebi.model.Category;
import id.komorebi.model.MenuItem;
import id.komorebi.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDAOImpl implements MenuItemDAO {

    @Override
    public MenuItem findById(int id) throws SQLException {
        String sql = "SELECT m.menu_id, m.name, m.description, m.price, m.category_id, c.name as category_name, m.available, m.image_path " +
                     "FROM menu_items m LEFT JOIN categories c ON m.category_id = c.category_id WHERE m.menu_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Category cat = new Category(rs.getInt("category_id"), rs.getString("category_name"));
                    MenuItem m = new MenuItem();
                    m.setMenuId(rs.getInt("menu_id"));
                    m.setName(rs.getString("name"));
                    m.setDescription(rs.getString("description"));
                    m.setPrice(rs.getBigDecimal("price"));
                    m.setCategory(cat);
                    m.setAvailable(rs.getInt("available") == 1);
                    m.setImagePath(rs.getString("image_path"));
                    return m;
                }
            }
        }
        return null;
    }

    @Override
    public List<MenuItem> findAll() throws SQLException {
        List<MenuItem> list = new ArrayList<>();
        String sql = "SELECT m.menu_id, m.name, m.description, m.price, m.category_id, c.name as category_name, m.available, m.image_path " +
                     "FROM menu_items m LEFT JOIN categories c ON m.category_id = c.category_id ORDER BY m.name";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Category cat = new Category(rs.getInt("category_id"), rs.getString("category_name"));
                MenuItem m = new MenuItem();
                m.setMenuId(rs.getInt("menu_id"));
                m.setName(rs.getString("name"));
                m.setDescription(rs.getString("description"));
                m.setPrice(rs.getBigDecimal("price"));
                m.setCategory(cat);
                m.setAvailable(rs.getInt("available") == 1);
                m.setImagePath(rs.getString("image_path"));
                list.add(m);
            }
        }
        return list;
    }

    @Override
    public List<MenuItem> findAvailable() throws SQLException {
        List<MenuItem> list = new ArrayList<>();
        String sql = "SELECT m.menu_id, m.name, m.description, m.price, m.category_id, c.name as category_name, m.available, m.image_path " +
                     "FROM menu_items m LEFT JOIN categories c ON m.category_id = c.category_id WHERE m.available = 1 ORDER BY m.name";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Category cat = new Category(rs.getInt("category_id"), rs.getString("category_name"));
                MenuItem m = new MenuItem();
                m.setMenuId(rs.getInt("menu_id"));
                m.setName(rs.getString("name"));
                m.setDescription(rs.getString("description"));
                m.setPrice(rs.getBigDecimal("price"));
                m.setCategory(cat);
                m.setAvailable(true);
                m.setImagePath(rs.getString("image_path"));
                list.add(m);
            }
        }
        return list;
    }

    @Override
    public int insert(MenuItem menu) throws SQLException {
        String sql = "INSERT INTO menu_items (name, description, price, category_id, available, image_path) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, menu.getName());
            ps.setString(2, menu.getDescription());
            ps.setBigDecimal(3, menu.getPrice());
            ps.setInt(4, menu.getCategory().getCategoryId());
            ps.setInt(5, menu.isAvailable() ? 1 : 0);
            ps.setString(6, menu.getImagePath());
            int affected = ps.executeUpdate();
            if (affected == 0) return -1;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    @Override
    public boolean update(MenuItem menu) throws SQLException {
        String sql = "UPDATE menu_items SET name=?, description=?, price=?, category_id=?, available=?, image_path=? WHERE menu_id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, menu.getName());
            ps.setString(2, menu.getDescription());
            ps.setBigDecimal(3, menu.getPrice());
            ps.setInt(4, menu.getCategory().getCategoryId());
            ps.setInt(5, menu.isAvailable() ? 1 : 0);
            ps.setString(6, menu.getImagePath());
            ps.setInt(7, menu.getMenuId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM menu_items WHERE menu_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<id.komorebi.model.ReportEntry> getTopSellingItems(java.time.LocalDate date) throws SQLException {
        List<id.komorebi.model.ReportEntry> list = new ArrayList<>();
        String sql = "SELECT m.menu_id, m.name, SUM(oi.quantity) AS qty, SUM(oi.quantity * oi.price_each) AS revenue " +
                     "FROM order_items oi JOIN orders o ON oi.order_id = o.order_id JOIN menu_items m ON oi.menu_id = m.menu_id " +
                     "WHERE DATE(o.created_at) = ? GROUP BY m.menu_id, m.name ORDER BY qty DESC LIMIT 10";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("menu_id");
                    String name = rs.getString("name");
                    long qty = rs.getLong("qty");
                    java.math.BigDecimal rev = rs.getBigDecimal("revenue");
                    list.add(new id.komorebi.model.ReportEntry(id, name, qty, rev));
                }
            }
        }
        return list;
    }
}