package id.komorebi.controller;

import id.komorebi.model.User;
import id.komorebi.service.AuthService;
import id.komorebi.service.OrderService;
import id.komorebi.service.InventoryService;
import id.komorebi.util.UIHelper;
import id.komorebi.view.*;

public class AuthController {
    private final AuthService authService;

    public AuthController() {
        this(new AuthService());
    }

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public void showLogin() {
        LoginView loginPanel = new LoginView(this);
        MainFrame.getInstance().addView("LOGIN", loginPanel);
        MainFrame.getInstance().showView("LOGIN");
    }

    public void handleLogin(String username, String password) {
        handleLoginProcess(username, password);
    }

    private void handleLoginProcess(String username, String password) {
        try {
            User u = authService.login(username, password);
            if (u != null) {
                UIHelper.showInfo("Welcome, " + u.getFullname());
                routeByRole(u);
                
            } else {
                UIHelper.showError("Invalid username or password.");
            }
        } catch (Exception e) {
            UIHelper.showError("Login Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void back() {
        MainFrame.getInstance().showView("MainMenu");
    }

    private void routeByRole(User u) {
        try {
            String roleName = u.getRole() != null ? u.getRole().getRoleName().toLowerCase() : "";
            LogoutCallback onLogout = () -> back();

            // --- 1. admin ---
            if (roleName.contains("admin")) {
                UserController userController = new UserController(authService);
                MenuController menuController = new MenuController();
                CategoryController catController = new CategoryController(); 
                ActivityLogController logController = new ActivityLogController();

                AdminController adminController = new AdminController(userController, menuController, catController, logController);
                AdminPanelView adminDashboard = new AdminPanelView(adminController, onLogout);
                MainFrame.getInstance().addView("ADMIN_DASHBOARD", adminDashboard);
                MainFrame.getInstance().showView("ADMIN_DASHBOARD");
                return;
            }

            // --- 2. StockManager ---
            if (roleName.contains("stock") || roleName.contains("inventory")) {
                InventoryController ic = new InventoryController(new InventoryService(), u);

                // Buat View (Panel) --> Masukkan ke mainframe --> Load Data awal --> Tampilkan
                InventoryView iv = new InventoryView(ic, onLogout);
                MainFrame.getInstance().addView("INVENTORY", iv);
                iv.setInventoryData(ic.getAllIngredients());
                
                MainFrame.getInstance().showView("INVENTORY");
                return;
            }

            // --- 3. cheff, kasir, waiter ---
            OrderController oc = new OrderController(new OrderService());
            
            OrderListView workstationView = new OrderListView(oc, u, onLogout, null); 

            String viewName = "WORKSTATION_" + roleName.toUpperCase();
            MainFrame.getInstance().addView(viewName, workstationView);
            workstationView.loadByFilter();
            
            MainFrame.getInstance().showView(viewName);

        } catch (Exception ex) {
            UIHelper.showError("Failed to open role view: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}