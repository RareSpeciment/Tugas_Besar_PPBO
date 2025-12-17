// package id.komorebi.controller;

// import id.komorebi.view.AdminUserView;
// import id.komorebi.view.AdminMenuView;
// import id.komorebi.view.AdminCategoryView;
// import id.komorebi.view.AdminLogsView;

// public class AdminController {

//     private final UserController userController;
//     private final MenuController menuController;

//     // Controller Internal (Dibuat otomatis di sini)
//     private final CategoryController categoryController;
//     private final ActivityLogController activityLogController;

//     // CONSTRUCTOR DIPERBAIKI: Menerima 3 Parameter (Sesuai AuthController)
//     public AdminController(UserController userController, MenuController menuController) {
//         this.userController = userController;
//         this.menuController = menuController;

//         this.categoryController = new CategoryController();
//         this.activityLogController = new ActivityLogController();
//     }

//     public UserController getUserController() {
//         return this.userController;
//     }

//     public MenuController getMenuController() {
//         return this.menuController;
//     }

//     public CategoryController getCategoryController() {
//         return this.categoryController;
//     }

//     public ActivityLogController getActivityLogController() {
//         return this.activityLogController;
//     }

//     public void openUserManagement() {
//         new AdminUserView(userController).setVisible(true);
//     }

//     public void openMenuManagement() {
//         new AdminMenuView(menuController).setVisible(true);
//     }

//     public void openCategoryManagement() {
//         new AdminCategoryView(categoryController).setVisible(true);
//     }

//     public void openLogs() {
//         new AdminLogsView(activityLogController).setVisible(true);
//     }

//     public void showRevenueReport() {
//         try {
//             // Menggunakan PaymentDAO langsung (Sesuai kode kamu sebelumnya)
//             id.komorebi.dao.PaymentDAO paymentDao = id.komorebi.dao.DAOFactory.createPaymentDao();
//             double total = paymentDao.getTotalSalesByDate(java.time.LocalDate.now());

//             String formatted = "Rp " + java.text.NumberFormat.getInstance().format(total);
//             id.komorebi.util.UIHelper.showInfo("Total Pendapatan Hari Ini:\n\n" + formatted);
//         } catch (Exception e) {}
//     }
// }

package id.komorebi.controller;

import id.komorebi.view.MainFrame;

public class AdminController {
    private final UserController userController;
    private final MenuController menuController;
    private final CategoryController categoryController;
    private final ActivityLogController activityLogController;

    // UPDATE CONSTRUCTOR: Terima 4 Parameter agar cocok dengan AuthController
    public AdminController(UserController userController, MenuController menuController, CategoryController categoryController, ActivityLogController activityLogController) {
        this.userController = userController;
        this.menuController = menuController;
        this.categoryController = categoryController;
        this.activityLogController = activityLogController;
    }

    // --- GETTER ---
    public UserController getUserController() { return userController; }
    public MenuController getMenuController() { return menuController; }
    public CategoryController getCategoryController() { return categoryController; }
    public ActivityLogController getActivityLogController() { return activityLogController; }

    // --- NAVIGATION LOGIC (Gunakan MainFrame) ---
    public void openUserManagement() {
        // Jangan 'new AdminUserView', tapi panggil view yang sudah didaftarkan di AdminPanelView
        MainFrame.getInstance().showView("ADMIN_USER");
    }

    public void openMenuManagement() {
        MainFrame.getInstance().showView("ADMIN_MENU");
    }

    public void openCategoryManagement() {
        MainFrame.getInstance().showView("ADMIN_CATEGORY");
    }

    public void openLogs() {
        MainFrame.getInstance().showView("ADMIN_LOGS");
    }

    public void showRevenueReport() {
        try {
            id.komorebi.dao.PaymentDAO paymentDao = id.komorebi.dao.DAOFactory.createPaymentDao();
            double total = paymentDao.getTotalSalesByDate(java.time.LocalDate.now());
            String formatted = "Rp " + java.text.NumberFormat.getInstance().format(total);
            id.komorebi.util.UIHelper.showInfo("Total Pendapatan Hari Ini:\n\n" + formatted);
        } catch (Exception e) {}
    }
}