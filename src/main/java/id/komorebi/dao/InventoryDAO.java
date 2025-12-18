package id.komorebi.dao;

import id.komorebi.model.Inventory;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface InventoryDAO {
    Optional<Inventory> findById(int id) throws SQLException;
    Optional<Inventory> findByName(String name) throws SQLException;
    List<Inventory> findAll() throws SQLException;
    List<Inventory> findLowStock() throws SQLException; // quantity < min_stock
    int insert(Inventory inv) throws SQLException;
    boolean update(Inventory inv) throws SQLException;
    boolean adjustQuantity(int ingredientId, java.math.BigDecimal delta, int userId, String note) throws SQLException;
    boolean checkAvailability(int menuId) throws SQLException;
    boolean restoreStockForMenu(int menuId, int quantityRestored, int userId) throws SQLException;

    boolean checkAvailability(int menuId, int quantity) throws SQLException;
    boolean reduceStockForMenu(int menuId, int quantity) throws SQLException;
}
