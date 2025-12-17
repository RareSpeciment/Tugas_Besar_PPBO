package id.komorebi.dao.impl;

import id.komorebi.dao.MenuIngredientDAO;
import id.komorebi.model.MenuIngredient;
import id.komorebi.model.MenuItem;
import id.komorebi.model.Inventory;
import id.komorebi.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuIngredientDAOImpl implements MenuIngredientDAO {

    @Override
    public MenuIngredient findById(int id) throws SQLException {
        String sql = "SELECT mi.id, mi.menu_id, mi.ingredient_id, mi.quantity_required, m.name as menu_name, i.ingredient_name " +
                     "FROM menu_ingredients mi " +
                     "LEFT JOIN menu_items m ON mi.menu_id = m.menu_id " +
                     "LEFT JOIN inventory i ON mi.ingredient_id = i.ingredient_id WHERE mi.id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MenuItem menu = new MenuItem();
                    menu.setMenuId(rs.getInt("menu_id"));
                    menu.setName(rs.getString("menu_name"));
                    Inventory inv = new Inventory();
                    inv.setIngredientId(rs.getInt("ingredient_id"));
                    inv.setIngredientName(rs.getString("ingredient_name"));
                    MenuIngredient mi = new MenuIngredient();
                    mi.setId(rs.getInt("id"));
                    mi.setMenuItem(menu);
                    mi.setIngredient(inv);
                    mi.setQuantityRequired(rs.getBigDecimal("quantity_required"));
                    return mi;
                }
            }
        }
        return null;
    }

    @Override
    public List<MenuIngredient> findByMenuId(int menuId) throws SQLException {
        List<MenuIngredient> list = new ArrayList<>();
        String sql = "SELECT mi.id, mi.menu_id, mi.ingredient_id, mi.quantity_required, m.name as menu_name, i.ingredient_name " +
                     "FROM menu_ingredients mi " +
                     "LEFT JOIN menu_items m ON mi.menu_id = m.menu_id " +
                     "LEFT JOIN inventory i ON mi.ingredient_id = i.ingredient_id WHERE mi.menu_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, menuId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MenuItem menu = new MenuItem();
                    menu.setMenuId(rs.getInt("menu_id"));
                    menu.setName(rs.getString("menu_name"));
                    Inventory inv = new Inventory();
                    inv.setIngredientId(rs.getInt("ingredient_id"));
                    inv.setIngredientName(rs.getString("ingredient_name"));
                    MenuIngredient mi = new MenuIngredient();
                    mi.setId(rs.getInt("id"));
                    mi.setMenuItem(menu);
                    mi.setIngredient(inv);
                    mi.setQuantityRequired(rs.getBigDecimal("quantity_required"));
                    list.add(mi);
                }
            }
        }
        return list;
    }

    @Override
    public int insert(MenuIngredient mi) throws SQLException {
        String sql = "INSERT INTO menu_ingredients (menu_id, ingredient_id, quantity_required) VALUES (?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, mi.getMenuItem().getMenuId());
            ps.setInt(2, mi.getIngredient().getIngredientId());
            ps.setBigDecimal(3, mi.getQuantityRequired());
            int affected = ps.executeUpdate();
            if (affected == 0) return -1;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    @Override
    public boolean update(MenuIngredient mi) throws SQLException {
        String sql = "UPDATE menu_ingredients SET menu_id=?, ingredient_id=?, quantity_required=? WHERE id=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, mi.getMenuItem().getMenuId());
            ps.setInt(2, mi.getIngredient().getIngredientId());
            ps.setBigDecimal(3, mi.getQuantityRequired());
            ps.setInt(4, mi.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM menu_ingredients WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}