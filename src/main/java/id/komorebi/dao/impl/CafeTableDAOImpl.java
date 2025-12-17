package id.komorebi.dao.impl;

import id.komorebi.dao.CafeTableDAO;
import id.komorebi.model.CafeTable;
import id.komorebi.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CafeTableDAOImpl implements CafeTableDAO {

    @Override
    public CafeTable findById(int id) throws SQLException {
        String sql = "SELECT table_id, table_name, capacity FROM cafe_tables WHERE table_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new CafeTable(rs.getInt("table_id"), rs.getString("table_name"), rs.getInt("capacity"));
            }
        }
        return null;
    }

    @Override
    public List<CafeTable> findAll() throws SQLException {
        List<CafeTable> list = new ArrayList<>();
        String sql = "SELECT table_id, table_name, capacity FROM cafe_tables ORDER BY table_name";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(new CafeTable(rs.getInt("table_id"), rs.getString("table_name"), rs.getInt("capacity")));
        }
        return list;
    }

    @Override
    public int insert(CafeTable table) throws SQLException {
        String sql = "INSERT INTO cafe_tables (table_name, capacity) VALUES (?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, table.getTableName());
            ps.setInt(2, table.getCapacity());
            int affected = ps.executeUpdate();
            if (affected == 0) return -1;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    @Override
    public boolean update(CafeTable table) throws SQLException {
        String sql = "UPDATE cafe_tables SET table_name = ?, capacity = ? WHERE table_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, table.getTableName());
            ps.setInt(2, table.getCapacity());
            ps.setInt(3, table.getTableId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM cafe_tables WHERE table_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}