package id.komorebi.view;

import id.komorebi.controller.MenuController;
import id.komorebi.model.Category;
import id.komorebi.model.MenuItem;
import id.komorebi.util.UIHelper;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class AdminMenuView extends JPanel {
    private final MenuController controller;
    private JTable table;

    public AdminMenuView(MenuController controller) {
        this.controller = controller;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIHelper.PRIMARY_BROWN);
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton btnBack = UIHelper.createButton("<< Back");
        btnBack.setBackground(Color.WHITE);
        btnBack.setForeground(UIHelper.PRIMARY_BROWN);
        btnBack.addActionListener(e -> MainFrame.getInstance().showView("ADMIN_DASHBOARD"));

        JLabel title = UIHelper.createNavTitle("Menu Management");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); 
        left.setOpaque(false); left.add(btnBack);
        
        header.add(left, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        header.add(Box.createHorizontalStrut(80), BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // TABLE
        table = new JTable();
        UIHelper.styleTable(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(UIHelper.BACKGROUND_WHITE);
        JButton btnAdd = UIHelper.createSuccessButton("Add New Item");
        JButton btnDel = UIHelper.createErrorButton("Delete Selected");

        btnAdd.addActionListener(e -> showAddDialog());
        btnDel.addActionListener(e -> deleteSelected());

        btnPanel.add(btnDel);
        btnPanel.add(btnAdd);
        add(btnPanel, BorderLayout.SOUTH);

        loadData();
    }

    // ... (Bagian Load Data, Add Dialog, dll SAMA PERSIS, sesuaikan 'Frame parent' di dialog) ...
    
    private void loadData() {
        try {
            List<MenuItem> items = controller.getAllMenu();
            String[] cols = { "ID", "Name", "Price", "Category", "Status" };
            DefaultTableModel model = new DefaultTableModel(cols, 0);
            for (MenuItem m : items) {
                model.addRow(new Object[]{
                    m.getMenuId(), 
                    m.getName(), 
                    "Rp " + m.getPrice(), 
                    m.getCategory().getName(), 
                    m.isAvailable() ? "Available" : "Sold Out"
                });
            }
            table.setModel(model);
        } catch(Exception e) {}
    }

    private void showAddDialog() {
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parent, "Add Menu Item", true);
        dialog.setSize(450, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(6, 1, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        form.setBackground(Color.WHITE);

        JTextField txtName = UIHelper.createTextField();
        JTextField txtPrice = UIHelper.createTextField();
        JComboBox<Category> cbCategory = new JComboBox<>();

        try {
            List<Category> cats = controller.getCategories();
            for (Category c : cats) cbCategory.addItem(c);
        } catch (Exception e) {}

        JLabel imgLabel = new JLabel("No Image Selected");
        JButton btnImg = UIHelper.createButton("Select Image");
        final String[] selectedPath = {null};
        btnImg.addActionListener(e -> {
            String path = id.komorebi.util.ImageUtil.selectAndSaveImage(dialog);
            if(path != null) {
                selectedPath[0] = path;
                imgLabel.setText("Image Selected!");
            }
        });

        form.add(new JLabel("Menu Name:")); form.add(txtName);
        form.add(new JLabel("Price:")); form.add(txtPrice);
        form.add(new JLabel("Category:")); form.add(cbCategory);
        form.add(new JLabel("Image:")); 
        
        JPanel imgPanel = new JPanel(new BorderLayout());
        imgPanel.add(imgLabel, BorderLayout.CENTER);
        imgPanel.add(btnImg, BorderLayout.EAST);
        form.add(imgPanel);

        JButton btnSave = UIHelper.createSuccessButton("Save Menu");
        btnSave.addActionListener(e -> {
            try {
                String name = txtName.getText();
                String priceStr = txtPrice.getText();
                Category cat = (Category) cbCategory.getSelectedItem();
                
                if (name.isEmpty() || priceStr.isEmpty() || cat == null) {
                    UIHelper.showError("Please fill all fields.");
                    return;
                }

                MenuItem item = new MenuItem();
                item.setName(name);
                item.setPrice(new BigDecimal(priceStr));
                item.setCategory(cat);
                item.setAvailable(true);
                item.setImagePath(selectedPath[0]);

                if(controller.createMenu(item)) {
                    UIHelper.showInfo("Menu Added!");
                    dialog.dispose();
                    loadData();
                }
            } catch (Exception ex) {
                UIHelper.showError("Error: " + ex.getMessage());
            }
        });

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnSave, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deleteSelected() {
        int r = table.getSelectedRow();
        if(r < 0) { UIHelper.showError("Select item to delete."); return; }
        
        int id = (int) table.getValueAt(r, 0);
        if(UIHelper.confirm("Delete this menu item?")) {
            try {
                if(controller.deleteMenu(id)) {
                    loadData();
                    UIHelper.showInfo("Deleted.");
                }
            } catch(Exception e) { UIHelper.showError(e.getMessage()); }
        }
    }
}