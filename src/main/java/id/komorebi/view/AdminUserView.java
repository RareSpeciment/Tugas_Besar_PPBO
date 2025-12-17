package id.komorebi.view;

import id.komorebi.controller.UserController;
import id.komorebi.model.User;
import id.komorebi.util.UIHelper;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminUserView extends JPanel {
    private final UserController controller;
    private JTable table;

    public AdminUserView(UserController controller) {
        this.controller = controller;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // 3. Header dengan Tombol Back
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIHelper.PRIMARY_BROWN);
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton btnBack = UIHelper.createButton("<< Back");
        btnBack.setBackground(Color.WHITE);
        btnBack.setForeground(UIHelper.PRIMARY_BROWN);
        btnBack.addActionListener(e -> MainFrame.getInstance().showView("ADMIN_DASHBOARD")); // Balik ke Dashboard

        JLabel title = UIHelper.createNavTitle("User Management");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Panel dummy biar judul tetap di tengah
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); 
        left.setOpaque(false); left.add(btnBack);
        
        header.add(left, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        header.add(Box.createHorizontalStrut(80), BorderLayout.EAST); // Spacer penyeimbang
        
        add(header, BorderLayout.NORTH);

        table = new JTable();
        UIHelper.styleTable(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(UIHelper.BACKGROUND_WHITE);

        JButton btnAdd = UIHelper.createSuccessButton("Create User");
        JButton btnDeactivate = UIHelper.createErrorButton("Deactivate Selected");
        JButton btnActivate = UIHelper.createButton("Activate Selected");

        btnAdd.addActionListener(e -> showCreateUserDialog());
        btnDeactivate.addActionListener(e -> deactivateUser());
        btnActivate.addActionListener(e -> activateUser());

        btnPanel.add(btnActivate);
        btnPanel.add(btnDeactivate);
        btnPanel.add(btnAdd);
        add(btnPanel, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        try {
            List<User> users = controller.getAllUsers();
            String[] cols = { "ID", "Username", "Full Name", "Role", "Status" };
            DefaultTableModel model = new DefaultTableModel(cols, 0);
            for (User u : users) {
                model.addRow(new Object[]{u.getUserId(), u.getUsername(), u.getFullname(), u.getRole().getRoleName(), u.getStatus()});
            }
            table.setModel(model);
        } catch (Exception e) { UIHelper.showError(e.getMessage()); }
    }

    private void showCreateUserDialog() {
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parent, "Create New User", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        form.setBackground(Color.WHITE);

        JTextField txtUser = UIHelper.createTextField();
        JPasswordField txtPass = new JPasswordField();
        JTextField txtName = UIHelper.createTextField();
        JComboBox<String> cbRole = new JComboBox<>(new String[]{"Administrator", "Cashier", "Barista", "Waiter", "Chef", "StockManager"}); // Hardcode atau ambil dari DB

        form.add(new JLabel("Username:")); form.add(txtUser);
        form.add(new JLabel("Password:")); form.add(txtPass);
        form.add(new JLabel("Full Name:")); form.add(txtName);
        form.add(new JLabel("Role:")); form.add(cbRole);

        JButton btnSave = UIHelper.createSuccessButton("Save");
        btnSave.addActionListener(e -> {
            int roleId = mapRoleToId((String)cbRole.getSelectedItem()); 
            if(controller.createUser(txtUser.getText(), new String(txtPass.getPassword()), txtName.getText(), roleId)) {
                UIHelper.showInfo("User Created!");
                dialog.dispose();
                loadData();
            }
        });

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnSave, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private int mapRoleToId(String role) {
        switch(role) {
            case "Administrator": return 1;
            case "Cashier": return 3;
            case "Barista": return 4;
            case "Waiter": return 5;
            case "Chef": return 6;
            case "StockManager": return 7;
            default: return 5;
        }
    }

    private void deactivateUser() {
        int r = table.getSelectedRow();
        if(r<0) return;
        int uid = (int)table.getValueAt(r, 0);
        if(controller.deactivateUser(uid)) loadData();
    }
    
    private void activateUser() {
        int r = table.getSelectedRow();
        if(r<0) return;
        int uid = (int)table.getValueAt(r, 0);
        if(controller.activateUser(uid)) loadData();
    }
}