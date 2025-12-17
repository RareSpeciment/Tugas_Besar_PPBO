package id.komorebi.dao.impl;

import id.komorebi.dao.CategoryDAO;
import id.komorebi.model.Category;
import id.komorebi.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {

    @Override
    public Category findById(int id) throws SQLException {
        String sql = "SELECT category_id, name FROM categories WHERE category_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Category(rs.getInt("category_id"), rs.getString("name"));
            }
        }
        return null;
    }

    @Override
    public List<Category> findAll() throws SQLException {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT category_id, name FROM categories";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Category(rs.getInt("category_id"), rs.getString("name")));
            }
        }
        return list;
    }

    @Override
    public int insert(Category category) throws SQLException {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, category.getName());
            int affected = ps.executeUpdate();
            if (affected == 0) return -1;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    @Override
    public boolean update(Category category) throws SQLException {
        String sql = "UPDATE categories SET name = ? WHERE category_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, category.getName());
            ps.setInt(2, category.getCategoryId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM categories WHERE category_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}