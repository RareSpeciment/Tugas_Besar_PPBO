package id.komorebi.view;

import id.komorebi.controller.ActivityLogController;
import id.komorebi.model.ActivityLog;
import id.komorebi.util.UIHelper;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminLogsView extends JPanel {
    private final ActivityLogController controller;
    private JTable table;
    
    public AdminLogsView(ActivityLogController controller) {
        this.controller = controller;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        
        // HEADER dengan Tombol Back
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIHelper.PRIMARY_BROWN);
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton btnBack = UIHelper.createButton("<< Back");
        btnBack.setBackground(Color.WHITE);
        btnBack.setForeground(UIHelper.PRIMARY_BROWN);
        btnBack.addActionListener(e -> MainFrame.getInstance().showView("ADMIN_DASHBOARD")); // BALIK KE DASHBOARD

        JLabel title = UIHelper.createNavTitle("System Activity Logs");
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

        JButton refresh = UIHelper.createButton("Refresh Logs");
        refresh.addActionListener(e -> loadData());
        
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.add(refresh);
        add(p, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        try {
            List<ActivityLog> logs = controller.getAllLogs();
            String[] cols = { "ID", "User", "Action", "Details", "Timestamp" };
            DefaultTableModel model = new DefaultTableModel(cols, 0);
            
            for(ActivityLog log : logs) {
                model.addRow(new Object[]{
                    log.getLogId(),
                    log.getUser().getUsername(),
                    log.getAction(),
                    log.getDetails(),
                    log.getCreatedAt()
                });
            }
            table.setModel(model);
        } catch(Exception e) { UIHelper.showError("Error loading logs: " + e.getMessage()); }
    }
}