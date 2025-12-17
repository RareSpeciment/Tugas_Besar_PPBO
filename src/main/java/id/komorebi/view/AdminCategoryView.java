// package id.komorebi.view;

// import id.komorebi.controller.CategoryController;
// import id.komorebi.model.Category;
// import id.komorebi.util.UIHelper;
// import javax.swing.*;
// import javax.swing.table.DefaultTableModel;
// import java.awt.*;
// import java.util.List;

// public class AdminCategoryView extends JFrame {
//     private final CategoryController controller;
//     private JTable table;

//     public AdminCategoryView(CategoryController controller) {
//         this.controller = controller;
//         UIHelper.applyFrameSettings(this, "Manage Categories", 600, 400);
//         setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//         initUI();
//     }

//     private void initUI() {
//         setLayout(new BorderLayout());
        
//         JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
//         header.setBackground(UIHelper.PRIMARY_BROWN);
//         JLabel title = UIHelper.createNavTitle("Category Management");
//         header.add(title);
//         add(header, BorderLayout.NORTH);

//         table = new JTable();
//         UIHelper.styleTable(table);
//         add(new JScrollPane(table), BorderLayout.CENTER);

//         JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//         btnPanel.setBackground(Color.WHITE);
        
//         JButton btnAdd = UIHelper.createSuccessButton("Add Category");
//         JButton btnDel = UIHelper.createErrorButton("Delete");

//         btnAdd.addActionListener(e -> showAddDialog());
//         btnDel.addActionListener(e -> deleteCategory());

//         btnPanel.add(btnDel);
//         btnPanel.add(btnAdd);
//         add(btnPanel, BorderLayout.SOUTH);

//         loadData();
//     }

//     private void loadData() {
//         try {
//             List<Category> list = controller.getAllCategories(); 
//             DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name"}, 0);
//             for(Category c : list) model.addRow(new Object[]{c.getCategoryId(), c.getName()});
//             table.setModel(model);
//         } catch(Exception e) {}
//     }

//     private void showAddDialog() {
//         JDialog d = new JDialog(this, "Add Category", true);
//         d.setSize(300, 150);
//         d.setLocationRelativeTo(this);
//         d.setLayout(new FlowLayout());

//         JTextField txtName = UIHelper.createTextField();
//         txtName.setPreferredSize(new Dimension(200, 30));
//         JButton btnSave = UIHelper.createSuccessButton("Save");

//         btnSave.addActionListener(e -> {
//         if(!txtName.getText().isEmpty()) {
//             // Panggil method controller yang baru kita buat
//             if (controller.createCategory(txtName.getText())) {
//                 UIHelper.showInfo("Category Added!");
//                 d.dispose();
//                 loadData();
//                 }
//             }
//         });

//         d.add(new JLabel("Name: "));
//         d.add(txtName);
//         d.add(btnSave);
//         d.setVisible(true);
//     }

//     private void deleteCategory() {
//         int r = table.getSelectedRow();
//         if(r >= 0) {
//             int id = (int)table.getValueAt(r, 0);
//             if(UIHelper.confirm("Delete category?")) {
//                 // Panggil method controller
//                 if (controller.deleteCategory(id)) {
//                     UIHelper.showInfo("Category deleted.");
//                     loadData();
//                 }
//             }
//         }
//     }
// }

package id.komorebi.view;

import id.komorebi.controller.CategoryController;
import id.komorebi.model.Category;
import id.komorebi.util.UIHelper;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// BERUBAH: extends JPanel
public class AdminCategoryView extends JPanel {
    private final CategoryController controller;
    private JTable table;

    public AdminCategoryView(CategoryController controller) {
        this.controller = controller;
        // HAPUS: frame settings
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        
        // HEADER + BACK BUTTON
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIHelper.PRIMARY_BROWN);
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton btnBack = UIHelper.createButton("<< Back");
        btnBack.setBackground(Color.WHITE);
        btnBack.setForeground(UIHelper.PRIMARY_BROWN);
        btnBack.addActionListener(e -> MainFrame.getInstance().showView("ADMIN_DASHBOARD"));

        JLabel title = UIHelper.createNavTitle("Category Management");
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
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        
        JButton btnAdd = UIHelper.createSuccessButton("Add Category");
        JButton btnDel = UIHelper.createErrorButton("Delete");

        btnAdd.addActionListener(e -> showAddDialog());
        btnDel.addActionListener(e -> deleteCategory());

        btnPanel.add(btnDel);
        btnPanel.add(btnAdd);
        add(btnPanel, BorderLayout.SOUTH);

        loadData();
    }

    // ... (Bagian loadData, showAddDialog, deleteCategory SAMA PERSIS dengan sebelumnya) ...
    // ... Copy paste logika CRUD di sini ...
    
    private void loadData() {
        try {
            List<Category> list = controller.getAllCategories(); 
            DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name"}, 0);
            for(Category c : list) model.addRow(new Object[]{c.getCategoryId(), c.getName()});
            table.setModel(model);
        } catch(Exception e) {}
    }

    private void showAddDialog() {
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this); // Fix parent dialog
        JDialog d = new JDialog(parent, "Add Category", true);
        d.setSize(300, 150);
        d.setLocationRelativeTo(this);
        d.setLayout(new FlowLayout());

        JTextField txtName = UIHelper.createTextField();
        txtName.setPreferredSize(new Dimension(200, 30));
        JButton btnSave = UIHelper.createSuccessButton("Save");

        btnSave.addActionListener(e -> {
        if(!txtName.getText().isEmpty()) {
            if (controller.createCategory(txtName.getText())) {
                UIHelper.showInfo("Category Added!");
                d.dispose();
                loadData();
                }
            }
        });

        d.add(new JLabel("Name: ")); d.add(txtName); d.add(btnSave);
        d.setVisible(true);
    }

    private void deleteCategory() {
        int r = table.getSelectedRow();
        if(r >= 0) {
            int id = (int)table.getValueAt(r, 0);
            if(UIHelper.confirm("Delete category?")) {
                if (controller.deleteCategory(id)) {
                    UIHelper.showInfo("Category deleted.");
                    loadData();
                }
            }
        }
    }
}