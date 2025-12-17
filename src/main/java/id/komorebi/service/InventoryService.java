// package id.komorebi.service;

// import id.komorebi.dao.DAOFactory;
// import id.komorebi.dao.InventoryDAO;
// import id.komorebi.dao.InventoryLogDAO;
// import id.komorebi.model.Inventory;
// import id.komorebi.model.InventoryLog;

// import java.math.BigDecimal;
// import java.sql.SQLException;
// import java.util.List;
// import java.util.Optional;

// public class InventoryService {

//     private final InventoryDAO inventoryDAO;
//     private final InventoryLogDAO logDAO;

//     public InventoryService() {
//         this.inventoryDAO = DAOFactory.createInventoryDao();
//         this.logDAO = DAOFactory.createInventoryLogDao();
//     }

//     public List<Inventory> getAllInventory() {
//         try {
//             return inventoryDAO.findAll();
//         } catch (SQLException e) {
//             throw new RuntimeException(e);
//         }
//     }

//     public boolean adjustStock(int ingredientId, BigDecimal delta, int userId, String note) {
//         try {
//             boolean ok = inventoryDAO.adjustQuantity(ingredientId, new BigDecimal(Double.valueOf(delta.toString())), userId, note);
//             if (ok) {
//                 InventoryLog log = new InventoryLog();
//                 // InventoryLog expects Inventory and User objects
//                 id.komorebi.model.Inventory invRef = new id.komorebi.model.Inventory();
//                 invRef.setIngredientId(ingredientId);
//                 id.komorebi.model.User userRef = new id.komorebi.model.User();
//                 userRef.setUserId(userId);
//                 log.setIngredient(invRef);
//                 log.setUser(userRef);
//                 log.setChangeAmount(new BigDecimal(Double.valueOf(delta.toString())));
//                 log.setNote(note);
//                 logDAO.insert(log);
//             }
//             return ok;
//         } catch (SQLException e) {
//             return false;
//         }
//     }

//     public boolean updateMinStock(int ingredientId, double minStock) {
//         try {
//             Optional<Inventory> oi = inventoryDAO.findById(ingredientId);
//             if (!oi.isPresent()) return false;
//             Inventory inv = oi.get();
//             inv.setMinStock(new BigDecimal(Double.toString(minStock)));
//             return inventoryDAO.update(inv);
//         } catch (SQLException e) {
//             return false;
//         }
//     }
// }

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
        // Logika bisnis tambahan bisa ditaruh di sini (misal: validasi jam kerja)
        // Tapi untuk sekarang, langsung teruskan ke DAO
        return inventoryDAO.adjustQuantity(ingredientId, delta, userId, note);
    }
    
    public List<Inventory> getLowStockItems() throws Exception {
        return inventoryDAO.findLowStock();
    }
}