package id.komorebi.service;

import id.komorebi.dao.DAOFactory;
import id.komorebi.dao.InventoryDAO;
import id.komorebi.model.Inventory;
import java.math.BigDecimal;
import java.util.List;

public class InventoryService {
    
    // Kita panggil DAO lewat sini
    private final InventoryDAO inventoryDAO;

    public InventoryService() {
        // Menggunakan Factory agar jika database ganti, service tidak perlu diubah
        this.inventoryDAO = DAOFactory.createInventoryDao();
    }

    public List<Inventory> getAllInventory() throws Exception {
        return inventoryDAO.findAll();
    }

    public boolean adjustStock(int ingredientId, BigDecimal delta, int userId, String note) throws Exception {
        // Tapi untuk sekarang, langsung teruskan ke DAO
        return inventoryDAO.adjustQuantity(ingredientId, delta, userId, note);
    }
    
    public List<Inventory> getLowStockItems() throws Exception {
        return inventoryDAO.findLowStock();
    }
}