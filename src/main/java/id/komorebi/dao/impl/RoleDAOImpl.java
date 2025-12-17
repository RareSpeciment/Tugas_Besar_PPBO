package id.komorebi.dao.impl;

import id.komorebi.dao.RoleDAO;
import id.komorebi.model.Role;
import id.komorebi.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAOImpl implements RoleDAO {

    @Override
    public Role findById(int id) throws SQLException {
        String sql = "SELECT role_id, role_name FROM roles WHERE role_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Role(rs.getInt("role_id"), rs.getString("role_name"));
                }
            }
        }
        return null;
    }

    @Override
    public Role findByName(String name) throws SQLException {
        String sql = "SELECT role_id, role_name FROM roles WHERE role_name = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Role(rs.getInt("role_id"), rs.getString("role_name"));
                }
            }
        }
        return null;
    }

    @Override
    public List<Role> findAll() throws SQLException {
        List<Role> list = new ArrayList<>();
        String sql = "SELECT role_id, role_name FROM roles";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Role(rs.getInt("role_id"), rs.getString("role_name")));
            }
        }
        return list;
    }

    @Override
    public int insert(Role role) throws SQLException {
        String sql = "INSERT INTO roles (role_name) VALUES (?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, role.getRoleName());
            int affected = ps.executeUpdate();
            if (affected == 0) return -1;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    @Override
    public boolean update(Role role) throws SQLException {
        String sql = "UPDATE roles SET role_name = ? WHERE role_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, role.getRoleName());
            ps.setInt(2, role.getRoleId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM roles WHERE role_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}