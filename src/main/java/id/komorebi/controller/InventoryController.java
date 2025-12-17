// // package id.komorebi.controller;

// // import id.komorebi.model.Inventory;
// // import id.komorebi.service.InventoryService;
// // import id.komorebi.util.UIHelper;

// // import java.util.List;

// // public class InventoryController {

// //     private final InventoryService service;

// //     public InventoryController(InventoryService service) {
// //         this.service = service;
// //     }

// //     public List<Inventory> getAllIngredients() {
// //         return service.getAllIngredients();
// //     }

// //     public boolean updateQuantity(int ingredientId, double amount, int userId, String note) {
// //         try {
// //             service.adjustStock(ingredientId, amount, userId, note);
// //             UIHelper.showInfo("Stock updated.");
// //             return true;
// //         } catch (Exception e) {
// //             UIHelper.showError("Failed to update stock: " + e.getMessage());
// //             return false;
// //         }
// //     }

// //     public boolean updateMinStock(int id, double minStock) {
// //         try {
// //             service.updateMinStock(id, minStock);
// //             return true;
// //         } catch (Exception e) {
// //             UIHelper.showError("Failed: " + e.getMessage());
// //             return false;
// //         }
// //     }

// //     public void updateStock(javax.swing.JTable table) {
// //         // TODO: Extract selected row from table and call updateQuantity with row data
// //         UIHelper.showInfo("Table-based stock update not yet implemented.");
// //     }
// // }


// package id.komorebi.controller;

// import id.komorebi.model.Inventory;
// import id.komorebi.model.User;
// import id.komorebi.service.InventoryService;
// import id.komorebi.util.UIHelper;
// import id.komorebi.view.InventoryView;
// import id.komorebi.view.StockUpdateDialog;

// import javax.swing.*;
// import java.math.BigDecimal;
// import java.util.List;

// public class InventoryController {
    
//     private final InventoryService service;
//     private List<Inventory> currentList; // Kita simpan list agar bisa ambil ID dari index baris

//     public InventoryController(InventoryService service) {
//         this.service = service;
//     }

//     private User currentUser;

//     public InventoryController(InventoryService service, User currentUser) {
//         this.service = service;
//         this.currentUser = currentUser;
//     }

//     public List<Inventory> getAllIngredients() {
//         try {
//             this.currentList = service.getAllInventory(); // Asumsi Service memanggil DAO.findAll()
//             return currentList;
//         } catch (Exception e) {
//             e.printStackTrace();
//             return null;
//         }
//     }

//     public void updateStock(JTable table) {
//         int selectedRow = table.getSelectedRow();
//         if (selectedRow < 0) {
//             UIHelper.showError("Please select an item first.");
//             return;
//         }

//         // Ambil object Inventory asli berdasarkan baris yang dipilih
//         // (Pastikan urutan di table sama dengan urutan di currentList)
//         Inventory selectedItem = currentList.get(selectedRow);
        
//         // Buka Custom Dialog
//         // Tolong yak, Kita butuh referensi ke Parent Frame agar dialog muncul di tengahnya
//         // (Hack: ambil root component dari table)
//         JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(table);
//         StockUpdateDialog dialog = new StockUpdateDialog(parentFrame, selectedItem.getIngredientName());
        
//         dialog.setVisible(true); // Program akan berhenti di sini sampai dialog ditutup (karena Modal)

//         if (dialog.isConfirmed()) {
//             BigDecimal delta = dialog.getQuantityChange();
//             String note = dialog.getNote();

//             // Validasi sederhana
//             if (delta.compareTo(BigDecimal.ZERO) == 0) {
//                 UIHelper.showError("Change amount cannot be zero.");
//                 return;
//             }
//             if (note.isEmpty()) {
//                 UIHelper.showError("Please provide a note (e.g., 'Restock' or 'Spilled').");
//                 return;
//             }

//             try {
//                 // TODO: Ambil current user ID dari session. 
//                 // Karena Controller ini mungkin tidak pegang User, kita hardcode atau ambil dari AuthController static
//                 // Untuk tugas besar, boleh hardcode ID admin sementara, atau pass User ke Controller ini.
//                 int currentUserId = 1; // Contoh: Default Admin
                
//                 // Panggil Service/DAO
//                 // Perhatikan: Method adjustQuantity ada di InventoryDAO
//                 boolean success = service.adjustStock(selectedItem.getIngredientId(), delta, currentUserId, note);
                
//                 if (success) {
//                     UIHelper.showInfo("Stock updated successfully.");
                    
//                     // Refresh table
//                     InventoryView view = (InventoryView) parentFrame;
//                     view.setInventoryData(getAllIngredients());
//                 } else {
//                     UIHelper.showError("Failed to update stock.");
//                 }

//             } catch (Exception e) {
//                 UIHelper.showError("Database Error: " + e.getMessage());
//                 e.printStackTrace();
//             }
//         }
//     }
// }

package id.komorebi.controller;

import id.komorebi.model.Inventory;
import id.komorebi.model.User; // Import User
import id.komorebi.service.InventoryService;
import id.komorebi.util.UIHelper;
import id.komorebi.view.InventoryView;
import id.komorebi.view.StockUpdateDialog;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.List;
import java.awt.Window;

public class InventoryController {
    
    private final InventoryService service;
    private final User currentUser; // Tambahkan variabel ini
    private List<Inventory> currentList; 

    // Constructor menerima User
    public InventoryController(InventoryService service, User user) {
        this.service = service;
        this.currentUser = user;
    }

    public List<Inventory> getAllIngredients() {
        try {
            this.currentList = service.getAllInventory();
            return currentList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateStock(InventoryView view) {
        JTable table = view.getTable(); // Ambil tabel dari View
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow < 0) {
            UIHelper.showError("Please select an item first.");
            return;
        }

        // Pastikan currentList sinkron
        if (currentList == null || currentList.size() <= selectedRow) {
            getAllIngredients(); // Refresh list jika null
        }
        Inventory selectedItem = currentList.get(selectedRow);
        
        // Ambil Parent Window dari View (Panel) -> Ini akan dapat MainFrame
        Window parentWindow = SwingUtilities.getWindowAncestor(view); 
        
        StockUpdateDialog dialog = new StockUpdateDialog(parentWindow, selectedItem.getIngredientName());
        dialog.setVisible(true); // Modal dialog blocking here

        if (dialog.isConfirmed()) {
            BigDecimal delta = dialog.getQuantityChange();
            String note = dialog.getNote();

            if (delta.compareTo(BigDecimal.ZERO) == 0) {
                UIHelper.showError("Change amount cannot be zero.");
                return;
            }
            if (note.isEmpty()) {
                UIHelper.showError("Please provide a note.");
                return;
            }

            try {
                int userId = (currentUser != null) ? currentUser.getUserId() : 1; 
                boolean success = service.adjustStock(selectedItem.getIngredientId(), delta, userId, note);
                
                if (success) {
                    UIHelper.showInfo("Stock updated successfully.");
                    // Refresh data di view yang sama
                    view.setInventoryData(getAllIngredients());
                } else {
                    UIHelper.showError("Failed to update stock.");
                }

            } catch (Exception e) {
                UIHelper.showError("Database Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}