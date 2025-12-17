// package id.komorebi.view;

// import id.komorebi.controller.AdminController;
// import id.komorebi.controller.LogoutCallback;
// import id.komorebi.util.UIHelper;
// import javax.swing.*;
// import java.awt.*;

// public class AdminPanelView extends JFrame {
//     private AdminController controller;
//     private LogoutCallback logoutCallback;

//     public AdminPanelView(AdminController controller, LogoutCallback logoutCallback) {
//         this.controller = controller;
//         this.logoutCallback = logoutCallback;
        
//         UIHelper.applyFrameSettings(this, "Admin Dashboard", 900, 600);
//         initUI();
//     }

//     private void initUI() {
//         setLayout(new BorderLayout());

//         JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
//         header.setBackground(UIHelper.PRIMARY_BROWN);
//         JLabel title = UIHelper.createNavTitle("Admin Dashboard");
//         header.add(title);
//         add(header, BorderLayout.NORTH);

//         add(UIHelper.createHeaderPanel("Admin Dashboard", e -> handleLogout()), BorderLayout.NORTH);

//         JPanel gridPanel = new JPanel(new GridLayout(2, 2, 20, 20));
//         gridPanel.setBackground(UIHelper.BACKGROUND_WHITE);
//         gridPanel.setBorder(new javax.swing.border.EmptyBorder(40, 50, 40, 50));

//         gridPanel.add(createDashboardCard("Manage Users", "Add, Edit, or Remove Staff", e -> controller.openUserManagement()));
//         gridPanel.add(createDashboardCard("Manage Categories", "Organize Menu Categories", e -> controller.openCategoryManagement()));
//         gridPanel.add(createDashboardCard("Manage Menu Items", "Update Prices & Products", e -> controller.openMenuManagement()));
//         gridPanel.add(createDashboardCard("View Activity Logs", "Monitor System History", e -> controller.openLogs()));
//         gridPanel.add(createDashboardCard("Today's Revenue", "Check Daily Income", e -> controller.showRevenueReport()));

//         add(gridPanel, BorderLayout.CENTER);
        
//         JPanel footer = new JPanel();
//         footer.setBackground(UIHelper.BACKGROUND_WHITE);
//         footer.add(UIHelper.createSmallLabel("Komorebi POS System v1.0"));
//         add(footer, BorderLayout.SOUTH);
//     }

//     private JButton createDashboardCard(String title, String subtitle, java.awt.event.ActionListener action) {
//         JButton btn = new JButton();
//         btn.setLayout(new BorderLayout());
//         btn.setBackground(Color.WHITE);
//         btn.setBorder(BorderFactory.createLineBorder(UIHelper.BORDER_LIGHT, 2));
//         btn.setFocusPainted(false);
//         btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

//         JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
//         titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
//         titleLbl.setForeground(UIHelper.PRIMARY_BROWN);
        
//         JLabel subLbl = new JLabel(subtitle, SwingConstants.CENTER);
//         subLbl.setFont(UIHelper.SMALL_FONT);
//         subLbl.setForeground(UIHelper.TEXT_LIGHT);

//         JPanel content = new JPanel(new GridLayout(2, 1));
//         content.setBackground(Color.WHITE);
//         content.add(titleLbl);
//         content.add(subLbl);
        
//         btn.add(content, BorderLayout.CENTER);
//         btn.addActionListener(action);

//         btn.addMouseListener(new java.awt.event.MouseAdapter() {
//             public void mouseEntered(java.awt.event.MouseEvent evt) {
//                 btn.setBorder(BorderFactory.createLineBorder(UIHelper.PRIMARY_BROWN, 2));
//                 content.setBackground(UIHelper.ACCENT_CREAM);
//             }
//             public void mouseExited(java.awt.event.MouseEvent evt) {
//                 btn.setBorder(BorderFactory.createLineBorder(UIHelper.BORDER_LIGHT, 2));
//                 content.setBackground(Color.WHITE);
//             }
//         });
        
//         return btn;
//     }

//     private void handleLogout() {
//         dispose();
//         if (logoutCallback != null) {
//             logoutCallback.onLogout();
//         }
//     }
// }

package id.komorebi.view;

import id.komorebi.controller.AdminController;
import id.komorebi.controller.LogoutCallback;
import id.komorebi.util.UIHelper;
import javax.swing.*;
import java.awt.*;

// 1. Ganti extends JFrame -> JPanel
public class AdminPanelView extends JPanel {
    private AdminController controller;
    private LogoutCallback logoutCallback;

    public AdminPanelView(AdminController controller, LogoutCallback logoutCallback) {
        this.controller = controller;
        this.logoutCallback = logoutCallback;
        
        initUI();
        
        MainFrame.getInstance().addView("ADMIN_USER", new AdminUserView(controller.getUserController()));
        MainFrame.getInstance().addView("ADMIN_CATEGORY", new AdminCategoryView(controller.getCategoryController()));
        MainFrame.getInstance().addView("ADMIN_MENU", new AdminMenuView(controller.getMenuController()));
        MainFrame.getInstance().addView("ADMIN_LOGS", new AdminLogsView(controller.getActivityLogController()));
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
        header.setBackground(UIHelper.PRIMARY_BROWN);
        JLabel title = UIHelper.createNavTitle("Admin Dashboard");
        header.add(title);
        add(header, BorderLayout.NORTH);

        add(UIHelper.createHeaderPanel("Admin Dashboard", e -> handleLogout()), BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        gridPanel.setBackground(UIHelper.BACKGROUND_WHITE);
        gridPanel.setBorder(new javax.swing.border.EmptyBorder(40, 50, 40, 50));

        // 4. UBAH ACTION LISTENER: Jangan controller.open... tapi MainFrame.showView(...)
        gridPanel.add(createDashboardCard("Manage Users", "Add, Edit, or Remove Staff", 
            e -> MainFrame.getInstance().showView("ADMIN_USER")));
            
        gridPanel.add(createDashboardCard("Manage Categories", "Organize Menu Categories", 
            e -> MainFrame.getInstance().showView("ADMIN_CATEGORY")));
            
        gridPanel.add(createDashboardCard("Manage Menu Items", "Update Prices & Products", 
            e -> MainFrame.getInstance().showView("ADMIN_MENU")));
            
        gridPanel.add(createDashboardCard("View Activity Logs", "Monitor System History", 
            e -> MainFrame.getInstance().showView("ADMIN_LOGS")));
            
        gridPanel.add(createDashboardCard("Today's Revenue", "Check Daily Income", 
            e -> controller.showRevenueReport())); // Ini popup info, jadi boleh tetap controller

        add(gridPanel, BorderLayout.CENTER);
        
        JPanel footer = new JPanel();
        footer.setBackground(UIHelper.BACKGROUND_WHITE);
        footer.add(UIHelper.createSmallLabel("Komorebi POS System v1.0"));
        add(footer, BorderLayout.SOUTH);
    }

    private JButton createDashboardCard(String title, String subtitle, java.awt.event.ActionListener action) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout());
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createLineBorder(UIHelper.BORDER_LIGHT, 2));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLbl.setForeground(UIHelper.PRIMARY_BROWN);
        
        JLabel subLbl = new JLabel(subtitle, SwingConstants.CENTER);
        subLbl.setFont(UIHelper.SMALL_FONT);
        subLbl.setForeground(UIHelper.TEXT_LIGHT);

        JPanel content = new JPanel(new GridLayout(2, 1));
        content.setBackground(Color.WHITE);
        content.add(titleLbl);
        content.add(subLbl);
        
        btn.add(content, BorderLayout.CENTER);
        btn.addActionListener(action);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBorder(BorderFactory.createLineBorder(UIHelper.PRIMARY_BROWN, 2));
                content.setBackground(UIHelper.ACCENT_CREAM);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBorder(BorderFactory.createLineBorder(UIHelper.BORDER_LIGHT, 2));
                content.setBackground(Color.WHITE);
            }
        });
        
        return btn;
    }

    private void handleLogout() {
        if (logoutCallback != null) {
            logoutCallback.onLogout();
        }
    }
}