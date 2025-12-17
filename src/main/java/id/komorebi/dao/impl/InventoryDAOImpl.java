package id.komorebi.dao.impl;

import id.komorebi.dao.InventoryDAO;
import id.komorebi.model.Inventory;
import id.komorebi.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InventoryDAOImpl implements InventoryDAO {
    @Override
    public boolean restoreStockForMenu(int menuId, int quantityRestored, int userId) throws SQLException {
        Connection c = null;
        try {
            c = DBConnection.getConnection();
            c.setAutoCommit(false);

            String sqlResep = "SELECT ingredient_id, quantity_required FROM menu_recipes WHERE menu_id = ?";
            try (PreparedStatement psResep = c.prepareStatement(sqlResep)) {
                psResep.setInt(1, menuId);
                ResultSet rs = psResep.executeQuery();

                while (rs.next()) {
                    int ingId = rs.getInt("ingredient_id");
                    java.math.BigDecimal qtyNeeded = rs.getBigDecimal("quantity_required");
                    java.math.BigDecimal totalRestore = qtyNeeded.multiply(new java.math.BigDecimal(quantityRestored));

                    String sqlUpdate = "UPDATE inventory SET quantity = quantity + ? WHERE ingredient_id = ?";
                    try (PreparedStatement psUpd = c.prepareStatement(sqlUpdate)) {
                        psUpd.setBigDecimal(1, totalRestore);
                        psUpd.setInt(2, ingId);
                        psUpd.executeUpdate();
                    }

                    String sqlLog = "INSERT INTO inventory_logs (ingredient_id, user_id, change_amount, note) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement psLog = c.prepareStatement(sqlLog)) {
                        psLog.setInt(1, ingId);
                        psLog.setInt(2, userId);
                        psLog.setBigDecimal(3, totalRestore);
                        psLog.setString(4, "Restock from Canceled Order (Menu ID: " + menuId + ")");
                        psLog.executeUpdate();
                    }
                }
            }
            c.commit();
            return true;
        } catch (SQLException e) {
            if (c != null) c.rollback();
            throw e;
        } finally {
            if (c != null) c.setAutoCommit(true);
        }
    }

    @Override
    public Optional<Inventory> findById(int id) throws SQLException {
        String sql = "SELECT ingredient_id, ingredient_name, quantity, unit, min_stock, updated_at FROM inventory WHERE ingredient_id = ?";
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Inventory inv = new Inventory(
                            rs.getInt("ingredient_id"),
                            rs.getString("ingredient_name"),
                            rs.getBigDecimal("quantity"),
                            rs.getString("unit"),
                            rs.getBigDecimal("min_stock"),
                            rs.getTimestamp("updated_at").toLocalDateTime());
                    return Optional.of(inv);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Inventory> findByName(String name) throws SQLException {
        String sql = "SELECT ingredient_id, ingredient_name, quantity, unit, min_stock, updated_at FROM inventory WHERE ingredient_name = ?";
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Inventory inv = new Inventory(
                            rs.getInt("ingredient_id"),
                            rs.getString("ingredient_name"),
                            rs.getBigDecimal("quantity"),
                            rs.getString("unit"),
                            rs.getBigDecimal("min_stock"),
                            rs.getTimestamp("updated_at").toLocalDateTime());
                    return Optional.of(inv);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean checkAvailability(int menuId) throws SQLException {
        // Query ini mengecek: Adakah bahan yang stoknya KURANG DARI kebutuhan resep?
        String sql = "SELECT COUNT(*) FROM menu_recipes r " +
                     "JOIN inventory i ON r.ingredient_id = i.ingredient_id " +
                     "WHERE r.menu_id = ? AND i.quantity < r.quantity_required";
        
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, menuId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Jika count > 0, berarti ada bahan yang kurang -> TIDAK AVAILABLE
                    return rs.getInt(1) == 0; 
                }
            }
        }
        return true; // Jika tidak ada resep atau stok cukup
    }

    // @Override
    // public boolean reduceStockForMenu(int menuId, int quantitySold, int userId) throws SQLException {
    //     Connection c = null;
    //     try {
    //         c = DBConnection.getConnection();
    //         c.setAutoCommit(false); // Transactional Mode

    //         // 1. Ambil Resep
    //         String sqlResep = "SELECT ingredient_id, quantity_required FROM menu_recipes WHERE menu_id = ?";
    //         try (PreparedStatement psResep = c.prepareStatement(sqlResep)) {
    //             psResep.setInt(1, menuId);
    //             ResultSet rs = psResep.executeQuery();

    //             while (rs.next()) {
    //                 int ingId = rs.getInt("ingredient_id");
    //                 java.math.BigDecimal qtyNeeded = rs.getBigDecimal("quantity_required");
    //                 // Total pengurangan = butuh * jumlah pesanan
    //                 java.math.BigDecimal totalDeduct = qtyNeeded.multiply(new java.math.BigDecimal(quantitySold));

    //                 // 2. Kurangi Stok
    //                 String sqlUpdate = "UPDATE inventory SET quantity = quantity - ? WHERE ingredient_id = ?";
    //                 try (PreparedStatement psUpd = c.prepareStatement(sqlUpdate)) {
    //                     psUpd.setBigDecimal(1, totalDeduct);
    //                     psUpd.setInt(2, ingId);
    //                     psUpd.executeUpdate();
    //                 }

    //                 // 3. Catat Log
    //                 String sqlLog = "INSERT INTO inventory_logs (ingredient_id, user_id, change_amount, note) VALUES (?, ?, ?, ?)";
    //                 try (PreparedStatement psLog = c.prepareStatement(sqlLog)) {
    //                     psLog.setInt(1, ingId);
    //                     psLog.setInt(2, userId);
    //                     psLog.setBigDecimal(3, totalDeduct.negate()); // Negatif karena pengurangan
    //                     psLog.setString(4, "Order Sold (Menu ID: " + menuId + ")");
    //                     psLog.executeUpdate();
    //                 }
    //             }
    //         }
            
    //         c.commit();
    //         return true;
    //     } catch (SQLException e) {
    //         if (c != null) c.rollback();
    //         throw e;
    //     } finally {
    //         if (c != null) c.setAutoCommit(true);
    //     }
    // }

    @Override
    public List<Inventory> findAll() throws SQLException {
        List<Inventory> list = new ArrayList<>();
        String sql = "SELECT ingredient_id, ingredient_name, quantity, unit, min_stock, updated_at FROM inventory ORDER BY ingredient_name";
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public List<Inventory> findLowStock() throws SQLException {
        List<Inventory> list = new ArrayList<>();
        String sql = "SELECT ingredient_id, ingredient_name, quantity, unit, min_stock, updated_at FROM inventory WHERE quantity < min_stock ORDER BY ingredient_name";
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Inventory inv = new Inventory(
                        rs.getInt("ingredient_id"),
                        rs.getString("ingredient_name"),
                        rs.getBigDecimal("quantity"),
                        rs.getString("unit"),
                        rs.getBigDecimal("min_stock"),
                        rs.getTimestamp("updated_at").toLocalDateTime());
                list.add(inv);
            }
        }
        return list;
    }

    @Override
    public int insert(Inventory inv) throws SQLException {
        String sql = "INSERT INTO inventory (ingredient_name, quantity, unit, min_stock) VALUES (?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, inv.getIngredientName());
            ps.setBigDecimal(2, inv.getQuantity());
            ps.setString(3, inv.getUnit());
            ps.setBigDecimal(4, inv.getMinStock());
            int affected = ps.executeUpdate();
            if (affected == 0)
                return -1;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next())
                    return rs.getInt(1);
            }
        }
        return -1;
    }

    @Override
    public boolean update(Inventory inv) throws SQLException {
        String sql = "UPDATE inventory SET ingredient_name=?, quantity=?, unit=?, min_stock=? WHERE ingredient_id=?";
        try (Connection c = DBConnection.getConnection();
                PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, inv.getIngredientName());
            ps.setBigDecimal(2, inv.getQuantity());
            ps.setString(3, inv.getUnit());
            ps.setBigDecimal(4, inv.getMinStock());
            ps.setInt(5, inv.getIngredientId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean adjustQuantity(int ingredientId, java.math.BigDecimal delta, int userId, String note)
            throws SQLException {
        String updateSql = "UPDATE inventory SET quantity = quantity + ?, updated_at = NOW() WHERE ingredient_id = ?";
        String insertLog = "INSERT INTO inventory_logs (ingredient_id, user_id, change_amount, note) VALUES (?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement psUpd = c.prepareStatement(updateSql);
                    PreparedStatement psLog = c.prepareStatement(insertLog)) {

                psUpd.setBigDecimal(1, delta);
                psUpd.setInt(2, ingredientId);
                int affected = psUpd.executeUpdate();
                if (affected == 0) {
                    c.rollback();
                    return false;
                }

                psLog.setInt(1, ingredientId);
                psLog.setInt(2, userId);
                psLog.setBigDecimal(3, delta);
                psLog.setString(4, note);
                psLog.executeUpdate();

                c.commit();
                return true;
            } catch (SQLException ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    private Inventory mapRow(ResultSet rs) throws SQLException {
        return new Inventory(
                rs.getInt("ingredient_id"),
                rs.getString("ingredient_name"),
                rs.getBigDecimal("quantity"),
                rs.getString("unit"),
                rs.getBigDecimal("min_stock"),
                rs.getTimestamp("updated_at").toLocalDateTime());
    }
}