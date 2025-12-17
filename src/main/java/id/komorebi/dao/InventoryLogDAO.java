package id.komorebi.dao;

import id.komorebi.model.InventoryLog;
import java.sql.SQLException;
import java.util.List;

public interface InventoryLogDAO {
    InventoryLog findById(int id) throws SQLException;
    List<InventoryLog> findByIngredientId(int ingredientId) throws SQLException;
    List<InventoryLog> findAll() throws SQLException;
    int insert(InventoryLog log) throws SQLException;
}