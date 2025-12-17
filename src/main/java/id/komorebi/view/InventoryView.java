package id.komorebi.view;

import id.komorebi.controller.InventoryController;
import id.komorebi.model.Inventory;
import id.komorebi.controller.LogoutCallback;
import id.komorebi.util.UIHelper;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class InventoryView extends JPanel { // Pastikan extends JPanel

    private InventoryController controller;
    private JTable table;
    private LogoutCallback logoutCallback;

    public InventoryView(InventoryController controller, LogoutCallback logoutCallback) {
        this.controller = controller;
        this.logoutCallback = logoutCallback;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        
        // Header
        add(UIHelper.createHeaderPanel("Inventory Stock", e -> handleLogout()), BorderLayout.NORTH);
        
        table = new JTable();
        UIHelper.styleTable(table);
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));
        tablePanel.setBackground(UIHelper.BACKGROUND_WHITE);
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(UIHelper.BORDER_LIGHT));
        tablePanel.add(scroll, BorderLayout.CENTER);
        
        add(tablePanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        bottomPanel.setBackground(UIHelper.BACKGROUND_WHITE);
        
        JButton updateBtn = UIHelper.createButton("Update Stock");
        updateBtn.setPreferredSize(new Dimension(150, 40));
        
        // PERUBAHAN: Kirim 'this' (View ini sendiri) ke controller
        updateBtn.addActionListener(e -> controller.updateStock(this));
        
        bottomPanel.add(updateBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // TAMBAHAN: Getter agar Controller bisa baca tabel
    public JTable getTable() {
        return table;
    }

    public void setInventoryData(List<Inventory> items) {
        String[] cols = { "Ingredient Name", "Quantity", "Unit", "Min Stock" };
        Object[][] data = new Object[items.size()][4];

        for (int i = 0; i < items.size(); i++) {
            Inventory it = items.get(i);
            data[i][0] = it.getIngredientName();
            data[i][1] = it.getQuantity();
            data[i][2] = it.getUnit();
            data[i][3] = it.getMinStock();
        }
        table.setModel(new javax.swing.table.DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
    }

    private void handleLogout() {
        if (logoutCallback != null) {
            logoutCallback.onLogout();
        } else {
             MainFrame.getInstance().showView("KIOSK");
        }
    }
}