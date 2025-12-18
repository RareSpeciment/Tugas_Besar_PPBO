package id.komorebi.controller;

import id.komorebi.model.Inventory;
import id.komorebi.model.User;
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
    private final User currentUser;
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
        dialog.setVisible(true);

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